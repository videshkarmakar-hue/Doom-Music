// AppInfoScreen.kt

package com.videshkarmakar.doom.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.videshkarmakar.doom.BuildConfig
import com.videshkarmakar.doom.R
import com.videshkarmakar.doom.core.updater.GithubRepoUrl
import com.videshkarmakar.doom.ui.component.LocalZoomOutOverlayState
import com.videshkarmakar.doom.ui.component.SubSettingsScaffold
import com.videshkarmakar.doom.ui.screens.update.openUrl
import com.videshkarmakar.doom.ui.screens.update.showLicenseSheet
import com.videshkarmakar.doom.ui.theme.InterFontFamily

// the route the settings list's "App Info" bubble opens
const val AppInfoRoute = "app_info"

// where the support rows go. the app's own licences are listed inside it, the rest is the project's issue tracker,
// so repointing it is a single line here.
private const val ReportProblemUrl = "$GithubRepoUrl/issues/new"

// settings > app info: the logo and what doom is, the app's own details, and where its legal and support pages live
@Composable
fun AppInfoScreen(navController: NavController) {
    val context = LocalContext.current
    val sheets = LocalZoomOutOverlayState.current

    SubSettingsScaffold(
        title = stringResource(R.string.app_info_title),
        onBack = { navController.navigateUp() },
    ) {
        Spacer(Modifier.height(6.dp))

        // ---- the mark, the name, the line it goes by ----
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .background(IconBgColor),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    model = R.drawable.ic_doom_mark,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(76.dp),
                )
            }
            Spacer(Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.app_info_application_value),
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp,
                lineHeight = 38.sp,
                letterSpacing = (-1.2).sp,
                color = Color.White,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.app_info_tagline),
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 19.sp,
                color = Color.White.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
            )
        }

        Spacer(Modifier.height(22.dp))

        // ---- what it is, in its own words ----
        InfoBubble {
            Paragraph(stringResource(R.string.app_info_intro_one))
            Paragraph(stringResource(R.string.app_info_intro_two))
            Paragraph(stringResource(R.string.app_info_intro_three))
        }

        Spacer(Modifier.height(26.dp))
        SectionLabel(stringResource(R.string.app_info_section_information))
        Spacer(Modifier.height(10.dp))

        InfoBubble {
            SpecRow(stringResource(R.string.app_info_application), stringResource(R.string.app_info_application_value))
            SpecDivider()
            SpecRow(stringResource(R.string.app_info_version), BuildConfig.VERSION_NAME)
            SpecDivider()
            SpecRow(stringResource(R.string.app_info_platform), stringResource(R.string.app_info_platform_value))
            SpecDivider()
            SpecRow(stringResource(R.string.app_info_developed_by), stringResource(R.string.app_info_developed_by_value))
            SpecDivider()
            SpecRow(stringResource(R.string.app_info_design), stringResource(R.string.app_info_design_value))
        }

        Spacer(Modifier.height(26.dp))
        SectionLabel(stringResource(R.string.app_info_section_about))
        Spacer(Modifier.height(10.dp))

        InfoBubble {
            Paragraph(stringResource(R.string.app_info_about_one))
            Paragraph(stringResource(R.string.app_info_about_two))
            Paragraph(stringResource(R.string.app_info_about_three))
            Paragraph(
                text = stringResource(R.string.app_info_motto),
                color = Color.White,
                weight = FontWeight.SemiBold,
            )
            Paragraph(stringResource(R.string.app_info_motto_note))
        }

        Spacer(Modifier.height(26.dp))
        SectionLabel(stringResource(R.string.app_info_section_legal))
        Spacer(Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.app_info_legal_intro),
            fontFamily = InterFontFamily,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            color = Color.White.copy(alpha = 0.55f),
            modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 12.dp),
        )

        InfoBubble(contentPadding = 6.dp) {
            LinkRow(
                iconRes = R.drawable.github,
                title = stringResource(R.string.app_info_open_source),
                subtitle = stringResource(R.string.app_info_open_source_desc),
                onClick = { sheets.showLicenseSheet() },
            )
            LinkDivider()
            LinkRow(
                iconRes = R.drawable.bug_report,
                title = stringResource(R.string.app_info_report),
                subtitle = stringResource(R.string.app_info_report_desc),
                onClick = { context.openUrl(ReportProblemUrl) },
            )
        }

        Spacer(Modifier.height(26.dp))
        SectionLabel(stringResource(R.string.app_info_section_version))
        Spacer(Modifier.height(10.dp))

        InfoBubble {
            Text(
                text = stringResource(R.string.app_info_version_line, BuildConfig.VERSION_NAME),
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = Color.White,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.app_info_released),
                fontFamily = InterFontFamily,
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.55f),
            )
            Spacer(Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.8.dp)
                    .background(Color.White.copy(alpha = 0.08f)),
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.app_info_copyright_line),
                fontFamily = InterFontFamily,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = Color.White.copy(alpha = 0.55f),
            )
        }

        // ---- the sign-off, the way the page opened ----
        Spacer(Modifier.height(34.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.app_info_application_value),
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                letterSpacing = (-0.6).sp,
                color = Color.White.copy(alpha = 0.4f),
            )
            Spacer(Modifier.height(3.dp))
            Text(
                text = stringResource(R.string.app_info_tagline),
                fontFamily = InterFontFamily,
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.3f),
                textAlign = TextAlign.Center,
            )
        }
    }
}

// the bubble every block on the page sits in: the settings list's own card colour, so the page matches the list it came from.
// internal so Developer Credit, the page next to this one, is built from the same pieces
@Composable
internal fun InfoBubble(
    contentPadding: androidx.compose.ui.unit.Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(BubbleColor)
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = content,
    )
}

@Composable
internal fun SectionLabel(text: String) {
    Text(
        text = text,
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        letterSpacing = 1.1.sp,
        color = Color.White.copy(alpha = 0.45f),
        modifier = Modifier.padding(start = 4.dp),
    )
}

@Composable
internal fun Paragraph(
    text: String,
    color: Color = Color.White.copy(alpha = 0.72f),
    weight: FontWeight = FontWeight.Normal,
) {
    Text(
        text = text,
        fontFamily = InterFontFamily,
        fontWeight = weight,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = color,
    )
}

// one line of the app information table: what the field is on the left, what it holds on the right
@Composable
internal fun SpecRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            fontFamily = InterFontFamily,
            fontSize = 13.sp,
            color = Color.White.copy(alpha = 0.55f),
            modifier = Modifier.weight(1f),
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = value,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = Color.White,
            textAlign = TextAlign.End,
        )
    }
}

@Composable
internal fun SpecDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.8.dp)
            .background(Color.White.copy(alpha = 0.08f)),
    )
}

@Composable
private fun LinkDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 62.dp)
            .height(0.8.dp)
            .background(Color.White.copy(alpha = 0.08f)),
    )
}

@Composable
private fun LinkRow(
    iconRes: Int,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 10.dp),
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(IconBgColor),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(19.dp),
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = Color.White,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontFamily = InterFontFamily,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = Color.White.copy(alpha = 0.55f),
            )
        }
        Spacer(Modifier.width(6.dp))
        Icon(
            painter = painterResource(R.drawable.ic_chevron_right),
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.3f),
            modifier = Modifier.size(16.dp),
        )
    }
}
