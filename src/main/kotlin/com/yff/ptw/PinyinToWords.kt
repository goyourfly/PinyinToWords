package com.yff.ptw

import com.yff.ptw.obj.Word
import com.yff.ptw.parse.DefaultWordsParser
import com.yff.ptw.parse.WordsParser
import com.yff.ptw.provider.DefaultPathProvider
import com.yff.ptw.provider.PathProvider
import java.io.RandomAccessFile

object PinyinToWords {
    private lateinit var pathProvider: PathProvider
    private lateinit var wordParser: WordsParser
    private lateinit var pinyinNode: PinyinNode

    private var isInit = false

    @Synchronized
    fun setPathProvider(pathProvider: PathProvider) {
        isInit = false
        this.pathProvider = pathProvider
    }

    @Synchronized
    fun setWordsParser(wordParser: WordsParser) {
        isInit = false
        this.wordParser = wordParser
    }

    /**
     * Cost time
     */
    @Synchronized
    fun init() {
        if (isInit) throw IllegalAccessException("You has init")
        if (!this::pathProvider.isInitialized) {
            pathProvider =
                DefaultPathProvider()
        }
        if (!this::wordParser.isInitialized) {
            wordParser =
                DefaultWordsParser()
        }
        pinyinNode = PinyinNode(
            pathProvider,
            wordParser
        )
        pinyinNode.init()
        isInit = true
    }

    fun isInit() = isInit

    private fun checkInit(){
        if (!isInit) throw IllegalAccessException("You have not init")
    }

    @Synchronized
    fun findWords(pinyin:String):List<Word> {
        checkInit()
        val node = pinyinNode.findNode(pinyin)
        val list = mutableListOf<Word>()
        RandomAccessFile(pathProvider.getDictPath(),"rw").use { ra ->
            for(p in node.value){
                ra.seek(p)
                val line = String(ra.readLine().toByteArray(Charsets.ISO_8859_1),Charsets.UTF_8)
                list.add(wordParser.parse(line))
            }
        }
        return list
    }
}