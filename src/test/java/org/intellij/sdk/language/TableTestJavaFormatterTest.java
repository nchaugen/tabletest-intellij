package org.intellij.sdk.language;

public class TableTestJavaFormatterTest extends TableTestFormatterTestCase {

    public void testJavaFormatter() {
        formatFile("FormatterTestData.java");
        myFixture.checkResultByFile("DefaultTestData.java");
    }

    public void testJavaFormatterAlignsWithLeastIndentedRow() {
        format(
            "Test.java", """
                public class Test {
                    //language=tabletest
                    @TableTest(\"""
                            header1|header2
                        a|b<caret>
                            c|d
                        \""")
                    void test() {}
                }
                """
        );
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
                    header1 | header2
                    a       | b
                    c       | d
                    \""")
                void test() {}
            }
            """);
    }

    public void testJavaFormatterAddsContinuationIndentWithCaretOutsideTable() {
        // This test verifies that when caret is outside the injected table:
        // - The Java formatter does a continuation indent for text block
        // - The whole table shifts so that the row with least indentation is
        //   at the continuation
        format(
            "Test.java", """
                public class Test {
                    //language=tabletest
                    @TableTest(\"""
                    header1|header2
                                a|b
                                        c|d
                        \""")
                    void test() {}<caret>
                }
                """
        );
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
                        header1|header2
                                    a|b
                                            c|d
                        \""")
                void test() {
                }
            }
            """);
    }

    public void testJavaFormattingPlusTableTestFormattingAlignsTableAtContinuationIndent() {
        // This test verifies that combining Java formatting first
        // and TableTets formatting next gives a decent result
        format(
            "Test.java", """
                public class Test {
                    //language=tabletest
                    @TableTest(\"""
                    header1|header2
                                a|b
                                        c|d
                        \""")
                    void test() {}<caret>
                }
                """
        );
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
                        header1|header2
                                    a|b
                                            c|d
                        \""")
                void test() {
                }
            }
            """);
        format(
            "Test.java", """
                public class Test {
                    //language=tabletest
                    @TableTest(\"""
                            header1|header2
                                        a|b<caret>
                                                c|d
                            \""")
                    void test() {
                    }
                }
                """
        );
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
                        header1 | header2
                        a       | b
                        c       | d
                        \""")
                void test() {
                }
            }
            """);
    }

    public void testTableTestFormattingPlusJavaFormattingAlignsTableAtContinuationIndent() {
        // This test verifies that combining TableTest formatting first
        // and Java formatting next gives a decent result
        format(
            "Test.java", """
                public class Test {
                    //language=tabletest
                    @TableTest(\"""
                    header1|header2
                                a|b
                                   <caret>     c|d
                        \""")
                    void test() {}
                }
                """
        );
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
                header1 | header2
                a       | b
                c       | d
                    \""")
                void test() {}
            }
            """);
        format(
            "Test.java", """
                public class Test {<caret>
                    //language=tabletest
                    @TableTest(\"""
                    header1 | header2
                    a       | b
                    c       | d
                        \""")
                    void test() {}
                }
                """
        );
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
                        header1 | header2
                        a       | b
                        c       | d
                        \""")
                void test() {
                }
            }
            """);
    }

    public void testJavaFormatterAlignsRowImmediatelyAfterComment() {
        // Regression test: the row immediately following a comment line must align
        // with rows before the comment.
        format(
            "Test.java", """
                public class Test {
                    //language=tabletest
                    @TableTest(\"""
                        header1|header2
                        a|b
                        // comment
                        c|d
                        e|f<caret>
                        \""")
                    void test() {}
                }
                """
        );
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
                    header1 | header2
                    a       | b
                    // comment
                    c       | d
                    e       | f
                    \""")
                void test() {}
            }
            """);
    }

}
