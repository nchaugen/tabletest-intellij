package org.intellij.sdk.language;

import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;

public class TableTestCommenterTest extends LightJavaCodeInsightFixtureTestCase {

    public void testCommentLineInTableFile() {
        myFixture.configureByText("test.table", "<caret>value1|value2");
        myFixture.performEditorAction(IdeActions.ACTION_COMMENT_LINE);
        myFixture.checkResult("// value1|value2");
    }

    public void testUncommentLineInTableFile() {
        myFixture.configureByText("test.table", "<caret>// value1|value2");
        myFixture.performEditorAction(IdeActions.ACTION_COMMENT_LINE);
        myFixture.checkResult("value1|value2");
    }

    public void testCommentLineInJavaFile() {
        myFixture.configureByText("Test.java", """
                public class Test {
                
                    //language=tabletest
                    @TableTest(\"""
                        abc | def | ghi
                        1   | 2   | 3<caret>
                        4   | 5   | 6
                        \""")
                    void test(String a, String b, String c) {}
                }
                """);
        myFixture.performEditorAction(IdeActions.ACTION_COMMENT_LINE);
        myFixture.checkResult("""
                public class Test {
                
                    //language=tabletest
                    @TableTest(""\"
                        abc | def | ghi
                //        1   | 2   | 3
                        4   | 5   | 6
                        ""\")
                    void test(String a, String b, String c) {}
                }
                """);
    }

    public void testUncommentLineInJavaFile() {
        myFixture.configureByText("Test.java", """
                public class Test {
                
                    //language=tabletest
                    @TableTest(\"""
                        abc | def | ghi
                //        1   | 2   | 3<caret>
                        4   | 5   | 6
                        \""")
                    void test(String a, String b, String c) {}
                }
                """);
        myFixture.performEditorAction(IdeActions.ACTION_COMMENT_LINE);
        myFixture.checkResult("""
                public class Test {
                
                    //language=tabletest
                    @TableTest(\"""
                        abc | def | ghi
                        1   | 2   | 3
                        4   | 5   | 6
                        \""")
                    void test(String a, String b, String c) {}
                }
                """);
    }

    public void testCommentLineInKotlinFile() {
        myFixture.configureByText("Test.kt", """
                class Test {
                
                    //language=tabletest
                    @TableTest(\"""
                        <caret>abc | def | ghi
                        1   | 2   | 3
                        4   | 5   | 6
                        \""")
                    fun test(a: String, b: String, c: String) {}
                }
                """);
        myFixture.performEditorAction(IdeActions.ACTION_COMMENT_LINE);
        myFixture.checkResult("""
                class Test {
                
                    //language=tabletest
                    @TableTest(""\"
                //        abc | def | ghi
                        1   | 2   | 3
                        4   | 5   | 6
                        ""\")
                    fun test(a: String, b: String, c: String) {}
                }
                """);
    }

    public void testUncommentLineInKotlinFile() {
        myFixture.configureByText("Test.kt", """
                class Test {
                
                    //language=tabletest
                    @TableTest(\"""
                        abc | def | ghi
                        1   | 2   | 3
                //        4   | 5 <caret>  | 6
                        \""")
                    fun test(a: String, b: String, c: String) {}
                }
                """);
        myFixture.performEditorAction(IdeActions.ACTION_COMMENT_LINE);
        myFixture.checkResult("""
                class Test {
                
                    //language=tabletest
                    @TableTest(\"""
                        abc | def | ghi
                        1   | 2   | 3
                        4   | 5   | 6
                        \""")
                    fun test(a: String, b: String, c: String) {}
                }
                """);
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/testData";
    }

}
