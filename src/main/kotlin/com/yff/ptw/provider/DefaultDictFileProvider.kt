package com.yff.ptw.provider

import java.io.File
import java.io.FileOutputStream

class DefaultDictFileProvider(private val cacheDir:File) : DictFileProvider {
    override fun getDictFile(): File {
        // copy to cache file
        val outFile = File(cacheDir, "webdict_with_freq_with_pinyin.txt")
        if(outFile.exists() && outFile.length() > 10){
            return outFile
        }
        if(!outFile.parentFile.exists()){
            outFile.parentFile.mkdirs()
        }
        outFile.createNewFile()
        Thread.currentThread().contextClassLoader.getResourceAsStream("webdict_with_freq_with_pinyin.txt").use { inputStream ->
            FileOutputStream(outFile).use { fileOutputStream ->
                inputStream?.copyTo(fileOutputStream)
            }
        }
        return outFile
    }
}