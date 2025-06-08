package io.github.nchaugen.tabletest.language;

import com.intellij.psi.tree.IElementType;
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

COMMENT=[^\r\n]*
DATA_CHAR=[^ \t\r\n]
UNQUOTED_CHAR=[^|\[\{\"\' \t\r\n]
UNQUOTED_STRING=[^|\[\{\"\' \t\r\n]([^|\r\n]*[^| \t\r\n])?
DOUBLE_QUOTED_STRING=[^\"]+
SINGLE_QUOTED_STRING=[^\']+
MAP_KEY_STRING=[^|,:\[\]\{\}\"\' \t\r\n]([^|,:\[\]\r\n]*[^|,:\[\] \t\r\n])?
UNQUOTED_ELEMENT_STRING=[^|,:\[\]\{\}\"\' \t\r\n]([^,|:\]\}\r\n]*[^,|:\]\} \t\r\n])?

%state HEADER_ROW, DATA, DATA_ROW, COMMENT_LINE, DOUBLE_QUOTED, SINGLE_QUOTED, COMPOUND

%%

<YYINITIAL> {
    {UNQUOTED_CHAR} { yypushback(1); yybegin(HEADER_ROW); }
    {CRLF}          { return TableTestTypes.INITIAL_NEWLINE; }
 }

<HEADER_ROW> {
    {UNQUOTED_STRING}\? { return TableTestTypes.OUTPUT_HEADER; }
    {UNQUOTED_STRING}   { return TableTestTypes.INPUT_HEADER; }
    \|                  { return TableTestTypes.PIPE; }
    {CRLF}              { yybegin(DATA); return TableTestTypes.NEWLINE; }
}

<DATA> {
    "//"        { yybegin(COMMENT_LINE); return TableTestTypes.LINE_COMMENT; }
    {DATA_CHAR} { yypushback(1); yybegin(DATA_ROW); }
    {CRLF}      { return TableTestTypes.BLANK_LINE; }
}

<DATA_ROW> {
    \[\:\]             { return TableTestTypes.EMPTY_MAP; }
    \[                 { stateStack.push(yystate()); yybegin(COMPOUND); return TableTestTypes.LEFT_BRACKET; }
    \{                 { stateStack.push(yystate()); yybegin(COMPOUND); return TableTestTypes.LEFT_BRACE; }
    \"                 { stateStack.push(yystate()); yybegin(DOUBLE_QUOTED); return TableTestTypes.DOUBLE_QUOTE; }
    \'                 { stateStack.push(yystate()); yybegin(SINGLE_QUOTED); return TableTestTypes.SINGLE_QUOTE; }
    {UNQUOTED_STRING}  { return TableTestTypes.STRING_VALUE; }
    \|                 { return TableTestTypes.PIPE; }
    {CRLF}             { yybegin(DATA); return TableTestTypes.NEWLINE; }
}

<COMMENT_LINE> {
    {COMMENT} { return TableTestTypes.COMMENT; }
    {CRLF}    { yybegin(DATA); return TableTestTypes.NEWLINE; }
}

<DOUBLE_QUOTED> {
    \"                      { yybegin(stateStack.pop()); return TableTestTypes.DOUBLE_QUOTE; }
    {DOUBLE_QUOTED_STRING}  { return TableTestTypes.STRING_VALUE; }
}

<SINGLE_QUOTED> {
    \'                      { yybegin(stateStack.pop()); return TableTestTypes.SINGLE_QUOTE; }
    {SINGLE_QUOTED_STRING}  { return TableTestTypes.STRING_VALUE; }
}

<COMPOUND> {
    \]                  { yybegin(stateStack.pop()); return TableTestTypes.RIGHT_BRACKET; }
    \}                  { yybegin(stateStack.pop()); return TableTestTypes.RIGHT_BRACE; }
    \"                  { stateStack.push(yystate()); yybegin(DOUBLE_QUOTED); return TableTestTypes.DOUBLE_QUOTE; }
    \'                  { stateStack.push(yystate()); yybegin(SINGLE_QUOTED); return TableTestTypes.SINGLE_QUOTE; }
    \[\:\]              { return TableTestTypes.EMPTY_MAP; }
    \[                  { stateStack.push(yystate()); yybegin(COMPOUND); return TableTestTypes.LEFT_BRACKET; }
    \{                  { stateStack.push(yystate()); yybegin(COMPOUND); return TableTestTypes.LEFT_BRACE; }
    \,                  { return TableTestTypes.COMMA; }
    \:                  { return TableTestTypes.COLON; }
    {MAP_KEY_STRING}/[ \t]*\: { return TableTestTypes.MAP_KEY; }
    {UNQUOTED_ELEMENT_STRING} { return TableTestTypes.STRING_VALUE; }
}

{WHITESPACE}+           { return TokenType.WHITE_SPACE; }
[^]                     { return TokenType.BAD_CHARACTER; }
