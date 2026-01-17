package io.github.nchaugen.tabletest.language

import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.formatting.SpacingBuilder
import com.intellij.formatting.Wrap
import com.intellij.formatting.WrapType
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import io.github.nchaugen.tabletest.language.psi.TableTestTypes

/**
 * Block for comment lines in a table.
 *
 * Comment lines need to align with the first column of data rows. To achieve this,
 * the first token (LINE_COMMENT "//") gets the firstColumnAlignment shared with
 * the first cells of data rows.
 */
class TableTestCommentLineBlock(
    node: ASTNode,
    wrap: Wrap?,
    alignment: Alignment?,
    private val spacingBuilder: SpacingBuilder?,
    private val firstColumnAlignment: Alignment
) : AbstractBlock(node, wrap, alignment) {

    override fun buildChildren(): List<Block> {
        val blocks: MutableList<Block> = ArrayList()
        var child = node.firstChildNode
        var isFirstToken = true

        while (child != null) {
            if (child.elementType !in listOf(TokenType.WHITE_SPACE, TableTestTypes.NEWLINE)) {
                // First token (LINE_COMMENT "//") gets firstColumnAlignment
                val alignment = if (isFirstToken) {
                    isFirstToken = false
                    firstColumnAlignment
                } else {
                    null
                }
                blocks.add(
                    TableTestCommentTokenBlock(
                        child,
                        Wrap.createWrap(WrapType.NONE, false),
                        alignment,
                        spacingBuilder
                    )
                )
            }
            child = child.treeNext
        }
        return blocks
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? =
        spacingBuilder?.getSpacing(this, child1, child2)

    override fun isLeaf(): Boolean = false

    override fun getIndent(): Indent? = Indent.getNoneIndent()
}

/**
 * Leaf block for comment tokens (LINE_COMMENT and COMMENT).
 */
private class TableTestCommentTokenBlock(
    node: ASTNode,
    wrap: Wrap?,
    alignment: Alignment?,
    private val spacingBuilder: SpacingBuilder?
) : AbstractBlock(node, wrap, alignment) {

    override fun buildChildren(): List<Block> = emptyList()

    override fun getSpacing(child1: Block?, child2: Block): Spacing? =
        spacingBuilder?.getSpacing(this, child1, child2)

    override fun isLeaf(): Boolean = true

    override fun getIndent(): Indent? = Indent.getNoneIndent()
}
