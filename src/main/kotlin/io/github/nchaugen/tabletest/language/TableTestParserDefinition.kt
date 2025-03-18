package io.github.nchaugen.tabletest.language

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import io.github.nchaugen.tabletest.language.parser.TableTestParser
import io.github.nchaugen.tabletest.language.psi.TableTestTypes

class TableTestParserDefinition: ParserDefinition {

    override fun createLexer(project: Project?): Lexer = TableTestLexerAdapter()

    override fun createParser(p0: Project?): PsiParser = TableTestParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getCommentTokens(): TokenSet = TokenSet.EMPTY

    override fun getStringLiteralElements(): TokenSet = TableTestTokenSets.STRINGS

    override fun createElement(node: ASTNode?): PsiElement = TableTestTypes.Factory.createElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = TableTestFile(viewProvider)

}

val FILE = IFileElementType(TableTestLanguage)
