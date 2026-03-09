package org.intellij.sdk.language;

import com.intellij.application.options.CodeStyle;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import io.github.nchaugen.tabletest.language.TableTestCodeStyleSettingsProvider;
import io.github.nchaugen.tabletest.language.TableTestLanguage;

import java.util.ArrayList;
import java.util.List;

public class TableTestTableFormatterTest extends TableTestFormatterTestCase {

    public void testTableFormatter() {
        formatFile("FormatterTestData.table");
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

    public void testTableTestDefaultSpacingSettings() {
        com.intellij.psi.codeStyle.CommonCodeStyleSettings settings = CodeStyle
            .getSettings(getProject())
            .getCommonSettings(TableTestLanguage.INSTANCE);

        assertFalse("Expected SPACE_BEFORE_COMMA default to be false", settings.SPACE_BEFORE_COMMA);
        assertTrue("Expected SPACE_AFTER_COMMA default to be true", settings.SPACE_AFTER_COMMA);
        assertFalse("Expected SPACE_BEFORE_COLON default to be false", settings.SPACE_BEFORE_COLON);
        assertTrue("Expected SPACE_AFTER_COLON default to be true", settings.SPACE_AFTER_COLON);
        assertFalse("Expected SPACE_WITHIN_BRACKETS default to be false", settings.SPACE_WITHIN_BRACKETS);
        assertFalse("Expected SPACE_WITHIN_BRACES default to be false", settings.SPACE_WITHIN_BRACES);
    }

    public void testTableTestCodeStyleSettingsPageProviderIsRegistered() {
        boolean registered = LanguageCodeStyleSettingsProvider.getSettingsPagesProviders()
            .stream()
            .anyMatch(provider -> provider.getLanguage().equals(TableTestLanguage.INSTANCE));
        assertTrue("Expected TableTest code style settings page provider to be registered", registered);
    }

    public void testFormatterUsesConfiguredSpaceAfterComma() {
        withTableTestSpacingSettings(
            settings -> settings.SPACE_AFTER_COMMA = false,
            () -> {
                format(
                    "test.table", """
                        value
                        [a,b]
                        """
                );
                myFixture.checkResult("""
                    value
                    [a,b]
                    """);
            }
        );
    }

    public void testFormatterUsesConfiguredSpaceBeforeComma() {
        withTableTestSpacingSettings(
            settings -> {
                settings.SPACE_BEFORE_COMMA = true;
                settings.SPACE_AFTER_COMMA = true;
            },
            () -> {
                format(
                    "test.table", """
                        value
                        [a,b]
                        """
                );
                myFixture.checkResult("""
                    value
                    [a , b]
                    """);
            }
        );
    }

    public void testFormatterUsesConfiguredSpaceBeforeAndAfterColon() {
        withTableTestSpacingSettings(
            settings -> {
                settings.SPACE_BEFORE_COLON = true;
                settings.SPACE_AFTER_COLON = false;
            },
            () -> {
                format(
                    "test.table", """
                        value
                        [k:v]
                        """
                );
                myFixture.checkResult("""
                    value
                    [k :v]
                    """);
            }
        );
    }

    public void testFormatterUsesConfiguredSpacesWithinBrackets() {
        withTableTestSpacingSettings(
            settings -> settings.SPACE_WITHIN_BRACKETS = true,
            () -> {
                format(
                    "test.table", """
                        value
                        [a,b]
                        """
                );
                myFixture.checkResult("""
                    value
                    [ a, b ]
                    """);
            }
        );
    }

    public void testFormatterUsesConfiguredSpacesWithinBraces() {
        withTableTestSpacingSettings(
            settings -> settings.SPACE_WITHIN_BRACES = true,
            () -> {
                format(
                    "test.table", """
                        value
                        {a,b}
                        """
                );
                myFixture.checkResult("""
                    value
                    { a, b }
                    """);
            }
        );
    }

    public void testCodeStyleSampleFormatsWithAlignedColumns() {
        TableTestCodeStyleSettingsProvider provider = new TableTestCodeStyleSettingsProvider();
        format(
            "sample.table",
            provider.getCodeSample(LanguageCodeStyleSettingsProvider.SettingsType.SPACING_SETTINGS)
        );

        String formatted = myFixture.getFile().getText();
        assertPipesAligned(formatted);
        assertTrue("Expected list spacing to use default settings", formatted.contains("[a, b]"));
        assertTrue("Expected map spacing to use default settings", formatted.contains("[k: v, foo: bar]"));
        assertTrue("Expected set spacing to use default settings", formatted.contains("{1, 2}"));
    }

    public void testCodeStyleSampleRealignsWhenValueSpacingChanges() {
        withTableTestSpacingSettings(
            settings -> {
                settings.SPACE_AFTER_COMMA = false;
                settings.SPACE_AFTER_COLON = false;
            },
            () -> {
                TableTestCodeStyleSettingsProvider provider = new TableTestCodeStyleSettingsProvider();
                format(
                    "sample.table",
                    provider.getCodeSample(LanguageCodeStyleSettingsProvider.SettingsType.SPACING_SETTINGS)
                );

                String formatted = myFixture.getFile().getText();
                assertPipesAligned(formatted);
                assertTrue("Expected list spacing to reflect settings", formatted.contains("[a,b]"));
                assertTrue("Expected map spacing to reflect settings", formatted.contains("[k:v,foo:bar]"));
                assertTrue("Expected set spacing to reflect settings", formatted.contains("{1,2}"));
            }
        );
    }

    private void assertPipesAligned(String tableText) {
        String[] lines = tableText.split("\\n");
        List<String> tableLines = new ArrayList<>();
        for (String line : lines) {
            if (!line.isBlank() && line.contains("|")) {
                tableLines.add(line);
            }
        }

        assertTrue("Expected at least two table lines to check alignment", tableLines.size() >= 2);
        List<Integer> expectedPipePositions = pipePositions(tableLines.getFirst());
        for (String line : tableLines) {
            assertEquals(
                "Expected aligned pipe positions for line: " + line,
                expectedPipePositions,
                pipePositions(line)
            );
        }
    }

    private List<Integer> pipePositions(String line) {
        List<Integer> positions = new ArrayList<>();
        for (int index = 0; index < line.length(); index++) {
            if (line.charAt(index) == '|') {
                positions.add(index);
            }
        }
        return positions;
    }

}
