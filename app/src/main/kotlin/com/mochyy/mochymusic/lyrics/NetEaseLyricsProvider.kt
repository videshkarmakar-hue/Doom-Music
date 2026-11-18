// NetEaseLyricsProvider.kt

package com.videshkarmakar.doom.lyrics

import android.content.Context
import com.music.directlyrics.NetEase
import com.videshkarmakar.doom.constants.EnableNetEaseKey
import com.videshkarmakar.doom.utils.dataStore
import com.videshkarmakar.doom.utils.get

object NetEaseLyricsProvider : LyricsProvider {
    override val name = "NetEase"
    override fun isEnabled(context: Context): Boolean = context.dataStore[EnableNetEaseKey] ?: true

    override suspend fun getLyrics(
        id: String,
        title: String,
        artist: String,
        duration: Int,
        album: String?,
    ): Result<String> = NetEase.getLyrics(title, artist, duration)
}
