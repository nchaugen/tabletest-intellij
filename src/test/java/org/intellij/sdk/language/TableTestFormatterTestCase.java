package org.intellij.sdk.language;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;

import java.util.List;

public abstract class TableTestFormatterTestCase extends LightJavaCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/testData";
    }

    protected void format(String fileName, String content) {
        myFixture.configureByText(fileName, content);
        reformatTable();
    }

    protected void formatFile(String filePath) {
        myFixture.configureByFile(filePath);
        reformatTable();
    }

    private void reformatTable() {
        // Uses reformatText() instead of reformat() as a workaround for an IntelliJ bug
        // where reformat() corrupts output when injected content has inconsistent indentation.
        // See https://platform.jetbrains.com/t/codestylemanager-reformat-produces-corrupted-output-for-injected-content-with-inconsistent-indentation/3504

        myFixture.setCaresAboutInjection(true);
        WriteCommandAction.writeCommandAction(getProject())
            .run(() ->
                CodeStyleManager.getInstance(getProject())
                    .reformatText(
                        myFixture.getFile(),
                        List.of(myFixture.getFile().getTextRange())
                    )
            );
    }
}
