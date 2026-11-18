// QQMusicLyricsProvider.kt

package com.videshkarmakar.doom.lyrics

import android.content.Context
import com.music.directlyrics.QQMusic
import com.videshkarmakar.doom.constants.EnableQQMusicKey
import com.videshkarmakar.doom.utils.dataStore
import com.videshkarmakar.doom.utils.get

object QQMusicLyricsProvider : LyricsProvider {
    override val name = "QQMusic"
    override fun isEnabled(context: Context): Boolean = context.dataStore[EnableQQMusicKey] ?: true

    override suspend fun getLyrics(
        id: String,
        title: String,
        artist: String,
        duration: Int,
        album: String?,
    ): Result<String> = QQMusic.getLyrics(title, artist, duration)
}
