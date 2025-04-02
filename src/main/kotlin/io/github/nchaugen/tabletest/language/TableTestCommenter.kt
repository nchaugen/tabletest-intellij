package io.github.nchaugen.tabletest.language

import com.intellij.lang.Commenter

class TableTestCommenter : Commenter {
    override fun getLineCommentPrefix() = "// "

    override fun getBlockCommentPrefix() = null

    override fun getBlockCommentSuffix() = null

    override fun getCommentedBlockCommentPrefix() = null

    override fun getCommentedBlockCommentSuffix() = null
}
