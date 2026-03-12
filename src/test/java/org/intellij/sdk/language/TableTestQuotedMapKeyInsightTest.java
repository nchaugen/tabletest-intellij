package org.intellij.sdk.language;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;

import java.util.Collection;
import java.util.List;

public class TableTestQuotedMapKeyInsightTest extends LightJavaCodeInsightFixtureTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myFixture.addFileToProject(
            "org/tabletest/junit/TableTest.java",
            """
            package org.tabletest.junit;
            import java.lang.annotation.*;
            @Retention(RetentionPolicy.RUNTIME)
            @Target(ElementType.METHOD)
            public @interface TableTest {
                String value();
            }
            """
        );
        myFixture.setCaresAboutInjection(true);
    }

    public void testJavaQuotedMapKeys() {
        myFixture.configureByText("Test.java", """
            import org.tabletest.junit.TableTest;
            public class Test {
                @TableTest(\"""
                    Header
                    ["double": 1, 'single': 2, unquoted: 3]
                    \""")
                void test() {}
            }
            """);
        assertNoParseErrors();
    }

    public void testKotlinQuotedMapKeys() {
        myFixture.configureByText("Test.kt", """
            import org.tabletest.junit.TableTest
            class Test {
                @TableTest(\"""
                    Header
                    ["double": 1, 'single': 2, unquoted: 3]
                    \""")
                fun test() {
                }
            }
            """);
        assertNoParseErrors();
    }

    public void testStandaloneTableFileQuotedMapKeys() {
        myFixture.configureByText("Standalone.table", """
            Header
            ["double": 1, 'single': 2, unquoted: 3]
            """);
        assertNoParseErrors();
    }

    private void assertNoParseErrors() {
        com.intellij.psi.PsiFile file = myFixture.getFile();
        if (file.getFileType().getName().equals("TableTest")) {
            // Standalone file
            assertNoErrors(file);
        } else {
            // Injected
            InjectedLanguageManager injectedManager = InjectedLanguageManager.getInstance(getProject());
            PsiElement host = PsiTreeUtil.findChildOfType(file, com.intellij.psi.PsiLanguageInjectionHost.class);
            assertNotNull("No injection host found", host);
            List<Pair<PsiElement, TextRange>> injections = injectedManager.getInjectedPsiFiles(host);
            assertNotNull("No injections found", injections);
            assertFalse("Injections list is empty", injections.isEmpty());
            assertNoErrors(injections.get(0).first);
        }
    }

    private void assertNoErrors(PsiElement root) {
        Collection<PsiErrorElement> errors = PsiTreeUtil.collectElementsOfType(root, PsiErrorElement.class);
        if (!errors.isEmpty()) {
            fail("Found parse errors in TableTest (" + root.getContainingFile().getName() + "):\n" +
                 errors.stream().map(e -> e.getErrorDescription() + " at " + e.getTextRange()).reduce("", (a, b) -> a + "\n" + b));
        }
    }
}
