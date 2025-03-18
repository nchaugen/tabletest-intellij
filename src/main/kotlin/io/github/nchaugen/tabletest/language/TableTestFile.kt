package io.github.nchaugen.tabletest.language

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider

class TableTestFile(viewProvider: FileViewProvider): PsiFileBase(viewProvider, TableTestLanguage) {
    override fun getFileType() = TableTestFileType

    override fun toString() = "TableTest File"
}
