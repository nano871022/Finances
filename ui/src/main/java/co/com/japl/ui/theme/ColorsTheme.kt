package co.com.japl.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

val LighColorSchema = lightColorScheme(
    primaryContainer = color_theme_light_primary_container,
    onPrimaryContainer = color_theme_light_on_primary_container,
    primary = color_theme_light_primary,
    onPrimary = color_theme_light_on_primary,
    background = color_theme_light_background,
    onBackground = color_theme_light_on_background ,
    surface = color_theme_light_surface,
    onSurface = color_theme_light_on_surface
    , surfaceVariant = color_theme_light_surface_variant
    , onSurfaceVariant = color_theme_light_on_surface_variant
    , error = color_theme_light_error
    , outlineVariant = color_theme_light_outline_variant
    , outline = color_theme_light_outline
)

val DarkColorSchema = darkColorScheme(
    primaryContainer = color_theme_dark_primary_container,
    onPrimaryContainer = color_theme_dark_on_primary_container,

    primary = color_theme_dark_primary,
    onPrimary = color_theme_dark_on_primary,

    background = color_theme_dark_background
    , onBackground = color_theme_dark_on_background
    , surface = color_theme_dark_surface
    , onSurface = color_theme_dark_on_surface,
    surfaceVariant = color_theme_dark_surface_variant
    , onSurfaceVariant = color_theme_dark_on_surface_variant

    , error = color_theme_dark_error
    , outlineVariant = color_theme_dark_outline_variant
    , outline = color_theme_dark_outline
)