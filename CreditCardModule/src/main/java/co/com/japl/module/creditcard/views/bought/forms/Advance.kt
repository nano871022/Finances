package co.com.japl.module.creditcard.views.bought.forms

import android.app.Application
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
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.controllers.bought.forms.AdvanceViewModel
import co.com.japl.module.creditcard.controllers.bought.forms.WalletViewModel
import co.com.japl.ui.Prefs
import co.com.japl.ui.components.FieldDatePicker
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.theme.values.ModifiersCustom
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun Advance (viewModel:AdvanceViewModel){
    val isLoadingState = remember {viewModel.loading}
    val loadingState = remember { viewModel.progress }

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.main()
        }


    if(isLoadingState.value){
        LinearProgressIndicator(progress = loadingState.floatValue,modifier=Modifier.fillMaxWidth())
    }else {
        viewModel.creditRateEmpty()
        Scaffold(
            floatingActionButton = {

                FloatingButtons(viewModel)

            }
        ) {
            Body(viewModel,modifier = Modifier.padding(it))
        }
    }
}

@Composable
private fun FloatingButtons(viewModel: AdvanceViewModel) {
    val isNewState = remember { viewModel.isNew }
    Column {
        FloatButton(
            imageVector = Icons.Rounded.CleaningServices,
            descriptionIcon = co.com.japl.ui.R.string.clear,
            onClick = {viewModel.clear()}
        )

        if(isNewState.value) {
            FloatButton(
                imageVector = Icons.Rounded.Save,
                descriptionIcon = R.string.save,
                onClick = {viewModel.create()}
            )
        }else {

            FloatButton(
                imageVector = Icons.Rounded.Edit,
                descriptionIcon = R.string.edit,
                onClick = { viewModel.update() }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
private fun Body(viewModel: AdvanceViewModel,modifier:Modifier){
    val creditCardState = remember { viewModel.creditCardName }
    val nameState = remember { viewModel.nameProduct }
    val errorNameState = remember { viewModel.errorNameProduct }
    val valueState = remember { viewModel.valueProduct }
    val errorValueState = remember { viewModel.errorValueProduct }
    val creditRateState = remember { viewModel.creditRate }
    val monthsState = remember { viewModel.monthProduct }
    val valueCapitalState = remember { viewModel.capitalValue }
    val dateBoughtState = remember { viewModel.dateBought }
    val errorDateBoughtState = remember { viewModel.errorDateBought}
    val quoteValueState = remember { viewModel.quoteValue }
    val interestValueState = remember { viewModel.interestValue }
    val creditRateKindState = remember { viewModel.creditRateKind }

    Column(modifier= Modifier
        .padding(Dimensions.PADDING_SHORT)
        .verticalScroll(rememberScrollState())) {
        FieldView(
            name = R.string.credit_card,
            value = creditCardState.value,
            modifier = ModifiersCustom.FieldFillMAxWidhtAndPaddingShort(),
            isMoney = false
        )

        FieldDatePicker(title = androidx.compose.material3.R.string.m3c_date_picker_headline
            ,value = dateBoughtState.value
            , callable = {dateBoughtState.value = it}
            , isError = errorDateBoughtState
            , validation = {viewModel.validate()}
            , modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp))

        FieldText(title = stringResource(id = R.string.name_product),
            value=nameState.value,
            icon= Icons.Rounded.Cancel,
            hasErrorState = errorNameState,
            validation = {viewModel.validate()},
            callback = {nameState.value = it},
            modifier= ModifiersCustom.FieldFillMAxWidhtAndPaddingShort())

        FieldText(title = stringResource(id = R.string.value_product),
            value=valueState.value,
            icon= Icons.Rounded.Cancel,
            hasErrorState = errorValueState,
            validation = {viewModel.validate()},
            callback = {valueState.value = it},
            currency = true,
            modifier=ModifiersCustom.FieldFillMAxWidhtAndPaddingShort())

        FieldView(name = stringResource(id = R.string.months),
            value=monthsState.value,
            modifier=ModifiersCustom.FieldFillMAxWidhtAndPaddingShort()
            ,isMoney = false)

        Row {
            FieldView(
                name = R.string.credit_rate,
                value = (creditRateState.value?.takeIf { it.isNotBlank() }?.let { "$it %" }
                    ?: "").toString(),
                modifier = Modifier.weight(2f),
                isMoney = false
            )

            FieldView(
                name = R.string.credit_rate,
                value = creditRateKindState.value,
                modifier = Modifier.weight(1f),
                isMoney = false
            )
        }

        FieldView(
            name = R.string.capital_value,
            value = valueCapitalState.value,
            modifier = ModifiersCustom.FieldFillMAxWidhtAndPaddingShort(),
            isMoney = false
        )

        FieldView(
            name = R.string.interest_value,
            value = interestValueState.value,
            modifier = ModifiersCustom.FieldFillMAxWidhtAndPaddingShort(),
            isMoney = false
        )

        FieldView(
            name = R.string.quote_value,
            value = quoteValueState.value,
            modifier = ModifiersCustom.FieldFillMAxWidhtAndPaddingShort(),
            isMoney = false
        )

    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
internal fun AdvancePreviewDark(){
    val viewModel = viweModel()
    MaterialThemeComposeUI {
        Advance(viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
internal fun AdvancePreview(){
    val viewModel = viweModel()
    MaterialThemeComposeUI {
        Advance(viewModel)
    }
}

@Composable
private fun viweModel():AdvanceViewModel{
    val prefs = Prefs(LocalContext.current)
    val viewModel = AdvanceViewModel(0,0, LocalDateTime.now(),null,null,null,null,prefs)
    viewModel.loading.value = false
    return viewModel
}