package org.intellij.sdk.language;

import com.intellij.psi.PsiElement;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import io.github.nchaugen.tabletest.inspections.TableTestImplicitUsageProvider;
import io.github.nchaugen.tabletest.inspections.TableTestKotlinImplicitUsageProvider;
import org.jetbrains.kotlin.psi.KtNamedFunction;

import static com.intellij.psi.util.PsiTreeUtil.*;

public class TableTestInspectionsTest extends LightJavaCodeInsightFixtureTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        // Add @TableTest definition
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

        // Add @TypeConverter definition
        myFixture.addFileToProject(
            "org/tabletest/junit/TypeConverter.java",
            """
            package org.tabletest.junit;
            import java.lang.annotation.*;
            @Retention(RetentionPolicy.RUNTIME)
            @Target(ElementType.METHOD)
            public @interface TypeConverter {}
            """
        );
    }

    public void testTableTestMethodIsUsed() {
        myFixture.configureByText("Test.java", """
            import org.tabletest.junit.TableTest;
            public class Test {
                @TableTest("header\\nvalue")
                void usedMethod() {}
            }
            """);
        PsiElement method = findChildOfType(myFixture.getFile(), com.intellij.psi.PsiMethod.class);
        assertTrue(
            "Expected @TableTest method to be implicit usage",
            new TableTestImplicitUsageProvider().isImplicitUsage(method)
        );
    }

    public void testTypeConverterMethodIsUsed() {
        myFixture.configureByText("ConverterTest.java", """
            import org.tabletest.junit.TypeConverter;
            public class ConverterTest {
                @TypeConverter
                public Object convert(String s) { return s; }
            }
            """);
        PsiElement method = findChildOfType(myFixture.getFile(), com.intellij.psi.PsiMethod.class);
        assertTrue(
            "Expected @TypeConverter method to be implicit usage",
            new TableTestImplicitUsageProvider().isImplicitUsage(method)
        );
    }

    public void testKotlinTableTestMethodIsUsed() {
        myFixture.configureByText("Test.kt", """
            import org.tabletest.junit.TableTest
            class Test {
                @TableTest("header\\nvalue")
                fun usedMethod() {}
            }
            """);
        PsiElement function = findChildOfType(myFixture.getFile(), KtNamedFunction.class);
        assertTrue(
            "Expected @TableTest function to be implicit usage",
            new TableTestKotlinImplicitUsageProvider().isImplicitUsage(function)
        );
    }
}
