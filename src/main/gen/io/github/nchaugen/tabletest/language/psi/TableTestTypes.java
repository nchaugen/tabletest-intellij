// This is a generated file. Not intended for manual editing.
package io.github.nchaugen.tabletest.language.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import io.github.nchaugen.tabletest.language.psi.impl.*;

public interface TableTestTypes {

  IElementType BLANK_LINE = new TableTestElementType("BLANK_LINE");
  IElementType COMMENT_LINE = new TableTestElementType("COMMENT_LINE");
  IElementType ELEMENT = new TableTestElementType("ELEMENT");
  IElementType HEADER = new TableTestElementType("HEADER");
  IElementType HEADER_ROW = new TableTestElementType("HEADER_ROW");
  IElementType LIST = new TableTestElementType("LIST");
  IElementType MAP = new TableTestElementType("MAP");
  IElementType MAP_ENTRY = new TableTestElementType("MAP_ENTRY");
  IElementType ROW = new TableTestElementType("ROW");
  IElementType STRING = new TableTestElementType("STRING");

  IElementType COLON = new TableTestTokenType(":");
  IElementType COMMA = new TableTestTokenType(",");
  IElementType COMMENT = new TableTestTokenType("COMMENT");
  IElementType DOUBLE_QUOTE = new TableTestTokenType("\"");
  IElementType EMPTY_MAP = new TableTestTokenType("EMPTY_MAP");
  IElementType INPUT_HEADER = new TableTestTokenType("INPUT_HEADER");
  IElementType LEFT_BRACKET = new TableTestTokenType("[");
  IElementType MAP_KEY = new TableTestTokenType("MAP_KEY");
  IElementType NEWLINE = new TableTestTokenType("\\n");
  IElementType OUTPUT_HEADER = new TableTestTokenType("OUTPUT_HEADER");
  IElementType PIPE = new TableTestTokenType("|");
  IElementType RIGHT_BRACKET = new TableTestTokenType("]");
  IElementType SINGLE_QUOTE = new TableTestTokenType("'");
  IElementType STRING_VALUE = new TableTestTokenType("STRING_VALUE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == BLANK_LINE) {
        return new TableTestBlankLineImpl(node);
      }
      else if (type == COMMENT_LINE) {
        return new TableTestCommentLineImpl(node);
      }
      else if (type == ELEMENT) {
        return new TableTestElementImpl(node);
      }
      else if (type == HEADER) {
        return new TableTestHeaderImpl(node);
      }
      else if (type == HEADER_ROW) {
        return new TableTestHeaderRowImpl(node);
      }
      else if (type == LIST) {
        return new TableTestListImpl(node);
      }
      else if (type == MAP) {
        return new TableTestMapImpl(node);
      }
      else if (type == MAP_ENTRY) {
        return new TableTestMapEntryImpl(node);
      }
      else if (type == ROW) {
        return new TableTestRowImpl(node);
      }
      else if (type == STRING) {
        return new TableTestStringImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
