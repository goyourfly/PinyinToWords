package com.yff.ptw

import com.yff.ptw.obj.ProtoBuf
import com.yff.ptw.obj.TileNode
import com.yff.ptw.parse.WordsParser
import com.yff.ptw.provider.DictFileProvider
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class PinyinNodeProtoBuf(
    cachePath:File,
    dictFileProvider: DictFileProvider,
    wordsParser: WordsParser) : PinyinNode(cachePath,dictFileProvider, wordsParser) {

    override fun cacheFile(): String {
        return File(cachePath,"treecache.proto").absolutePath
    }

    override fun cacheTree() {
        val file = File(cacheFile())
        if(file.exists() && file.length() > 0){
            file.delete()
        }
        if(!file.parentFile.exists()){
            file.parentFile.mkdir()
        }
        file.createNewFile()
        val proto = ProtoBuf.TileNodePB.newBuilder()
        generateProto(proto,root)
        FileOutputStream(file).use {
            proto.build().writeTo(it)
        }
    }

    override fun readFromCache(): TileNode? {
        try {
            val file = File(cacheFile())
            FileInputStream(file).use {
                val proto = ProtoBuf.TileNodePB.parseFrom(it)
                return generateNode(proto)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            val file = File(cacheFile())
            file.delete()
        }
        return null
    }

    private fun generateProto(proto: ProtoBuf.TileNodePB.Builder,node: TileNode){
        proto.letter = node.letter.toString()
        proto.addAllPositions(node.positions)
        val list = mutableListOf<ProtoBuf.TileNodePB>()
        for(child in node.children){
            val protoChild = ProtoBuf.TileNodePB.newBuilder()
            generateProto(protoChild,child)
            list.add(protoChild.build())
        }
        proto.addAllChildren(list)
    }

    private fun generateNode(proto: ProtoBuf.TileNodePB): TileNode {
        val node = TileNode(proto.letter[0], mutableListOf(), mutableListOf())
        node.letter = proto.letter[0]
        node.positions.addAll(proto.positionsList)
        node.children.addAll(proto.childrenList.map { generateNode(it) })
        return node
    }
}