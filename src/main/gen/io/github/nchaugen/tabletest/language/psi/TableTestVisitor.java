// This is a generated file. Not intended for manual editing.
package io.github.nchaugen.tabletest.language.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class TableTestVisitor extends PsiElementVisitor {

  public void visitCommentLine(@NotNull TableTestCommentLine o) {
    visitPsiElement(o);
  }

  public void visitElement(@NotNull TableTestElement o) {
    visitPsiElement(o);
  }

  public void visitHeader(@NotNull TableTestHeader o) {
    visitPsiElement(o);
  }

  public void visitHeaderRow(@NotNull TableTestHeaderRow o) {
    visitPsiElement(o);
  }

  public void visitList(@NotNull TableTestList o) {
    visitPsiElement(o);
  }

  public void visitMap(@NotNull TableTestMap o) {
    visitPsiElement(o);
  }

  public void visitMapEntry(@NotNull TableTestMapEntry o) {
    visitPsiElement(o);
  }

  public void visitRow(@NotNull TableTestRow o) {
    visitPsiElement(o);
  }

  public void visitString(@NotNull TableTestString o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
