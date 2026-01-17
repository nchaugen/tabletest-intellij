package org.intellij.sdk.language;

import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;

public class TableTestMoveRowUpDownTest extends LightJavaCodeInsightFixtureTestCase {

    public void testMoveRowDown() {
        myFixture.configureByText(
            "test.table", """
                header
                <caret>row1
                row2
                """
        );
        myFixture.performEditorAction(IdeActions.ACTION_MOVE_STATEMENT_DOWN_ACTION);
        myFixture.checkResult("""
            header
            row2
            row1
            """);
    }

    public void testMoveRowUp() {
        myFixture.configureByText(
            "test.table", """
                header
                row1
                <caret>row2
                """
        );
        myFixture.performEditorAction(IdeActions.ACTION_MOVE_STATEMENT_UP_ACTION);
        myFixture.checkResult("""
            header
            row2
            row1
            """);
    }

    public void testMoveMultiColumnRow() {
        myFixture.configureByText(
            "test.table", """
                a | b | c
                <caret>1 | 2 | 3
                4 | 5 | 6
                """
        );
        myFixture.performEditorAction(IdeActions.ACTION_MOVE_STATEMENT_DOWN_ACTION);
        myFixture.checkResult("""
            a | b | c
            4 | 5 | 6
            1 | 2 | 3
            """);
    }

    public void testCannotMoveHeaderRow() {
        myFixture.configureByText(
            "test.table", """
                <caret>header
                row1
                """
        );
        myFixture.performEditorAction(IdeActions.ACTION_MOVE_STATEMENT_DOWN_ACTION);
        myFixture.checkResult("""
            header
            row1
            """);
    }

    public void testCannotMoveRowAboveHeader() {
        myFixture.configureByText(
            "test.table", """
                header
                <caret>row1
                row2
                """
        );
        myFixture.performEditorAction(IdeActions.ACTION_MOVE_STATEMENT_UP_ACTION);
        myFixture.checkResult("""
            header
            row1
            row2
            """);
    }

    public void testCannotMoveRowBelowEndingTripleQuotes() {
        myFixture.configureByText(
            "test.table", """
                header
                row1
                <caret>row2
                """
        );
        myFixture.performEditorAction(IdeActions.ACTION_MOVE_STATEMENT_DOWN_ACTION);
        myFixture.checkResult("""
            header
            row1
            row2
            """);
    }

    public void testMoveRowDownSwapsAroundComment() {
        // When moving a data row down, it swaps with the next data row
        // Comments stay in place - only data rows swap
        myFixture.configureByText(
            "test.table", """
                header
                <caret>row1
                // comment
                row2
                """
        );
        myFixture.performEditorAction(IdeActions.ACTION_MOVE_STATEMENT_DOWN_ACTION);
        myFixture.checkResult("""
            header
            row2
            // comment
            row1
            """);
    }

    public void testMoveRowUpSwapsAroundComment() {
        // When moving a data row up, it swaps with the previous data row
        // Comments stay in place - only data rows swap
        myFixture.configureByText(
            "test.table", """
                header
                row1
                // comment
                <caret>row2
                """
        );
        myFixture.performEditorAction(IdeActions.ACTION_MOVE_STATEMENT_UP_ACTION);
        myFixture.checkResult("""
            header
            row2
            // comment
            row1
            """);
    }

    public void testMoveRowDownSwapsAroundBlankLine() {
        // When moving a data row down, it swaps with the next data row
        // Blank lines stay in place - only data rows swap
        myFixture.configureByText(
            "test.table", """
                header
                <caret>row1

                row2
                """
        );
        myFixture.performEditorAction(IdeActions.ACTION_MOVE_STATEMENT_DOWN_ACTION);
        myFixture.checkResult("""
            header
            row2

            row1
            """);
    }

    public void testMoveRowUpSwapsAroundBlankLine() {
        // When moving a data row up, it swaps with the previous data row
        // Blank lines stay in place - only data rows swap
        myFixture.configureByText(
            "test.table", """
                header
                row1

                <caret>row2
                """
        );
        myFixture.performEditorAction(IdeActions.ACTION_MOVE_STATEMENT_UP_ACTION);
        myFixture.checkResult("""
            header
            row2

            row1
            """);
    }

    public void testMoveRowDownInJavaTextBlock() {
        myFixture.setCaresAboutInjection(true);
        myFixture.configureByText(
            "Test.java", """
                public class Test {
                    //language=tabletest
                    @TableTest(\"""
                        header
                        <caret>row1
                        row2
                        \""")
                    void test() {}
                }
                """
        );
        myFixture.performEditorAction(IdeActions.ACTION_MOVE_STATEMENT_DOWN_ACTION);
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
                    header
                    row2
                    row1
                    \""")
                void test() {}
            }
            """);
    }

    public void testMoveRowUpInJavaTextBlock() {
        myFixture.setCaresAboutInjection(true);
        myFixture.configureByText(
            "Test.java", """
                public class Test {
                    //language=tabletest
                    @TableTest(\"""
                        header
                        row1
                        <caret>row2
                        \""")
                    void test() {}
                }
                """
        );
        myFixture.performEditorAction(IdeActions.ACTION_MOVE_STATEMENT_UP_ACTION);
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
                    header
                    row2
                    row1
                    \""")
                void test() {}
            }
            """);
    }

    public void testCannotMoveRowAboveHeaderInJavaTextBlock() {
        myFixture.setCaresAboutInjection(true);
        myFixture.configureByText(
            "Test.java", """
                public class Test {
                    //language=tabletest
                    @TableTest(\"""
                        header
                        <caret>row1
                        row2
                        \""")
                    void test() {}
                }
                """
        );
        myFixture.performEditorAction(IdeActions.ACTION_MOVE_STATEMENT_UP_ACTION);
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
                    header
                    row1
                    row2
                    \""")
                void test() {}
            }
            """);
    }

    public void testMoveRowDownInKotlinRawString() {
        myFixture.setCaresAboutInjection(true);
        myFixture.configureByText(
            "Test.kt", """
                class Test {
                    //language=tabletest
                    @TableTest(\"""
                        header
                        <caret>row1
                        row2
                        \""")
                    fun test() {}
                }
                """
        );
        myFixture.performEditorAction(IdeActions.ACTION_MOVE_STATEMENT_DOWN_ACTION);
        myFixture.checkResult("""
            class Test {
                //language=tabletest
                @TableTest(\"""
                    header
                    row2
                    row1
                    \""")
                fun test() {}
            }
            """);
    }

    public void testCannotMoveRowBelowEndingTripleQuotesInJavaTextBlock() {
        myFixture.setCaresAboutInjection(true);
        myFixture.configureByText(
            "Test.java", """
                public class Test {
                    //language=tabletest
                    @TableTest(\"""
                        header
                        row1
                        <caret>row2
                        \""")
                    void test() {}
                }
                """
        );
        myFixture.performEditorAction(IdeActions.ACTION_MOVE_STATEMENT_DOWN_ACTION);
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
                    header
                    row1
                    row2
                    \""")
                void test() {}
            }
            """);
    }

    public void testCannotMoveRowBelowEndingTripleQuotesInKotlinRawString() {
        myFixture.setCaresAboutInjection(true);
        myFixture.configureByText(
            "Test.kt", """
                class Test {
                    //language=tabletest
                    @TableTest(\"""
                        header
                        row1
                        <caret>row2
                        \""")
                    fun test() {}
                }
                """
        );
        myFixture.performEditorAction(IdeActions.ACTION_MOVE_STATEMENT_DOWN_ACTION);
        myFixture.checkResult("""
            class Test {
                //language=tabletest
                @TableTest(\"""
                    header
                    row1
                    row2
                    \""")
                fun test() {}
            }
            """);
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/testData";
    }

}
