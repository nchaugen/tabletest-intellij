class Test {

    //language=tabletest
    @TableTest(
        """
        a       | b     | c
        asdf| asdf  | asdfasfasf
        asdf         | adf   | as
        aa;lsdkldsjf | asdfa | asdf
        // asdfasfd
        af|asf|asdf<caret>
        a         | b     | c
        
        aasdfdsf     | af    | sdaf
        asd      | asf   | as
        a   | a  | a
        """
    )
    fun test(a: String, b: String, c: String) {}
}
