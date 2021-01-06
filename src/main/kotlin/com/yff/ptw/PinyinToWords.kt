package com.yff.ptw

import com.yff.ptw.obj.CacheType
import com.yff.ptw.obj.Word
import com.yff.ptw.parse.DefaultWordsParser
import com.yff.ptw.parse.WordsParser
import com.yff.ptw.provider.DefaultDictFileProvider
import com.yff.ptw.provider.DictFileProvider
import java.io.File
import java.io.RandomAccessFile

object PinyinToWords {
    private lateinit var dictFileProvider: DictFileProvider
    private lateinit var wordParser: WordsParser
    private lateinit var pinyinNode: PinyinNode

    private var isInit = false

    @Synchronized
    fun setPathProvider(dictFileProvider: DictFileProvider) {
        isInit = false
        this.dictFileProvider = dictFileProvider
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
    @JvmOverloads
    fun init(
        cachePath:File,
        cacheType:CacheType = CacheType.TYPE_PROTO_BUF) {
        if (isInit) throw IllegalAccessException("You has init")
        if (!this::dictFileProvider.isInitialized) {
            dictFileProvider =
                DefaultDictFileProvider(cachePath)
        }
        if (!this::wordParser.isInitialized) {
            wordParser =
                DefaultWordsParser()
        }

        when(cacheType) {
            CacheType.TYPE_JSON -> {
                pinyinNode = PinyinNode(
                    cachePath,
                    dictFileProvider,
                    wordParser
                )
            }
            CacheType.TYPE_PROTO_BUF -> {
                pinyinNode = PinyinNodeProtoBuf(
                    cachePath,
                    dictFileProvider,
                    wordParser
                )
            }
        }
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
        val list = mutableListOf<Word>()
        val node = pinyinNode.findNode(pinyin)?:return list
        RandomAccessFile(dictFileProvider.getDictFile(),"rw").use { ra ->
            for(p in node.positions){
                ra.seek(p)
                val line = String(ra.readLine().toByteArray(Charsets.ISO_8859_1),Charsets.UTF_8)
                list.add(wordParser.parse(line))
            }
        }
        return list
    }
}