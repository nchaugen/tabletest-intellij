package org.intellij.sdk.language;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import io.github.nchaugen.tabletest.language.TableTestSyntaxHighlighter;
import junit.framework.TestCase;

public class TableTestSyntaxHighlighterTest extends TestCase {

    public void testExpectedHeaderUsesFunctionDeclarationAsFallback() {
        TextAttributesKey fallback =
            TableTestSyntaxHighlighter.Companion.getOUTPUT_HEADER_KEY().getFallbackAttributeKey();

        assertSame(DefaultLanguageHighlighterColors.FUNCTION_DECLARATION, fallback);
    }
}
