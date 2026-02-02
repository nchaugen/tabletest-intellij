package io.github.nchaugen.tabletest.inspections

import com.intellij.codeInsight.daemon.ImplicitUsageProvider
import com.intellij.psi.PsiElement

class TableTestImplicitUsageProvider : ImplicitUsageProvider {
    override fun isImplicitUsage(element: PsiElement): Boolean =
        ConverterAnnotationUtil.isConverterAnnotatedJava(element) ||
            KotlinConverterAnnotationUtil.isConverterAnnotatedKotlin(element)

    override fun isImplicitRead(element: PsiElement): Boolean = false

    override fun isImplicitWrite(element: PsiElement): Boolean = false
}
