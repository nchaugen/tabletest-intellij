public class Test {

    //language=tabletest
    @TableTest("""
        a              | b                              | c d e?
        []             | [:]                            | {}
        [[]]           | [a: []]                        | {[]}
        [[:]]          | [a: [:]]                       | {[:]}
        [{}]           | [a: {}]                        | {{}}
        [1, 2, 3]      | [one: 1, two: 2, three: 3]     | {1, 2, 3}
        ['1', "2", 3]  | [one: '1', two: "2", three: 3] | {'1', "2", 3}
        [[], [], []]   | [a: [], b: [], c: []]          | {[], [], []}
        abc            | a,b,c                          | > [ a , b , c ] <
        1              | 2                              | 3
        '1'            | "2"                            | 3
        ''             | ""                             |
        
        //
        // comment
        
        xx             | yy                             | zz
        æææ            | øøø                            | ååå
        [1, 2, 3]      | [one: 1, two: 2, three: 3]     | {1, 2, 3}
        !@#$%^&*()_+-= | "|,[]{}:"                      | 12:34:56.789
        naïve résumé   | α β γ δ ε                      | ∑ ∏ ∫ √
        ┌─┐│ │└─┘      | $€£¥₹                          | «»""''—–
        //   the end
        """)
    void test(String a, String b, String c) {}
}
