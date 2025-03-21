package io.github.nchaugen.tabletest.language;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import io.github.nchaugen.tabletest.language.psi.TableTestTokenType;
import io.github.nchaugen.tabletest.language.psi.TableTestTypes;
import com.intellij.psi.TokenType;
import java.util.Stack;

%%

%class TableTestLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

%{
    Stack<Integer> stateStack = new Stack<>();
%}

CRLF=\R
WHITESPACE=[ \t]

UNQUOTED_CHAR=[^,:| \t\n\[\]\"]
UNQUOTED_STRING=[^,:| \t\n\[\]\"][^,:|\[\]\"\n]*[^,:| \t\n\[\]\"]
QUOTED_STRING=[^\"]+

%state DATA, QUOTED_STRING, LIST

%%

<YYINITIAL> {
    {UNQUOTED_STRING}   { return TableTestTypes.HEADER; }
    {UNQUOTED_CHAR}     { return TableTestTypes.HEADER; }
    \|                  { return TableTestTypes.PIPE; }
    {CRLF}              { yybegin(DATA); return TableTestTypes.NEWLINE; }
}

<DATA> {
    \[                  { stateStack.push(yystate()); yybegin(LIST); return TableTestTypes.LEFT_BRACKET; }
    \"                  { stateStack.push(yystate()); yybegin(QUOTED_STRING); return TableTestTypes.DOUBLE_QUOTE; }
    {UNQUOTED_STRING}   { return TableTestTypes.STRING_VALUE; }
    {UNQUOTED_CHAR}     { return TableTestTypes.STRING_VALUE; }
    \|                  { return TableTestTypes.PIPE; }
    {CRLF}              { return TableTestTypes.NEWLINE; }
}

<QUOTED_STRING> {
    \"                  { yybegin(stateStack.pop()); return TableTestTypes.DOUBLE_QUOTE; }
    {QUOTED_STRING}     { return TableTestTypes.STRING_VALUE; }
}

<LIST> {
    \]                  { yybegin(stateStack.pop()); return TableTestTypes.RIGHT_BRACKET; }
    \"                  { stateStack.push(yystate()); yybegin(QUOTED_STRING); return TableTestTypes.DOUBLE_QUOTE; }
    \[                  { stateStack.push(yystate()); yybegin(LIST); return TableTestTypes.LEFT_BRACKET; }
    \,                  { return TableTestTypes.COMMA; }
    \:                  { return TableTestTypes.COLON; }
    {UNQUOTED_STRING}   { return TableTestTypes.STRING_VALUE; }
}

{WHITESPACE}+           { return TokenType.WHITE_SPACE; }
