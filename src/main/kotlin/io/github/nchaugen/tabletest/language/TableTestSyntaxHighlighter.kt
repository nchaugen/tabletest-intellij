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
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.HEADER
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.LEFT_BRACKET
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.PIPE
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.RIGHT_BRACKET


class TableTestSyntaxHighlighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer(): Lexer = TableTestLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey?> =
        when (tokenType) {
            HEADER -> HEADER_KEYS
            PIPE -> COLUMN_SEPARATOR_KEYS
            COMMA -> ELEMENT_SEPARATOR_KEYS
            COLON -> KEY_VALUE_SEPARATOR_KEYS
            DOUBLE_QUOTE -> QUOTE_KEYS
            LEFT_BRACKET, RIGHT_BRACKET -> BRACKET_KEYS
            BAD_CHARACTER -> BAD_CHAR_KEYS
            else -> STRING_KEYS
        }

    companion object {
        val HEADER_KEY: TextAttributesKey? =
            createTextAttributesKey("TABLE_TEST_HEADER", DefaultLanguageHighlighterColors.INSTANCE_METHOD)
        val SEPARATOR_KEY: TextAttributesKey? =
            createTextAttributesKey("TABLE_TEST_COLUMN_SEPARATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val ELEMENT_SEPARATOR_KEY: TextAttributesKey? =
            createTextAttributesKey("TABLE_TEST_ELEMENT_SEPARATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val KEY_VALUE_SEPARATOR_KEY: TextAttributesKey? =
            createTextAttributesKey("TABLE_TEST_KEY_VALUE_SEPARATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val STRING_KEY: TextAttributesKey? =
            createTextAttributesKey("TABLE_TEST_STRING", DefaultLanguageHighlighterColors.STRING)
        val BRACKETS_KEY: TextAttributesKey? =
            createTextAttributesKey("TABLE_TEST_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS)
        val QUOTES_KEY: TextAttributesKey? =
            createTextAttributesKey("TABLE_TEST_QUOTES", DefaultLanguageHighlighterColors.PARENTHESES)
        val COMMENT_KEY: TextAttributesKey? =
            createTextAttributesKey("TABLE_TEST_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val BAD_CHARACTER_KEY: TextAttributesKey? =
            createTextAttributesKey("TABLE_TEST_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)


        private val BAD_CHAR_KEYS = arrayOf<TextAttributesKey?>(BAD_CHARACTER_KEY)
        private val HEADER_KEYS = arrayOf<TextAttributesKey?>(HEADER_KEY)
        private val COLUMN_SEPARATOR_KEYS = arrayOf<TextAttributesKey?>(SEPARATOR_KEY)
        private val ELEMENT_SEPARATOR_KEYS = arrayOf<TextAttributesKey?>(ELEMENT_SEPARATOR_KEY)
        private val KEY_VALUE_SEPARATOR_KEYS = arrayOf<TextAttributesKey?>(KEY_VALUE_SEPARATOR_KEY)
        private val STRING_KEYS = arrayOf<TextAttributesKey?>(STRING_KEY)
        private val BRACKET_KEYS = arrayOf<TextAttributesKey?>(BRACKETS_KEY)
        private val QUOTE_KEYS = arrayOf<TextAttributesKey?>(QUOTES_KEY)
        private val COMMENT_KEYS = arrayOf<TextAttributesKey?>(COMMENT_KEY)
    }
}
