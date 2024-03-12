package co.com.japl.ui.theme.values

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

object ModifiersCustom {
    fun RowScope.FillMaxWidthAndWeight1f(): Modifier {
        return Modifier.fillMaxWidth().weight(1f)
    }

    fun RowScope.Weight1f(): Modifier {
        return Modifier.weight(1f)
    }

    fun RowScope.Weight1fAndAlightCenterVertical():Modifier{
        return Modifier.weight(1f).align(alignment = Alignment.CenterVertically)
    }

    fun RowScope.Weight1fAndAlightCenterVerticalAndPaddingRightSpace():Modifier{
        return Modifier.weight(1f).align(alignment = Alignment.CenterVertically).padding(end=Dimensions.PADDING_SPACE_BETWEEN_FIELD_RIGHT)
    }

    fun RowScope.Weight1fAndPaddintRightSpace():Modifier{
        return Modifier.weight(1f).padding(end=Dimensions.PADDING_SPACE_BETWEEN_FIELD_RIGHT)
    }

    fun RowScope.AlignCenterVerticalAndPaddingRightSpace():Modifier{
        return Modifier.align(alignment = Alignment.CenterVertically).padding(end=Dimensions.PADDING_SPACE_BETWEEN_FIELD_RIGHT)
    }

    fun FieldFillMAxWidhtAndPaddingShort():Modifier{
        return Modifier.fillMaxWidth().padding(bottom=Dimensions.PADDING_SHORT)
    }

}