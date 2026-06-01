package co.com.japl.module.credit.views.forms

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import co.com.japl.module.credit.R
import co.com.japl.module.credit.controllers.forms.CreditFormViewModel
import co.com.japl.ui.components.FieldDatePicker
import co.com.japl.ui.components.FieldSelect
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1f
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1fAndPaddintRightSpace
import co.com.japl.ui.utils.DateUtils
import androidx.lifecycle.SavedStateHandle
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.KindPaymentsEnums
import java.math.BigDecimal

@Composable
fun CreditForm(viewModel: CreditFormViewModel){
    Scaffold (
        floatingActionButton = {
            Buttons(viewModel)
        }
    ){
        Column(modifier = Modifier.padding(it).verticalScroll(rememberScrollState())) {
            Form(viewModel)
        }
    }
}

@Composable
private fun Buttons(viewModel: CreditFormViewModel){
    Column {
        FloatButton(imageVector = Icons.Rounded.CleaningServices, descriptionIcon = R.string.clean_credit) {
            viewModel.clean()
        }
        FloatButton(imageVector = Icons.Rounded.Save, descriptionIcon = R.string.save) {
            viewModel.onSubmitFormClicked()
        }
    }
}

@Composable
private fun Form(viewModel: CreditFormViewModel){
    val name by viewModel.name.value.collectAsState()
    val value by viewModel.value.value.collectAsState()
    val rate by viewModel.rate.value.collectAsState()
    val month by viewModel.month.value.collectAsState()
    val quoteCredit by viewModel.quoteCredit.value.collectAsState()
    val creditDate by viewModel.creditDate.value.collectAsState()
    val kindPayment by viewModel.kindPayment.value.collectAsState()
    val kindRate by viewModel.kindRate.value.collectAsState()

    Column(modifier = Modifier.padding(Dimensions.PADDING_SHORT)) {
        FieldText(
            title = stringResource(id = R.string.name_credit),
            value = name,
            callback = { viewModel.name.onValueChange(it) },
            modifier = Modifier.fillMaxWidth(),
            validation = { viewModel.name.validate() }
        )

        Row(modifier = Modifier.fillMaxWidth().padding(top = Dimensions.PADDING_SHORT)) {
            FieldText(
                title = stringResource(id = R.string.value_credit),
                value = value,
                callback = { viewModel.value.onValueChangeStr(it) },
                modifier = Weight1fAndPaddintRightSpace(),
                validation = { viewModel.value.validate() },
                currency = true
            )

            FieldText(
                title = stringResource(id = R.string.rate_credit),
                value = rate,
                callback = { viewModel.rate.onValueChangeStr(it) },
                modifier = Weight1f(),
                validation = { viewModel.rate.validate() },
                decimal = true
            )
        }

        Row(modifier = Modifier.fillMaxWidth().padding(top = Dimensions.PADDING_SHORT)) {
            FieldText(
                title = stringResource(id = R.string.periods_credit),
                value = month,
                callback = { viewModel.month.onValueChangeStr(it) },
                modifier = Weight1fAndPaddintRightSpace(),
                validation = { viewModel.month.validate() },
                decimal = true
            )

            FieldText(
                title = stringResource(id = R.string.quote_credit),
                value = quoteCredit,
                callback = { viewModel.quoteCredit.onValueChangeStr(it) },
                modifier = Weight1f(),
                validation = { viewModel.quoteCredit.validate() },
                currency = true
            )
        }

        Row(modifier = Modifier.fillMaxWidth().padding(top = Dimensions.PADDING_SHORT)) {
            FieldDatePicker(
                title = R.string.date_credit,
                value = DateUtils.localDateToString(creditDate),
                callable = { date -> viewModel.creditDate.onValueChange(DateUtils.toLocalDate(date)) },
                modifier = Weight1fAndPaddintRightSpace(),
                validation = { viewModel.creditDate.validate() }
            )

            FieldSelect(
                title = stringResource(id = R.string.type_payment_credit),
                value = kindPayment?.second ?: "",
                list = KindPaymentsEnums.entries.map { Pair(it.ordinal, it.name) }.toMutableStateList(),
                modifier = Weight1f(),
                callAble = { pair -> pair?.let { viewModel.kindPayment.onValueChange(Triple(it.first, it.second, KindPaymentsEnums.entries[it.first])) } }
            )
        }

        FieldSelect(
            title = stringResource(id = R.string.kind_rate),
            value = kindRate?.second ?: "",
            list = KindOfTaxEnum.entries.map { Pair(it.ordinal, it.name) }.toMutableStateList(),
            modifier = Modifier.fillMaxWidth().padding(top = Dimensions.PADDING_SHORT),
            callAble = { pair -> pair?.let { viewModel.kindRate.onValueChange(Triple(it.first, it.second, KindOfTaxEnum.entries[it.first])) } }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
fun CreditFormPreview(){
    MaterialThemeComposeUI {
        CreditForm(creditViewModel())
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun CreditFormPreviewDark(){
    MaterialThemeComposeUI {
        CreditForm(creditViewModel())
    }
}

fun creditViewModel(): CreditFormViewModel {
    return CreditFormViewModel(
        SavedStateHandle(),
        creditSvc = TODO()
    )
}
