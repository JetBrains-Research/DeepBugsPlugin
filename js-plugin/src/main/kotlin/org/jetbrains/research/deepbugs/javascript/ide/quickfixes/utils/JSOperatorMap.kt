package org.jetbrains.research.deepbugs.javascript.ide.quickfixes.utils

import com.intellij.lang.javascript.JSTokenTypes

internal val operators by lazy {
    hashMapOf(
        JSTokenTypes.AND.toString() to "&",
        JSTokenTypes.ANDAND.toString() to "&&",
        JSTokenTypes.AS_KEYWORD.toString() to "as",
        JSTokenTypes.COMMA.toString() to ",",
        JSTokenTypes.DIV.toString() to "/",
        JSTokenTypes.EQEQ.toString() to "==",
        JSTokenTypes.EQEQEQ.toString() to "===",
        JSTokenTypes.GE.toString() to ">=",
        JSTokenTypes.GT.toString() to ">",
        JSTokenTypes.GTGT.toString() to ">>",
        JSTokenTypes.GTGTGT.toString() to ">>>",
        JSTokenTypes.INSTANCEOF_KEYWORD.toString() to "instance of",
        JSTokenTypes.IN_KEYWORD.toString() to "in",
        JSTokenTypes.LE.toString() to "<=",
        JSTokenTypes.LT.toString() to "<",
        JSTokenTypes.LTLT.toString() to "<<",
        JSTokenTypes.MINUS.toString() to "-",
        JSTokenTypes.MULT.toString() to "*",
        JSTokenTypes.NE.toString() to "!=",
        JSTokenTypes.NEQEQ.toString() to "!==",
        JSTokenTypes.OR.toString() to "|",
        JSTokenTypes.OROR.toString() to "||",
        JSTokenTypes.PERC.toString() to "%",
        JSTokenTypes.PLUS.toString() to "+",
        JSTokenTypes.XOR.toString() to "^"
    )
}
