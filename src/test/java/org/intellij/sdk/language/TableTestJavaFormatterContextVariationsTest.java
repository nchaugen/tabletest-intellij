package org.intellij.sdk.language;

/**
 * Systematic tests for formatting alignment across all context variations in Java text blocks.
 * <p>
 * Test matrix:
 * - Line type being tested: Comment, Data row
 * - Input indent of tested line: Less than header, More than header
 * - Context above: Data row, Comment, Blank line
 * - Context below: Data row, Comment, Blank line
 * <p>
 * Total: 2 × 2 × 3 × 3 = 36 test cases
 * <p>
 * Expected behavior: All lines should align together after formatting.
 * The tested line starts misaligned and should become aligned with the header.
 */
public class TableTestJavaFormatterContextVariationsTest extends TableTestFormatterTestCase {

    private void formatJava(String tableContent) {
        format(
            "Test.java", """
                public class Test {
                    //language=tabletest
                    @TableTest(\"""
                %s<caret>
                        \""")
                    void test() {}
                }
                """.formatted(tableContent)
        );
    }

    // ==========================================================================
    // COMMENT WITH LESS INDENT (comment is further LEFT than header)
    // ==========================================================================

    public void testCommentLess_AboveRow_BelowRow() {
        formatJava("""
                header1|header2
                a|b
            // comment
                c|d""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            // comment
            c       | d
                    \""")
                void test() {}
            }
            """);
    }

    public void testCommentLess_AboveRow_BelowComment() {
        formatJava("""
                header1|header2
                a|b
            // comment1
                // comment2
                c|d""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            // comment1
            // comment2
            c       | d
                    \""")
                void test() {}
            }
            """);
    }

    public void testCommentLess_AboveRow_BelowBlank() {
        formatJava("""
                header1|header2
                a|b
            // comment
            
                c|d""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            // comment
            
            c       | d
                    \""")
                void test() {}
            }
            """);
    }

    public void testCommentLess_AboveComment_BelowRow() {
        formatJava("""
                header1|header2
                a|b
                // comment1
            // comment2
                c|d""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            // comment1
            // comment2
            c       | d
                    \""")
                void test() {}
            }
            """);
    }

    public void testCommentLess_AboveComment_BelowComment() {
        formatJava("""
                header1|header2
                a|b
                // comment1
            // comment2
                // comment3
                c|d""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            // comment1
            // comment2
            // comment3
            c       | d
                    \""")
                void test() {}
            }
            """);
    }

    public void testCommentLess_AboveComment_BelowBlank() {
        formatJava("""
                header1|header2
                a|b
                // comment1
            // comment2
            
                c|d""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            // comment1
            // comment2
            
            c       | d
                    \""")
                void test() {}
            }
            """);
    }

    public void testCommentLess_AboveBlank_BelowRow() {
        formatJava("""
                header1|header2
                a|b
            
            // comment
                c|d""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            
            // comment
            c       | d
                    \""")
                void test() {}
            }
            """);
    }

    public void testCommentLess_AboveBlank_BelowComment() {
        formatJava("""
                header1|header2
                a|b
            
            // comment1
                // comment2
                c|d""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            
            // comment1
            // comment2
            c       | d
                    \""")
                void test() {}
            }
            """);
    }

    public void testCommentLess_AboveBlank_BelowBlank() {
        formatJava("""
                header1|header2
                a|b
            
            // comment
            
                c|d""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            
            // comment
            
            c       | d
                    \""")
                void test() {}
            }
            """);
    }

    // ==========================================================================
    // COMMENT WITH MORE INDENT (comment is further RIGHT than header)
    // ==========================================================================

    public void testCommentMore_AboveRow_BelowRow() {
        formatJava("""
            header1|header2
            a|b
                    // comment
            c|d""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            // comment
            c       | d
                    \""")
                void test() {}
            }
            """);
    }

    public void testCommentMore_AboveRow_BelowComment() {
        formatJava("""
            header1|header2
            a|b
                    // comment1
            // comment2
            c|d""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            // comment1
            // comment2
            c       | d
                    \""")
                void test() {}
            }
            """);
    }

    public void testCommentMore_AboveRow_BelowBlank() {
        formatJava("""
            header1|header2
            a|b
                    // comment
            
            c|d""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            // comment
            
            c       | d
                    \""")
                void test() {}
            }
            """);
    }

    public void testCommentMore_AboveComment_BelowRow() {
        formatJava("""
            header1|header2
            a|b
            // comment1
                    // comment2
            c|d""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            // comment1
            // comment2
            c       | d
                    \""")
                void test() {}
            }
            """);
    }

    public void testCommentMore_AboveComment_BelowComment() {
        formatJava("""
            header1|header2
            a|b
            // comment1
                    // comment2
            // comment3
            c|d""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            // comment1
            // comment2
            // comment3
            c       | d
                    \""")
                void test() {}
            }
            """);
    }

    public void testCommentMore_AboveComment_BelowBlank() {
        formatJava("""
            header1|header2
            a|b
            // comment1
                    // comment2
            
            c|d""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            // comment1
            // comment2
            
            c       | d
                    \""")
                void test() {}
            }
            """);
    }

    public void testCommentMore_AboveBlank_BelowRow() {
        formatJava("""
            header1|header2
            a|b
            
                    // comment
            c|d""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            
            // comment
            c       | d
                    \""")
                void test() {}
            }
            """);
    }

    public void testCommentMore_AboveBlank_BelowComment() {
        formatJava("""
            header1|header2
            a|b
            
                    // comment1
            // comment2
            c|d""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            
            // comment1
            // comment2
            c       | d
                    \""")
                void test() {}
            }
            """);
    }

    public void testCommentMore_AboveBlank_BelowBlank() {
        formatJava("""
            header1|header2
            a|b
            
                    // comment
            
            c|d""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            
            // comment
            
            c       | d
                    \""")
                void test() {}
            }
            """);
    }

    // ==========================================================================
    // DATA ROW WITH LESS INDENT (row is further LEFT than header)
    // ==========================================================================

    public void testRowLess_AboveRow_BelowRow() {
        formatJava("""
                header1|header2
                a|b
            c|d
                e|f""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            c       | d
            e       | f
                    \""")
                void test() {}
            }
            """);
    }

    public void testRowLess_AboveRow_BelowComment() {
        formatJava("""
                header1|header2
                a|b
            c|d
                // comment
                e|f""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            c       | d
            // comment
            e       | f
                    \""")
                void test() {}
            }
            """);
    }

    public void testRowLess_AboveRow_BelowBlank() {
        formatJava("""
                header1|header2
                a|b
            c|d
            
                e|f""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            c       | d
            
            e       | f
                    \""")
                void test() {}
            }
            """);
    }

    public void testRowLess_AboveComment_BelowRow() {
        formatJava("""
                header1|header2
                a|b
                // comment
            c|d
                e|f""");
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

    public void testRowLess_AboveComment_BelowComment() {
        formatJava("""
                header1|header2
                a|b
                // comment1
            c|d
                // comment2
                e|f""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            // comment1
            c       | d
            // comment2
            e       | f
                    \""")
                void test() {}
            }
            """);
    }

    public void testRowLess_AboveComment_BelowBlank() {
        formatJava("""
                header1|header2
                a|b
                // comment
            c|d
            
                e|f""");
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

    public void testRowLess_AboveBlank_BelowRow() {
        formatJava("""
                header1|header2
                a|b
            
            c|d
                e|f""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            
            c       | d
            e       | f
                    \""")
                void test() {}
            }
            """);
    }

    public void testRowLess_AboveBlank_BelowComment() {
        formatJava("""
                header1|header2
                a|b
            
            c|d
                // comment
                e|f""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            
            c       | d
            // comment
            e       | f
                    \""")
                void test() {}
            }
            """);
    }

    public void testRowLess_AboveBlank_BelowBlank() {
        formatJava("""
                header1|header2
                a|b
            
            c|d
            
                e|f""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            
            c       | d
            
            e       | f
                    \""")
                void test() {}
            }
            """);
    }

    // ==========================================================================
    // DATA ROW WITH MORE INDENT (row is further RIGHT than header)
    // ==========================================================================

    public void testRowMore_AboveRow_BelowRow() {
        formatJava("""
            header1|header2
            a|b
                    c|d
            e|f""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            c       | d
            e       | f
                    \""")
                void test() {}
            }
            """);
    }

    public void testRowMore_AboveRow_BelowComment() {
        formatJava("""
            header1|header2
            a|b
                    c|d
            // comment
            e|f""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            c       | d
            // comment
            e       | f
                    \""")
                void test() {}
            }
            """);
    }

    public void testRowMore_AboveRow_BelowBlank() {
        formatJava("""
            header1|header2
            a|b
                    c|d
            
            e|f""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            c       | d
            
            e       | f
                    \""")
                void test() {}
            }
            """);
    }

    public void testRowMore_AboveComment_BelowRow() {
        formatJava("""
            header1|header2
            a|b
            // comment
                    c|d
            e|f""");
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

    public void testRowMore_AboveComment_BelowComment() {
        formatJava("""
            header1|header2
            a|b
            // comment1
                    c|d
            // comment2
            e|f""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            // comment1
            c       | d
            // comment2
            e       | f
                    \""")
                void test() {}
            }
            """);
    }

    public void testRowMore_AboveComment_BelowBlank() {
        formatJava("""
            header1|header2
            a|b
            // comment
                    c|d
            
            e|f""");
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

    public void testRowMore_AboveBlank_BelowRow() {
        formatJava("""
            header1|header2
            a|b
            
                    c|d
            e|f""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            
            c       | d
            e       | f
                    \""")
                void test() {}
            }
            """);
    }

    public void testRowMore_AboveBlank_BelowComment() {
        formatJava("""
            header1|header2
            a|b
            
                    c|d
            // comment
            e|f""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            
            c       | d
            // comment
            e       | f
                    \""")
                void test() {}
            }
            """);
    }

    public void testRowMore_AboveBlank_BelowBlank() {
        formatJava("""
            header1|header2
            a|b
            
                    c|d
            
            e|f""");
        myFixture.checkResult("""
            public class Test {
                //language=tabletest
                @TableTest(\"""
            header1 | header2
            a       | b
            
            c       | d
            
            e       | f
                    \""")
                void test() {}
            }
            """);
    }
}
