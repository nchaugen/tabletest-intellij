// This is a generated file. Not intended for manual editing.
package io.github.nchaugen.tabletest.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import io.github.nchaugen.tabletest.language.psi.TableTestElement;
import io.github.nchaugen.tabletest.language.psi.TableTestSet;
import io.github.nchaugen.tabletest.language.psi.TableTestVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TableTestSetImpl extends ASTWrapperPsiElement implements TableTestSet {

  public TableTestSetImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TableTestVisitor visitor) {
    visitor.visitSet(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TableTestVisitor) accept((TableTestVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<TableTestElement> getElementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TableTestElement.class);
  }

}
