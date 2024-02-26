package co.japl.android.myapplication.finanzas.view.recap

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import co.japl.android.myapplication.R
import co.com.japl.ui.components.CardValues
import co.com.japl.ui.components.FieldView
import co.japl.android.myapplication.utils.NumbersUtil

@Composable
fun CardProjections(title:String,totalSaved:Double,projectionValue:Double){
    CardValues(title = title) {

        Row {
            FieldView(
                name = stringResource(id = R.string.num_tot_save),
                value = NumbersUtil.toString(totalSaved),
                modifier = Modifier.weight(1f)
            )

            FieldView(
                name = stringResource(id = R.string.num_save_close),
                value = NumbersUtil.toString(projectionValue),
                modifier = Modifier.weight(1f)
            )
        }
    }
}