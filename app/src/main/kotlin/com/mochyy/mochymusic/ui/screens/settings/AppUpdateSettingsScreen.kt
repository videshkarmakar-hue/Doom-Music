// AppUpdateSettingsScreen.kt

package com.videshkarmakar.doom.ui.screens.settings

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.videshkarmakar.doom.BuildConfig
import com.videshkarmakar.doom.R
import com.videshkarmakar.doom.core.updater.UpdateState
import com.videshkarmakar.doom.core.updater.getAutoUpdateCheckSetting
import com.videshkarmakar.doom.core.updater.getLastCheckedTime
import com.videshkarmakar.doom.core.updater.getUpdateNotificationsSetting
import com.videshkarmakar.doom.core.updater.saveAutoUpdateCheckSetting
import com.videshkarmakar.doom.core.updater.saveLastCheckedTime
import com.videshkarmakar.doom.core.updater.saveUpdateNotificationsSetting
import com.videshkarmakar.doom.ui.component.AppSwitch
import com.videshkarmakar.doom.ui.component.LocalZoomOutOverlayState
import com.videshkarmakar.doom.ui.component.SettingsGroup
import com.videshkarmakar.doom.ui.component.SettingsGroupStyle
import com.videshkarmakar.doom.ui.component.SettingsItem
import com.videshkarmakar.doom.ui.component.SubSettingsScaffold
import com.videshkarmakar.doom.ui.screens.update.checkUpdatesNow
import com.videshkarmakar.doom.ui.screens.update.showUpdateSheet
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// the route the settings list's "App update" bubble opens
const val AppUpdateSettingsRoute = "app_update_settings"

private val CheckedAtFormat = DateTimeFormatter.ofPattern("d MMMM yyyy, h:mm a")

// settings > app update: what's installed, whether something newer is out, and how updates arrive
@Composable
fun AppUpdateSettingsScreen(navController: NavController) {
    if (BuildConfig.PLAY_DISTRIBUTION) {
        SubSettingsScaffold(
            title = stringResource(R.string.app_updates_title),
            onBack = { navController.navigateUp() },
        ) {
            Spacer(Modifier.height(6.dp))
            InfoBubble {
                Paragraph("Updates are delivered by Google Play. Install or update DOOM from the Play Store.")
            }
        }
        return
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sheets = LocalZoomOutOverlayState.current

    var state by remember { mutableStateOf<UpdateState>(UpdateState.UpToDate) }
    var checking by remember { mutableStateOf(false) }
    var lastChecked by remember { mutableStateOf(getLastCheckedTime(context)) }
    var autoCheck by remember { mutableStateOf(getAutoUpdateCheckSetting(context)) }
    var notifications by remember { mutableStateOf(getUpdateNotificationsSetting(context)) }

    suspend fun runCheck() {
        checking = true
        state = runCatching { checkUpdatesNow(context) }
            .getOrElse { UpdateState.Failed(it.message ?: "Couldn't reach GitHub") }
        val stamp = LocalDateTime.now().format(CheckedAtFormat)
        saveLastCheckedTime(context, stamp)
        lastChecked = stamp
        checking = false
    }

    // the page opens on its own answer, unless the automatic check was switched off
    LaunchedEffect(Unit) {
        if (autoCheck) runCheck()
    }

    val release = (state as? UpdateState.Available)?.release

    SubSettingsScaffold(
        title = stringResource(R.string.app_updates_title),
        onBack = { navController.navigateUp() },
    ) {
        Text(
            text = stringResource(R.string.app_update_intro),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.6f),
            modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 14.dp),
        )

        // ---- what's installed ----
        SettingsGroup(
            style = SettingsGroupStyle.Grouped,
            items = listOf(
                SettingsItem(
                    title = { Text(stringResource(R.string.app_update_version_title)) },
                    descriptionText = BuildConfig.VERSION_NAME,
                    icon = painterResource(R.drawable.ic_doom_mark),
                    iconShape = CircleShape,
                ),
                SettingsItem(
                    title = { Text(stringResource(R.string.app_update_last_checked)) },
                    descriptionText = lastChecked.ifBlank { stringResource(R.string.app_update_never_checked) },
                    icon = painterResource(R.drawable.sync),
                    iconShape = CircleShape,
                    onClick = { scope.launch { runCheck() } },
                ),
            ),
        )

        Spacer(Modifier.height(18.dp))

        // ---- the check itself, and the update when there is one ----
        val checkTitle = when {
            checking -> stringResource(R.string.checking_for_updates)
            else -> stringResource(R.string.check_for_update)
        }
        val checkDescription = when (val current = state) {
            is UpdateState.Checking -> stringResource(R.string.checking_for_updates)
            is UpdateState.UpToDate -> stringResource(R.string.on_latest_version)
            is UpdateState.Available -> stringResource(R.string.update_to_v, current.release.version)
            is UpdateState.Failed -> current.message.ifBlank { stringResource(R.string.check_internet_connection) }
        }

        val updateItems = buildList {
            add(
                SettingsItem(
                    title = { Text(checkTitle) },
                    descriptionText = checkDescription,
                    icon = painterResource(R.drawable.update),
                    iconShape = CircleShape,
                    enabled = !checking,
                    onClick = { scope.launch { runCheck() } },
                    trailingContent = if (checking) {
                        { CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = Color.White.copy(alpha = 0.7f)) }
                    } else {
                        null
                    },
                ),
            )
            if (release != null) {
                add(
                    SettingsItem(
                        title = { Text(stringResource(R.string.update_to_v, release.version)) },
                        descriptionText = buildString {
                            append(stringResource(R.string.update_size_v, (release.apkSizeBytes / (1024 * 1024)).toString()))
                            if (release.publishedAt.isNotBlank()) {
                                append(" · ")
                                append(stringResource(R.string.release_date_v, release.publishedAt.take(10)))
                            }
                        },
                        detail = release.body.takeIf { it.isNotBlank() },
                        icon = painterResource(R.drawable.download),
                        iconShape = CircleShape,
                        onClick = { sheets.showUpdateSheet(state) },
                    ),
                )
                add(
                    SettingsItem(
                        title = { Text(stringResource(R.string.whats_new)) },
                        descriptionText = release.body.lineSequence()
                            .map { it.trim().trimStart('-', '*', '#', ' ') }
                            .firstOrNull { it.isNotBlank() }
                            ?: stringResource(R.string.no_changelog_available),
                        detail = release.body.takeIf { it.isNotBlank() },
                        icon = painterResource(R.drawable.list),
                        iconShape = CircleShape,
                        onClick = { sheets.showUpdateSheet(state) },
                    ),
                )
            }
        }

        SettingsGroup(style = SettingsGroupStyle.Grouped, items = updateItems)

        Spacer(Modifier.height(18.dp))

        // ---- how updates arrive ----
        SettingsGroup(
            style = SettingsGroupStyle.Grouped,
            items = listOf(
                SettingsItem(
                    title = { Text(stringResource(R.string.auto_update_check)) },
                    descriptionText = stringResource(R.string.auto_update_check_subtitle),
                    icon = painterResource(R.drawable.network_update),
                    iconShape = CircleShape,
                    onClick = {
                        autoCheck = !autoCheck
                        saveAutoUpdateCheckSetting(context, autoCheck)
                    },
                    trailingContent = {
                        AppSwitch(checked = autoCheck, onCheckedChange = {
                            autoCheck = it
                            saveAutoUpdateCheckSetting(context, it)
                        })
                    },
                ),
                SettingsItem(
                    title = { Text(stringResource(R.string.update_notifications)) },
                    descriptionText = stringResource(R.string.update_notifications_subtitle),
                    icon = painterResource(R.drawable.info),
                    iconShape = CircleShape,
                    onClick = {
                        notifications = !notifications
                        saveUpdateNotificationsSetting(context, notifications)
                    },
                    trailingContent = {
                        AppSwitch(checked = notifications, onCheckedChange = {
                            notifications = it
                            saveUpdateNotificationsSetting(context, it)
                        })
                    },
                ),
            ),
        )

        Spacer(Modifier.height(140.dp))
    }
}
