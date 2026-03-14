package org.intellij.sdk.language;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.util.PsiTreeUtil;
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
                ["double quoted": 1, 'single quoted': 2]
                ["key with:[],{}": 3]
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

    public void testJavaTextBlockHighlightingWithUnicodeAndCollections() {
        myFixture.configureByText(
            "Test.java", """
                @interface TableTest {
                    String value();
                }
                
                public class Test {
                    //language=tabletest
                    @TableTest(\"""
                        Collection literal | Map literal                        | Set literal?
                        []                 | [:]                                | {}
                        [[]]               | [a: []]                            | {[]}
                        [[:]]              | [a: [:]]                           | {[:]}
                        [{}]               | [a: {}]                            | {{}}
                        [1, 2, 3]          | [one: 1, two: 2, three: 3]         | {1, 2, 3}
                        ['1', "2", 3]     | [one: '1', two: "2", three: 3]     | {'1', "2", 3}
                        [[], [], []]       | [a: [], b: [], c: []]              | {[], [], []}
                        abc                | a,b,c                              | > [ a , b , c ] <
                                           |                                    |
                        1                  | 2                                  | 3
                        '1'                | "2"                                | 3
                        ''                 | ""                                 |
                
                        //
                        // comment
                
                        xx                 | yy                                 | zz
                        æææ                | øøø                                | ååå
                        [1, 2, 3]          | [one: 1, two: 2, three: 3]         | {1, 2, 3}
                        !@#$%^&*()_+-=     | "|,[]{}:"                          | 12:34:56.789
                        你好世界           | こんにちは世界                     | 안녕하세요
                        مرحبا بالعالم      | Привет мир                         | Γεια σου κόσμε
                        שלום עולם          | สวัสดีโลก                            | नमस्ते संसार
                        😀                 | Hello 👋 World                     | Café ☕ tastes good 😋
                        🌍🌎🌏             | 👨‍💻                                 | 🇺🇸
                        naïve résumé       | α β γ δ ε                          | ∑ ∏ ∫ √
                        ┌─┐│ │└─┘          | $€£¥₹                              | «»""''—–
                        //   the end
                        \""")
                    void test(String collectionLiteral, String mapLiteral) {}
                }
                """
        );

        myFixture.checkHighlighting(false, false, false, true);
    }

    public void testJavaArrayAutoInjectionHighlightingRowsStartingWithBraces() {
        myFixture.addFileToProject(
            "io/github/nchaugen/tabletest/junit/TableTest.java",
            """
                package io.github.nchaugen.tabletest.junit;
                
                import java.lang.annotation.*;
                
                @Retention(RetentionPolicy.RUNTIME)
                @Target(ElementType.METHOD)
                public @interface TableTest {
                    String[] value();
                }
                """
        );

        myFixture.configureByText(
            "Test.java", """
                import io.github.nchaugen.tabletest.junit.TableTest;
                
                public class Test {
                    @TableTest({
                        "Collection literal | Map literal | Set literal?",
                        "{}                 | [:]         | {}",
                        "[]                 | [:]         | {}",
                        "[[]]               | [a: []]     | {[]}",
                        "[{}]               | [a: {}]     | {{}}"
                    })
                    void test(String collectionLiteral, String mapLiteral, String setLiteral) {}
                }
                """
        );

        myFixture.checkHighlighting(false, false, false, true);
    }

    public void testJavaAutoInjectionHighlightingWithUnicodeAndCollections() {
        myFixture.addFileToProject(
            "io/github/nchaugen/tabletest/junit/TableTest.java",
            """
                package io.github.nchaugen.tabletest.junit;
                
                import java.lang.annotation.*;
                
                @Retention(RetentionPolicy.RUNTIME)
                @Target(ElementType.METHOD)
                public @interface TableTest {
                    String value();
                }
                """
        );

        myFixture.configureByText(
            "TableCollectionsAndUnicodeProbe.java", """
                import io.github.nchaugen.tabletest.junit.TableTest;
                
                public class TableCollectionsAndUnicodeProbe {
                    @TableTest(\"""
                        Collection literal | Map literal                        | Set literal?
                        []                 | [:]                                | {}
                        [[]]               | [a: []]                            | {[]}
                        [[:]]              | [a: [:]]                           | {[:]}
                        [{}]               | [a: {}]                            | {{}}
                        [1, 2, 3]          | [one: 1, two: 2, three: 3]         | {1, 2, 3}
                        ['1', "2", 3]     | [one: '1', two: "2", three: 3]     | {'1', "2", 3}
                        [[], [], []]       | [a: [], b: [], c: []]              | {[], [], []}
                        abc                | a,b,c                              | > [ a , b , c ] <
                                           |                                    |
                        1                  | 2                                  | 3
                        '1'                | "2"                                | 3
                        ''                 | ""                                 |
                
                        //
                        // comment
                
                        xx                 | yy                                 | zz
                        æææ                | øøø                                | ååå
                        [1, 2, 3]          | [one: 1, two: 2, three: 3]         | {1, 2, 3}
                        !@#$%^&*()_+-=     | "|,[]{}:"                          | 12:34:56.789
                        你好世界           | こんにちは世界                     | 안녕하세요
                        مرحبا بالعالم      | Привет мир                         | Γεια σου κόσμε
                        שלום עולם          | สวัสดีโลก                            | नमस्ते संसार
                        😀                 | Hello 👋 World                     | Café ☕ tastes good 😋
                        🌍🌎🌏             | 👨‍💻                                 | 🇺🇸
                        naïve résumé       | α β γ δ ε                          | ∑ ∏ ∫ √
                        ┌─┐│ │└─┘          | $€£¥₹                              | «»""''—–
                        //   the end
                        \""")
                    void test(String collectionLiteral, String mapLiteral) {}
                }
                """
        );

        myFixture.checkHighlighting(false, false, false, true);
    }

    public void testJavaAutoInjectionHighlightingLeapYearTableWithSetValues() {
        myFixture.addFileToProject(
            "io/github/nchaugen/tabletest/junit/TableTest.java",
            """
                package io.github.nchaugen.tabletest.junit;
                
                import java.lang.annotation.*;
                
                @Retention(RetentionPolicy.RUNTIME)
                @Target(ElementType.METHOD)
                public @interface TableTest {
                    String value();
                }
                """
        );

        myFixture.configureByText(
            "LegacyLeapYearExampleTest.java", """
                import io.github.nchaugen.tabletest.junit.TableTest;
                
                import java.time.Year;
                
                import static org.junit.jupiter.api.Assertions.assertEquals;
                
                public class LegacyLeapYearExampleTest {
                    @TableTest(\"""
                        Scenario                           | Example years       | Is leap year?
                        years not divisible by 4           | {2001, 2002, 2003} | false
                        years divisible by 4               | {2004, 2008, 2012} | true
                        years divisible by 100 but not 400 | {2100, 2200, 2300} | false
                        years divisible by 400             | {2000, 2400, 2800} | true
                        \""")
                    void shouldDetermineLeapYear(Year year, boolean expectedResult) {
                        assertEquals(expectedResult, year.isLeap(), "Year " + year);
                    }
                }
                """
        );

        myFixture.checkHighlighting(false, false, false, true);
    }

    public void testJavaAutoInjectionHighlightingTextBlockWhenAnnotationValueIsStringArray() {
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

        myFixture.configureByText(
            "ModernLeapYearExampleTest.java", """
                package io.github.nchaugen.examples.modern;
                
                import org.tabletest.junit.TableTest;
                
                import java.time.Year;
                
                import static org.junit.jupiter.api.Assertions.assertEquals;
                
                public class ModernLeapYearExampleTest {
                
                    @TableTest(\"""
                        Scenario                              | Example years      | Is leap year?
                        years not divisible by 4              | {2001, 2002, 2003} | false
                        years divisible by 4                  | {2004, 2008, 2012} | true
                        years divisible by 100 but not by 400 | {2100, 2200, 2300} | false
                        years divisible by 400                | {2000, 2400, 2800} | true
                        \""")
                    void shouldDetermineLeapYear(Year year, boolean expectedResult) {
                        assertEquals(expectedResult, year.isLeap(), "Year " + year);
                    }
                }
                """
        );

        myFixture.checkHighlighting(false, false, false, true);
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/testData";
    }

    public void testNegativeSyntax() {
        // Malformed list
        myFixture.configureByText("test.table", "header\n[a, b}\n");
        assertHasStandaloneError();

        // Malformed map
        myFixture.configureByText("test.table", "header\n[key: value}\n");
        assertHasStandaloneError();

        // Malformed set
        myFixture.configureByText("test.table", "header\n{a, b]\n");
        assertHasStandaloneError();
    }

    private void assertHasStandaloneError() {
        PsiElement file = myFixture.getFile();
        assertFalse(
            "Expected parse errors in TableTest, but none found in: " + file.getText(),
            PsiTreeUtil.collectElementsOfType(file, PsiErrorElement.class).isEmpty()
        );
    }

}
