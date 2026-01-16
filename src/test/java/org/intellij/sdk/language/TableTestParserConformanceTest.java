package org.intellij.sdk.language;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.ParsingTestCase;
import io.github.nchaugen.tabletest.language.TableTestParserDefinition;
import io.github.nchaugen.tabletest.parser.TableParser;
import io.github.nchaugen.tabletest.parser.TableTestParseException;

import java.util.Collection;

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

    // --- Helper methods ---

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
}
