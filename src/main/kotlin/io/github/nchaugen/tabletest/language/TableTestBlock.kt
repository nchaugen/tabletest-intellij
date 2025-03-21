package io.github.nchaugen.tabletest.language

import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.Spacing
import com.intellij.formatting.SpacingBuilder
import com.intellij.formatting.Wrap
import com.intellij.formatting.WrapType
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import io.github.nchaugen.tabletest.language.psi.TableTestTypes

class TableTestBlock(
    node: ASTNode,
    wrap: Wrap?,
    alignment: Alignment?,
    val spacingBuilder: SpacingBuilder?,
    val columnAlignments: MutableList<Alignment> = ArrayList(),
    val columnWidths: MutableList<Int> = ArrayList()
) : AbstractBlock(node, wrap, alignment) {

    init {
        calculateColumnWidths(node)
    }

    private fun calculateColumnWidths(node: ASTNode) {
        var child = node.firstChildNode
        var columnIndex = 0

        while (child != null) {
            if (child.elementType in listOf(TableTestTypes.ROW, TableTestTypes.HEADER_ROW)) {
                var element = child.firstChildNode
                columnIndex = 0
                while (element != null) {
                    if (element.elementType in listOf(TableTestTypes.ELEMENT, TableTestTypes.STRING, TableTestTypes.HEADER)) {
                        val width = element.textLength + 1
                        if (columnIndex >= columnWidths.size) {
                            columnWidths.add(width)
                        } else {
                            columnWidths[columnIndex] = maxOf(columnWidths[columnIndex], width)
                        }
                        columnIndex++
                    }
                    element = element.treeNext
                }
            }
            child = child.treeNext
        }
    }

    override fun buildChildren(): List<Block> {
        val blocks: MutableList<Block> = ArrayList()
        var child = node.firstChildNode
        var columnIndex = 0

        while (child != null) {
            if (child.elementType !== TokenType.WHITE_SPACE) {
                var columnAlignment: Alignment? = null
                if (child.elementType == TableTestTypes.PIPE) {
                    if (columnIndex >= columnAlignments.size) {
                        columnAlignment = Alignment.createAlignment()
                        columnAlignments.add(columnAlignment)
                    } else {
                        columnAlignment = columnAlignments[columnIndex]
                    }
                    columnIndex++
                } else if (child.elementType == TableTestTypes.ROW) {
                    columnIndex = 0 // Reset columnIndex for each new row
                }
                val block = TableTestBlock(
                    child,
                    Wrap.createWrap(WrapType.NONE, false),
                    columnAlignment,
                    spacingBuilder,
                    columnAlignments,
                    columnWidths
                )
                blocks.add(block)
            }
            child = child.treeNext
        }
        return blocks
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? =
        when {
            child1 is TableTestBlock && child2 is TableTestBlock && child2.isPipe() ->
                calculatePadding(child1, child2.alignment).let { padding ->
                    Spacing.createSpacing(padding, padding, 0, true, 0)
                }

            else -> spacingBuilder?.getSpacing(this, child1, child2)
        }

    override fun isLeaf(): Boolean = node.firstChildNode == null

    private fun calculatePadding(block: TableTestBlock, alignment: Alignment?): Int =
        columnWidth(alignment)?.let { it - width(block) } ?: 0

    private fun width(block: TableTestBlock): Int =
        when {
            block.isPipe() -> -1
            else -> block.node.textLength
        }

    private fun columnWidth(alignment: Alignment?): Int? = columnWidths.getOrNull(columnAlignments.indexOf(alignment))

    private fun isPipe(): Boolean = this.node.elementType == TableTestTypes.PIPE
}
