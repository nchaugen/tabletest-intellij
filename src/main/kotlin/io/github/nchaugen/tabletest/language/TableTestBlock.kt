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
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.HEADER_ROW
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.NEWLINE
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.PIPE
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.ROW

class TableTestBlock(
    node: ASTNode,
    wrap: Wrap?,
    alignment: Alignment?,
    val spacingBuilder: SpacingBuilder?,
    val injectedInKotlin: Boolean
) : AbstractBlock(node, wrap, alignment) {

    override fun buildChildren(): List<Block> {
        val blocks: MutableList<Block> = ArrayList()

        val pipeAlignments: List<Alignment> = this.node
            .getChildren(TokenSet.create(HEADER_ROW))
            .flatMap { it.getChildren(TokenSet.create(PIPE)).toList<ASTNode>() }
            .map { Alignment.createAlignment(true) }

        var child = node.firstChildNode

        while (child != null) {
            if (child.elementType !in listOf(WHITE_SPACE, NEWLINE)) {
                val block = if (child.elementType in listOf(HEADER_ROW, ROW)) {
                    TableTestRowBlock(
                        child,
                        Wrap.createWrap(WrapType.NONE, false),
                        null,
                        spacingBuilder,
                        pipeAlignments,
                        injectedInKotlin
                    )
                } else {
                    TableTestUnformattedLineBlock(
                        child,
                        Wrap.createWrap(WrapType.NONE, false),
                        null,
                        spacingBuilder,
                        injectedInKotlin
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
