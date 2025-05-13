package org.intellij.sdk.language;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;

import java.util.List;

public class TableTestCodeInsightTest extends LightJavaCodeInsightFixtureTestCase {

    public void testAnnotator() {
        myFixture.configureByFiles("AnnotatorTestData.java");
        myFixture.checkHighlighting(false, false, false, true);
    }

    public void testFormatter() {
        myFixture.configureByFile("FormatterTestData.table");
        WriteCommandAction.writeCommandAction(getProject())
            .run(() ->
                     CodeStyleManager.getInstance(getProject())
                         .reformatText(
                             myFixture.getFile(),
                             List.of(myFixture.getFile().getTextRange())
                         )
            );
        myFixture.checkResultByFile("DefaultTestData.table");
    }

    public void testJavaFormatter() {
        myFixture.setCaresAboutInjection(true);
        myFixture.configureByFile("FormatterTestData.java");
        WriteCommandAction.writeCommandAction(getProject())
            .run(() ->
                     CodeStyleManager.getInstance(getProject())
                         .reformat(myFixture.getFile())
            );
        myFixture.checkResultByFile("DefaultTestData.java");
    }

//    @Ignore("Kotlin test not working yet")
//    public void testKotlinFormatter() {
//        myFixture.setCaresAboutInjection(true);
//        myFixture.configureByFile("FormatterTestData.kt");
//        WriteCommandAction.writeCommandAction(getProject())
//            .run(() ->
//                     CodeStyleManager.getInstance(getProject())
//                         .reformat(myFixture.getFile())
//            );
//        myFixture.checkResultByFile("DefaultTestData.kt");
//    }

    @Override
    protected String getTestDataPath() {
        return "src/test/testData";
    }

    //        MavenDependencyUtil
    //            .addFromMaven(
    //                ModuleRootManager.getInstance(getModule()).getModifiableModel(),
    //                "io.github.nchaugen:tabletest-junit:0.1.0"
    //            );

}
