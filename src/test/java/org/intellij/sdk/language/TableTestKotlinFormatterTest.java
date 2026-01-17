package org.intellij.sdk.language;

public class TableTestKotlinFormatterTest extends TableTestFormatterTestCase {

    public void testKotlinFormatter() {
        formatFile("FormatterTestData.kt");
        myFixture.checkResultByFile("DefaultTestData.kt");
    }

    public void testKotlinFormatterWithBlankLines() {
        formatFile("FormatterTestDataWithBlankLines.kt");
        myFixture.checkResultByFile("DefaultTestDataWithBlankLines.kt");
    }

    public void testKotlinFormatterWithComments() {
        formatFile("FormatterTestDataWithComments.kt");
        myFixture.checkResultByFile("DefaultTestDataWithComments.kt");
    }

    public void testKotlinFormatterWithBlankLinesAndComments() {
        formatFile("FormatterTestDataWithBlankLinesAndComments.kt");
        myFixture.checkResultByFile("DefaultTestDataWithBlankLinesAndComments.kt");
    }

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

    public void testKotlinFormatterMinimalBlankLine() {
        // Minimal test case to isolate the blank line bug:
        // Pipe alignment spans across a blank line - does offset mapping break?
        format(
            "Test.kt", """
                class Test {
                    //language=tabletest
                    @TableTest(
                        \"""
                        a|b

                        c|d<caret>
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
                    a | b

                    c | d
                    \"""
                )
                fun test() {}
            }
            """);
    }

    public void testKotlinFormatterAlignsRowImmediatelyAfterComment() {
        // Regression test: the row immediately following a comment line must align
        // with rows before the comment. Previously, only the first row after a comment
        // was misaligned; subsequent rows aligned correctly with each other.
        format(
            "Test.kt", """
                class Test {
                    //language=tabletest
                    @TableTest(
                        \"""
                        header1|header2
                        a|b
                        // comment
                        c|d
                        e|f<caret>
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
                    // comment
                    c       | d
                    e       | f
                    \"""
                )
                fun test() {}
            }
            """);
    }

    public void testKotlinFormatterMultipleRowsWithBlankLine() {
        // More rows to see if volume matters
        format(
            "Test.kt", """
                class Test {
                    //language=tabletest
                    @TableTest(
                        \"""
                        header1|header2|header3
                        a|b|c
                        d|e|f

                        g|h|i<caret>
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
                    header1 | header2 | header3
                    a       | b       | c
                    d       | e       | f

                    g       | h       | i
                    \"""
                )
                fun test() {}
            }
            """);
    }

}
