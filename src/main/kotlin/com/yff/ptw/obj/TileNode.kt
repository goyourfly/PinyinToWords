package com.yff.ptw.obj

data class TileNode(val letter: Char, val children: MutableList<TileNode>, val value: MutableList<Long>)