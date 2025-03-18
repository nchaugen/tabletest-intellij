package io.github.nchaugen.tabletest.language

import com.intellij.openapi.fileTypes.LanguageFileType

object TableTestFileType: LanguageFileType(TableTestLanguage) {
    override fun getName() = "TableTest"
    override fun getDescription() = "TableTest language file"
    override fun getDefaultExtension() = "table"
    override fun getIcon() = TableTestIcons.FILE
}
