package co.japl.android.myapplication.finanzas.view.recap

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import co.japl.android.myapplication.R
import co.com.japl.ui.components.CardValues
import co.com.japl.ui.components.FieldViewCards
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.utils.NumbersUtil

@Composable
fun CardMonthlyToPay(totalPayed: Double, totalCredits: Double, totalCreditCard: Double) {
    CardValues(title = stringResource(id = R.string.to_pay)) {
        Column(modifier = Modifier.padding(Dimensions.PADDING_SHORT)) {
            FieldViewCards(
                name = R.string.total_paid,
                value = NumbersUtil.COPtoString(totalPayed),
                modifier = Modifier
            )
            FieldViewCards(
                name = R.string.total_credits,
                value = NumbersUtil.COPtoString(totalCredits),
                modifier = Modifier
            )
            FieldViewCards(
                name = R.string.total_quote_credit_card,
                value = NumbersUtil.COPtoString(totalCreditCard),
                modifier = Modifier
            )
            FieldViewCards(
                name = R.string.total,
                value = NumbersUtil.COPtoString(totalPayed + totalCredits + totalCreditCard),
                modifier = Modifier
            )
        }
    }
}
