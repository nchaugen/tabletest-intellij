package io.github.nchaugen.tabletest.language

import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.ElementManipulators
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiArrayInitializerMemberValue
import com.intellij.psi.PsiAnnotationMemberValue
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiNameValuePair
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.impl.source.codeStyle.PostFormatProcessor
import com.intellij.psi.util.PsiTreeUtil

/**
 * Aligns closing quotes for Java @TableTest String[] rows by normalising row lengths.
 *
 * Responsibility: update trailing spaces in array element strings after formatting.
 */
class TableTestTrailingWhitespacePostFormatProcessor : PostFormatProcessor {

    override fun processElement(source: PsiElement, settings: CodeStyleSettings): PsiElement {
        processText(source.containingFile, source.textRange, settings)
        return source
    }

    override fun processText(source: PsiFile, rangeToReformat: TextRange, settings: CodeStyleSettings): TextRange =
        when {
            source.language == TableTestLanguage -> processInjectedTableTestArray(source, rangeToReformat)
            source is PsiJavaFile -> processJavaTableTestArrays(source, rangeToReformat)
            else -> rangeToReformat
        }

    override fun isWhitespaceOnly(): Boolean = true

    private fun processInjectedTableTestArray(source: PsiFile, rangeToReformat: TextRange): TextRange {
        val hostLiteral: PsiLiteralExpression = injectedArrayHostLiteral(source) ?: return rangeToReformat
        val literals: List<PsiLiteralExpression> = supportedArrayStringLiterals(hostLiteral) ?: return rangeToReformat

        val documentManager: PsiDocumentManager = PsiDocumentManager.getInstance(source.project)
        val hostDocument: Document = documentManager.getDocument(hostLiteral.containingFile) ?: return rangeToReformat
        val replacements: List<TextReplacement> = quoteAlignmentReplacements(literals, hostDocument.charsSequence)
        if (replacements.isEmpty()) return rangeToReformat

        applyReplacements(documentManager, hostDocument, replacements)
        return rangeToReformat
    }

    private fun processJavaTableTestArrays(source: PsiJavaFile, rangeToReformat: TextRange): TextRange {
        val documentManager: PsiDocumentManager = PsiDocumentManager.getInstance(source.project)
        val document: Document = documentManager.getDocument(source) ?: return rangeToReformat

        val replacements: List<TextReplacement> = tableTestArrayStringLiterals(source, rangeToReformat)
            .flatMap { literals -> quoteAlignmentReplacements(literals, document.charsSequence) }
        if (replacements.isEmpty()) return rangeToReformat

        val updatedRangeMarker = document.createRangeMarker(rangeToReformat.startOffset, rangeToReformat.endOffset)
        applyReplacements(documentManager, document, replacements)
        val updatedRange: TextRange = updatedRangeMarker.textRange
        updatedRangeMarker.dispose()
        return updatedRange
    }

    private fun applyReplacements(
        documentManager: PsiDocumentManager,
        document: Document,
        replacements: List<TextReplacement>
    ) {
        replacements
            .sortedByDescending { replacement -> replacement.range.startOffset }
            .forEach { replacement ->
                document.replaceString(
                    replacement.range.startOffset,
                    replacement.range.endOffset,
                    replacement.newText
                )
            }
        documentManager.commitDocument(document)
    }

    private fun tableTestArrayStringLiterals(
        source: PsiJavaFile,
        rangeToReformat: TextRange
    ): List<List<PsiLiteralExpression>> =
        PsiTreeUtil.findChildrenOfType(source, PsiArrayInitializerMemberValue::class.java)
            .asSequence()
            .filter { arrayValue -> arrayValue.textRange.intersects(rangeToReformat) }
            .mapNotNull(::supportedArrayStringLiterals)
            .toList()

    private fun injectedArrayHostLiteral(source: PsiFile): PsiLiteralExpression? {
        val injectedLanguageManager: InjectedLanguageManager = InjectedLanguageManager.getInstance(source.project)
        if (!injectedLanguageManager.isInjectedFragment(source)) return null

        val injectionHost: PsiLanguageInjectionHost = injectedLanguageManager.getInjectionHost(source) ?: return null
        val hostLiteral: PsiLiteralExpression = injectionHost as? PsiLiteralExpression ?: return null
        return if (isSupportedTableTestArrayElement(hostLiteral)) hostLiteral else null
    }

    private fun supportedArrayStringLiterals(hostLiteral: PsiLiteralExpression): List<PsiLiteralExpression>? {
        val arrayValue: PsiArrayInitializerMemberValue = hostLiteral.parent as? PsiArrayInitializerMemberValue ?: return null
        return supportedArrayStringLiterals(arrayValue)
    }

    private fun supportedArrayStringLiterals(arrayValue: PsiArrayInitializerMemberValue): List<PsiLiteralExpression>? {
        if (!isSupportedTableTestArray(arrayValue)) return null

        val literals: List<PsiLiteralExpression> = arrayValue.initializers
            .mapNotNull(::asStringLiteral)
        if (literals.size != arrayValue.initializers.size) return null
        return literals
    }

    private fun quoteAlignmentReplacements(
        literals: List<PsiLiteralExpression>,
        sourceText: CharSequence
    ): List<TextReplacement> {
        val rows: List<RowValue> = literals
            .mapNotNull { literal -> rowValue(literal, sourceText) }
        if (rows.size != literals.size) return emptyList()

        val maxRowLength: Int = rows
            .map { row -> trimTrailingHorizontalWhitespace(row.rawText) }
            .filter { trimmed -> trimmed.isNotEmpty() }
            .maxOfOrNull { trimmed -> trimmed.length }
            ?: 0

        return rows
            .mapNotNull { row ->
                val trimmed: String = trimTrailingHorizontalWhitespace(row.rawText)
                val hasTrailingWhitespace: Boolean = row.rawText.length != trimmed.length
                val needsPadding: Boolean = trimmed.isNotEmpty() && trimmed.length < maxRowLength
                if (!hasTrailingWhitespace && !needsPadding) {
                    return@mapNotNull null
                }
                val aligned: String = alignedRowText(trimmed, maxRowLength)
                TextReplacement(row.range, aligned)
            }
    }

    private fun asStringLiteral(initializer: PsiAnnotationMemberValue): PsiLiteralExpression? {
        val literal: PsiLiteralExpression = initializer as? PsiLiteralExpression ?: return null
        return if (literal.value is String) literal else null
    }

    private fun rowValue(literal: PsiLiteralExpression, sourceText: CharSequence): RowValue? {
        val host: PsiLanguageInjectionHost = literal as? PsiLanguageInjectionHost ?: return null
        val valueRange: TextRange =
            ElementManipulators.getValueTextRange(host).shiftRight(literal.textRange.startOffset)
        val rawText: String = sourceText.subSequence(valueRange.startOffset, valueRange.endOffset).toString()
        return RowValue(valueRange, rawText)
    }

    private fun alignedRowText(trimmedRowText: String, maxRowLength: Int): String {
        if (trimmedRowText.isEmpty()) return ""
        return trimmedRowText.padEnd(maxRowLength, ' ')
    }

    private fun trimTrailingHorizontalWhitespace(text: String): String = text.trimEnd(' ', '\t')

    private fun isSupportedTableTestArrayElement(literal: PsiLiteralExpression): Boolean {
        if (literal.value !is String) return false

        val arrayValue: PsiArrayInitializerMemberValue = literal.parent as? PsiArrayInitializerMemberValue ?: return false
        return isSupportedTableTestArray(arrayValue)
    }

    private fun isSupportedTableTestArray(arrayValue: PsiArrayInitializerMemberValue): Boolean {
        val valuePair: PsiNameValuePair = arrayValue.parent as? PsiNameValuePair ?: return false
        if (!isTableTestValueParameter(valuePair)) return false

        val annotation: PsiAnnotation =
            PsiTreeUtil.getParentOfType(valuePair, PsiAnnotation::class.java, true) ?: return false
        val qualifiedName: String = annotation.qualifiedName ?: return false
        return qualifiedName in SUPPORTED_ANNOTATION_FQNS
    }

    private fun isTableTestValueParameter(valuePair: PsiNameValuePair): Boolean =
        valuePair.name == null || valuePair.name == PsiAnnotation.DEFAULT_REFERENCED_METHOD_NAME

    private data class RowValue(
        val range: TextRange,
        val rawText: String,
    )

    private data class TextReplacement(
        val range: TextRange,
        val newText: String,
    )

    companion object {
        private val SUPPORTED_ANNOTATION_FQNS: Set<String> = setOf(
            "io.github.nchaugen.tabletest.junit.TableTest",
            "org.tabletest.junit.TableTest",
        )
    }
}
