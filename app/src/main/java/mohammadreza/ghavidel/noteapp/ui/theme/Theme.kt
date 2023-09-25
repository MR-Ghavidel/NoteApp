package mohammadreza.ghavidel.noteapp.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val darkColorPalette = darkColors(
    primary = primary,
    background = background,
    onBackground = onBackground,
    surface = surface,
    onSurface = onSurface,
    secondary = secondary,
)

private val lightColorPalette = lightColors(
    primary = Navy,
    onPrimary = White700,
    background = White500,
    onBackground = SemiBlack,
    surface = LightGray,
    onSurface = Navy,
)

@Composable
fun NoteAppTheme(
    content: @Composable () -> Unit
) {
    val colors = lightColorPalette
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}