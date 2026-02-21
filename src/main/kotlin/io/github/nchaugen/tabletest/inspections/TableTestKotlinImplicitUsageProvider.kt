package io.github.nchaugen.tabletest.inspections

import com.intellij.codeInsight.daemon.ImplicitUsageProvider
import com.intellij.psi.PsiElement

/**
 * Marks Kotlin @TypeConverter functions as implicitly used.
 */
class TableTestKotlinImplicitUsageProvider : ImplicitUsageProvider {
    override fun isImplicitUsage(element: PsiElement): Boolean =
        KotlinConverterAnnotationUtil.isConverterAnnotatedKotlin(element)

    override fun isImplicitRead(element: PsiElement): Boolean = false

    override fun isImplicitWrite(element: PsiElement): Boolean = false
}
