// YouTubeBrowseScreen.kt

package com.videshkarmakar.doom.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.music.innertube.models.AlbumItem
import com.music.innertube.models.ArtistItem
import com.music.innertube.models.PlaylistItem
import com.music.innertube.models.SongItem
import com.videshkarmakar.doom.LocalPlayerAwareWindowInsets
import com.videshkarmakar.doom.LocalPlayerConnection
import com.videshkarmakar.doom.R
import com.videshkarmakar.doom.constants.GridItemSize
import com.videshkarmakar.doom.constants.GridItemsSizeKey
import com.videshkarmakar.doom.constants.GridThumbnailHeight
import com.videshkarmakar.doom.models.toMediaMetadata
import com.videshkarmakar.doom.playback.queues.YouTubeQueue
import com.videshkarmakar.doom.ui.component.IconButton
import com.videshkarmakar.doom.ui.component.LocalMenuState
import com.videshkarmakar.doom.ui.component.YouTubeGridItem
import com.videshkarmakar.doom.ui.component.shimmer.GridItemPlaceHolder
import com.videshkarmakar.doom.ui.component.shimmer.ShimmerHost
import com.videshkarmakar.doom.ui.menu.YouTubeAlbumMenu
import com.videshkarmakar.doom.ui.menu.YouTubeArtistMenu
import com.videshkarmakar.doom.ui.menu.YouTubePlaylistMenu
import com.videshkarmakar.doom.ui.menu.YouTubeSongMenu
import com.videshkarmakar.doom.ui.utils.backToMain
import com.videshkarmakar.doom.utils.rememberEnumPreference
import com.videshkarmakar.doom.viewmodels.YouTubeBrowseViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun YouTubeBrowseScreen(
    navController: NavController,
    viewModel: YouTubeBrowseViewModel = hiltViewModel(),
) {
    val menuState = LocalMenuState.current
    val haptic = LocalHapticFeedback.current
    val playerConnection = LocalPlayerConnection.current ?: return
    val isPlaying by playerConnection.isEffectivelyPlaying.collectAsState()
    val mediaMetadata by playerConnection.mediaMetadata.collectAsState()

    val browseResult by viewModel.result.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val gridItemSize by rememberEnumPreference(GridItemsSizeKey, GridItemSize.BIG)

    val allItems = remember(browseResult) {
        browseResult?.items?.flatMap { it.items }?.distinctBy { it.id } ?: emptyList()
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = GridThumbnailHeight + if (gridItemSize == GridItemSize.BIG) 24.dp else (-24).dp),
        contentPadding = LocalPlayerAwareWindowInsets.current.asPaddingValues(),
    ) {
        if (browseResult == null) {
            items(8) {
                ShimmerHost {
                    GridItemPlaceHolder(fillMaxWidth = true)
                }
            }
        }

        items(
            items = allItems,
            key = { it.id },
            contentType = { it::class }
        ) { item ->
            val active = when (item) {
                is SongItem -> mediaMetadata?.id == item.id
                is AlbumItem -> mediaMetadata?.album?.id == item.id
                else -> false
            }
            YouTubeGridItem(
                item = item,
                isActive = active,
                isPlaying = active && isPlaying,
                fillMaxWidth = true,
                coroutineScope = coroutineScope,
                modifier = Modifier
                    .combinedClickable(
                        onClick = {
                            when (item) {
                                is SongItem -> {
                                    if (item.id == mediaMetadata?.id) {
                                        playerConnection.togglePlayPause()
                                    } else {
                                        playerConnection.playQueue(
                                            YouTubeQueue.radio(item.toMediaMetadata())
                                        )
                                    }
                                }
                                is AlbumItem -> navController.navigate("album/${item.id}")
                                is ArtistItem -> navController.navigate("artist/${item.id}")
                                is PlaylistItem -> navController.navigate("online_playlist/${item.id}")
                            }
                        },
                        onLongClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            menuState.show {
                                when (item) {
                                    is SongItem ->
                                        YouTubeSongMenu(
                                            song = item,
                                            navController = navController,
                                            onDismiss = menuState::dismiss,
                                        )
                                    is AlbumItem ->
                                        YouTubeAlbumMenu(
                                            albumItem = item,
                                            navController = navController,
                                            onDismiss = menuState::dismiss,
                                        )
                                    is ArtistItem ->
                                        YouTubeArtistMenu(
                                            artist = item,
                                            onDismiss = menuState::dismiss,
                                        )
                                    is PlaylistItem ->
                                        YouTubePlaylistMenu(
                                            playlist = item,
                                            coroutineScope = coroutineScope,
                                            onDismiss = menuState::dismiss,
                                            onImportedPlaylist = { playlistId ->
                                                navController.navigate("local_playlist/$playlistId")
                                            },
                                        )
                                }
                            }
                        }
                    )
                    .animateItem()
            )
        }
    }

    TopAppBar(
        title = { Text(browseResult?.title.orEmpty()) },
        navigationIcon = {
            IconButton(
                onClick = navController::navigateUp,
                onLongClick = navController::backToMain
            ) {
                Icon(
                    painterResource(R.drawable.arrow_back_ios),
                    contentDescription = null
                )
            }
        }
    )
}
