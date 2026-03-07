package io.github.nchaugen.tabletest.language

import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.RangeMarker
import com.intellij.openapi.util.TextRange
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.psi.ElementManipulators
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiArrayInitializerMemberValue
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiNameValuePair
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.impl.source.codeStyle.PostFormatProcessor
import com.intellij.psi.util.PsiTreeUtil

/**
 * Trims trailing whitespace left at end-of-row in formatted TableTest content.
 *
 * Responsibility: remove end-of-row padding after formatting TableTest content.
 */
class TableTestTrailingWhitespacePostFormatProcessor : PostFormatProcessor {

    override fun processElement(source: PsiElement, settings: CodeStyleSettings): PsiElement {
        processText(source.containingFile, source.textRange, settings)
        return source
    }

    override fun processText(source: PsiFile, rangeToReformat: TextRange, settings: CodeStyleSettings): TextRange {
        val documentManager: PsiDocumentManager = PsiDocumentManager.getInstance(source.project)
        val document: Document = documentManager.getDocument(source) ?: return rangeToReformat

        val trailingWhitespaceRanges: List<TextRange> = when {
            source.language == TableTestLanguage && isInjectedTableTestArrayFragment(source) ->
                trailingWhitespaceBeforeNewlineRanges(document.charsSequence, rangeToReformat)

            source is PsiJavaFile ->
                javaArrayLiteralTrailingWhitespaceRanges(source, document, rangeToReformat)

            else -> emptyList()
        }

        return trimRanges(documentManager, document, rangeToReformat, trailingWhitespaceRanges)
    }

    override fun isWhitespaceOnly(): Boolean = true

    private fun trimRanges(
        documentManager: PsiDocumentManager,
        document: Document,
        rangeToReformat: TextRange,
        trailingWhitespaceRanges: List<TextRange>
    ): TextRange {
        if (trailingWhitespaceRanges.isEmpty()) return rangeToReformat

        val updatedRangeMarker: RangeMarker =
            document.createRangeMarker(rangeToReformat.startOffset, rangeToReformat.endOffset)
        trailingWhitespaceRanges
            .sortedByDescending { it.startOffset }
            .forEach { range ->
                document.deleteString(range.startOffset, range.endOffset)
            }
        documentManager.commitDocument(document)

        val updatedRange: TextRange = updatedRangeMarker.textRange
        updatedRangeMarker.dispose()
        return updatedRange
    }

    private fun trailingWhitespaceBeforeNewlineRanges(
        sourceText: CharSequence,
        rangeToReformat: TextRange
    ): List<TextRange> {
        val rangeText: String = sourceText.subSequence(rangeToReformat.startOffset, rangeToReformat.endOffset).toString()

        return TRAILING_WHITESPACE_BEFORE_NEWLINE.findAll(rangeText)
            .map { matchResult ->
                val startOffset: Int = rangeToReformat.startOffset + matchResult.range.first
                val endOffset: Int = rangeToReformat.startOffset + matchResult.range.last + 1
                TextRange(startOffset, endOffset)
            }
            .toList()
    }

    private fun isInjectedTableTestArrayFragment(source: PsiFile): Boolean {
        val injectedLanguageManager: InjectedLanguageManager = InjectedLanguageManager.getInstance(source.project)
        if (!injectedLanguageManager.isInjectedFragment(source)) return false

        val injectionHost: PsiLanguageInjectionHost = injectedLanguageManager.getInjectionHost(source) ?: return false
        val hostLiteral: PsiLiteralExpression = injectionHost as? PsiLiteralExpression ?: return false
        return isSupportedTableTestArrayElement(hostLiteral)
    }

    private fun javaArrayLiteralTrailingWhitespaceRanges(
        source: PsiJavaFile,
        document: Document,
        rangeToReformat: TextRange
    ): List<TextRange> =
        PsiTreeUtil.findChildrenOfType(source, PsiLiteralExpression::class.java)
            .asSequence()
            .filter { literal -> literal.textRange.intersects(rangeToReformat) }
            .filter(::isSupportedTableTestArrayElement)
            .mapNotNull { literal -> trailingWhitespaceRange(literal, document.charsSequence) }
            .toList()

    private fun trailingWhitespaceRange(
        literal: PsiLiteralExpression,
        sourceText: CharSequence
    ): TextRange? {
        val host: PsiLanguageInjectionHost = literal as? PsiLanguageInjectionHost ?: return null
        val valueRange: TextRange =
            ElementManipulators.getValueTextRange(host).shiftRight(literal.textRange.startOffset)
        val trimStartOffset: Int =
            valueRange.startOffset + sourceText
                .subSequence(valueRange.startOffset, valueRange.endOffset)
                .toString()
                .trimEnd(' ', '\t')
                .length

        return if (trimStartOffset < valueRange.endOffset) {
            TextRange(trimStartOffset, valueRange.endOffset)
        } else {
            null
        }
    }

    private fun isSupportedTableTestArrayElement(literal: PsiLiteralExpression): Boolean {
        if (literal.value !is String) return false

        val arrayValue: PsiArrayInitializerMemberValue = literal.parent as? PsiArrayInitializerMemberValue ?: return false
        val valuePair: PsiNameValuePair = arrayValue.parent as? PsiNameValuePair ?: return false
        if (!isTableTestValueParameter(valuePair)) return false

        val annotation: PsiAnnotation =
            PsiTreeUtil.getParentOfType(valuePair, PsiAnnotation::class.java, true) ?: return false
        val qualifiedName: String = annotation.qualifiedName ?: return false
        return qualifiedName in SUPPORTED_ANNOTATION_FQNS
    }

    private fun isTableTestValueParameter(valuePair: PsiNameValuePair): Boolean =
        valuePair.name == null || valuePair.name == PsiAnnotation.DEFAULT_REFERENCED_METHOD_NAME

    companion object {
        private val TRAILING_WHITESPACE_BEFORE_NEWLINE: Regex = Regex("[ \\t]+(?=\\r?\\n)")

        private val SUPPORTED_ANNOTATION_FQNS: Set<String> = setOf(
            "io.github.nchaugen.tabletest.junit.TableTest",
            "org.tabletest.junit.TableTest",
        )
    }
}
