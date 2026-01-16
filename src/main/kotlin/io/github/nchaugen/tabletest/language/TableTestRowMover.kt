package io.github.nchaugen.tabletest.language

import com.intellij.codeInsight.editorActions.moveUpDown.LineRange
import com.intellij.codeInsight.editorActions.moveUpDown.StatementUpDownMover
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import io.github.nchaugen.tabletest.language.psi.TableTestHeaderRow
import io.github.nchaugen.tabletest.language.psi.TableTestRow

/**
 * Enables moving TableTest data rows up and down using keyboard shortcuts.
 *
 * Behaviour:
 * - Data rows can be swapped with adjacent data rows
 * - Header rows cannot be moved
 * - Data rows cannot be moved above the header
 * - Works in both native .table files and injected content
 */
class TableTestRowMover : StatementUpDownMover() {

    override fun checkAvailable(editor: Editor, file: PsiFile, info: MoveInfo, down: Boolean): Boolean {
        val offset = editor.caretModel.offset

        if (file is TableTestFile) {
            return checkAvailableInTableTestFile(file, offset, editor.document, info, down)
        }

        return checkAvailableInInjectedContent(file, offset, editor.document, info, down)
    }

    private fun checkAvailableInTableTestFile(
        file: TableTestFile,
        offset: Int,
        document: Document,
        info: MoveInfo,
        down: Boolean
    ): Boolean {
        val element = file.findElementAt(offset) ?: return false
        return checkAvailableForElement(element, document, info, down) { it.textRange.startOffset }
    }

    private fun checkAvailableInInjectedContent(
        hostFile: PsiFile,
        hostOffset: Int,
        hostDocument: Document,
        info: MoveInfo,
        down: Boolean
    ): Boolean {
        val injectedLanguageManager = InjectedLanguageManager.getInstance(hostFile.project)
        val injectedElement = injectedLanguageManager.findInjectedElementAt(hostFile, hostOffset) ?: return false
        val injectedFile = injectedElement.containingFile

        if (injectedFile !is TableTestFile) return false

        return checkAvailableForElement(injectedElement, hostDocument, info, down) { element ->
            injectedLanguageManager.injectedToHost(element, element.textRange).startOffset
        }
    }

    private fun checkAvailableForElement(
        element: PsiElement,
        document: Document,
        info: MoveInfo,
        down: Boolean,
        toHostOffset: (PsiElement) -> Int
    ): Boolean {
        val row = PsiTreeUtil.getParentOfType(element, TableTestRow::class.java, false)
        val headerRow = PsiTreeUtil.getParentOfType(element, TableTestHeaderRow::class.java, false)

        if (headerRow != null) {
            info.toMove2 = null
            return true
        }

        if (row == null) return false

        val adjacentRow = findAdjacentDataRow(row, down)
        if (adjacentRow == null) {
            info.toMove2 = null
            return true
        }

        val sourceRange = lineRangeOf(row, document, toHostOffset)
        val targetRange = lineRangeOf(adjacentRow, document, toHostOffset)

        info.toMove = sourceRange
        info.toMove2 = targetRange
        return true
    }

    private fun findAdjacentDataRow(row: TableTestRow, down: Boolean): TableTestRow? =
        generateSequence(if (down) row.nextSibling else row.prevSibling) {
            if (down) it.nextSibling else it.prevSibling
        }.filterIsInstance<TableTestRow>().firstOrNull()

    private fun lineRangeOf(
        element: PsiElement,
        document: Document,
        toHostOffset: (PsiElement) -> Int
    ): LineRange {
        val startOffset = toHostOffset(element)
        val endOffset = maxOf(startOffset + element.textLength - 1, startOffset)
        return LineRange(
            document.getLineNumber(startOffset),
            document.getLineNumber(endOffset) + 1
        )
    }
}
