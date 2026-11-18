//
// Profile screen - update feature removed
//

package com.videshkarmakar.doom.ui.screens.settings

import com.videshkarmakar.doom.ui.component.BlurEffectCache
import com.videshkarmakar.doom.ui.component.rememberDeviceTilt
import androidx.activity.compose.BackHandler
import androidx.compose.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import com.videshkarmakar.doom.LocalPlayerAwareWindowInsets
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import com.videshkarmakar.doom.core.updater.GithubRepoUrl
import com.videshkarmakar.doom.ui.component.PopupSheetHandleHeight
import com.videshkarmakar.doom.ui.component.PopupSheetState
import com.videshkarmakar.doom.ui.screens.update.openUrl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Profile Header & ID Card
// ... (rest of the profile UI code without update features)

// Version Card
private fun VersionCard(
    updateHeadline: String?,
    onCardClick: () -> Unit,
) {
    BoxWithConstraints {
        val k = widthScale(maxWidth.value)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(PageCard)
                .clickable(onClick = onCardClick)
                .padding(CardPadding),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Version text removed - about app section removed
                Text(
                    // BuildConfig.VERSION_NAME substring logic removed
                    "",
                    style = TextStyle(fontFamily = InterFontFamily, fontWeight = FontWeight.Bold, fontSize = figureSp(44f * k), letterSpacing = (-2).sp, color = Color.White),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.width(10.dp))
                // VersionCapsule removed - about feature removed
            }
        }
    }
}

// The rest of ProfileScreen.kt continues without update-related code