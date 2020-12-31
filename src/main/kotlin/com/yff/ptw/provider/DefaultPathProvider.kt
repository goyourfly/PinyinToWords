package com.yff.ptw.provider

import java.nio.file.Paths

class DefaultPathProvider : PathProvider {
    override fun getDictPath(): String {
        return Paths.get("").toAbsolutePath().toString() + "/src/main/resources/webdict_with_freq_with_pinyin.txt"
    }

    override fun getCacheDir(): String {
        return Paths.get("").toAbsolutePath().toString() + "/cache"
    }
}