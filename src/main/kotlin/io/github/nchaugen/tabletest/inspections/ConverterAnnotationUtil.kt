package io.github.nchaugen.tabletest.inspections

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil

internal object ConverterAnnotationUtil {
    internal const val CONVERTER_ANNOTATION = "org.tabletest.junit.TypeConverter"

    fun isConverterAnnotatedJava(element: PsiElement): Boolean {
        val method = PsiTreeUtil.getParentOfType(element, PsiMethod::class.java, false) ?: return false
        return method.hasAnnotation(CONVERTER_ANNOTATION)
    }
}
