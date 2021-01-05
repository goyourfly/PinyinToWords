package com.yff.ptw.obj

import com.google.gson.annotations.SerializedName

data class TileNode(
    @SerializedName("a")
    var letter: Char,
    @SerializedName("b")
    var children: MutableList<TileNode>,
    @SerializedName("c")
    var positions: MutableList<Long>)