package co.japl.android.myapplication.finanzas.view.recap

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import co.japl.android.myapplication.R
import co.com.japl.ui.components.CardValues
import co.com.japl.ui.components.FieldView
import co.japl.android.myapplication.utils.NumbersUtil

@Composable
fun CardRecap(title:String,totalInbound:Double,totalPayment:Double){
    CardValues(title = title) {

        FieldView(name = stringResource(id = R.string.input_total)
            , value = NumbersUtil.toString(totalInbound)
            , modifier= Modifier.fillMaxWidth())

        Row {
            FieldView(
                name = stringResource(id = R.string.total_paids)
                , value = NumbersUtil.toString( totalPayment)
                , modifier = Modifier.weight(1f)
            )

            val valueRest = totalInbound - totalPayment

            FieldView(
                name = stringResource(id = R.string.total_input_paids)
                , value = NumbersUtil.toString(valueRest)
                , modifier = Modifier.weight(1f)
                , color = if(valueRest > 0.0) MaterialTheme.colorScheme.onPrimaryContainer else Color.Red
            )
        }
    }
}