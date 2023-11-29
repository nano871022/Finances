package co.com.japl.ui.theme

import android.content.Context
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LighColorSchema = lightColorScheme(
    onPrimaryContainer = color_theme_light_on_primary_container,
    primaryContainer = color_theme_light_primary_container,
    surfaceVariant = color_theme_light_surface_variant,
    primary = color_theme_light_primary,
    onPrimary = color_theme_light_on_primary
)

private val DarkColorSchema = darkColorScheme(
    primaryContainer = color_theme_dark_primary_container,
    onPrimaryContainer = color_theme_dark_on_primary_container,
    surfaceVariant = color_theme_dark_surface_variant,
    primary = color_theme_dark_primary,
    onPrimary = color_theme_dark_on_primary
)
@Composable
fun MaterialThemeComposeUI(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
){
    val colorSchema = getColorSchema(darkTheme, dynamicColor, LocalContext.current)

    //settingView(colorSchema, darkTheme)

    applyMaterialTheme(colorScheme = colorSchema, content = content)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun applyMaterialTheme(colorScheme: ColorScheme, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

@Composable
private fun settingView(colorScheme: ColorScheme, darkTheme: Boolean){
    val view = LocalView.current
    if(!view.isInEditMode){
        SideEffect {
            val window = (view.context as ComponentActivity).window
            window.statusBarColor = colorScheme.secondary.toArgb()
            WindowCompat.getInsetsController(window,view).isAppearanceLightStatusBars = darkTheme
        }
    }
}

private fun getColorSchema(darkTheme: Boolean , dynamicColor: Boolean=false ,context: Context):ColorScheme{
    return when{
        dynamicColor  -> {
            if(darkTheme){
                dynamicDarkColorScheme(context)
            }else{
                dynamicLightColorScheme(context)
            }
        }
        darkTheme -> DarkColorSchema
        else -> LighColorSchema
    }
}