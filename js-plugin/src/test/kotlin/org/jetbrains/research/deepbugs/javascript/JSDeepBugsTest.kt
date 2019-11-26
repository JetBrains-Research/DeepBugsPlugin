package org.jetbrains.research.deepbugs.javascript

class JSDeepBugsTest: DeepBugsTestBase() {
    fun `test bin operand`() {
        runHighlightTestForFile("testIncorrectBinOperandJS.js")
    }

    fun `test bin operator`() {
        runHighlightTestForFile("testIncorrectBinOperatorJS.js")
    }


    fun `test swapped argument`() {
        runHighlightTestForFile("testSwappedArgumentsJS.js")
    }
}
