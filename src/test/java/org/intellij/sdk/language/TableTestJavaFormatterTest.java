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

    public void testJavaFormatterArrayLiteralAlignsAcrossElements() {
        myFixture.addFileToProject(
            "org/tabletest/junit/TableTest.java",
            """
            package org.tabletest.junit;

            import java.lang.annotation.*;

            @Retention(RetentionPolicy.RUNTIME)
            @Target(ElementType.METHOD)
            public @interface TableTest {
                String[] value();
            }
            """
        );

        format(
            "Test.java", """
                import org.tabletest.junit.TableTest;

                public class Test {
                    @TableTest({
                        "a|bb",
                        "longer|c<caret>"
                    })
                    void test() {}
                }
                """
        );
        checkResultInTopLevelFile("""
            import org.tabletest.junit.TableTest;

            public class Test {
                @TableTest({
                    "a      | bb",
                    "longer | c\s"
                })
                void test() {}
            }
            """);
    }

    public void testJavaFormatterArrayLiteralPreservesBlankRowElement() {
        myFixture.addFileToProject(
            "org/tabletest/junit/TableTest.java",
            """
            package org.tabletest.junit;

            import java.lang.annotation.*;

            @Retention(RetentionPolicy.RUNTIME)
            @Target(ElementType.METHOD)
            public @interface TableTest {
                String[] value();
            }
            """
        );

        format(
            "Test.java", """
                import org.tabletest.junit.TableTest;

                public class Test {
                    @TableTest({
                        "aa|b",
                        "",
                        "c|dddd<caret>"
                    })
                    void test() {}
                }
                """
        );
        checkResultInTopLevelFile("""
            import org.tabletest.junit.TableTest;

            public class Test {
                @TableTest({
                    "aa | b\s\s\s",
                    "",
                    "c  | dddd"
                })
                void test() {}
            }
            """);
    }

    public void testJavaFormatterArrayLiteralAlignsClosingQuotesAcrossRows() {
        myFixture.addFileToProject(
            "org/tabletest/junit/TableTest.java",
            """
            package org.tabletest.junit;

            import java.lang.annotation.*;

            @Retention(RetentionPolicy.RUNTIME)
            @Target(ElementType.METHOD)
            public @interface TableTest {
                String[] value();
            }
            """
        );

        format(
            "Test.java", """
                import org.tabletest.junit.TableTest;

                public class Test {
                    @TableTest({
                        "Scenario              | Purchases in last 30 days | Discount?",
                        "No discount           | 0                         | 0%<caret>",
                        "Tier 1 discount       | 4                         | 5%",
                        "Tier 2 discount       | 9                         | 10%",
                        "Maximum tier discount | 40                        | 40%"
                    })
                    void test() {}
                }
                """
        );
        checkResultInTopLevelFile("""
            import org.tabletest.junit.TableTest;

            public class Test {
                @TableTest({
                    "Scenario              | Purchases in last 30 days | Discount?",
                    "No discount           | 0                         | 0%\s\s\s\s\s\s\s",
                    "Tier 1 discount       | 4                         | 5%\s\s\s\s\s\s\s",
                    "Tier 2 discount       | 9                         | 10%\s\s\s\s\s\s",
                    "Maximum tier discount | 40                        | 40%\s\s\s\s\s\s"
                })
                void test() {}
            }
            """);
    }

    public void testJavaFormatterUsesConfiguredSpacingInsideInjectedTable() {
        withTableTestSpacingSettings(
            settings -> settings.SPACE_AFTER_COMMA = false,
            () -> {
                format(
                    "Test.java", """
                        public class Test {
                            //language=tabletest
                            @TableTest(\"""
                                values
                                [a,b]<caret>
                                \""")
                            void test() {}
                        }
                        """
                );
                myFixture.checkResult("""
                    public class Test {
                        //language=tabletest
                        @TableTest(\"""
                            values
                            [a,b]
                            \""")
                        void test() {}
                    }
                    """);
            }
        );
    }

}
