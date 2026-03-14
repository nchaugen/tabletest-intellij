package io.github.nchaugen.tabletest.inspections

import com.intellij.codeInsight.daemon.ImplicitUsageProvider
import com.intellij.psi.PsiElement

/**
 * Marks Java @TypeConverter methods as implicitly used.
 */
class TableTestImplicitUsageProvider : ImplicitUsageProvider {
    override fun isImplicitUsage(element: PsiElement): Boolean =
        isTableTestAnnotated(element) || ConverterAnnotationUtil.isConverterAnnotatedJava(element)

    private fun isTableTestAnnotated(element: PsiElement): Boolean {
        val method = com.intellij.psi.util.PsiTreeUtil.getParentOfType(element, com.intellij.psi.PsiMethod::class.java, false) ?: return false
        return method.hasAnnotation("io.github.nchaugen.tabletest.junit.TableTest") ||
                method.hasAnnotation("org.tabletest.junit.TableTest")
    }

    override fun isImplicitRead(element: PsiElement): Boolean = false

    override fun isImplicitWrite(element: PsiElement): Boolean = false
}
