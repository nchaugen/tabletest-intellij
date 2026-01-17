package org.intellij.sdk.language;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.codeStyle.CodeStyleManager;

public class TableTestKotlinFormatterTest extends TableTestFormatterTestCase {

    public void testKotlinFormatter() {
        myFixture.setCaresAboutInjection(true);
        myFixture.configureByFile("FormatterTestData.kt");
        WriteCommandAction.writeCommandAction(getProject())
            .run(() ->
                CodeStyleManager.getInstance(getProject())
                    .reformat(myFixture.getFile())
            );
        myFixture.checkResultByFile("DefaultTestData.kt");
    }

    // Kotlin formatter tests with comments and blank lines are disabled due to an
    // IntelliJ test framework bug. When using LightJavaCodeInsightFixtureTestCase
    // with setCaresAboutInjection(true), formatting injected content in Kotlin raw
    // strings corrupts the text due to incorrect offset mapping. For example,
    // "one:1" becomes "o ne:1". This is a test-only issue - formatting works
    // correctly in the actual editor.
    // The equivalent test for Java text blocks works correctly.

//    public void testKotlinFormatterWithBlankLines() {
//        myFixture.setCaresAboutInjection(true);
//        myFixture.configureByFile("FormatterTestDataWithBlankLines.kt");
//        WriteCommandAction.writeCommandAction(getProject())
//            .run(() ->
//                CodeStyleManager.getInstance(getProject())
//                    .reformat(myFixture.getFile())
//            );
//        myFixture.checkResultByFile("DefaultTestDataWithBlankLines.kt");
//    }
//
//    public void testKotlinFormatterWithComments() {
//        myFixture.setCaresAboutInjection(true);
//        myFixture.configureByFile("FormatterTestDataWithComments.kt");
//        WriteCommandAction.writeCommandAction(getProject())
//            .run(() ->
//                CodeStyleManager.getInstance(getProject())
//                    .reformat(myFixture.getFile())
//            );
//        myFixture.checkResultByFile("DefaultTestDataWithComments.kt");
//    }
//
//    public void testKotlinFormatterWithBlankLinesAndComments() {
//        myFixture.setCaresAboutInjection(true);
//        myFixture.configureByFile("FormatterTestDataWithBlankLinesAndComments.kt");
//        WriteCommandAction.writeCommandAction(getProject())
//            .run(() ->
//                CodeStyleManager.getInstance(getProject())
//                    .reformat(myFixture.getFile())
//            );
//        myFixture.checkResultByFile("DefaultTestDataWithBlankLinesAndComments.kt");
//    }

    public void testKotlinFormatterAlignsTableWithCaretInsideTable() {
        // When caret is inside the table in Kotlin code, reformat command will
        // align data rows with the header row and align the columns of all rows
        format(
            "Test.kt", """
                class Test {
                    //language=tabletest
                    @TableTest(
                        \"""
                        header1|header2
                        a|b<caret>
                        \"""
                    )
                    fun test() {}
                }
                """
        );
        myFixture.checkResult("""
            class Test {
                //language=tabletest
                @TableTest(
                    \"""
                    header1 | header2
                    a       | b
                    \"""
                )
                fun test() {}
            }
            """);
    }

    public void testKotlinFormatterAlignsDataRowsWithHeaderWithMoreIndent() {
        format(
            "Test.kt", """
                class Test {
                    //language=tabletest
                    @TableTest(
                        \"""
                            header1 | header2
                        a|b<caret>
                        \"""
                    )
                    fun test() {}
                }
                """
        );
        myFixture.checkResult("""
            class Test {
                //language=tabletest
                @TableTest(
                    \"""
                        header1 | header2
                        a       | b
                    \"""
                )
                fun test() {}
            }
            """);
    }

    public void testKotlinFormatterAlignsDataRowsWithHeaderWithLessIndent() {
        format(
            "Test.kt", """
                class Test {
                    //language=tabletest
                    @TableTest(
                        \"""
                    header1 | header2
                        a|b<caret>
                        \"""
                    )
                    fun test() {}
                }
                """
        );
        myFixture.checkResult("""
            class Test {
                //language=tabletest
                @TableTest(
                    \"""
                header1 | header2
                a       | b
                    \"""
                )
                fun test() {}
            }
            """);
    }

    public void testKotlinFormatterAlignsDataRowsWithHeaderWithNoIndent() {
        format(
            "Test.kt", """
                class Test {
                    //language=tabletest
                    @TableTest(
                        \"""
                header1 | header2
                        a|b<caret>
                        \"""
                    )
                    fun test() {}
                }
                """
        );
        myFixture.checkResult("""
            class Test {
                //language=tabletest
                @TableTest(
                    \"""
            header1 | header2
            a       | b
                    \"""
                )
                fun test() {}
            }
            """);
    }

    public void testKotlinFormatterDoesNotReformatTableWhenCaretIsOutsideTable() {
        format(
            "Test.kt", """
                class Test {<caret>
                    //language=tabletest
                    @TableTest(
                        \"""
                    header1 | header2
                            a|b
                        \"""
                    )
                    fun test() {}
                }
                """
        );
        myFixture.checkResult("""
            class Test {
                //language=tabletest
                @TableTest(
                    \"""
                header1 | header2
                        a|b
                    \"""
                )
                fun test() {
                }
            }
            """);
    }

}
