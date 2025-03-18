package io.github.nchaugen.tabletest.language

import com.intellij.psi.tree.TokenSet
import io.github.nchaugen.tabletest.language.psi.TableTestTypes

interface TableTestTokenSets {
    companion object {
        val STRINGS: TokenSet
            get() = TokenSet.create(TableTestTypes.STRING)
    }
}
