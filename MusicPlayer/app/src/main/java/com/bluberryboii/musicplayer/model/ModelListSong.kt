package com.bluberryboii.musicplayer.model

import java.io.Serializable

class ModelListSong : Serializable {
    var strId: String? = null

    @JvmField
    var strSongTitle: String? = null

    @JvmField
    var strNameBand: String? = null

    @JvmField
    var strCoverSong: String? = null
}