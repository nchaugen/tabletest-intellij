package org.intellij.sdk.language;

/**
 * Systematic tests for formatting alignment across all context variations in .table files.
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
@SuppressWarnings({"TrailingWhitespacesInTextBlock", "JavadocBlankLines"})
public class TableTestTableFormatterContextVariationsTest extends TableTestFormatterTestCase {

    // ==========================================================================
    // COMMENT WITH LESS INDENT (comment is further LEFT than header)
    // ==========================================================================

    public void testCommentLess_AboveRow_BelowRow() {
        format(
            "test.table", """
                    header1|header2
                    a|b
                // comment
                    c|d
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            // comment
            c       | d
            """);
    }

    public void testCommentLess_AboveRow_BelowComment() {
        format(
            "test.table", """
                    header1|header2
                    a|b
                // comment1
                    // comment2
                    c|d
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            // comment1
            // comment2
            c       | d
            """);
    }

    public void testCommentLess_AboveRow_BelowBlank() {
        format(
            "test.table", """
                    header1|header2
                    a|b
                // comment
                
                    c|d
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            // comment
            
            c       | d
            """);
    }

    public void testCommentLess_AboveComment_BelowRow() {
        format(
            "test.table", """
                    header1|header2
                    a|b
                    // comment1
                // comment2
                    c|d
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            // comment1
            // comment2
            c       | d
            """);
    }

    public void testCommentLess_AboveComment_BelowComment() {
        format(
            "test.table", """
                    header1|header2
                    a|b
                    // comment1
                // comment2
                    // comment3
                    c|d
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            // comment1
            // comment2
            // comment3
            c       | d
            """);
    }

    public void testCommentLess_AboveComment_BelowBlank() {
        format(
            "test.table", """
                    header1|header2
                    a|b
                    // comment1
                // comment2
                
                    c|d
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            // comment1
            // comment2
            
            c       | d
            """);
    }

    public void testCommentLess_AboveBlank_BelowRow() {
        format(
            "test.table", """
                    header1|header2
                    a|b
                
                // comment
                    c|d
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            
            // comment
            c       | d
            """);
    }

    public void testCommentLess_AboveBlank_BelowComment() {
        format(
            "test.table", """
                    header1|header2
                    a|b
                
                // comment1
                    // comment2
                    c|d
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            
            // comment1
            // comment2
            c       | d
            """);
    }

    public void testCommentLess_AboveBlank_BelowBlank() {
        format(
            "test.table", """
                    header1|header2
                    a|b
                
                // comment
                
                    c|d
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            
            // comment
            
            c       | d
            """);
    }

    // ==========================================================================
    // COMMENT WITH MORE INDENT (comment is further RIGHT than header)
    // ==========================================================================

    public void testCommentMore_AboveRow_BelowRow() {
        format(
            "test.table", """
                header1|header2
                a|b
                        // comment
                c|d
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            // comment
            c       | d
            """);
    }

    public void testCommentMore_AboveRow_BelowComment() {
        format(
            "test.table", """
                header1|header2
                a|b
                        // comment1
                // comment2
                c|d
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            // comment1
            // comment2
            c       | d
            """);
    }

    public void testCommentMore_AboveRow_BelowBlank() {
        format(
            "test.table", """
                header1|header2
                a|b
                        // comment
                
                c|d
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            // comment
            
            c       | d
            """);
    }

    public void testCommentMore_AboveComment_BelowRow() {
        format(
            "test.table", """
                header1|header2
                a|b
                // comment1
                        // comment2
                c|d
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            // comment1
            // comment2
            c       | d
            """);
    }

    public void testCommentMore_AboveComment_BelowComment() {
        format(
            "test.table", """
                header1|header2
                a|b
                // comment1
                        // comment2
                // comment3
                c|d
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            // comment1
            // comment2
            // comment3
            c       | d
            """);
    }

    public void testCommentMore_AboveComment_BelowBlank() {
        format(
            "test.table", """
                header1|header2
                a|b
                // comment1
                        // comment2
                
                c|d
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            // comment1
            // comment2
            
            c       | d
            """);
    }

    public void testCommentMore_AboveBlank_BelowRow() {
        format(
            "test.table", """
                header1|header2
                a|b
                
                        // comment
                c|d
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            
            // comment
            c       | d
            """);
    }

    public void testCommentMore_AboveBlank_BelowComment() {
        format(
            "test.table", """
                header1|header2
                a|b
                
                        // comment1
                // comment2
                c|d
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            
            // comment1
            // comment2
            c       | d
            """);
    }

    public void testCommentMore_AboveBlank_BelowBlank() {
        format(
            "test.table", """
                header1|header2
                a|b
                
                        // comment
                
                c|d
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            
            // comment
            
            c       | d
            """);
    }

    // ==========================================================================
    // DATA ROW WITH LESS INDENT (row is further LEFT than header)
    // ==========================================================================

    public void testRowLess_AboveRow_BelowRow() {
        format(
            "test.table", """
                    header1|header2
                    a|b
                c|d
                    e|f
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            c       | d
            e       | f
            """);
    }

    public void testRowLess_AboveRow_BelowComment() {
        format(
            "test.table", """
                    header1|header2
                    a|b
                c|d
                    // comment
                    e|f
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            c       | d
            // comment
            e       | f
            """);
    }

    public void testRowLess_AboveRow_BelowBlank() {
        format(
            "test.table", """
                    header1|header2
                    a|b
                c|d
                
                    e|f
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            c       | d
            
            e       | f
            """);
    }

    public void testRowLess_AboveComment_BelowRow() {
        format(
            "test.table", """
                    header1|header2
                    a|b
                    // comment
                c|d
                    e|f
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            // comment
            c       | d
            e       | f
            """);
    }

    public void testRowLess_AboveComment_BelowComment() {
        format(
            "test.table", """
                    header1|header2
                    a|b
                    // comment1
                c|d
                    // comment2
                    e|f
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            // comment1
            c       | d
            // comment2
            e       | f
            """);
    }

    public void testRowLess_AboveComment_BelowBlank() {
        format(
            "test.table", """
                    header1|header2
                    a|b
                    // comment
                c|d
                
                    e|f
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            // comment
            c       | d
            
            e       | f
            """);
    }

    public void testRowLess_AboveBlank_BelowRow() {
        format(
            "test.table", """
                    header1|header2
                    a|b
                
                c|d
                    e|f
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            
            c       | d
            e       | f
            """);
    }

    public void testRowLess_AboveBlank_BelowComment() {
        format(
            "test.table", """
                    header1|header2
                    a|b
                
                c|d
                    // comment
                    e|f
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            
            c       | d
            // comment
            e       | f
            """);
    }

    public void testRowLess_AboveBlank_BelowBlank() {
        format(
            "test.table", """
                    header1|header2
                    a|b
                
                c|d
                
                    e|f
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            
            c       | d
            
            e       | f
            """);
    }

    // ==========================================================================
    // DATA ROW WITH MORE INDENT (row is further RIGHT than header)
    // ==========================================================================

    public void testRowMore_AboveRow_BelowRow() {
        format(
            "test.table", """
                header1|header2
                a|b
                        c|d
                e|f
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            c       | d
            e       | f
            """);
    }

    public void testRowMore_AboveRow_BelowComment() {
        format(
            "test.table", """
                header1|header2
                a|b
                        c|d
                // comment
                e|f
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            c       | d
            // comment
            e       | f
            """);
    }

    public void testRowMore_AboveRow_BelowBlank() {
        format(
            "test.table", """
                header1|header2
                a|b
                        c|d
                
                e|f
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            c       | d
            
            e       | f
            """);
    }

    public void testRowMore_AboveComment_BelowRow() {
        format(
            "test.table", """
                header1|header2
                a|b
                // comment
                        c|d
                e|f
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            // comment
            c       | d
            e       | f
            """);
    }

    public void testRowMore_AboveComment_BelowComment() {
        format(
            "test.table", """
                header1|header2
                a|b
                // comment1
                        c|d
                // comment2
                e|f
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            // comment1
            c       | d
            // comment2
            e       | f
            """);
    }

    public void testRowMore_AboveComment_BelowBlank() {
        format(
            "test.table", """
                header1|header2
                a|b
                // comment
                        c|d
                
                e|f
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            // comment
            c       | d
            
            e       | f
            """);
    }

    public void testRowMore_AboveBlank_BelowRow() {
        format(
            "test.table", """
                header1|header2
                a|b
                
                        c|d
                e|f
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            
            c       | d
            e       | f
            """);
    }

    public void testRowMore_AboveBlank_BelowComment() {
        format(
            "test.table", """
                header1|header2
                a|b
                
                        c|d
                // comment
                e|f
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            
            c       | d
            // comment
            e       | f
            """);
    }

    public void testRowMore_AboveBlank_BelowBlank() {
        format(
            "test.table", """
                header1|header2
                a|b
                
                        c|d
                
                e|f
                """
        );
        myFixture.checkResult("""
            header1 | header2
            a       | b
            
            c       | d
            
            e       | f
            """);
    }
}
