package io.github.nchaugen.tabletest.inspections

import com.intellij.codeInspection.InspectionSuppressor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.psi.PsiElement
import io.github.nchaugen.tabletest.inspections.ConverterAnnotationUtil.isConverterAnnotatedJava

class TableTestUnusedDeclarationSuppressor : InspectionSuppressor {
    override fun isSuppressedFor(element: PsiElement, toolId: String): Boolean {
        if (toolId != UNUSED_DECLARATION_TOOL_ID) {
            return false
        }

        return isConverterAnnotatedJava(element)
    }

    override fun getSuppressActions(element: PsiElement?, toolId: String): Array<SuppressQuickFix> =
        SuppressQuickFix.EMPTY_ARRAY

    private companion object {
        private const val UNUSED_DECLARATION_TOOL_ID = "UnusedDeclaration"
    }
}
