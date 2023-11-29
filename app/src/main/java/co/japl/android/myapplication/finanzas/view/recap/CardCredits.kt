package co.japl.android.myapplication.finanzas.view.recap

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.view.components.CardValues
import co.japl.android.myapplication.finanzas.view.components.FieldView
import co.japl.android.myapplication.utils.NumbersUtil

@Composable
fun CardCredits(title:String, totalCC:Double,warningValue:Double){
    CardValues(title = title) {
        FieldView(
            name = stringResource(id = R.string.total_credits),
            value = NumbersUtil.toString(totalCC),
            modifier = Modifier.fillMaxWidth()
        )

        Row{
            FieldView(
                name = stringResource(id = R.string.warning_quote_value),
                value = NumbersUtil.toString(warningValue),
                modifier = Modifier.weight(1f)
            )

            val limite = warningValue - totalCC

            FieldView(name = stringResource(id = R.string.value_spend)
                , value = NumbersUtil.toString(limite)
                , modifier = Modifier.weight(1f)
                , color = if (limite > 0.0) MaterialTheme.colorScheme.onPrimaryContainer else Color.Red)
        }
    }

}