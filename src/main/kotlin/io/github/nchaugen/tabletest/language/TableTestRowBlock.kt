package io.github.nchaugen.tabletest.language

import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.formatting.SpacingBuilder
import com.intellij.formatting.Wrap
import com.intellij.formatting.WrapType
import com.intellij.lang.ASTNode
import com.intellij.platform.diagnostic.telemetry.Indexes
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import io.github.nchaugen.tabletest.language.psi.TableTestTypes

class TableTestRowBlock(
    node: ASTNode,
    wrap: Wrap?,
    alignment: Alignment?,
    val spacingBuilder: SpacingBuilder?,
    val pipeAlignments: List<Alignment>
) : AbstractBlock(node, wrap, alignment) {

    override fun buildChildren(): List<Block> {
        val blocks: MutableList<Block> = ArrayList()
        var child = node.firstChildNode
        var columnIndex = 0

        while (child != null) {
            if (child.elementType !in listOf(TokenType.WHITE_SPACE, TableTestTypes.NEWLINE)) {
                val alignment = if (child.elementType == TableTestTypes.PIPE) {
                    pipeAlignments.getOrNull(columnIndex).also { columnIndex++}
                } else null
                val block = TableTestCellBlock(
                    child,
                    Wrap.createWrap(WrapType.NONE, false),
                    alignment,
                    spacingBuilder
                )
                blocks.add(block)
            }
            child = child.treeNext
        }
        return blocks
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? =
        spacingBuilder?.getSpacing(this, child1, child2)

    override fun isLeaf(): Boolean = node.firstChildNode == null

    override fun getIndent(): Indent? = Indent.getNoneIndent()
}
