// DeezerLyricsProvider.kt

package com.videshkarmakar.doom.lyrics

import android.content.Context
import com.music.directlyrics.Deezer
import com.videshkarmakar.doom.constants.EnableDeezerKey
import com.videshkarmakar.doom.utils.dataStore
import com.videshkarmakar.doom.utils.get

object DeezerLyricsProvider : LyricsProvider {
    override val name = "Deezer"
    override fun isEnabled(context: Context): Boolean = context.dataStore[EnableDeezerKey] ?: true

    override suspend fun getLyrics(
        id: String,
        title: String,
        artist: String,
        duration: Int,
        album: String?,
    ): Result<String> = Deezer.getLyrics(title, artist, duration)
}
