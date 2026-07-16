package io.github.nchaugen.tabletest.inspections

import com.intellij.codeInspection.InspectionSuppressor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.psi.PsiElement

/**
 * Suppresses "Unused declaration" for Kotlin functions annotated with @TableTest or @TypeConverter.
 *
 * Registered in TableTest-kotlin.xml so this class only loads when the Kotlin plugin
 * is available; it must not be referenced from classes registered in the main plugin.xml.
 */
class TableTestKotlinUnusedDeclarationSuppressor : InspectionSuppressor {
    override fun isSuppressedFor(element: PsiElement, toolId: String): Boolean {
        if (toolId != UNUSED_DECLARATION_TOOL_ID) {
            return false
        }

        return KotlinConverterAnnotationUtil.isConverterAnnotatedKotlin(element) ||
                KotlinConverterAnnotationUtil.isTableTestAnnotatedKotlin(element)
    }

    override fun getSuppressActions(element: PsiElement?, toolId: String): Array<SuppressQuickFix> =
        SuppressQuickFix.EMPTY_ARRAY

    private companion object {
        private const val UNUSED_DECLARATION_TOOL_ID = "UnusedDeclaration"
    }
}
