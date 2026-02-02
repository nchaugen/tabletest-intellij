package io.github.nchaugen.tabletest.inspections

import com.intellij.codeInspection.reference.EntryPoint
import com.intellij.codeInspection.reference.RefElement
import com.intellij.psi.PsiElement
import org.jdom.Element

class TableTestEntryPoint : EntryPoint() {
    override fun getDisplayName(): String = "TableTest @Converter"

    override fun isEntryPoint(refElement: RefElement, psiElement: PsiElement): Boolean =
        isEntryPoint(psiElement)

    override fun isEntryPoint(psiElement: PsiElement): Boolean =
        ConverterAnnotationUtil.isConverterAnnotatedJava(psiElement)

    override fun isSelected(): Boolean = true

    override fun setSelected(selected: Boolean) = Unit

    override fun getIgnoreAnnotations(): Array<String> =
        arrayOf(ConverterAnnotationUtil.CONVERTER_ANNOTATION)

    override fun readExternal(element: Element?) = Unit

    override fun writeExternal(element: Element?) = Unit
}
