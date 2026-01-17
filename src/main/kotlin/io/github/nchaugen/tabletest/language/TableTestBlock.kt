package io.github.nchaugen.tabletest.language

import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.formatting.SpacingBuilder
import com.intellij.formatting.Wrap
import com.intellij.formatting.WrapType
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType.WHITE_SPACE
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.psi.tree.TokenSet
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.BLANK_LINE
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.HEADER_ROW
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.PIPE
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.ROW

class TableTestBlock(
    node: ASTNode,
    wrap: Wrap?,
    alignment: Alignment?,
    private val spacingBuilder: SpacingBuilder?
) : AbstractBlock(node, wrap, alignment) {

    override fun buildChildren(): List<Block> {
        val blocks: MutableList<Block> = ArrayList()

        val pipeAlignments: List<Alignment> = this.node
            .getChildren(TokenSet.create(HEADER_ROW))
            .flatMap { it.getChildren(TokenSet.create(PIPE)).toList<ASTNode>() }
            .map { Alignment.createAlignment(true) }

        // Create alignment for first column to align header with data rows
        val firstColumnAlignment: Alignment = Alignment.createAlignment(true)

        var child = node.firstChildNode

        while (child != null) {
            if (child.elementType !in listOf(WHITE_SPACE, BLANK_LINE)) {
                val block = if (child.elementType in listOf(HEADER_ROW, ROW)) {
                    TableTestRowBlock(
                        child,
                        Wrap.createWrap(WrapType.NONE, false),
                        null,
                        spacingBuilder,
                        pipeAlignments,
                        firstColumnAlignment
                    )
                } else {
                    TableTestCommentLineBlock(
                        child,
                        Wrap.createWrap(WrapType.NONE, false),
                        null,
                        spacingBuilder
                    )
                }
                blocks.add(block)
            }
            child = child.treeNext
        }
        return blocks
    }

    override fun getIndent(): Indent? = Indent.getNoneIndent()

    override fun getSpacing(child1: Block?, child2: Block): Spacing? =
        spacingBuilder?.getSpacing(this, child1, child2)

    override fun isLeaf(): Boolean = node.firstChildNode == null

}
