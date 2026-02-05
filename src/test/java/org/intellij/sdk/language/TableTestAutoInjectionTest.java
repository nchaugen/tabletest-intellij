package org.intellij.sdk.language;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;

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
}
