package io.github.nchaugen.tabletest.language

import com.intellij.lang.Language

object TableTestLanguage: Language("TableTest") {
    @Suppress("unused")
    private fun readResolve(): Any = TableTestLanguage
}
