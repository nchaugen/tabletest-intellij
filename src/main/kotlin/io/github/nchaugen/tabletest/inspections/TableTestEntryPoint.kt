package io.github.nchaugen.tabletest.inspections

import com.intellij.codeInspection.reference.EntryPoint
import com.intellij.codeInspection.reference.RefElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import org.jdom.Element

class TableTestEntryPoint : EntryPoint() {
    override fun getDisplayName(): String = "TableTest @TypeConverter"

    override fun isEntryPoint(refElement: RefElement, psiElement: PsiElement): Boolean =
        isEntryPoint(psiElement)

    override fun isEntryPoint(psiElement: PsiElement): Boolean =
        isTableTestAnnotated(psiElement) || ConverterAnnotationUtil.isConverterAnnotatedJava(psiElement)

    private fun isTableTestAnnotated(element: PsiElement): Boolean {
        val method = PsiTreeUtil.getParentOfType(element, PsiMethod::class.java, false) ?: return false
        return method.hasAnnotation("io.github.nchaugen.tabletest.junit.TableTest") ||
                method.hasAnnotation("org.tabletest.junit.TableTest")
    }

    override fun isSelected(): Boolean = true

    override fun setSelected(selected: Boolean) = Unit

    override fun getIgnoreAnnotations(): Array<String> =
        arrayOf(
            ConverterAnnotationUtil.CONVERTER_ANNOTATION,
            "io.github.nchaugen.tabletest.junit.TableTest",
            "org.tabletest.junit.TableTest"
        )

    override fun readExternal(element: Element?) = Unit

    override fun writeExternal(element: Element?) = Unit
}
