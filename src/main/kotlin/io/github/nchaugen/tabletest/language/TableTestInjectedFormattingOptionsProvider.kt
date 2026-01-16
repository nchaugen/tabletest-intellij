package io.github.nchaugen.tabletest.language

import com.intellij.formatting.InjectedFormattingOptionsProvider
import com.intellij.psi.PsiFile

class TableTestInjectedFormattingOptionsProvider : InjectedFormattingOptionsProvider {
    override fun shouldDelegateToTopLevel(psiFile: PsiFile): Boolean = psiFile !is TableTestFile
}
