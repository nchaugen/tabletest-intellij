package org.intellij.sdk.language;

import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;

public class TableTestHighlightingTest extends LightJavaCodeInsightFixtureTestCase {

    public void testJavaAnnotator() {
        myFixture.configureByFiles("AnnotatorTestData.java");
        myFixture.checkHighlighting(false, false, false, true);
    }

    public void testKotlinAnnotator() {
        myFixture.configureByFiles("AnnotatorTestData.kt");
        myFixture.checkHighlighting(false, false, false, true);
    }

    public void testTableAnnotator() {
        myFixture.configureByFiles("AnnotatorTestData.table");
        myFixture.checkHighlighting(false, false, false, true);
    }

    public void testHighlightingOutputHeaders() {
        myFixture.configureByText(
            "test.table", """
                input | expected?
                1     | 2
                """
        );
        myFixture.checkHighlighting(false, false, false, true);
    }

    public void testHighlightingQuotedStrings() {
        myFixture.configureByText(
            "test.table", """
                header
                "double quoted"
                'single quoted'
                "with spaces"
                ''
                ""
                """
        );
        myFixture.checkHighlighting(false, false, false, true);
    }

    public void testHighlightingLists() {
        myFixture.configureByText(
            "test.table", """
                header
                [a, b, c]
                []
                [1, 2, 3]
                [[nested], [lists]]
                """
        );
        myFixture.checkHighlighting(false, false, false, true);
    }

    public void testHighlightingSets() {
        myFixture.configureByText(
            "test.table", """
                header
                {a, b, c}
                {}
                {1, 2, 3}
                """
        );
        myFixture.checkHighlighting(false, false, false, true);
    }

    public void testHighlightingMaps() {
        myFixture.configureByText(
            "test.table", """
                header
                [key: value]
                [a: 1, b: 2]
                [:]
                """
        );
        myFixture.checkHighlighting(false, false, false, true);
    }

    public void testHighlightingComments() {
        myFixture.configureByText(
            "test.table", """
                // comment before header
                header
                // comment between rows
                value
                // comment at end
                """
        );
        myFixture.checkHighlighting(false, false, false, true);
    }

    public void testHighlightingEmptyCells() {
        myFixture.configureByText(
            "test.table", """
                a | b | c
                  | x |
                y |   | z
                """
        );
        myFixture.checkHighlighting(false, false, false, true);
    }

    public void testHighlightingNestedStructures() {
        myFixture.configureByText(
            "test.table", """
                header
                [key: [nested, list]]
                {[a, b], [c, d]}
                [[key: value]]
                """
        );
        myFixture.checkHighlighting(false, false, false, true);
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/testData";
    }
}
