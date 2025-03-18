package io.github.nchaugen.tabletest.language

import com.intellij.lexer.FlexAdapter

class TableTestLexerAdapter : FlexAdapter(TableTestLexer(null))
