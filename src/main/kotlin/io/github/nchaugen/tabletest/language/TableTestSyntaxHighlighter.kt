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
            INPUT_HEADER -> INPUT_HEADER_KEYS
            OUTPUT_HEADER -> OUTPUT_HEADER_KEYS
            PIPE -> COLUMN_SEPARATOR_KEYS
            DOUBLE_QUOTE, SINGLE_QUOTE -> QUOTE_KEYS
            MAP_KEY -> MAP_KEY_KEYS
            LEFT_BRACKET, RIGHT_BRACKET, EMPTY_MAP, COMMA, COLON -> LIST_KEYS
            BAD_CHARACTER -> BAD_CHAR_KEYS
            STRING_VALUE -> STRING_KEYS
            else -> EMPTY_KEYS
        }

    companion object {
        val INPUT_HEADER_KEY: TextAttributesKey? =
            createTextAttributesKey("TABLE_TEST_INPUT_HEADER", DefaultLanguageHighlighterColors.INSTANCE_METHOD)
        val OUTPUT_HEADER_KEY: TextAttributesKey? =
            createTextAttributesKey("TABLE_TEST_OUTPUT_HEADER", DefaultLanguageHighlighterColors.STATIC_METHOD)
        val MAP_KEY_KEY: TextAttributesKey? =
            createTextAttributesKey("TABLE_TEST_MAP_KEY", DefaultLanguageHighlighterColors.KEYWORD)
        val SEPARATOR_KEY: TextAttributesKey? =
            createTextAttributesKey("TABLE_TEST_COLUMN_SEPARATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val LIST_KEY: TextAttributesKey? =
            createTextAttributesKey("TABLE_TEST_LIST", DefaultLanguageHighlighterColors.BRACKETS)
        val STRING_KEY: TextAttributesKey? =
            createTextAttributesKey("TABLE_TEST_STRING", DefaultLanguageHighlighterColors.STRING)
        val QUOTES_KEY: TextAttributesKey? =
            createTextAttributesKey("TABLE_TEST_QUOTES", DefaultLanguageHighlighterColors.PARENTHESES)
        val COMMENT_KEY: TextAttributesKey? =
            createTextAttributesKey("TABLE_TEST_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val BAD_CHARACTER_KEY: TextAttributesKey? =
            createTextAttributesKey("TABLE_TEST_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)


        private val BAD_CHAR_KEYS = arrayOf<TextAttributesKey?>(BAD_CHARACTER_KEY)
        private val INPUT_HEADER_KEYS = arrayOf<TextAttributesKey?>(INPUT_HEADER_KEY)
        private val OUTPUT_HEADER_KEYS = arrayOf<TextAttributesKey?>(OUTPUT_HEADER_KEY)
        private val MAP_KEY_KEYS = arrayOf<TextAttributesKey?>(MAP_KEY_KEY)
        private val COLUMN_SEPARATOR_KEYS = arrayOf<TextAttributesKey?>(SEPARATOR_KEY)
        private val LIST_KEYS = arrayOf<TextAttributesKey?>(LIST_KEY)
        private val STRING_KEYS = arrayOf<TextAttributesKey?>(STRING_KEY)
        private val QUOTE_KEYS = arrayOf<TextAttributesKey?>(QUOTES_KEY)
        private val COMMENT_KEYS = arrayOf<TextAttributesKey?>(COMMENT_KEY)
        private val EMPTY_KEYS = arrayOf<TextAttributesKey?>()
    }
}
