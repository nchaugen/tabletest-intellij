// This is a generated file. Not intended for manual editing.
package io.github.nchaugen.tabletest.language.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface TableTestElement extends PsiElement {

  @Nullable
  TableTestList getList();

  @Nullable
  TableTestMap getMap();

  @Nullable
  TableTestSet getSet();

  @Nullable
  TableTestString getString();

}
