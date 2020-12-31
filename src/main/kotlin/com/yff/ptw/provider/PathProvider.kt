package com.yff.ptw.provider

interface PathProvider{
    fun getWorkDir():String
    fun getDictPath():String
    fun getCacheDir():String
}