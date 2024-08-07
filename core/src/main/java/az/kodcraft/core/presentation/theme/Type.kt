package az.kodcraft.core.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import az.kodcraft.core.R

val MeshqFont = FontFamily(
    Font(R.font.dosis_bold, FontWeight.Bold),
    Font(R.font.dosis_regular, FontWeight.Normal),
    Font(R.font.dosis_light, FontWeight.Light),
    Font(R.font.dosis_extra_light, FontWeight.ExtraLight),
    Font(R.font.dosis_medium, FontWeight.Medium),
)


val Typography.largeTitle: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 42.sp,
        fontFamily = MeshqFont,
        lineHeight = 50.sp,
        letterSpacing = 0.sp
    )

val Typography.mediumTitle: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.Bold,
        fontFamily = MeshqFont,
        fontSize = 38.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    )
val Typography.smallTitle: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.Bold,
        fontFamily = MeshqFont,
        fontSize = 24.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    )

val Typography.largeHeadLine: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.Normal,
        fontFamily = MeshqFont,
        fontSize = 32.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    )

val Typography.headLine: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.Normal,
        fontFamily = MeshqFont,
        fontSize = 17.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    )

val Typography.body: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.Normal,
        fontFamily = MeshqFont,
        fontSize = 16.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp
    )

val Typography.header: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.Bold,
        fontFamily = MeshqFont,
        fontSize = 16.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp
    )

val bodyLarge: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.Normal,
        fontFamily = MeshqFont,
        fontSize = 18.sp
    )
val bodyLargeLight: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.Light,
        fontFamily = MeshqFont,
        fontSize = 22.sp
    )
val Typography.bodyLight: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.ExtraLight,
        fontFamily = MeshqFont,
        fontSize = 16.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp
    )
val bodySmall: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.Normal,
        fontFamily = MeshqFont,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp
    )
val  Typography.bodySmallLight: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.ExtraLight,
        fontFamily = MeshqFont,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp
    )



val Typography.footNote: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.Normal,
        fontFamily = MeshqFont,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    )

val Typography.footNoteLight: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.ExtraLight,
        fontFamily = MeshqFont,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    )

val Typography.buttonTypo: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.Bold,
        fontFamily = MeshqFont,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp
    )

val Typography.buttonTypoLight: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.Light,
        fontFamily = MeshqFont,
        fontSize = 16.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp
    )
val Typography.buttonTypoLightSmall: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.Light,
        fontFamily = MeshqFont,
        fontSize = 12.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.sp
    )

internal val MeshqTypography = Typography(
    titleLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        fontFamily = MeshqFont,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    )
)