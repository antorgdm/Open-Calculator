package com.example.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.preferences.AngleMode
import com.example.data.preferences.AppPreferences
import com.example.data.preferences.ThemeMode
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val colors = LocalFlatThemeColors.current
    val isDark = colors.isDark
    val preferences = remember { AppPreferences.getInstance(context) }

    val themeMode by preferences.themeMode.collectAsState()
    val haptics by preferences.hapticsEnabled.collectAsState()
    val sound by preferences.soundEnabled.collectAsState()
    val precision by preferences.decimalPrecision.collectAsState()
    val angleMode by preferences.angleMode.collectAsState()
    val languageCode by preferences.languageCode.collectAsState()
    val numeralScript by preferences.numeralScript.collectAsState()

    val surfaceColor = colors.surface
    val borderColor = colors.border
    val bgColor = colors.background

    var showLanguageDialog by remember { mutableStateOf(false) }
    var showNumeralDialog by remember { mutableStateOf(false) }
    var langSearchQuery by remember { mutableStateOf("") }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    val currentLang = remember(languageCode) {
        com.example.domain.languages.LanguageEngine.allLanguages.firstOrNull { it.code == languageCode }
            ?: com.example.domain.languages.LanguageEngine.allLanguages.first()
    }

    Scaffold(
        containerColor = bgColor,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            "Settings & Preferences",
                            fontWeight = FontWeight.Bold,
                            color = colors.headerContent
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colors.header
                    )
                )
                HorizontalDivider(thickness = 1.dp, color = borderColor)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(modifier = Modifier.height(2.dp))

            // 1. Language & Regional Section
            Text(
                "LANGUAGE & REGIONAL SCRIPTS",
                style = MaterialTheme.typography.labelMedium,
                color = if (isDark) FlatDarkPrimary else FlatLightPrimary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = surfaceColor,
                border = BorderStroke(1.dp, borderColor),
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Language row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "App Language",
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) FlatDarkText else FlatLightText
                            )
                            Text(
                                "${currentLang.englishName} (${currentLang.nativeName})",
                                style = MaterialTheme.typography.bodySmall,
                                color = FlatBelizeHole,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Surface(
                            onClick = {
                                langSearchQuery = ""
                                showLanguageDialog = true
                            },
                            shape = RoundedCornerShape(4.dp),
                            color = if (isDark) FlatWetAsphalt else FlatClouds,
                            border = BorderStroke(1.dp, borderColor),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Box(modifier = Modifier.padding(horizontal = 12.dp), contentAlignment = Alignment.Center) {
                                Text("Change", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    HorizontalDivider(thickness = 1.dp, color = borderColor)

                    // Numeral script row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Numeral System",
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) FlatDarkText else FlatLightText
                            )
                            Text(
                                "${numeralScript.displayName} • ${numeralScript.zeroToNine}",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted
                            )
                        }
                        Surface(
                            onClick = { showNumeralDialog = true },
                            shape = RoundedCornerShape(4.dp),
                            color = if (isDark) FlatWetAsphalt else FlatClouds,
                            border = BorderStroke(1.dp, borderColor),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Box(modifier = Modifier.padding(horizontal = 12.dp), contentAlignment = Alignment.Center) {
                                Text("Select", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // 2. Appearance Section
            Text(
                "APPEARANCE & THEME",
                style = MaterialTheme.typography.labelMedium,
                color = colors.primaryButton,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = surfaceColor,
                border = BorderStroke(1.dp, borderColor),
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    // Standard theme selection (System, Light, Dark)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Theme Mode",
                            fontWeight = FontWeight.Bold,
                            color = colors.mainText
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf(
                                ThemeMode.SYSTEM to "System",
                                ThemeMode.LIGHT to "Light",
                                ThemeMode.DARK to "Dark"
                            ).forEach { (mode, label) ->
                                val isSelected = themeMode == mode
                                Surface(
                                    onClick = { preferences.setThemeMode(mode) },
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (isSelected) colors.primaryButton else (if (isDark) FlatWetAsphalt else FlatClouds),
                                    border = BorderStroke(1.dp, borderColor),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Box(modifier = Modifier.padding(horizontal = 10.dp), contentAlignment = Alignment.Center) {
                                        Text(
                                            text = label,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color(0xFF07332C) else colors.mainText
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 3. Calculation Engine Section
            Text(
                "CALCULATION ENGINE",
                style = MaterialTheme.typography.labelMedium,
                color = if (isDark) FlatDarkPrimary else FlatLightPrimary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = surfaceColor,
                border = BorderStroke(1.dp, borderColor),
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Trig Angle Unit",
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) FlatDarkText else FlatLightText
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Surface(
                                onClick = { preferences.setAngleMode(AngleMode.DEGREE) },
                                shape = RoundedCornerShape(4.dp),
                                color = if (angleMode == AngleMode.DEGREE) FlatBelizeHole else (if (isDark) FlatWetAsphalt else FlatClouds),
                                border = BorderStroke(1.dp, borderColor),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Box(modifier = Modifier.padding(horizontal = 10.dp), contentAlignment = Alignment.Center) {
                                    Text(
                                        "Degrees",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (angleMode == AngleMode.DEGREE) Color.White else (if (isDark) FlatDarkText else FlatLightText)
                                    )
                                }
                            }
                            Surface(
                                onClick = { preferences.setAngleMode(AngleMode.RADIAN) },
                                shape = RoundedCornerShape(4.dp),
                                color = if (angleMode == AngleMode.RADIAN) FlatBelizeHole else (if (isDark) FlatWetAsphalt else FlatClouds),
                                border = BorderStroke(1.dp, borderColor),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Box(modifier = Modifier.padding(horizontal = 10.dp), contentAlignment = Alignment.Center) {
                                    Text(
                                        "Radians",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (angleMode == AngleMode.RADIAN) Color.White else (if (isDark) FlatDarkText else FlatLightText)
                                    )
                                }
                            }
                        }
                    }

                    HorizontalDivider(thickness = 1.dp, color = borderColor)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Decimal Precision",
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) FlatDarkText else FlatLightText
                            )
                            Text(
                                "$precision decimal places",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val canDec = precision > 2
                            Surface(
                                onClick = { preferences.setDecimalPrecision(precision - 1) },
                                enabled = canDec,
                                shape = RoundedCornerShape(4.dp),
                                color = (if (isDark) FlatWetAsphalt else FlatClouds).copy(alpha = if (canDec) 1f else 0.35f),
                                border = BorderStroke(1.dp, borderColor.copy(alpha = if (canDec) 1f else 0.35f)),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.Remove,
                                        contentDescription = "Decrease",
                                        modifier = Modifier.size(18.dp),
                                        tint = colors.mainText.copy(alpha = if (canDec) 1f else 0.35f)
                                    )
                                }
                            }
                            Text(
                                "$precision",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 14.dp),
                                color = colors.mainText
                            )
                            val canInc = precision < 12
                            Surface(
                                onClick = { preferences.setDecimalPrecision(precision + 1) },
                                enabled = canInc,
                                shape = RoundedCornerShape(4.dp),
                                color = (if (isDark) FlatWetAsphalt else FlatClouds).copy(alpha = if (canInc) 1f else 0.35f),
                                border = BorderStroke(1.dp, borderColor.copy(alpha = if (canInc) 1f else 0.35f)),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "Increase",
                                        modifier = Modifier.size(18.dp),
                                        tint = colors.mainText.copy(alpha = if (canInc) 1f else 0.35f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. Feedback & Audio Section
            Text(
                "FEEDBACK & TACTILITY",
                style = MaterialTheme.typography.labelMedium,
                color = if (isDark) FlatDarkPrimary else FlatLightPrimary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = surfaceColor,
                border = BorderStroke(1.dp, borderColor),
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Haptic Feedback", fontWeight = FontWeight.Bold, color = if (isDark) FlatDarkText else FlatLightText)
                            Text("Vibration tap on button presses", style = MaterialTheme.typography.bodySmall, color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted)
                        }
                        Switch(
                            checked = haptics,
                            onCheckedChange = { preferences.setHapticsEnabled(it) }
                        )
                    }

                    HorizontalDivider(thickness = 1.dp, color = borderColor)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Key Click Sound", fontWeight = FontWeight.Bold, color = if (isDark) FlatDarkText else FlatLightText)
                            Text("Audio tone when typing digits", style = MaterialTheme.typography.bodySmall, color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted)
                        }
                        Switch(
                            checked = sound,
                            onCheckedChange = { preferences.setSoundEnabled(it) }
                        )
                    }
                }
            }

            // 5. Information Section
            Text(
                "INFORMATION",
                style = MaterialTheme.typography.labelMedium,
                color = if (isDark) FlatDarkPrimary else FlatLightPrimary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = surfaceColor,
                border = BorderStroke(1.dp, borderColor),
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showPrivacyDialog = true }
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = FlatTurquoise)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Privacy Policy", fontWeight = FontWeight.SemiBold, color = if (isDark) FlatDarkText else FlatLightText)
                        }
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = if (isDark) FlatDarkTextMuted else FlatLightTextMuted)
                    }

                    HorizontalDivider(thickness = 1.dp, color = borderColor)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showTermsDialog = true }
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Description, contentDescription = null, tint = FlatOrange)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Terms & Conditions", fontWeight = FontWeight.SemiBold, color = if (isDark) FlatDarkText else FlatLightText)
                        }
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = if (isDark) FlatDarkTextMuted else FlatLightTextMuted)
                    }

                    HorizontalDivider(thickness = 1.dp, color = borderColor)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showAboutDialog = true }
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = FlatBelizeHole)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("About Open Calculator", fontWeight = FontWeight.SemiBold, color = if (isDark) FlatDarkText else FlatLightText)
                        }
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = if (isDark) FlatDarkTextMuted else FlatLightTextMuted)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            shape = RoundedCornerShape(4.dp),
            containerColor = surfaceColor,
            tonalElevation = 0.dp,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = FlatTurquoise)
                    Text("Privacy Policy", fontWeight = FontWeight.Bold, color = if (isDark) FlatDarkText else FlatLightText)
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .heightIn(max = 480.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (isDark) FlatMidnightBlue else FlatClouds,
                        border = BorderStroke(1.dp, borderColor)
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                "Open Calculator — operated by SE7EN (\"we\", \"us\", the \"Service Provider\")",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = if (isDark) FlatDarkText else FlatLightText
                            )
                            Text(
                                "Effective date: 2026-10-15",
                                fontSize = 11.sp,
                                color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted
                            )
                        }
                    }

                    PolicySectionItem(
                        title = "Summary",
                        content = "Open Calculator works entirely on your device. We do not collect, store on a server, sell, share or transmit any personal information. The app has no accounts, no advertising, no analytics, no crash-reporting service and no internet permission.",
                        isDark = isDark
                    )

                    PolicySectionItem(
                        title = "What the app stores on your device",
                        content = "The following is saved locally on your phone only, so the app can work, and never leaves it:",
                        bullets = listOf(
                            "Your calculation history (expressions, results, favorites, timestamps)",
                            "Your settings (theme, language, numeral style, decimal precision, angle mode, sound and vibration choices)",
                            "Memory and variable values used by the calculator"
                        ),
                        isDark = isDark
                    )

                    Text(
                        text = "You can delete history at any time inside the app (History screen). Uninstalling the app removes all of this data. App data is excluded from Android cloud backup and device-to-device transfer.",
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted
                    )

                    PolicySectionItem(
                        title = "Information we do not collect",
                        content = "We do not collect your name, email, phone number, location, contacts, photos, device identifiers, IP address, usage statistics or any other personal data. We do not use cookies, tracking pixels or advertising identifiers.",
                        isDark = isDark
                    )

                    PolicySectionItem(
                        title = "Permissions",
                        content = "The app requests only the Vibrate permission, used for optional haptic feedback on key presses. It does not request internet, location, camera, microphone, storage or contacts permissions.",
                        isDark = isDark
                    )

                    PolicySectionItem(
                        title = "Clipboard",
                        content = "The Copy and Paste buttons use the system clipboard only when you tap them. Clipboard contents are never read in the background or sent anywhere.",
                        isDark = isDark
                    )

                    PolicySectionItem(
                        title = "Third parties",
                        content = "The app contains no third-party analytics, advertising or tracking SDKs, and shares no data with third parties.",
                        isDark = isDark
                    )

                    PolicySectionItem(
                        title = "Children",
                        content = "The app does not collect personal information from anyone, including children.",
                        isDark = isDark
                    )

                    PolicySectionItem(
                        title = "Your rights",
                        content = "Because we hold no personal data about you, there is nothing for us to access, correct or delete. You control all locally stored data directly in the app or by uninstalling it.",
                        isDark = isDark
                    )

                    PolicySectionItem(
                        title = "Changes to this policy",
                        content = "If this policy changes, the updated version will be posted with a new effective date. Previous versions are available on request.",
                        isDark = isDark
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(
                            text = "Contact",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (isDark) FlatDarkText else FlatLightText
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    try {
                                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                                            data = Uri.parse("mailto:mdrabiulalam056@gmail.com")
                                        }
                                        context.startActivity(intent)
                                    } catch (_: Exception) {}
                                }
                                .padding(vertical = 2.dp)
                        ) {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = "Email",
                                tint = FlatBelizeHole,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "mdrabiulalam056@gmail.com",
                                fontSize = 12.sp,
                                color = FlatBelizeHole
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) {
                    Text("Understood", color = FlatBelizeHole, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            shape = RoundedCornerShape(4.dp),
            containerColor = surfaceColor,
            tonalElevation = 0.dp,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Description, contentDescription = null, tint = FlatOrange)
                    Text("Terms & Conditions", fontWeight = FontWeight.Bold, color = if (isDark) FlatDarkText else FlatLightText)
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .heightIn(max = 480.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (isDark) FlatMidnightBlue else FlatClouds,
                        border = BorderStroke(1.dp, borderColor)
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                "Open Calculator — operated by SE7EN (the \"Service Provider\")",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = if (isDark) FlatDarkText else FlatLightText
                            )
                            Text(
                                "Effective date: 2026-10-15",
                                fontSize = 11.sp,
                                color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                "By downloading or using Open Calculator (the \"Application\"), you agree to these Terms. If you do not agree, please do not use the Application.",
                                fontSize = 11.sp,
                                lineHeight = 16.sp,
                                color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted
                            )
                        }
                    }

                    PolicySectionItem(
                        title = "1. License",
                        content = "The Service Provider grants you a limited, non-exclusive, non-transferable, revocable license to install and use the Application on your personal devices. You may not copy, modify, distribute, sell, reverse engineer or create derivative works from the Application except where applicable law expressly allows it.",
                        isDark = isDark
                    )

                    PolicySectionItem(
                        title = "2. Intellectual property",
                        content = "The Application, including its code, design, name and branding, remains the property of the Service Provider. These Terms do not grant you any right to use its trademarks, logos or branding.",
                        isDark = isDark
                    )

                    PolicySectionItem(
                        title = "3. Accuracy of results — please read",
                        content = "The Application is provided for general calculation, conversion and educational use. We work to make results accurate, but we do not guarantee that every result is error-free. In particular:",
                        bullets = listOf(
                            "Currency exchange rates are built into the Application, are approximate, and are not live. Do not rely on them for financial transactions.",
                            "Financial, loan, tax, date and statistical tools give estimates only and are not financial, legal, tax or professional advice.",
                            "Floating-point arithmetic can produce tiny rounding differences. You are responsible for verifying any result used for important decisions, such as medical, engineering, legal or financial ones."
                        ),
                        isDark = isDark
                    )

                    PolicySectionItem(
                        title = "4. Offline operation and your data",
                        content = "The Application works offline and keeps its data on your device, as described in the Privacy Policy. You are responsible for the security of your device.",
                        isDark = isDark
                    )

                    PolicySectionItem(
                        title = "5. Updates and changes",
                        content = "We may update, change, suspend or discontinue the Application, or its features, at any time. We may charge for the Application or certain features in the future; any charges will be clearly communicated before you are asked to pay. We do not guarantee compatibility with every device or Android version.",
                        isDark = isDark
                    )

                    PolicySectionItem(
                        title = "6. Termination",
                        content = "You may stop using the Application at any time by uninstalling it. We may terminate your license if you materially breach these Terms, in which case you must stop using the Application and delete all copies.",
                        isDark = isDark
                    )

                    PolicySectionItem(
                        title = "7. Disclaimer of warranties",
                        content = "To the fullest extent permitted by law, the Application is provided \"as is\" and \"as available\", without warranties of any kind, express or implied, including merchantability, fitness for a particular purpose and non-infringement.",
                        isDark = isDark
                    )

                    PolicySectionItem(
                        title = "8. Limitation of liability",
                        content = "To the fullest extent permitted by law, the Service Provider is not liable for any indirect, incidental, special, consequential or punitive damages, or for loss of profits, data or business, arising from your use of the Application. Where the Application is free, our total liability is limited to the minimum amount permitted by applicable law. Nothing in these Terms limits liability that cannot be limited by law, or any consumer rights you have under mandatory law.",
                        isDark = isDark
                    )

                    PolicySectionItem(
                        title = "9. Governing law",
                        content = "These Terms are governed by the laws of the jurisdiction in which the Service Provider is established, except where mandatory consumer protection laws of your country provide otherwise.",
                        isDark = isDark
                    )

                    PolicySectionItem(
                        title = "10. Severability and entire agreement",
                        content = "If any part of these Terms is found unenforceable, the rest remains in effect. These Terms and the Privacy Policy are the entire agreement between you and the Service Provider about the Application.",
                        isDark = isDark
                    )

                    PolicySectionItem(
                        title = "11. Changes to these Terms",
                        content = "We may update these Terms from time to time. Updated Terms will be posted with a new effective date; continued use means you accept them. Previous versions are available on request.",
                        isDark = isDark
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(
                            text = "12. Contact",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (isDark) FlatDarkText else FlatLightText
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    try {
                                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                                            data = Uri.parse("mailto:mdrabiulalam056@gmail.com")
                                        }
                                        context.startActivity(intent)
                                    } catch (_: Exception) {}
                                }
                                .padding(vertical = 2.dp)
                        ) {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = "Email",
                                tint = FlatBelizeHole,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "mdrabiulalam056@gmail.com",
                                fontSize = 12.sp,
                                color = FlatBelizeHole
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTermsDialog = false }) {
                    Text("Understood", color = FlatBelizeHole, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            shape = RoundedCornerShape(4.dp),
            containerColor = surfaceColor,
            tonalElevation = 0.dp,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Image(
                        painter = painterResource(id = com.example.R.drawable.app_logo),
                        contentDescription = "App Logo",
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Column {
                        Text(
                            "Open Calculator v1.0",
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) FlatDarkText else FlatLightText,
                            fontSize = 17.sp
                        )
                        Text(
                            "Clean & High Precision",
                            fontSize = 12.sp,
                            color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted
                        )
                    }
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    Text(
                        "Open Calculator is a modern, high-precision Android calculator featuring:",
                        color = if (isDark) FlatDarkText else FlatLightText,
                        fontSize = 13.sp
                    )
                    Text("• Classic Flat UI design with zero clutter", fontSize = 12.sp, color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted)
                    Text("• Basic & Scientific calculation with DEG/RAD and history", fontSize = 12.sp, color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted)
                    Text("• 190+ world currencies & 13 unit conversion categories", fontSize = 12.sp, color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted)
                    Text("• 140+ languages and global numeral scripts", fontSize = 12.sp, color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted)
                    Text("• Percentage, Finance, Date & Time, Geometry, and Programmer tools", fontSize = 12.sp, color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted)

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = borderColor,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    // Project Contributor Shoutout Card
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (isDark) FlatMidnightBlue else FlatClouds,
                        border = BorderStroke(1.dp, borderColor)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Project Contributor",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = FlatBelizeHole
                            )
                            Text(
                                text = "Md Rabiul Alam",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = if (isDark) FlatDarkText else FlatLightText
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable {
                                        try {
                                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                                data = Uri.parse("mailto:mdrabiulalam056@gmail.com")
                                            }
                                            context.startActivity(intent)
                                        } catch (_: Exception) {}
                                    }
                                    .padding(vertical = 2.dp)
                            ) {
                                Icon(
                                    Icons.Default.Email,
                                    contentDescription = "Email",
                                    tint = FlatBelizeHole,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "mdrabiulalam056@gmail.com",
                                    fontSize = 12.sp,
                                    color = FlatBelizeHole
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable {
                                        try {
                                            val intent = Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse("https://rabiulalamofficial.blogspot.com/")
                                            )
                                            context.startActivity(intent)
                                        } catch (_: Exception) {}
                                    }
                                    .padding(vertical = 2.dp)
                            ) {
                                Icon(
                                    Icons.Default.Language,
                                    contentDescription = "Website",
                                    tint = FlatBelizeHole,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "https://rabiulalamofficial.blogspot.com/",
                                    fontSize = 12.sp,
                                    color = FlatBelizeHole
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("Close", color = FlatBelizeHole, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    if (showLanguageDialog) {
        val filteredList = remember(langSearchQuery) {
            com.example.domain.languages.LanguageEngine.searchLanguages(langSearchQuery)
        }
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            shape = RoundedCornerShape(4.dp),
            containerColor = surfaceColor,
            tonalElevation = 0.dp,
            title = {
                Column {
                    Text("Select App Language", fontWeight = FontWeight.Bold, color = if (isDark) FlatDarkText else FlatLightText)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = langSearchQuery,
                        onValueChange = { langSearchQuery = it },
                        placeholder = { Text("Search 140+ languages...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (langSearchQuery.isNotEmpty()) {
                                IconButton(onClick = { langSearchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(4.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FlatBelizeHole,
                            unfocusedBorderColor = borderColor
                        )
                    )
                }
            },
            text = {
                Box(modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp)) {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(filteredList, key = { it.code + it.englishName }) { lang ->
                            val isSelected = languageCode == lang.code
                            Surface(
                                shape = RoundedCornerShape(3.dp),
                                color = if (isSelected) (if (isDark) FlatWetAsphalt else FlatClouds) else surfaceColor,
                                border = if (isSelected) BorderStroke(1.dp, FlatBelizeHole) else null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        preferences.setLanguageCode(lang.code)
                                        showLanguageDialog = false
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(lang.englishName, fontWeight = FontWeight.Bold, color = if (isDark) FlatDarkText else FlatLightText)
                                        Text(lang.nativeName, style = MaterialTheme.typography.bodySmall, color = FlatBelizeHole)
                                        Text(lang.countryOrRegion, style = MaterialTheme.typography.labelSmall, color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted)
                                    }
                                    if (isSelected) {
                                        Icon(Icons.Default.Check, contentDescription = "Selected", tint = FlatBelizeHole)
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text("Close", color = if (isDark) FlatDarkText else FlatLightText)
                }
            }
        )
    }

    if (showNumeralDialog) {
        AlertDialog(
            onDismissRequest = { showNumeralDialog = false },
            shape = RoundedCornerShape(4.dp),
            containerColor = surfaceColor,
            tonalElevation = 0.dp,
            title = { Text("Select Numeral Script", fontWeight = FontWeight.Bold, color = if (isDark) FlatDarkText else FlatLightText) },
            text = {
                Box(modifier = Modifier.height(350.dp)) {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(com.example.domain.languages.NumeralScript.values()) { script ->
                            val isSelected = numeralScript == script
                            Surface(
                                shape = RoundedCornerShape(3.dp),
                                color = if (isSelected) (if (isDark) FlatWetAsphalt else FlatClouds) else surfaceColor,
                                border = if (isSelected) BorderStroke(1.dp, FlatBelizeHole) else null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        preferences.setNumeralScript(script)
                                        showNumeralDialog = false
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(script.displayName, fontWeight = FontWeight.Bold, color = if (isDark) FlatDarkText else FlatLightText)
                                        Text(script.zeroToNine, style = MaterialTheme.typography.bodyMedium, color = FlatBelizeHole)
                                    }
                                    if (isSelected) {
                                        Icon(Icons.Default.Check, contentDescription = "Selected", tint = FlatBelizeHole)
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showNumeralDialog = false }) {
                    Text("Close", color = if (isDark) FlatDarkText else FlatLightText)
                }
            }
        )
    }
}

@Composable
private fun PolicySectionItem(
    title: String,
    content: String,
    isDark: Boolean,
    bullets: List<String> = emptyList()
) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = if (isDark) FlatDarkText else FlatLightText
        )
        if (content.isNotBlank()) {
            Text(
                text = content,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted
            )
        }
        bullets.forEach { bullet ->
            Text(
                text = "• $bullet",
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted,
                modifier = Modifier.padding(start = 6.dp)
            )
        }
    }
}
