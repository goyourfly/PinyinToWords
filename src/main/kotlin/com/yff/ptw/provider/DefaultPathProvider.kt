package com.yff.ptw.provider

import com.yff.ptw.provider.PathProvider
import java.nio.file.Paths

class DefaultPathProvider : PathProvider {
    override fun getWorkDir(): String {
        return Paths.get("").toAbsolutePath().toString()
    }

    override fun getDictPath(): String {
        return getWorkDir() + "/src/main/resources/webdict_with_freq_with_pinyin.txt"
    }

    override fun getCacheDir(): String {
        return Paths.get("").toAbsolutePath().toString() + "/cache"
    }
}