package io.github.nchaugen.tabletest.inspections

import com.intellij.codeInspection.InspectionSuppressor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.psi.PsiElement
import io.github.nchaugen.tabletest.inspections.ConverterAnnotationUtil.isConverterAnnotatedJava

/**
 * Suppresses "Unused declaration" for Java methods annotated with @TableTest or @TypeConverter.
 *
 * Registered in the main plugin.xml, so it must only reference classes that are always
 * available; Kotlin-aware suppression lives in [TableTestKotlinUnusedDeclarationSuppressor],
 * which is registered behind the optional Kotlin plugin dependency.
 */
class TableTestUnusedDeclarationSuppressor : InspectionSuppressor {
    override fun isSuppressedFor(element: PsiElement, toolId: String): Boolean {
        if (toolId != UNUSED_DECLARATION_TOOL_ID) {
            return false
        }

        return isConverterAnnotatedJava(element) ||
                isTableTestAnnotatedJava(element)
    }

    private fun isTableTestAnnotatedJava(element: PsiElement): Boolean {
        val method = com.intellij.psi.util.PsiTreeUtil.getParentOfType(element, com.intellij.psi.PsiMethod::class.java, false) ?: return false
        return method.hasAnnotation("io.github.nchaugen.tabletest.junit.TableTest") ||
                method.hasAnnotation("org.tabletest.junit.TableTest")
    }

    override fun getSuppressActions(element: PsiElement?, toolId: String): Array<SuppressQuickFix> =
        SuppressQuickFix.EMPTY_ARRAY

    private companion object {
        private const val UNUSED_DECLARATION_TOOL_ID = "UnusedDeclaration"
    }
}
