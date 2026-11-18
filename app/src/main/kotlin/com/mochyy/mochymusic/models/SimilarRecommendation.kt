// SimilarRecommendation.kt

package com.videshkarmakar.doom.models

import com.music.innertube.models.YTItem
import com.videshkarmakar.doom.db.entities.LocalItem

data class SimilarRecommendation(
    val title: LocalItem,
    val items: List<YTItem>,
)
