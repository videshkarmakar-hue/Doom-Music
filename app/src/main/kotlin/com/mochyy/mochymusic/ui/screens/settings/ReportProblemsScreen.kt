// ReportProblemsScreen.kt

package com.videshkarmakar.doom.ui.screens.settings

import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.videshkarmakar.doom.BuildConfig
import com.videshkarmakar.doom.R
import com.videshkarmakar.doom.ui.component.SubSettingsScaffold
import com.videshkarmakar.doom.ui.screens.update.openUrl
import com.videshkarmakar.doom.ui.theme.InterFontFamily

// the route the settings list's "Report Problems" bubble opens
const val ReportProblemsRoute = "report_problems"

private const val ReportEmail = "videshkarmakar@gmail.com"
private const val ReportEmailUrl = "mailto:$ReportEmail?subject=Problem%20report%20-%20Doom%20Music"
private const val ReportInstagramUrl = "https://ig.me/m/code.red_videsh"
private const val ReportGithubIssuesUrl = "https://github.com/videshkarmakar-hue/Doom-Music/issues/new"

private val DoomSurface = Color(0xFF161618)
private val DoomBorder = Color(0xFF242429)
private val DoomMuted = Color(0xFF8E8E93)
private val DoomSurfaceElevated = Color(0xFF1A1A1E)

@Composable
fun ReportProblemsScreen(navController: NavController) {
    val context = LocalContext.current

    var selectedCategory by remember { mutableStateOf("Bug / Crash") }
    var description by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var attachDiagnostics by remember { mutableStateOf(true) }

    val categories = listOf("Bug / Crash", "Audio / Visual", "Performance", "Other")

    SubSettingsScaffold(
        title = stringResource(R.string.report_problems_title),
        onBack = { navController.navigateUp() },
    ) {
        Spacer(Modifier.height(6.dp))

        // Direct Channels
        SectionLabel("DIRECT CHANNELS")
        Spacer(Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(DoomSurface)
                .border(1.dp, DoomBorder, RoundedCornerShape(16.dp)),
        ) {
            DirectChannelRow(
                iconRes = R.drawable.chat_msg,
                title = "Direct Message Developer",
                subtitle = "Direct ticket system · Average reply < 24h",
                onClick = { context.openUrl(ReportEmailUrl) },
            )
            ChannelDivider()
            DirectChannelRow(
                iconRes = R.drawable.bug_report,
                title = "Submit Crash / Bug Ticket",
                subtitle = "Forward stack trace directly to engineering desk",
                onClick = { context.openUrl(ReportGithubIssuesUrl) },
            )
            ChannelDivider()
            DirectChannelRow(
                iconRes = R.drawable.sparks,
                title = "Feature Request & Feedback",
                subtitle = "Suggest enhancements or UI refinements",
                onClick = { context.openUrl(ReportGithubIssuesUrl) },
            )
        }

        Spacer(Modifier.height(24.dp))

        // Instant Issue Report
        SectionLabel("INSTANT ISSUE REPORT")
        Spacer(Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(DoomSurface)
                .border(1.dp, DoomBorder, RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Category pills
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Issue Category",
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.8f),
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    categories.forEach { cat ->
                        val active = cat == selectedCategory
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (active) Color.White else Color(0xFF101012))
                                .border(1.dp, if (active) Color.White else Color(0xFF2A2A30), RoundedCornerShape(12.dp))
                                .clickable { selectedCategory = cat }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                        ) {
                            Text(
                                text = cat,
                                fontFamily = InterFontFamily,
                                fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal,
                                fontSize = 12.sp,
                                color = if (active) Color.Black else Color(0xFF9A9AA0),
                            )
                        }
                    }
                }
            }

            // Description
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "What happened?",
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.8f),
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = {
                        Text(
                            text = "Describe the problem briefly, steps to reproduce, or unexpected behavior...",
                            fontFamily = InterFontFamily,
                            fontSize = 12.sp,
                            color = Color(0xFF6B6B70),
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF101012),
                        unfocusedContainerColor = Color(0xFF101012),
                        focusedBorderColor = Color(0xFF3A3A40),
                        unfocusedBorderColor = Color(0xFF26262C),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                    ),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                )
            }

            // Contact
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Your Contact",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f),
                    )
                    Text(
                        text = "Optional · For developer reply only",
                        fontFamily = InterFontFamily,
                        fontSize = 10.sp,
                        color = Color(0xFF6B6B70),
                    )
                }
                OutlinedTextField(
                    value = contact,
                    onValueChange = { contact = it },
                    placeholder = {
                        Text(
                            text = "Email or Telegram handle (kept strictly private)",
                            fontFamily = InterFontFamily,
                            fontSize = 12.sp,
                            color = Color(0xFF6B6B70),
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF101012),
                        unfocusedContainerColor = Color(0xFF101012),
                        focusedBorderColor = Color(0xFF3A3A40),
                        unfocusedBorderColor = Color(0xFF26262C),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                    ),
                )
            }

            // Diagnostics toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Attach System Diagnostics",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = Color.White,
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "${Build.MODEL} · Android ${Build.VERSION.RELEASE} · DOOM v${BuildConfig.VERSION_NAME}",
                        fontFamily = InterFontFamily,
                        fontSize = 10.sp,
                        color = DoomMuted,
                    )
                }
                Switch(
                    checked = attachDiagnostics,
                    onCheckedChange = { attachDiagnostics = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Black,
                        checkedTrackColor = Color.White,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFF2A2A30),
                    ),
                )
            }

            // Submit
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .clickable {
                        val diag = if (attachDiagnostics) {
                            "\n\n--- Diagnostics ---\nApp: DOOM v${BuildConfig.VERSION_NAME}\nPlatform: Android ${Build.VERSION.RELEASE} (${Build.VERSION.SDK_INT})\nDevice: ${Build.MODEL} (${Build.MANUFACTURER})\nArch: ${BuildConfig.ARCHITECTURE}"
                        } else ""
                        val body = "Category: $selectedCategory\n\n$description\n\nContact: ${contact.ifBlank { "not provided" }}$diag"
                        val subject = "Problem report - DOOM Music ($selectedCategory)"
                        val url = "mailto:$ReportEmail?subject=${encode(subject)}&body=${encode(body)}"
                        context.openUrl(url)
                        Toast.makeText(context, "Report dispatched to DOOM Engineering Desk.", Toast.LENGTH_SHORT).show()
                    }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "SEND REPORT DIRECTLY",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Color.Black,
                        letterSpacing = 0.6.sp,
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(R.drawable.arrow_forward),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(14.dp),
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Client Environment
        SectionLabel("CLIENT ENVIRONMENT")
        Spacer(Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF121214))
                .border(1.dp, Color(0xFF202024), RoundedCornerShape(16.dp))
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            SpecRow("APPLICATION", "DOOM v${BuildConfig.VERSION_NAME} (Production)")
            SpecRow("PLATFORM", "Android ${Build.VERSION.RELEASE} (HyperOS)")
            SpecRow("DEVICE MODEL", Build.MODEL)
            SpecRow("BUILD ARCH", "${BuildConfig.ARCHITECTURE} · Release")
        }

        Spacer(Modifier.height(24.dp))

        // Footer
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "DOOM",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                letterSpacing = 4.sp,
                color = Color.White,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Crafted With Vision. Built With Purpose.",
                fontFamily = InterFontFamily,
                fontSize = 10.sp,
                color = Color(0xFF6B6B70),
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "© 2026 DOOM Engine. Direct developer dispatch mode active.",
                fontFamily = FontFamily.Monospace,
                fontSize = 9.sp,
                color = Color(0xFF5A5A60),
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun DirectChannelRow(
    iconRes: Int,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF242429))
                .border(1.dp, Color.White.copy(alpha = 0.05f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.size(20.dp),
            )
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color.White,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontFamily = InterFontFamily,
                fontSize = 12.sp,
                color = DoomMuted,
            )
        }
        Icon(
            painter = painterResource(R.drawable.ic_chevron_right),
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.35f),
            modifier = Modifier.size(16.dp),
        )
    }
}

@Composable
private fun ChannelDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 70.dp)
            .height(0.8.dp)
            .background(Color.White.copy(alpha = 0.06f)),
    )
}

private fun encode(value: String): String =
    java.net.URLEncoder.encode(value, "UTF-8").replace("+", "%20")
