package io.github.nchaugen.tabletest.inspections

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.asJava.elements.KtLightElement
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtNamedFunction

internal object KotlinConverterAnnotationUtil {
    private const val CONVERTER_ANNOTATION = "org.tabletest.junit.TypeConverter"
    private const val CONVERTER_SHORT_NAME = "TypeConverter"

    fun isConverterAnnotatedKotlin(element: PsiElement): Boolean {
        val function = findKotlinFunction(element) ?: return false
        return function.annotationEntries.any(::isConverterAnnotation)
    }

    private fun findKotlinFunction(element: PsiElement): KtNamedFunction? {
        PsiTreeUtil.getParentOfType(element, KtNamedFunction::class.java, false)?.let { return it }

        kotlinOriginFunction(element)?.let { return it }

        val navigation = element.navigationElement
        if (navigation != element) {
            PsiTreeUtil.getParentOfType(navigation, KtNamedFunction::class.java, false)?.let { return it }
            kotlinOriginFunction(navigation)?.let { return it }
        }

        val original = element.originalElement
        if (original != element) {
            PsiTreeUtil.getParentOfType(original, KtNamedFunction::class.java, false)?.let { return it }
            kotlinOriginFunction(original)?.let { return it }
        }

        return null
    }

    private fun kotlinOriginFunction(element: PsiElement): KtNamedFunction? {
        val origin = (element as? KtLightElement<*, *>)?.kotlinOrigin ?: return null
        return PsiTreeUtil.getParentOfType(origin, KtNamedFunction::class.java, false)
    }

    private fun isConverterAnnotation(entry: KtAnnotationEntry): Boolean {
        if (entry.shortName?.asString() != CONVERTER_SHORT_NAME) {
            return false
        }

        val typeReference = entry.typeReference?.text ?: return true
        if (!typeReference.contains('.')) {
            return true
        }
        return typeReference == CONVERTER_ANNOTATION
    }
}
