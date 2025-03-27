package io.github.nchaugen.tabletest.language

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType.BAD_CHARACTER
import com.intellij.psi.tree.IElementType
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.COLON
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.COMMA
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.DOUBLE_QUOTE
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.EMPTY_MAP
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.MAP_KEY
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.INPUT_HEADER
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.LEFT_BRACKET
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.OUTPUT_HEADER
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.PIPE
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.RIGHT_BRACKET
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.SINGLE_QUOTE
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.STRING_VALUE


class TableTestSyntaxHighlighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer(): Lexer = TableTestLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey?> =
        when (tokenType) {
            INPUT_HEADER -> arrayOf(INPUT_HEADER_KEY)
            OUTPUT_HEADER -> arrayOf(OUTPUT_HEADER_KEY)
            PIPE -> arrayOf(COLUMN_SEPARATOR_KEY)
            DOUBLE_QUOTE, SINGLE_QUOTE -> arrayOf(QUOTE_KEY)
            MAP_KEY -> arrayOf(MAP_KEY_KEY)
            LEFT_BRACKET, RIGHT_BRACKET, EMPTY_MAP, COMMA, COLON -> arrayOf(LIST_PUNCTUATION_KEY)
            BAD_CHARACTER -> arrayOf(BAD_CHARACTER_KEY)
            STRING_VALUE -> arrayOf(VALUE_KEY)
            else -> arrayOf()
        }

    companion object {
        val INPUT_HEADER_KEY: TextAttributesKey =
            createTextAttributesKey("TABLE_TEST_INPUT_HEADER", DefaultLanguageHighlighterColors.STATIC_FIELD)
        val OUTPUT_HEADER_KEY: TextAttributesKey =
            createTextAttributesKey("TABLE_TEST_OUTPUT_HEADER", DefaultLanguageHighlighterColors.STATIC_FIELD)
        val MAP_KEY_KEY: TextAttributesKey =
            createTextAttributesKey("TABLE_TEST_MAP_KEY", DefaultLanguageHighlighterColors.STRING)
        val COLUMN_SEPARATOR_KEY: TextAttributesKey =
            createTextAttributesKey("TABLE_TEST_COLUMN_SEPARATOR", DefaultLanguageHighlighterColors.KEYWORD)
        val LIST_PUNCTUATION_KEY: TextAttributesKey =
            createTextAttributesKey("TABLE_TEST_LIST_PUNCTUATION", DefaultLanguageHighlighterColors.IDENTIFIER)
        val VALUE_KEY: TextAttributesKey =
            createTextAttributesKey("TABLE_TEST_VALUE", DefaultLanguageHighlighterColors.IDENTIFIER)
        val QUOTE_KEY: TextAttributesKey =
            createTextAttributesKey("TABLE_TEST_QUOTE", DefaultLanguageHighlighterColors.IDENTIFIER)
        val COMMENT_KEY: TextAttributesKey =
            createTextAttributesKey("TABLE_TEST_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val BAD_CHARACTER_KEY: TextAttributesKey =
            createTextAttributesKey("TABLE_TEST_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)
    }
}
