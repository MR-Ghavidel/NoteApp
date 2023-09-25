package mohammadreza.ghavidel.noteapp.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import mohammadreza.ghavidel.noteapp.R

// Set of Material typography styles to start with
val Typography = Typography(
    h1 = TextStyle(
        fontFamily = FontFamily(Font(R.font.iran_yekan_mobile_extra_bold)),
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 0.36.sp,
        fontSize = 24.sp
    ),
    h2 = TextStyle(
        fontFamily = FontFamily(Font(R.font.iran_yekan_mobile_bold)),
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.27.sp,
        fontSize = 18.sp
    ),
    h3 = TextStyle(
        fontFamily = FontFamily(Font(R.font.iran_yekan_mobile_medium)),
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.21.sp,
        fontSize = 14.sp
    ),
    body1 = TextStyle(
        fontFamily = FontFamily(Font(R.font.iran_yekan_mobile_regular)),
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp,
        letterSpacing = 0.02.sp,
        fontSize = 13.sp
    ),
    body2 = TextStyle(
        fontFamily = FontFamily(Font(R.font.iran_yekan_mobile_bold)),
        fontWeight = FontWeight.Bold,
        lineHeight = 20.sp,
        letterSpacing = 0.21.sp,
        fontSize = 14.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = FontFamily(Font(R.font.iran_yekan_mobile_regular)),
        fontWeight = FontWeight.Normal,
        lineHeight = 17.sp,
        fontSize = 11.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily(Font(R.font.iran_yekan_mobile_regular)),
        fontWeight = FontWeight.Normal,
        lineHeight = 17.sp,
        fontSize = 12.sp
    ),
    button = TextStyle(
        fontFamily = FontFamily(Font(R.font.iran_yekan_mobile_extra_bold)),
        fontWeight = FontWeight.ExtraBold,
        fontSize = 16.sp
    )
)