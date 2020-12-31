package com.yff.ptw.parse

import com.yff.ptw.obj.Word

class DefaultWordsParser : WordsParser {
    override fun parse(line: String): Word {
        val sp = line.split(" ")
        return Word(sp[1].replace("'", ""), sp[0], sp[2].toInt())
    }
}