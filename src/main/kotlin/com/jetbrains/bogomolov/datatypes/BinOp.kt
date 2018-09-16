package com.jetbrains.bogomolov.datatypes

import com.beust.klaxon.Klaxon
import java.io.File

data class BinOp(val left: String,
                 val right: String,
                 val op: String,
                 val leftType: String,
                 val rightType: String,
                 val parent: String,
                 val grandParent: String,
                 val src: String)

/**
 * Read json with binary operations from file.
 * @param path path to file.
 * @return list of [BinOp] parsed from file.
 */
fun readBinOps(path: String) = Klaxon().parseArray<BinOp>(File(path).inputStream())
        ?: throw ParsingFailedException(path)

class ParsingFailedException(path: String) : Exception("Unable to parse file at:\n$path")