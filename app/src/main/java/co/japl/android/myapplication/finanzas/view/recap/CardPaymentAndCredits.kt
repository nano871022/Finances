package co.japl.android.myapplication.finanzas.view.recap

import androidx.compose.foundation.layout.Row
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
fun CardPaymentAndCredits(title:String,totalInbound:Double,totalPayed:Double,totalCredits:Double){
    CardValues(title = title) {
        Row{
            FieldView(
                name = stringResource(id = R.string.total_paid)
                , value = NumbersUtil.toString(totalPayed)
                , modifier = Modifier.weight(1f)
            )

            FieldView(name = stringResource(id = R.string.total_credits)
                , value = NumbersUtil.toString(totalCredits)
                , modifier = Modifier.weight(1f))
        }

        Row{
            FieldView(name = stringResource(id = R.string.total_paids)
                , value = NumbersUtil.toString(totalPayed + totalCredits)
                , modifier = Modifier.weight(1f))

            val value = totalInbound - (totalPayed + totalCredits)

            FieldView(name = stringResource(id = R.string.total_input_paids)
                , value = NumbersUtil.toString(value)
                , modifier = Modifier.weight(1f)
                , color =
                if(value > 0.0) MaterialTheme.colorScheme.onPrimaryContainer else Color.Red)
        }

    }
}