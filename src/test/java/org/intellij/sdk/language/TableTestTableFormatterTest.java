package org.intellij.sdk.language;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.codeStyle.CodeStyleManager;

import java.util.List;

public class TableTestTableFormatterTest extends TableTestFormatterTestCase {

    public void testTableFormatter() {
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

    public void testFormatterSingleColumn() {
        format(
            "test.table", """
                header
                value1
                value2
                """
        );
        myFixture.checkResult("""
            header
            value1
            value2
            """);
    }

    public void testFormatterHeaderOnly() {
        format(
            "test.table", """
                a | b | c
                """
        );
        myFixture.checkResult("""
            a | b | c
            """);
    }

    public void testFormatterSingleDataRow() {
        format(
            "test.table", """
                a   |   b   |  c
                1   |   2   |  3
                """
        );
        myFixture.checkResult("""
            a | b | c
            1 | 2 | 3
            """);
    }

    public void testFormatterExtremeWidthDifferences() {
        format(
            "test.table", """
                x|very long column header here|z
                a|b|c
                """
        );
        myFixture.checkResult("""
            x | very long column header here | z
            a | b                            | c
            """);
    }

    public void testFormatterManyColumns() {
        format(
            "test.table", """
                a|b|c|d|e|f|g|h
                1|2|3|4|5|6|7|8
                """
        );
        myFixture.checkResult("""
            a | b | c | d | e | f | g | h
            1 | 2 | 3 | 4 | 5 | 6 | 7 | 8
            """);
    }

    public void testFormatterDeeplyNestedStructures() {
        format(
            "test.table", """
                header
                [[[[deep]]]]
                [a:[b:[c:[d:value]]]]
                {{{nested}}}
                """
        );
        myFixture.checkResult("""
            header
            [[[[deep]]]]
            [a: [b: [c: [d: value]]]]
            {{{nested}}}
            """);
    }

    public void testFormatterEmptyCells() {
        format(
            "test.table", """
                a|b|c
                x||z
                """
        );
        myFixture.checkResult("""
            a | b | c
            x |   | z
            """);
    }

    public void testFormatterQuotedWhitespace() {
        format(
            "test.table", """
                header
                "  spaces  "
                '	tab	'
                "line1\\nline2"
                """
        );
        myFixture.checkResult("""
            header
            "  spaces  "
            '	tab	'
            "line1\\nline2"
            """);
    }

    public void testFormatterMixedContent() {
        format(
            "test.table", """
                input|list|map|set|expected?
                x|[a,b]|[k:v]|{1}|y
                """
        );
        myFixture.checkResult("""
            input | list   | map    | set | expected?
            x     | [a, b] | [k: v] | {1} | y
            """);
    }

    public void testFormatterCommentsPreserved() {
        format(
            "test.table", """
                // header comment
                a|b
                // row comment
                1|2
                //
                """
        );
        myFixture.checkResult("""
            // header comment
            a | b
            // row comment
            1 | 2
            //
            """);
    }

    public void testFormatterAlignsRowImmediatelyAfterComment() {
        // Regression test: the row immediately following a comment line must align
        // with rows before the comment.
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

}
