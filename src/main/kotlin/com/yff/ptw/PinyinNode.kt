package com.yff.ptw

import com.google.gson.Gson
import com.yff.ptw.obj.TileNode
import com.yff.ptw.parse.WordsParser
import com.yff.ptw.provider.DictFileProvider
import java.io.File
import java.io.RandomAccessFile

open class PinyinNode(
    protected val cachePath:File,
    protected val dictFileProvider: DictFileProvider,
    protected val wordsParser: WordsParser
) {
    protected lateinit var root:TileNode

    @Throws(Throwable::class)
    fun init() {
        if(haveCache()){
            val root = readFromCache()
            if(root != null) {
                this.root = root
                return
            }
        }
        root = TileNode(' ', mutableListOf(), mutableListOf())
        RandomAccessFile(dictFileProvider.getDictFile(), "rw").use { ra ->
            while (true) {
                val position = ra.filePointer
                val line = ra.readLine() ?: break
                val lineUTF8 = String(line.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)
                val wd = wordsParser.parse(lineUTF8)
                addNode(root, wd.pinyin, 0, position)
            }
            cacheTree()
        }
    }

    protected open fun cacheFile():String {
        return File(cachePath,"treecache.json").absolutePath
    }

    protected open fun cacheTree(){
        val file = File(cacheFile())
        if(file.exists() && file.length() > 0){
            file.delete()
        }
        if(!file.parentFile.exists()){
            file.parentFile.mkdir()
        }
        file.createNewFile()
        val json = Gson().toJson(root)
        file.writeText(json)
    }

    protected open fun readFromCache():TileNode?{
        try {
            val file = File(cacheFile())
            return Gson().fromJson<TileNode>(file.readText(),TileNode::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            val file = File(cacheFile())
            file.delete()
        }
        return null
    }

    private fun haveCache():Boolean{
        return File(cacheFile()).exists()
    }

    fun findNode(pinyin: String): TileNode {
        return findNode(root, pinyin, 0)
    }

    private tailrec fun addNode(node: TileNode, pinyin: String, offset: Int, position: Long): TileNode {
        if (offset > pinyin.length - 1) {
            // reach end
            node.positions.add(position)
            return node
        }
        val c = pinyin[offset]
        val subNode = node.children.find { it.letter == c } ?: TileNode(
            c,
            mutableListOf(),
            mutableListOf()
        ).also {
            node.children.add(it)
        }
        return addNode(subNode, pinyin, offset + 1, position)
    }

    private tailrec fun findNode(node: TileNode, pinyin: String, offset: Int): TileNode {
        if (offset > pinyin.length - 1) {
            // reach end
            return node
        }
        val c = pinyin[offset]
        val subNode = node.children.find { it.letter == c } ?: TileNode(
            c,
            mutableListOf(),
            mutableListOf()
        ).also {
            node.children.add(it)
        }
        return findNode(subNode, pinyin, offset + 1)
    }
}