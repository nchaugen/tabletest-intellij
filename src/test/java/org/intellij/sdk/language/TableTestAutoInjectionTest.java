package org.intellij.sdk.language;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;

import java.util.Collection;
import java.util.List;

/**
 * Tests whether language injection works automatically without the //language=tabletest hint.
 *
 * <p>The XML-based injection in injections.xml matches on the annotation class FQNs
 * ({@code io.github.nchaugen.tabletest.junit.TableTest} and
 * {@code org.tabletest.junit.TableTest}). Since Kotlin code imports and uses the same
 * Java annotation, auto-injection works in both languages.
 *
 * <p>Findings:
 * <ul>
 *   <li>With actual tabletest annotation: Auto-injection works in both Java and Kotlin</li>
 *   <li>With local annotation (different FQN): No auto-injection</li>
 *   <li>With //language=tabletest hint: Always works regardless of annotation</li>
 * </ul>
 */
public class TableTestAutoInjectionTest extends LightJavaCodeInsightFixtureTestCase {

    // === Tests with real TableTest annotations (auto-injection) ===

    /**
     * Tests auto-injection in Java with the legacy TableTest annotation package.
     * Expected: Injection works because the FQN matches the injection pattern.
     */
    public void testJavaAutoInjection_RealAnnotation_Works() {
        // Add the legacy TableTest annotation to the fixture's project
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

        myFixture.setCaresAboutInjection(true);
        myFixture.configureByText("Test.java", """
            import io.github.nchaugen.tabletest.junit.TableTest;

            public class Test {
                @TableTest(\"""
                    header1 | header2
                    a<caret>       | b
                    \""")
                void test() {}
            }
            """);

        assertInjectionPresent("Java with real annotation (no hint)");
    }

    /**
     * Tests auto-injection in Kotlin with the legacy TableTest annotation package.
     * Expected: Injection works because IntelliJ bridges Java injection patterns to Kotlin.
     */
    public void testKotlinAutoInjection_RealAnnotation_Works() {
        // Add the legacy TableTest annotation to the fixture's project
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

        myFixture.setCaresAboutInjection(true);
        myFixture.configureByText("Test.kt", """
            import io.github.nchaugen.tabletest.junit.TableTest

            class Test {
                @TableTest(\"""
                    header1 | header2
                    a<caret>       | b
                    \""")
                fun test() {}
            }
            """);

        assertInjectionPresent("Kotlin with real annotation (no hint)");
    }

    /**
     * Tests auto-injection in Java with the new TableTest annotation package.
     * Expected: Injection works because the FQN matches the injection pattern.
     */
    public void testJavaAutoInjection_NewAnnotation_Works() {
        // Add the new TableTest annotation to the fixture's project
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
        myFixture.configureByText("Test.java", """
            import org.tabletest.junit.TableTest;

            public class Test {
                @TableTest(\"""
                    header1 | header2
                    a<caret>       | b
                    \""")
                void test() {}
            }
            """);

        assertInjectionPresent("Java with new annotation (no hint)");
    }

    /**
     * Tests auto-injection in Java with a single-line string annotation value.
     * Expected: Injection works for Java versions without text blocks.
     */
    public void testJavaAutoInjection_RealAnnotation_SingleString_Works() {
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

        myFixture.setCaresAboutInjection(true);
        myFixture.configureByText("Test.java", """
            import io.github.nchaugen.tabletest.junit.TableTest;

            public class Test {
                @TableTest("header1 | heade<caret>r2")
                void test() {}
            }
            """);

        assertInjectionPresent("Java single string with real annotation (no hint)");
    }

    /**
     * Tests auto-injection in Java for array-based value declarations with the legacy annotation package.
     * Expected: Injection works across array entries.
     */
    public void testJavaAutoInjection_RealAnnotation_Array_Works() {
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

        myFixture.setCaresAboutInjection(true);
        myFixture.configureByText("Test.java", """
            import io.github.nchaugen.tabletest.junit.TableTest;

            public class Test {
                @TableTest({
                    "header1 | header2",
                    "a<caret>       | b"
                })
                void test() {}
            }
            """);

        assertSingleInjectionPresent("Java array with real annotation (no hint)");
    }

    /**
     * Tests auto-injection in Java for array-based value declarations with the new annotation package.
     * Expected: Injection works across array entries.
     */
    public void testJavaAutoInjection_NewAnnotation_Array_Works() {
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

        myFixture.setCaresAboutInjection(true);
        myFixture.configureByText("Test.java", """
            import org.tabletest.junit.TableTest;

            public class Test {
                @TableTest({
                    "header1 | header2",
                    "a<caret>       | b"
                })
                void test() {}
            }
            """);

        assertSingleInjectionPresent("Java array with new annotation (no hint)");
    }

    public void testJavaAutoInjection_ArrayContainsNewlinesBetweenElements() {
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

        myFixture.setCaresAboutInjection(true);
        myFixture.configureByText("Test.java", """
            import org.tabletest.junit.TableTest;

            public class Test {
                @TableTest({
                    "header1 | header2",
                    "a<caret> | b",
                    "c | d"
                })
                void test() {}
            }
            """);

        List<Pair<PsiElement, TextRange>> injections = findInjections();
        assertNotNull("Expected injection to be present", injections);
        assertEquals("Expected exactly one injection", 1, injections.size());

        String injectedText = injections.getFirst().first.getText();
        assertTrue("Expected newline between first and second array elements", injectedText.contains("header1 | header2\na | b"));
        assertTrue("Expected newline between second and third array elements", injectedText.contains("a | b\nc | d"));
    }

    public void testJavaAutoInjection_TextBlockContainsHeaderRowNewline() {
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
        myFixture.configureByText("ModernLeapYearExampleTest.java", """
            import org.tabletest.junit.TableTest;

            public class ModernLeapYearExampleTest {
                @TableTest(\"""
                    Scenario                              | Example years      | Is leap year?
                    years not divisible by 4              | {2001, 2002, 2003} | fal<caret>se
                    years divisible by 4                  | {2004, 2008, 2012} | true
                    \""")
                void shouldDetermineLeapYear() {}
            }
            """);

        List<Pair<PsiElement, TextRange>> injections = findInjections();
        assertNotNull("Expected injection to be present", injections);
        StringBuilder failures = new StringBuilder();
        for (int i = 0; i < injections.size(); i++) {
            PsiElement injectedFragment = injections.get(i).first;
            String text = injectedFragment.getText().replace("\n", "\\n");
            Collection<PsiErrorElement> errors = PsiTreeUtil.collectElementsOfType(injectedFragment, PsiErrorElement.class);
            if (!errors.isEmpty()) {
                failures.append(i)
                    .append(":<")
                    .append(text)
                    .append("> error=")
                    .append(errors.iterator().next().getErrorDescription())
                    .append('\n');
            }
        }
        assertTrue("Injected fragments with parse errors:\n" + failures, failures.isEmpty());
    }

    /**
     * Tests safe-failure behaviour for mixed array values with unsupported expressions.
     * Expected: No TableTest injection is created.
     */
    public void testJavaAutoInjection_ArrayWithUnsupportedEntry_NoInjection() {
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

        myFixture.setCaresAboutInjection(true);
        myFixture.configureByText("Test.java", """
            import org.tabletest.junit.TableTest;

            public class Test {
                @TableTest({
                    "header1 | heade<caret>r2",
                    "a | b" + suffix
                })
                void test() {}

                String suffix = "x";
            }
            """);

        assertNoInjection("Java array with unsupported entry (no hint)");
    }

    /**
     * Tests auto-injection in Kotlin with the new TableTest annotation package.
     * Expected: Injection works because IntelliJ bridges Java injection patterns to Kotlin.
     */
    public void testKotlinAutoInjection_NewAnnotation_Works() {
        // Add the new TableTest annotation to the fixture's project
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
        myFixture.configureByText("Test.kt", """
            import org.tabletest.junit.TableTest

            class Test {
                @TableTest(\"""
                    header1 | header2
                    a<caret>       | b
                    \""")
                fun test() {}
            }
            """);

        assertInjectionPresent("Kotlin with new annotation (no hint)");
    }

    // === Tests with local annotation (no auto-injection) ===

    /**
     * Tests that a local annotation with different FQN does not trigger auto-injection.
     */
    public void testJavaAutoInjection_LocalAnnotation_NoInjection() {
        myFixture.setCaresAboutInjection(true);
        myFixture.configureByText("Test.java", """
            public class Test {
                @TableTest(\"""
                    header1 | header2
                    a<caret>       | b
                    \""")
                void test() {}
            }

            @interface TableTest {
                String value();
            }
            """);

        assertNoInjection("Java with local annotation (no hint)");
    }

    /**
     * Tests that a local annotation with different FQN does not trigger auto-injection in Kotlin.
     */
    public void testKotlinAutoInjection_LocalAnnotation_NoInjection() {
        myFixture.setCaresAboutInjection(true);
        myFixture.configureByText("Test.kt", """
            annotation class TableTest(val value: String)

            class Test {
                @TableTest(\"""
                    header1 | header2
                    a<caret>       | b
                    \""")
                fun test() {}
            }
            """);

        assertNoInjection("Kotlin with local annotation (no hint)");
    }

    // === Tests with language hint (always works) ===

    /**
     * Tests that the //language=tabletest hint enables injection regardless of annotation FQN.
     */
    public void testJavaInjection_WithHint_Works() {
        myFixture.setCaresAboutInjection(true);
        myFixture.configureByText("Test.java", """
            public class Test {
                //language=tabletest
                @TableTest(\"""
                    header1 | header2
                    a<caret>       | b
                    \""")
                void test() {}
            }

            @interface TableTest {
                String value();
            }
            """);

        assertInjectionPresent("Java with hint");
    }

    /**
     * Tests that the //language=tabletest hint enables injection in Kotlin.
     */
    public void testKotlinInjection_WithHint_Works() {
        myFixture.setCaresAboutInjection(true);
        myFixture.configureByText("Test.kt", """
            annotation class TableTest(val value: String)

            class Test {
                //language=tabletest
                @TableTest(\"""
                    header1 | header2
                    a<caret>       | b
                    \""")
                fun test() {}
            }
            """);

        assertInjectionPresent("Kotlin with hint");
    }

    // === Helper methods ===

    private void assertInjectionPresent(String context) {
        List<Pair<PsiElement, TextRange>> injections = findInjections();
        assertNotNull(context + ": Expected injection to be present", injections);
        assertFalse(context + ": Expected at least one injection", injections.isEmpty());

        PsiElement injectedElement = injections.getFirst().first;
        assertEquals(context + ": Expected TableTest language",
            "TableTest", injectedElement.getLanguage().getID());
    }

    private void assertNoInjection(String context) {
        List<Pair<PsiElement, TextRange>> injections = findInjections();
        assertTrue(context + ": Expected no injection, but found one",
            injections == null || injections.isEmpty());
    }

    private void assertSingleInjectionPresent(String context) {
        List<Pair<PsiElement, TextRange>> injections = findInjections();
        assertNotNull(context + ": Expected injection to be present", injections);
        assertEquals(context + ": Expected exactly one injection", 1, injections.size());
        assertEquals(
            context + ": Expected TableTest language",
            "TableTest",
            injections.getFirst().first.getLanguage().getID()
        );
    }

    private List<Pair<PsiElement, TextRange>> findInjections() {
        PsiFile file = myFixture.getFile();
        int caretOffset = myFixture.getCaretOffset();
        PsiElement elementAtCaret = file.findElementAt(caretOffset);

        InjectedLanguageManager injectedManager = InjectedLanguageManager.getInstance(getProject());

        // Check if already in injected context
        PsiLanguageInjectionHost host = injectedManager.getInjectionHost(elementAtCaret);
        if (host != null) {
            return injectedManager.getInjectedPsiFiles(host);
        }

        // Find injection host in parent hierarchy
        PsiElement parent = elementAtCaret;
        while (parent != null && !(parent instanceof PsiLanguageInjectionHost)) {
            parent = parent.getParent();
        }

        if (parent instanceof PsiLanguageInjectionHost injectionHost) {
            return injectedManager.getInjectedPsiFiles(injectionHost);
        }

        return null;
    }
    /**
     * Tests auto-injection in Java using named 'value = ...' syntax with a text block.
     */
    public void testJavaAutoInjection_RealAnnotation_NamedValue_Works() {
        myFixture.addFileToProject(
            "io/github/nchaugen/tabletest/junit/TableTest.java",
            """
            package io.github.nchaugen.tabletest.junit;
            public @interface TableTest {
                String value();
            }
            """
        );
        myFixture.setCaresAboutInjection(true);
        myFixture.configureByText("Test.java", """
            import io.github.nchaugen.tabletest.junit.TableTest;
            class Test {
                @TableTest(value = \"""
                    header
                    v<caret>alue
                    \""")
                void test() {}
            }
            """);
        assertInjectionPresent("Java named value with real annotation");
    }

    /**
     * Tests auto-injection in Java for string arrays with named 'value = ...' syntax.
     */
    public void testJavaAutoInjection_RealAnnotation_Array_NamedValue_Works() {
        myFixture.addFileToProject(
            "io/github/nchaugen/tabletest/junit/TableTest.java",
            """
            package io.github.nchaugen.tabletest.junit;
            public @interface TableTest {
                String[] value();
            }
            """
        );
        myFixture.setCaresAboutInjection(true);
        myFixture.configureByText("Test.java", """
            import io.github.nchaugen.tabletest.junit.TableTest;
            class Test {
                @TableTest(value = {
                    "header",
                    "v<caret>alue"
                })
                void test() {}
            }
            """);
        assertSingleInjectionPresent("Java array named value with real annotation");
    }

    public void testNegativeSyntaxInjected() {
        myFixture.setCaresAboutInjection(true);
        // Add annotation definition to ensure auto-injection/hint works
        myFixture.addFileToProject(
            "org/tabletest/junit/TableTest.java",
            "package org.tabletest.junit; public @interface TableTest { String value(); }"
        );

        // Java malformed list
        myFixture.configureByText("Test.java", """
            import org.tabletest.junit.TableTest;
            class Test {
                @TableTest("[a<caret>, b}")
                void test() {}
            }
            """);
        assertHasError();

        // Kotlin malformed map
        myFixture.configureByText("Test.kt", """
            import org.tabletest.junit.TableTest
            class Test {
                @TableTest("[key<caret>: value}")
                fun test() {}
            }
            """);
        assertHasError();
    }

    private void assertHasError() {
        PsiFile file = myFixture.getFile();
        PsiElement injectedFile;
        if (file.getLanguage().getID().equals("TableTest")) {
            injectedFile = file;
        } else {
            InjectedLanguageManager injectedManager = InjectedLanguageManager.getInstance(getProject());
            PsiElement elementAtCaret = file.findElementAt(myFixture.getCaretOffset());
            assertNotNull("No element at caret", elementAtCaret);
            PsiLanguageInjectionHost host = PsiTreeUtil.getParentOfType(elementAtCaret, PsiLanguageInjectionHost.class, false);
            assertNotNull("No injection host found at caret", host);
            List<Pair<PsiElement, TextRange>> injections = injectedManager.getInjectedPsiFiles(host);
            assertNotNull("No injections found for host", injections);
            assertFalse("Injections list is empty", injections.isEmpty());
            injectedFile = injections.getFirst().first;
        }
        assertFalse(
            "Expected parse errors in injected TableTest, but none found",
            PsiTreeUtil.collectElementsOfType(injectedFile, PsiErrorElement.class).isEmpty()
        );
    }

}
