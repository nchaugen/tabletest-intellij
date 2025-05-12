public class Test {

    //language=tabletest
    @TableTest("""
        abc | def | ghi
        1 | 2 | 3
        4 | 5 | 6
        """)
    void test(String a, String b, String c) {}
}
