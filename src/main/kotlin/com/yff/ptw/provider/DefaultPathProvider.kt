package com.yff.ptw.provider

import java.io.File
import java.io.FileOutputStream
import java.nio.file.Paths

class DefaultPathProvider : PathProvider {
    override fun getDictPath(): String {
        // copy to cache file
        val outFile = File(getCacheDir() + "/webdict_with_freq_with_pinyin.txt")
        if(outFile.exists() && outFile.length() > 10){
            return outFile.path
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
        return outFile.path
    }

    override fun getCacheDir(): String {
        return Paths.get("").toAbsolutePath().toString() + "/cache"
    }
}