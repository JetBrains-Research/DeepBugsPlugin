package org.jetbrains.research.deepbugs.javascript

class JSDeepBugsTest : DeepBugsTestBase() {
    fun `test bin operand`() {
        runHighlightTestForFile("testIncorrectBinOperandJS.js")
    }

    fun `test bin operator`() {
        runHighlightTestForFile("testIncorrectBinOperatorJS.js")
    }

    fun `test swapped arguments`() {
        runHighlightTestForFile("testSwappedArgumentsJS.js")
    }

    fun `test swapped arguments math`() {
        runHighlightTestForFile("testSwappedArgumentsMathJS.js")
    }

    fun `test incorrect argument math`() {
        runHighlightTestForFile("testIncorrectArgumentMathJS.js")
    }
}
