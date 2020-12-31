package com.yff.ptw.parse

import com.yff.ptw.obj.Word

interface WordsParser {
    fun parse(line: String): Word
}