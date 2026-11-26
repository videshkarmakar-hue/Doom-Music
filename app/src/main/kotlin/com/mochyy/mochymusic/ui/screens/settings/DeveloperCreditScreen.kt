// DeveloperCreditScreen.kt

package com.videshkarmakar.doom.ui.screens.settings

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Facebook
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.videshkarmakar.doom.R
import com.videshkarmakar.doom.ui.component.SubSettingsScaffold
import com.videshkarmakar.doom.ui.screens.update.openUrl
import com.videshkarmakar.doom.ui.theme.InterFontFamily

// the route the settings list's "Developer Credit" bubble opens
const val DeveloperCreditRoute = "developer_credit"

private const val PortraitUrl =
    "https://cdn.phototourl.com/member/2026-09-11-84fc2df9-c2a9-4384-a9b8-25290f7a1a3d.png"
private const val WebsiteUrl = "https://videshkarmakarlive.vercel.app/"

private val CardSurface = Color(0xFF141418)
private val CardBorder = Color(0xFF232329)
private val ChipBg = Color.White.copy(alpha = 0.05f)
private val ChipBorder = Color(0xFF3A3A42)

// settings > developer credit: the developer, the work, and where to find him
@Composable
fun DeveloperCreditScreen(navController: NavController) {
    val context = LocalContext.current

    SubSettingsScaffold(
        title = stringResource(R.string.developer_credit_title),
        onBack = { navController.navigateUp() },
    ) {
        Spacer(Modifier.height(4.dp))

        // ---- brand header ----
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.developer_credit_wordmark),
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Black,
                fontSize = 40.sp,
                letterSpacing = 2.sp,
                color = Color.White,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.developer_credit_footer_tagline),
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                letterSpacing = 2.sp,
                color = Color(0xFF7E7E88),
                textAlign = TextAlign.Center,
            )
        }

        Spacer(Modifier.height(20.dp))

        // ---- hero card ----
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(CardSurface)
                .border(1.dp, CardBorder, RoundedCornerShape(24.dp))
                .padding(20.dp),
        ) {
            // ambient corner glow
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .align(Alignment.TopEnd)
                    .blur(24.dp)
                    .background(Color.White.copy(alpha = 0.06f), CircleShape),
            )

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // portrait
                    Box {
                        AsyncImage(
                            model = PortraitUrl,
                            contentDescription = "Portrait of Videsh Karmakar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(2.dp, Color(0xFF3A3A42), RoundedCornerShape(16.dp)),
                        )
                        // online dot
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .align(Alignment.BottomEnd)
                                .offset(x = 2.dp, y = 2.dp)
                                .background(Color(0xFF10B981), CircleShape)
                                .border(2.dp, CardSurface, CircleShape),
                        )
                    }

                    Spacer(Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Videsh Karmakar",
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            letterSpacing = (-0.4).sp,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Chip("Developer")
                            Chip("UI/UX Designer")
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFF2A2A30)),
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "DOOM is proudly conceptualized, designed, and developed by Videsh Karmakar — bringing together software engineering, product design, and a passion for technology to create a distinctive digital experience.",
                    fontFamily = InterFontFamily,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    color = Color(0xFFD1D5DB),
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "From the interface and visual identity to the underlying product experience, every detail of DOOM is thoughtfully developed with a focus on simplicity, performance, functionality, and usability.",
                    fontFamily = InterFontFamily,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    color = Color(0xFF9CA3AF),
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // ---- connect ----
        SectionLabel(stringResource(R.string.developer_credit_section_connect))
        Spacer(Modifier.height(10.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SocialCard(
                icon = Icons.Rounded.Language,
                tint = Color(0xFFD1D5DB),
                title = stringResource(R.string.developer_credit_official_website),
                subtitle = stringResource(R.string.developer_credit_official_website_value),
                onClick = { context.openUrl(WebsiteUrl) },
            )
            SocialCard(
                icon = Icons.Rounded.MusicNote,
                tint = Color(0xFF34D399),
                title = stringResource(R.string.developer_credit_spotify),
                subtitle = stringResource(R.string.developer_credit_spotify_value),
                onClick = { /* placeholder */ },
            )
            SocialCard(
                icon = Icons.Rounded.CameraAlt,
                tint = Color(0xFFFB7185),
                title = stringResource(R.string.developer_credit_instagram),
                subtitle = stringResource(R.string.developer_credit_instagram_value),
                onClick = { /* placeholder */ },
            )
            SocialCard(
                icon = Icons.Rounded.PlayCircle,
                tint = Color(0xFFEF4444),
                title = stringResource(R.string.developer_credit_youtube),
                subtitle = stringResource(R.string.developer_credit_youtube_value),
                onClick = { /* placeholder */ },
            )
            SocialCard(
                icon = Icons.Rounded.Facebook,
                tint = Color(0xFF60A5FA),
                title = stringResource(R.string.developer_credit_facebook),
                subtitle = stringResource(R.string.developer_credit_facebook_value),
                onClick = { /* placeholder */ },
            )
        }

        Spacer(Modifier.height(24.dp))

        // ---- development & design ----
        SectionLabel(stringResource(R.string.developer_credit_section_development))
        Spacer(Modifier.height(10.dp))
        InfoBubble {
            SpecRow(stringResource(R.string.developer_credit_product), stringResource(R.string.developer_credit_wordmark))
            SpecDivider()
            SpecRow(stringResource(R.string.developer_credit_developed_by), stringResource(R.string.developer_credit_developer))
            SpecDivider()
            SpecRow(stringResource(R.string.developer_credit_design), stringResource(R.string.developer_credit_design_value))
            SpecDivider()
            SpecRow(stringResource(R.string.developer_credit_platform), stringResource(R.string.developer_credit_platform_value))
        }

        Spacer(Modifier.height(24.dp))

        // ---- acknowledgement ----
        SectionLabel(stringResource(R.string.developer_credit_section_acknowledgement))
        Spacer(Modifier.height(10.dp))
        InfoBubble {
            Paragraph(stringResource(R.string.developer_credit_ack))
            Paragraph(
                text = stringResource(R.string.developer_credit_thanks),
                color = Color.White,
                weight = FontWeight.SemiBold,
            )
        }

        // ---- footer ----
        Spacer(Modifier.height(32.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.developer_credit_copyright),
                fontFamily = InterFontFamily,
                fontSize = 12.sp,
                color = Color(0xFF9CA3AF),
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.developer_credit_wordmark),
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                letterSpacing = 2.sp,
                color = Color.White.copy(alpha = 0.25f),
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = stringResource(R.string.developer_credit_footer_tagline),
                fontFamily = InterFontFamily,
                fontSize = 10.sp,
                letterSpacing = 2.sp,
                color = Color.White.copy(alpha = 0.2f),
                textAlign = TextAlign.Center,
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun Chip(text: String) {
    Text(
        text = text,
        fontFamily = InterFontFamily,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFFD1D5DB),
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(ChipBg)
            .border(1.dp, ChipBorder, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp),
    )
}

@Composable
private fun SocialCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardSurface)
            .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF0A0A0D))
                .border(1.dp, Color(0xFF2A2A30), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(18.dp),
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
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontFamily = InterFontFamily,
                fontSize = 11.sp,
                color = Color(0xFF7E7E88),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Spacer(Modifier.width(8.dp))
        Icon(
            painter = androidx.compose.ui.res.painterResource(com.videshkarmakar.doom.R.drawable.ic_chevron_right),
            contentDescription = null,
            tint = Color(0xFF4B5563),
            modifier = Modifier.size(16.dp),
        )
    }
}
