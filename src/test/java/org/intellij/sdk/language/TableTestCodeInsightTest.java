package org.intellij.sdk.language;

import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;

import java.util.List;

public class TableTestCodeInsightTest extends LightJavaCodeInsightFixtureTestCase {

    public void testAnnotator() {
        myFixture.configureByFiles("AnnotatorTestData.java");
        myFixture.checkHighlighting(false, false, false, true);
    }

    public void testHighlightingOutputHeaders() {
        myFixture.configureByText("test.table", """
            input | expected?
            1     | 2
            """);
        myFixture.checkHighlighting(false, false, false, true);
    }

    public void testHighlightingQuotedStrings() {
        myFixture.configureByText("test.table", """
            header
            "double quoted"
            'single quoted'
            "with spaces"
            ''
            ""
            """);
        myFixture.checkHighlighting(false, false, false, true);
    }

    public void testHighlightingLists() {
        myFixture.configureByText("test.table", """
            header
            [a, b, c]
            []
            [1, 2, 3]
            [[nested], [lists]]
            """);
        myFixture.checkHighlighting(false, false, false, true);
    }

    public void testHighlightingSets() {
        myFixture.configureByText("test.table", """
            header
            {a, b, c}
            {}
            {1, 2, 3}
            """);
        myFixture.checkHighlighting(false, false, false, true);
    }

    public void testHighlightingMaps() {
        myFixture.configureByText("test.table", """
            header
            [key: value]
            [a: 1, b: 2]
            [:]
            """);
        myFixture.checkHighlighting(false, false, false, true);
    }

    public void testHighlightingComments() {
        myFixture.configureByText("test.table", """
            // comment before header
            header
            // comment between rows
            value
            // comment at end
            """);
        myFixture.checkHighlighting(false, false, false, true);
    }

    public void testHighlightingEmptyCells() {
        myFixture.configureByText("test.table", """
            a | b | c
              | x |
            y |   | z
            """);
        myFixture.checkHighlighting(false, false, false, true);
    }

    public void testHighlightingNestedStructures() {
        myFixture.configureByText("test.table", """
            header
            [key: [nested, list]]
            {[a, b], [c, d]}
            [[key: value]]
            """);
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

    public void testFormatterSingleColumn() {
        configureAndFormat("test.table", """
            header
            value1
            value2
            """);
        myFixture.checkResult("""
            header
            value1
            value2
            """);
    }

    public void testFormatterHeaderOnly() {
        configureAndFormat("test.table", """
            a | b | c
            """);
        myFixture.checkResult("""
            a | b | c
            """);
    }

    public void testFormatterSingleDataRow() {
        configureAndFormat("test.table", """
            a   |   b   |  c
            1   |   2   |  3
            """);
        myFixture.checkResult("""
            a | b | c
            1 | 2 | 3
            """);
    }

    public void testFormatterExtremeWidthDifferences() {
        configureAndFormat("test.table", """
            x|very long column header here|z
            a|b|c
            """);
        myFixture.checkResult("""
            x | very long column header here | z
            a | b                            | c
            """);
    }

    public void testFormatterManyColumns() {
        configureAndFormat("test.table", """
            a|b|c|d|e|f|g|h
            1|2|3|4|5|6|7|8
            """);
        myFixture.checkResult("""
            a | b | c | d | e | f | g | h
            1 | 2 | 3 | 4 | 5 | 6 | 7 | 8
            """);
    }

    public void testFormatterDeeplyNestedStructures() {
        configureAndFormat("test.table", """
            header
            [[[[deep]]]]
            [a:[b:[c:[d:value]]]]
            {{{nested}}}
            """);
        myFixture.checkResult("""
            header
            [[[[deep]]]]
            [a: [b: [c: [d: value]]]]
            {{{nested}}}
            """);
    }

    public void testFormatterEmptyCells() {
        configureAndFormat("test.table", """
            a|b|c
            x||z
            """);
        myFixture.checkResult("""
            a | b | c
            x |   | z
            """);
    }

    public void testFormatterQuotedWhitespace() {
        configureAndFormat("test.table", """
            header
            "  spaces  "
            '	tab	'
            "line1\\nline2"
            """);
        myFixture.checkResult("""
            header
            "  spaces  "
            '	tab	'
            "line1\\nline2"
            """);
    }

    public void testFormatterMixedContent() {
        configureAndFormat("test.table", """
            input|list|map|set|expected?
            x|[a,b]|[k:v]|{1}|y
            """);
        myFixture.checkResult("""
            input | list   | map    | set | expected?
            x     | [a, b] | [k: v] | {1} | y
            """);
    }

    public void testFormatterCommentsPreserved() {
        configureAndFormat("test.table", """
            // header comment
            a|b
            // row comment
            1|2
            //
            """);
        myFixture.checkResult("""
            // header comment
            a | b
            // row comment
            1 | 2
            //
            """);
    }

    private void configureAndFormat(String fileName, String content) {
        myFixture.configureByText(fileName, content);
        WriteCommandAction.writeCommandAction(getProject())
            .run(() ->
                     CodeStyleManager.getInstance(getProject())
                         .reformatText(
                             myFixture.getFile(),
                             List.of(myFixture.getFile().getTextRange())
                         )
            );
    }

    public void testCommenter() {
        myFixture.configureByText("test.table", "<caret>value1|value2");
        myFixture.performEditorAction(IdeActions.ACTION_COMMENT_LINE);
        myFixture.checkResult("// value1|value2");
    }

    public void testUncomment() {
        myFixture.configureByText("test.table", "<caret>// value1|value2");
        myFixture.performEditorAction(IdeActions.ACTION_COMMENT_LINE);
        myFixture.checkResult("value1|value2");
    }

    // Kotlin formatter test is disabled due to an IntelliJ test framework bug.
    // When using LightJavaCodeInsightFixtureTestCase with setCaresAboutInjection(true),
    // formatting injected content in Kotlin raw strings corrupts the text due to
    // incorrect offset mapping. For example, "one:1" becomes "o ne:1".
    // This is a test-only issue - formatting works correctly in the actual editor.
    // The equivalent test for Java text blocks works correctly.
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
