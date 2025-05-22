// This is a generated file. Not intended for manual editing.
package io.github.nchaugen.tabletest.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import io.github.nchaugen.tabletest.language.psi.TableTestElement;
import io.github.nchaugen.tabletest.language.psi.TableTestList;
import io.github.nchaugen.tabletest.language.psi.TableTestMap;
import io.github.nchaugen.tabletest.language.psi.TableTestSet;
import io.github.nchaugen.tabletest.language.psi.TableTestString;
import io.github.nchaugen.tabletest.language.psi.TableTestVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TableTestElementImpl extends ASTWrapperPsiElement implements TableTestElement {

  public TableTestElementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TableTestVisitor visitor) {
    visitor.visitElement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TableTestVisitor) accept((TableTestVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TableTestList getList() {
    return findChildByClass(TableTestList.class);
  }

  @Override
  @Nullable
  public TableTestMap getMap() {
    return findChildByClass(TableTestMap.class);
  }

  @Override
  @Nullable
  public TableTestSet getSet() {
    return findChildByClass(TableTestSet.class);
  }

  @Override
  @Nullable
  public TableTestString getString() {
    return findChildByClass(TableTestString.class);
  }

}
