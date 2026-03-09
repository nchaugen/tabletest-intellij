package org.intellij.sdk.language;

import com.intellij.application.options.CodeStyle;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import io.github.nchaugen.tabletest.language.TableTestLanguage;

import java.util.List;
import java.util.function.Consumer;

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

    protected void checkResultInTopLevelFile(String expected) {
        PsiFile currentFile = myFixture.getFile();
        PsiFile topLevelFile = InjectedLanguageManager.getInstance(getProject()).getTopLevelFile(currentFile);
        assertEquals(expected, topLevelFile.getText());
    }

    protected void withTableTestSpacingSettings(
        Consumer<CommonCodeStyleSettings> configure,
        Runnable action
    ) {
        CommonCodeStyleSettings settings = CodeStyle.getSettings(getProject())
            .getCommonSettings(TableTestLanguage.INSTANCE);

        boolean originalSpaceBeforeComma = settings.SPACE_BEFORE_COMMA;
        boolean originalSpaceAfterComma = settings.SPACE_AFTER_COMMA;
        boolean originalSpaceBeforeColon = settings.SPACE_BEFORE_COLON;
        boolean originalSpaceAfterColon = settings.SPACE_AFTER_COLON;
        boolean originalSpaceWithinBrackets = settings.SPACE_WITHIN_BRACKETS;
        boolean originalSpaceWithinBraces = settings.SPACE_WITHIN_BRACES;

        configure.accept(settings);

        try {
            action.run();
        } finally {
            settings.SPACE_BEFORE_COMMA = originalSpaceBeforeComma;
            settings.SPACE_AFTER_COMMA = originalSpaceAfterComma;
            settings.SPACE_BEFORE_COLON = originalSpaceBeforeColon;
            settings.SPACE_AFTER_COLON = originalSpaceAfterColon;
            settings.SPACE_WITHIN_BRACKETS = originalSpaceWithinBrackets;
            settings.SPACE_WITHIN_BRACES = originalSpaceWithinBraces;
        }
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
