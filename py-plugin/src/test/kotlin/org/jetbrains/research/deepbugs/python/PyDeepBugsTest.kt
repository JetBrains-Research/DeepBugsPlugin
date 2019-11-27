package org.jetbrains.research.deepbugs.python

class PyDeepBugsTest : DeepBugsTestBase() {
    fun `test bin operand`() {
        runHighlightTestForFile("testIncorrectBinOperandPy.py")
    }

    fun `test bin operator`() {
        runHighlightTestForFile("testIncorrectBinOperatorPy.py")
    }

    fun `test swapped arguments`() {
        runHighlightTestForFile("testSwappedArgumentsPy.py")
    }
}
