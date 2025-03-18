package io.github.nchaugen.tabletest.language

import com.intellij.lang.Language

class TableTestLanguage: Language("TableTest") {
    companion object {
        val INSTANCE = TableTestLanguage()
    }

}
