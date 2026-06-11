package co.com.japl.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MaterialThemeComposeUI(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
){
    val colorSchema = getColorSchema(darkTheme, dynamicColor, LocalContext.current)

    settingView(colorSchema, darkTheme)

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
            val window = (view.context.findActivity())?.window
            if(window != null) {
                window.statusBarColor = Color.Transparent.toArgb()
                window.navigationBarColor = Color.Transparent.toArgb()
                WindowCompat.setDecorFitsSystemWindows(window, false)
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }
}

private fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

@RequiresApi(Build.VERSION_CODES.S)
private fun getColorSchema(darkTheme: Boolean, dynamicColor: Boolean=false, context: Context):ColorScheme{
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
