package co.com.japl.module.declaracion_renta_dian.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.japl.android.myapplication.utils.NumbersUtil
import co.com.japl.module.declaracion_renta_dian.R
import co.com.japl.module.declaracion_renta_dian.viewmodel.TaxDeclarationViewModel
import co.com.japl.finances.iports.dtos.LimitStatus
import java.math.RoundingMode

@Composable
fun ProjectionTab(viewModel: TaxDeclarationViewModel) {
    val projections by viewModel.projectionState.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(projections) { proj ->
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text= stringResource(R.string.projection,proj.year),
                        color= MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleLarge)
                    Text(text=stringResource(R.string.ytd, NumbersUtil.COPtoString(proj.currentYTD)),
                        color= MaterialTheme.colorScheme.onBackground)
                    Text(text=stringResource(R.string.projected,NumbersUtil.COPtoString(proj.projectedEndOfYear)),
                        color= MaterialTheme.colorScheme.onBackground)
                    Text(text=stringResource(R.string.threshold,NumbersUtil.COPtoString(proj.threshold)),
                        color= MaterialTheme.colorScheme.onBackground)
                    Text(text=stringResource(R.string.credit_debt,NumbersUtil.COPtoString(proj.creditDebt)),
                        color= MaterialTheme.colorScheme.onBackground)
                    Text(text=stringResource(R.string.credit_card_debt,NumbersUtil.COPtoString(proj.creditCardDebt)),
                        color= MaterialTheme.colorScheme.onBackground)

                    val color = when (proj.limitStatus) {
                        LimitStatus.SAFE -> Color.Green
                        LimitStatus.WARNING -> Color.Yellow
                        LimitStatus.EXCEEDED -> Color.Red
                    }
                    LinearProgressIndicator(
                        progress = (proj.projectedEndOfYear.divide(proj.threshold, 2, RoundingMode.HALF_UP).toFloat()).coerceAtMost(1f),
                        color = color,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                }
            }
        }
    }
}