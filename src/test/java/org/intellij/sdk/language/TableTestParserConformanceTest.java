package org.intellij.sdk.language;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.ParsingTestCase;
import io.github.nchaugen.tabletest.language.TableTestParserDefinition;
import io.github.nchaugen.tabletest.language.psi.TableTestRow;
import io.github.nchaugen.tabletest.language.psi.TableTestTypes;
import org.tabletest.parser.Row;
import org.tabletest.parser.TableParser;
import org.tabletest.parser.TableTestParseException;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Conformance tests verifying that the IntelliJ plugin parser accepts the same
 * inputs as the canonical tabletest-parser library.
 */
public class TableTestParserConformanceTest extends ParsingTestCase {

    public TableTestParserConformanceTest() {
        super("", "table", new TableTestParserDefinition());
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/testData";
    }

    // --- Valid inputs that both parsers should accept ---

    public void testUnmatchedSingleQuote() {
        assertBothParsersAccept("header\n'\n");
    }

    public void testUnmatchedDoubleQuote() {
        assertBothParsersAccept("header\n\"\n");
    }

    public void testEmptySingleQuotes() {
        assertBothParsersAccept("header\n''\n");
    }

    public void testEmptyDoubleQuotes() {
        assertBothParsersAccept("header\n\"\"\n");
    }

    public void testMatchedSingleQuotes() {
        assertBothParsersAccept("header\n'value'\n");
    }

    public void testMatchedDoubleQuotes() {
        assertBothParsersAccept("header\n\"value\"\n");
    }

    public void testMixedQuotesInList() {
        assertBothParsersAccept("header\n['a', \"b\", c]\n");
    }

    public void testUnquotedValue() {
        assertBothParsersAccept("header\nabc\n");
    }

    public void testMultipleColumns() {
        assertBothParsersAccept("a | b | c\n1 | 2 | 3\n");
    }

    public void testEmptyCell() {
        assertBothParsersAccept("a | b\n | \n");
    }

    public void testListValue() {
        assertBothParsersAccept("header\n[1, 2, 3]\n");
    }

    public void testSetValue() {
        assertBothParsersAccept("header\n{1, 2, 3}\n");
    }

    public void testMapValue() {
        assertBothParsersAccept("header\n[a: 1, b: 2]\n");
    }

    public void testNestedStructures() {
        assertBothParsersAccept("header\n[[1, 2], [3, 4]]\n");
    }

    public void testCommentLine() {
        assertBothParsersAccept("header\n// comment\nvalue\n");
    }

    public void testQuotedPipe() {
        assertBothParsersAccept("header\n'|'\n");
    }

    public void testQuotedBracket() {
        assertBothParsersAccept("header\n'['\n");
    }

    public void testQuotedBrace() {
        assertBothParsersAccept("header\n'{'\n");
    }

    // --- Patterns from TableParserTest.java ---

    public void testInlineCommentAtEndOfRow() {
        // from shouldIgnoreComments: 4 //  | 5 - comment starts at //
        assertBothParsersAccept("a | b\n4 // | 5\n");
    }

    public void testValueStartingWithSlashSlash() {
        // from shouldIgnoreComments: 6 | // 7
        assertBothParsersAccept("a | b\n6 | // 7\n");
    }

    public void testValueEndingWithSlashSlash() {
        // from shouldIgnoreComments: 8 | 9 //
        assertBothParsersAccept("a | b\n8 | 9 //\n");
    }

    public void testQuotedSlashSlash() {
        // from shouldIgnoreComments: '//2' | 3
        assertBothParsersAccept("a | b\n'//2' | 3\n");
    }

    public void testTimestampInList() {
        // from shouldThrowExceptionOnParsingErrors
        // [2025-08-01T00:00:00] contains colons - might be parsed as map
        assertBothParsersAccept("header\n[2025-08-01T00:00:00]\n");
    }

    public void testTimestampValue() {
        // Simple timestamp without brackets
        assertBothParsersAccept("header\n2025-08-01T00:00:00\n");
    }

    public void testListWithNestedLists() {
        // from shouldCaptureLists: [[a], [b], [c]]
        assertBothParsersAccept("header\n[[a], [b], [c]]\n");
    }

    public void testListWithSet() {
        // from shouldCaptureLists: [{a,b}, c]
        assertBothParsersAccept("header\n[{a,b}, c]\n");
    }

    public void testListWithMap() {
        // from shouldCaptureLists: [[a:b], c]
        assertBothParsersAccept("header\n[[a:b], c]\n");
    }

    public void testSetWithList() {
        // from shouldCaptureSets: {[a],[b],[b]}
        assertBothParsersAccept("header\n{[a],[b],[b]}\n");
    }

    public void testSetWithMap() {
        // from shouldCaptureSets: {[a:b], [a:b]}
        assertBothParsersAccept("header\n{[a:b], [a:b]}\n");
    }

    public void testNestedMap() {
        // from shouldCaptureMaps: [A:[a:1], B:[b:2], C:[c:3]]
        assertBothParsersAccept("header\n[A:[a:1], B:[b:2], C:[c:3]]\n");
    }

    public void testMapWithEmptyMap() {
        // from shouldCaptureMaps: [m:[:], s:a]
        assertBothParsersAccept("header\n[m:[:], s:a]\n");
    }

    public void testMapWithSet() {
        // from shouldCaptureMaps: [s:{a,b}, t:{c}]
        assertBothParsersAccept("header\n[s:{a,b}, t:{c}]\n");
    }

    public void testMapWithList() {
        // from shouldCaptureMaps: [l:[a,b], i:[c]]
        assertBothParsersAccept("header\n[l:[a,b], i:[c]]\n");
    }

    public void testMapWithQuotedValue() {
        // from shouldCaptureMaps: [q:"a,b", u:c]
        assertBothParsersAccept("header\n[q:\"a,b\", u:c]\n");
    }

    public void testScenarioColumn() {
        // Standard scenario column with multiple pipes
        assertBothParsersAccept("Scenario | Input | Result?\nTest | value | expected\n");
    }

    public void testBlankLinesWithWhitespace() {
        // from shouldIgnoreBlankLines: lines with only whitespace
        assertBothParsersAccept("a | b\n   \n1 | 2\n");
    }

    public void testEmptyQuotedStringInCell() {
        // empty quoted strings
        assertBothParsersAccept("header\n''\n");
    }

    public void testValueWithSpaces() {
        // from shouldCaptureStrings: abc def
        assertBothParsersAccept("header\nabc def\n");
    }

    public void testQuotedValueWithSpaces() {
        // from shouldCaptureStrings: ' a b c '
        assertBothParsersAccept("header\n' a b c '\n");
    }

    public void testMixedContentSet() {
        // from shouldCaptureSets: {{},a,a,{}}
        assertBothParsersAccept("header\n{{},a,a,{}}\n");
    }

    public void testNestedSet() {
        // from shouldCaptureSets: {{a}, {b}, {b}}
        assertBothParsersAccept("header\n{{a}, {b}, {b}}\n");
    }

    // --- Quoted strings with special characters inside compounds ---

    public void testMapWithSingleQuotedValueContainingComma() {
        // [q:'a,b', u:c] - single quoted value containing comma
        assertBothParsersAccept("header\n[q:'a,b', u:c]\n");
    }

    public void testListWithDoubleQuotedValueContainingComma() {
        // ["a,b", c] - double quoted value containing comma in list
        assertBothParsersAccept("header\n[\"a,b\", c]\n");
    }

    public void testListWithSingleQuotedValueContainingComma() {
        // ['a,b', c] - single quoted value containing comma in list
        assertBothParsersAccept("header\n['a,b', c]\n");
    }

    public void testSetWithDoubleQuotedValueContainingComma() {
        // {"a,b", c} - double quoted value containing comma in set
        assertBothParsersAccept("header\n{\"a,b\", c}\n");
    }

    public void testSetWithSingleQuotedValueContainingComma() {
        // {'a,b', c} - single quoted value containing comma in set
        assertBothParsersAccept("header\n{'a,b', c}\n");
    }

    public void testQuotedValueContainingColon() {
        // [key:'a:b'] - quoted value containing colon
        assertBothParsersAccept("header\n[key:'a:b']\n");
    }

    public void testQuotedValueContainingBracket() {
        // [key:'a]b'] - quoted value containing closing bracket
        assertBothParsersAccept("header\n[key:'a]b']\n");
    }

    public void testQuotedValueContainingBrace() {
        // {'a}b', c} - quoted value containing closing brace
        assertBothParsersAccept("header\n{'a}b', c}\n");
    }

    // More targeted tests to understand scope of issue

    public void testListWithQuotedCommaNotFirstElement() {
        // [a, "b,c"] - quoted comma NOT as first element in list
        assertBothParsersAccept("header\n[a, \"b,c\"]\n");
    }

    public void testSetWithQuotedCommaNotFirstElement() {
        // {a, 'b,c'} - quoted comma NOT as first element in set
        assertBothParsersAccept("header\n{a, 'b,c'}\n");
    }

    public void testListWithQuotedClosingBracket() {
        // ['a]b'] - quoted closing bracket in list
        assertBothParsersAccept("header\n['a]b']\n");
    }

    public void testSetWithQuotedClosingBrace() {
        // {'a}b'} - quoted closing brace in set (single element)
        assertBothParsersAccept("header\n{'a}b'}\n");
    }

    public void testSimpleMapWithQuotedValue() {
        // [key:'value'] - simple map with quoted value (no special chars)
        assertBothParsersAccept("header\n[key:'value']\n");
    }

    // --- Valid edge cases ---

    public void testQuotedEmptyList() {
        // "[]" - quoted brackets treated as string
        assertBothParsersAccept("header\n\"[]\"\n");
    }

    public void testQuotedEmptySet() {
        // "{}" - quoted braces treated as string
        assertBothParsersAccept("header\n\"{}\"\n");
    }

    public void testQuotedEmptyMap() {
        // "[:]" - quoted empty map treated as string
        assertBothParsersAccept("header\n\"[:]\"\n");
    }

    public void testSingleClosingBracket() {
        // ] - closing bracket only is valid string
        assertBothParsersAccept("header\n]\n");
    }

    public void testSingleClosingBrace() {
        // } - closing brace only is valid string
        assertBothParsersAccept("header\n}\n");
    }

    public void testMapClosingOnly() {
        // :] - map closing only is valid string
        assertBothParsersAccept("header\n:]\n");
    }

    public void testPrefixBeforeBrackets() {
        // a[] - prefix before brackets is valid string
        assertBothParsersAccept("header\na[]\n");
    }

    public void testPrefixBeforeBraces() {
        // a{} - prefix before braces is valid string
        assertBothParsersAccept("header\na{}\n");
    }

    public void testPrefixBeforeMap() {
        // a[:] - prefix before map is valid string
        assertBothParsersAccept("header\na[:]\n");
    }

    public void testWhitespaceInList() {
        // [  a  ,  a  ] - padded list
        assertBothParsersAccept("header\n  [  a  ,  a  ]  \n");
    }

    public void testWhitespaceInSet() {
        // {  a  ,  a  } - padded set
        assertBothParsersAccept("header\n  {  a  ,  a  }  \n");
    }

    public void testWhitespaceInMap() {
        // [   a   :  a   ] - padded map
        assertBothParsersAccept("header\n  [   a   :  a   ]  \n");
    }

    // --- Quoted Map Keys ---

    public void testSingleQuotedMapKey() {
        assertBothParsersAccept("header\n['key': 'value']\n");
    }

    public void testDoubleQuotedMapKey() {
        assertBothParsersAccept("header\n[\"key\": \"value\"]\n");
    }

    public void testQuotedMapKeyWithSpaces() {
        assertBothParsersAccept("header\n['key with spaces': value]\n");
    }

    public void testQuotedMapKeyWithColon() {
        assertBothParsersAccept("header\n['key:with:colon': value]\n");
    }

    public void testQuotedMapKeyWithSpecialChars() {
        assertBothParsersAccept("header\n['!@#$%^&*()': value]\n");
    }

    public void testQuotedMapKeyWithBrackets() {
        assertBothParsersAccept("header\n['key[with]brackets': value]\n");
    }

    public void testQuotedMapKeyWithBraces() {
        assertBothParsersAccept("header\n['key{with}braces': value]\n");
    }

    public void testQuotedMapKeyWithComma() {
        assertBothParsersAccept("header\n['key,with,comma': value]\n");
    }

    public void testQuotedMapKeyWithMixedSpecials() {
        assertBothParsersAccept("header\n['key:[],{}': value]\n");
    }

    public void testMixedQuotedAndUnquotedMapKeys() {
        assertBothParsersAccept("header\n['quoted': 1, unquoted: 2, \"double\": 3]\n");
    }

    // --- No escape sequences in quoted values (core pins no-escape semantics) ---

    public void testBackslashDoesNotEscapeDoubleQuote() {
        // "say \"hi\"" - quote closes at the second ", the rest is a stray element
        assertBothParsersReject("header\n\"say \\\"hi\\\"\"\n");
    }

    public void testBackslashDoesNotEscapeSingleQuote() {
        // 'it\'s' - quote closes before s, the rest is a stray element
        assertBothParsersReject("header\n'it\\'s'\n");
    }

    public void testTrailingBackslashInQuotedValue() {
        // "C:\temp\" - backslash before the closing quote is a literal character
        assertBothParsersAccept("header\n\"C:\\temp\\\"\n");
    }

    public void testBackslashesInListElement() {
        // [path\\file] - backslashes are literal characters in compounds
        assertBothParsersAccept("header\n[path\\\\file]\n");
    }

    public void testBackslashDoesNotEscapeQuoteInMapKey() {
        // ["a\"b": 1] - quoted map key closes at the second "
        assertBothParsersReject("header\n[\"a\\\"b\": 1]\n");
    }

    // --- Invalid syntax tests ---

    public void testTripleSingleQuotes() {
        // ''' - triple single quotes
        assertBothParsersReject("header\n'''\n");
    }

    public void testTripleDoubleQuotes() {
        // """ - triple double quotes
        assertBothParsersReject("header\n\"\"\"\n");
    }

    public void testStandaloneOpeningBracket() {
        // [ - standalone opening bracket
        assertBothParsersReject("header\n[\n");
    }

    public void testStandaloneOpeningBrace() {
        // { - standalone opening brace
        assertBothParsersReject("header\n{\n");
    }

    public void testMissingClosingForEmptyMap() {
        // [: - missing closing for empty map
        assertBothParsersReject("header\n[:\n");
    }

    public void testListWithBrace() {
        // [a, b} - list with brace
        assertBothParsersReject("header\n[a, b}\n");
    }

    public void testSetWithBracket() {
        // {a, b] - set with bracket
        assertBothParsersReject("header\n{a, b]\n");
    }

    public void testMapWithBrace() {
        // [a: b} - map with brace
        assertBothParsersReject("header\n[a: b}\n");
    }

    public void testBraceBracketMismatch() {
        // {a: b] - brace-bracket mismatch
        assertBothParsersReject("header\n{a: b]\n");
    }

    public void testDoubleOpeningBracket() {
        // [[] - double opening bracket
        assertBothParsersReject("header\n[[]\n");
    }

    public void testDoubleOpeningBrace() {
        // {{} - double opening brace
        assertBothParsersReject("header\n{{}\n");
    }

    public void testDoubleOpeningWithMap() {
        // [[:] - double opening with map
        assertBothParsersReject("header\n[[:]\n");
    }

    public void testDoubleClosingBracket() {
        // []] - double closing bracket
        assertBothParsersReject("header\n[]]\n");
    }

    public void testDoubleClosingBrace() {
        // {}} - double closing brace
        assertBothParsersReject("header\n{}}\n");
    }

    public void testDoubleClosingWithMap() {
        // [:]] - double closing with map
        assertBothParsersReject("header\n[:]]\n");
    }

    public void testTrailingCharAfterList() {
        // []a - after empty list
        assertBothParsersReject("header\n[]a\n");
    }

    public void testTrailingCharAfterSet() {
        // {}a - after empty set
        assertBothParsersReject("header\n{}a\n");
    }

    public void testTrailingCharAfterMap() {
        // [:]a - after empty map
        assertBothParsersReject("header\n[:]a\n");
    }

    public void testListTrailingComma() {
        // [a,] - list trailing comma
        assertBothParsersReject("header\n[a,]\n");
    }

    public void testSetTrailingComma() {
        // {a,} - set trailing comma
        assertBothParsersReject("header\n{a,}\n");
    }

    public void testMapTrailingComma() {
        // [a:b,] - map trailing comma
        assertBothParsersReject("header\n[a:b,]\n");
    }

    // --- Helper methods ---

    private static final TokenSet PIPE_TOKEN = TokenSet.create(TableTestTypes.PIPE);

    private void assertBothParsersAccept(String input) {
        boolean referenceParserAccepts = referenceParserAccepts(input);
        PsiFile file = createFile("test.table", input);
        boolean pluginParserAccepts = hasNoParseErrors(file);

        String psiTree = toParseTreeText(file, skipSpaces(), includeRanges());

        if (referenceParserAccepts && !pluginParserAccepts) {
            fail("Reference parser accepts but plugin parser rejects.\n" +
                 "Input: " + input.replace("\n", "\\n") + "\n" +
                 "PSI tree:\n" + psiTree);
        }

        if (!referenceParserAccepts && pluginParserAccepts) {
            fail("Reference parser rejects but plugin parser accepts.\n" +
                 "Input: " + input.replace("\n", "\\n") + "\n" +
                 "PSI tree:\n" + psiTree);
        }

        if (referenceParserAccepts && pluginParserAccepts) {
            assertSameRowStructure(input, file, psiTree);
        }
    }

    private void assertSameRowStructure(String input, PsiFile file, String psiTree) {
        List<Integer> referenceCellCounts = referenceCellCountsPerRow(input);
        List<Integer> pluginCellCounts = pluginCellCountsPerRow(file);

        if (!referenceCellCounts.equals(pluginCellCounts)) {
            fail("Both parsers accept but split data rows into different cells.\n" +
                 "Input: " + input.replace("\n", "\\n") + "\n" +
                 "Reference parser cell counts per row: " + referenceCellCounts + "\n" +
                 "Plugin parser cell counts per row: " + pluginCellCounts + "\n" +
                 "PSI tree:\n" + psiTree);
        }
    }

    private List<Integer> referenceCellCountsPerRow(String input) {
        return TableParser.parse(input).rows().stream()
            .map(Row::valueCount)
            .collect(toList());
    }

    private List<Integer> pluginCellCountsPerRow(PsiFile file) {
        return PsiTreeUtil.collectElementsOfType(file, TableTestRow.class).stream()
            .map(this::cellCount)
            .collect(toList());
    }

    /**
     * Empty cells produce no element PSI, so counting elements would undercount;
     * pipe count + 1 matches how the reference parser counts cells.
     */
    private int cellCount(TableTestRow row) {
        return row.getNode().getChildren(PIPE_TOKEN).length + 1;
    }

    private boolean referenceParserAccepts(String input) {
        try {
            TableParser.parse(input);
            return true;
        } catch (TableTestParseException e) {
            return false;
        }
    }

    private boolean hasNoParseErrors(PsiFile file) {
        // Check for PsiErrorElement nodes
        Collection<PsiErrorElement> errors = PsiTreeUtil.collectElementsOfType(file, PsiErrorElement.class);
        if (!errors.isEmpty()) {
            return false;
        }

        // Check for BAD_CHARACTER tokens
        return !containsBadCharacter(file);
    }

    private boolean containsBadCharacter(PsiElement element) {
        if (element.getNode().getElementType() == TokenType.BAD_CHARACTER) {
            return true;
        }
        for (PsiElement child : element.getChildren()) {
            if (containsBadCharacter(child)) {
                return true;
            }
        }
        return false;
    }

    private void assertBothParsersReject(String input) {
        boolean referenceParserAccepts = referenceParserAccepts(input);
        PsiFile file = createFile("test.table", input);
        boolean pluginParserAccepts = hasNoParseErrors(file);

        String psiTree = toParseTreeText(file, skipSpaces(), includeRanges());

        if (referenceParserAccepts && !pluginParserAccepts) {
            fail("Reference parser accepts but plugin parser rejects.\n" +
                 "Input: " + input.replace("\n", "\\n") + "\n" +
                 "PSI tree:\n" + psiTree);
        }

        if (!referenceParserAccepts && pluginParserAccepts) {
            fail("Reference parser rejects but plugin parser accepts.\n" +
                 "Input: " + input.replace("\n", "\\n") + "\n" +
                 "PSI tree:\n" + psiTree);
        }

        // For reject tests, verify both parsers actually reject (neither accepts)
        if (referenceParserAccepts) {
            fail("Expected both parsers to reject, but at least one accepts.\n" +
                 "Input: " + input.replace("\n", "\\n") + "\n" +
                 "Reference parser accepts: " + referenceParserAccepts + "\n" +
                 "Plugin parser accepts: " + pluginParserAccepts + "\n" +
                 "PSI tree:\n" + psiTree);
        }
    }
}
