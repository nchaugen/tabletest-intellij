// This is a generated file. Not intended for manual editing.
package io.github.nchaugen.tabletest.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static io.github.nchaugen.tabletest.language.psi.TableTestTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import io.github.nchaugen.tabletest.language.psi.*;

public class TableTestMapEntryImpl extends ASTWrapperPsiElement implements TableTestMapEntry {

  public TableTestMapEntryImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TableTestVisitor visitor) {
    visitor.visitMapEntry(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TableTestVisitor) accept((TableTestVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public TableTestElement getElement() {
    return findNotNullChildByClass(TableTestElement.class);
  }

}
