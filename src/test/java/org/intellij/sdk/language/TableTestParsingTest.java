package org.intellij.sdk.language;

import com.intellij.testFramework.ParsingTestCase;
import io.github.nchaugen.tabletest.language.TableTestParserDefinition;

public class TableTestParsingTest extends ParsingTestCase {

    public TableTestParsingTest() {
        super("", "table", new TableTestParserDefinition());
    }

    public void testParsingTestData() {
        doTest(true);
    }

    public void testFirstLineComment() {
        // Bug: first-line comment is incorrectly parsed as header
        doTest(true);
    }

    /**
     * @return path to test data file directory relative to root of this module.
     */
    @Override
    protected String getTestDataPath() {
        return "src/test/testData";
    }

    @Override
    protected boolean includeRanges() {
        return true;
    }

}
