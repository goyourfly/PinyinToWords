package com.yff.ptw.provider

interface PathProvider{
    fun getDictPath():String
    fun getCacheDir():String
}