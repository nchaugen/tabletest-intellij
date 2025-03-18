package io.github.nchaugen.tabletest.language.psi

import com.intellij.psi.tree.IElementType
import io.github.nchaugen.tabletest.language.TableTestLanguage

class TableTestTokenType : IElementType {
    constructor(debugName: String) : super(debugName, TableTestLanguage)
}
