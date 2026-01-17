package org.intellij.sdk.language;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;

import java.util.List;

public abstract class TableTestFormatterTestCase extends LightJavaCodeInsightFixtureTestCase {

    protected void format(String fileName, String content) {
        myFixture.setCaresAboutInjection(true);
        myFixture.configureByText(fileName, content);
        WriteCommandAction.writeCommandAction(getProject())
            .run(() ->
                CodeStyleManager.getInstance(getProject())
                    .reformatText(
                        myFixture.getFile(),
                        List.of(myFixture.getFile().getTextRange())
                    )
            );
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/testData";
    }
}
