package io.github.nchaugen.tabletest.language

import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage

class TableTestColorSettingsPage : ColorSettingsPage {
    override fun getIcon() = TableTestIcons.FILE

    override fun getHighlighter() = TableTestSyntaxHighlighter()

    override fun getDemoText() = """
        Value | List             | Set    | Result?
        123   | [10, 20, 30]     | {a, b} | [a: 133, b: 143, c: 153]
        ""    | []               | {}     | [:]
        abc   | [10, '20', "30"] | {1, 2} | [a: [1, 2, 3], b: []]
        ]     | [,               | {      | [:
    """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap() = null

    override fun getAttributeDescriptors() =
        arrayOf(
            AttributesDescriptor("Column input header", TableTestSyntaxHighlighter.INPUT_HEADER_KEY),
            AttributesDescriptor("Column output header", TableTestSyntaxHighlighter.OUTPUT_HEADER_KEY),
            AttributesDescriptor("Column separator", TableTestSyntaxHighlighter.COLUMN_SEPARATOR_KEY),
            AttributesDescriptor("Value", TableTestSyntaxHighlighter.VALUE_KEY),
            AttributesDescriptor("Quote", TableTestSyntaxHighlighter.QUOTE_KEY),
            AttributesDescriptor("Element separators", TableTestSyntaxHighlighter.LIST_PUNCTUATION_KEY),
            AttributesDescriptor("Map key", TableTestSyntaxHighlighter.MAP_KEY_KEY),
            AttributesDescriptor("Bad value", TableTestSyntaxHighlighter.BAD_CHARACTER_KEY)
        )

    override fun getColorDescriptors(): Array<out ColorDescriptor?> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName() = "TableTest"
}
