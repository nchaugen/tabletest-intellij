// This is a generated file. Not intended for manual editing.
package io.github.nchaugen.tabletest.language.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static io.github.nchaugen.tabletest.language.psi.TableTestTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class TableTestParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return table(b, l + 1);
  }

  /* ********************************************************** */
  // '//' COMMENT '\n'
  public static boolean comment_line(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comment_line")) return false;
    if (!nextTokenIs(b, LINE_COMMENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LINE_COMMENT, COMMENT, NEWLINE);
    exit_section_(b, m, COMMENT_LINE, r);
    return r;
  }

  /* ********************************************************** */
  // list | map | string
  public static boolean element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "element")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ELEMENT, "<element>");
    r = list(b, l + 1);
    if (!r) r = map(b, l + 1);
    if (!r) r = string(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // INPUT_HEADER | OUTPUT_HEADER
  public static boolean header(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header")) return false;
    if (!nextTokenIs(b, "<header>", INPUT_HEADER, OUTPUT_HEADER)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, HEADER, "<header>");
    r = consumeToken(b, INPUT_HEADER);
    if (!r) r = consumeToken(b, OUTPUT_HEADER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // header ('|' header)* '\n'
  public static boolean header_row(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_row")) return false;
    if (!nextTokenIs(b, "<header row>", INPUT_HEADER, OUTPUT_HEADER)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, HEADER_ROW, "<header row>");
    r = header(b, l + 1);
    r = r && header_row_1(b, l + 1);
    r = r && consumeToken(b, NEWLINE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ('|' header)*
  private static boolean header_row_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_row_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!header_row_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "header_row_1", c)) break;
    }
    return true;
  }

  // '|' header
  private static boolean header_row_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_row_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PIPE);
    r = r && header(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '[' (element (',' element)* )? ']'
  public static boolean list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list")) return false;
    if (!nextTokenIs(b, LEFT_BRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LEFT_BRACKET);
    r = r && list_1(b, l + 1);
    r = r && consumeToken(b, RIGHT_BRACKET);
    exit_section_(b, m, LIST, r);
    return r;
  }

  // (element (',' element)* )?
  private static boolean list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_1")) return false;
    list_1_0(b, l + 1);
    return true;
  }

  // element (',' element)*
  private static boolean list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = element(b, l + 1);
    r = r && list_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (',' element)*
  private static boolean list_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!list_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "list_1_0_1", c)) break;
    }
    return true;
  }

  // ',' element
  private static boolean list_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && element(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '[' map_entry (',' map_entry)* ']' | EMPTY_MAP
  public static boolean map(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "map")) return false;
    if (!nextTokenIs(b, "<map>", EMPTY_MAP, LEFT_BRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MAP, "<map>");
    r = map_0(b, l + 1);
    if (!r) r = consumeToken(b, EMPTY_MAP);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '[' map_entry (',' map_entry)* ']'
  private static boolean map_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "map_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LEFT_BRACKET);
    r = r && map_entry(b, l + 1);
    r = r && map_0_2(b, l + 1);
    r = r && consumeToken(b, RIGHT_BRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  // (',' map_entry)*
  private static boolean map_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "map_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!map_0_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "map_0_2", c)) break;
    }
    return true;
  }

  // ',' map_entry
  private static boolean map_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "map_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && map_entry(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // MAP_KEY ':' element
  public static boolean map_entry(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "map_entry")) return false;
    if (!nextTokenIs(b, MAP_KEY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, MAP_KEY, COLON);
    r = r && element(b, l + 1);
    exit_section_(b, m, MAP_ENTRY, r);
    return r;
  }

  /* ********************************************************** */
  // element ('|' element?)* '\n'
  public static boolean row(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "row")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ROW, "<row>");
    r = element(b, l + 1);
    r = r && row_1(b, l + 1);
    r = r && consumeToken(b, NEWLINE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ('|' element?)*
  private static boolean row_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "row_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!row_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "row_1", c)) break;
    }
    return true;
  }

  // '|' element?
  private static boolean row_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "row_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PIPE);
    r = r && row_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // element?
  private static boolean row_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "row_1_0_1")) return false;
    element(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // ('"' STRING_VALUE? '"') | ("'" STRING_VALUE? "'") | STRING_VALUE
  public static boolean string(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STRING, "<string>");
    r = string_0(b, l + 1);
    if (!r) r = string_1(b, l + 1);
    if (!r) r = consumeToken(b, STRING_VALUE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '"' STRING_VALUE? '"'
  private static boolean string_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOUBLE_QUOTE);
    r = r && string_0_1(b, l + 1);
    r = r && consumeToken(b, DOUBLE_QUOTE);
    exit_section_(b, m, null, r);
    return r;
  }

  // STRING_VALUE?
  private static boolean string_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_0_1")) return false;
    consumeToken(b, STRING_VALUE);
    return true;
  }

  // "'" STRING_VALUE? "'"
  private static boolean string_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SINGLE_QUOTE);
    r = r && string_1_1(b, l + 1);
    r = r && consumeToken(b, SINGLE_QUOTE);
    exit_section_(b, m, null, r);
    return r;
  }

  // STRING_VALUE?
  private static boolean string_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_1_1")) return false;
    consumeToken(b, STRING_VALUE);
    return true;
  }

  /* ********************************************************** */
  // (INITIAL_NEWLINE | comment_line)* header_row (BLANK_LINE | comment_line | row)*
  static boolean table(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = table_0(b, l + 1);
    r = r && header_row(b, l + 1);
    r = r && table_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (INITIAL_NEWLINE | comment_line)*
  private static boolean table_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!table_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "table_0", c)) break;
    }
    return true;
  }

  // INITIAL_NEWLINE | comment_line
  private static boolean table_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_0_0")) return false;
    boolean r;
    r = consumeToken(b, INITIAL_NEWLINE);
    if (!r) r = comment_line(b, l + 1);
    return r;
  }

  // (BLANK_LINE | comment_line | row)*
  private static boolean table_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!table_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "table_2", c)) break;
    }
    return true;
  }

  // BLANK_LINE | comment_line | row
  private static boolean table_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_2_0")) return false;
    boolean r;
    r = consumeToken(b, BLANK_LINE);
    if (!r) r = comment_line(b, l + 1);
    if (!r) r = row(b, l + 1);
    return r;
  }

}
