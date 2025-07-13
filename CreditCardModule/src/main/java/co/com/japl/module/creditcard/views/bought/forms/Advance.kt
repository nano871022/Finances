package co.com.japl.module.creditcard.views.bought.forms

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.controllers.bought.forms.AdvanceViewModel
import co.com.japl.ui.Prefs
import co.com.japl.ui.components.FieldDatePicker
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.theme.values.ModifiersCustom
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun Advance (viewModel:AdvanceViewModel){
    val isLoadingState = remember {viewModel.loading}
    val loadingState = remember { viewModel.progress }

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
    val creditCardName = viewModel.creditCardName.value.collectAsState()
    val dateBought = viewModel.dateBought.value.collectAsState()
    val nameProduct = viewModel.nameProduct.value.collectAsState()
    val valueProduct = viewModel.valueProduct.value.collectAsState()
    val months = viewModel.monthProduct.value.collectAsState()
    val creditRate = viewModel.creditRate.value.collectAsState()
    val creditKindRate = viewModel.creditRateKind.value.collectAsState()
    val capitalValue = viewModel.capitalValue.value.collectAsState()
    val interestValue = viewModel.interestValue.value.collectAsState()
    val quoteValue = viewModel.quoteValue.value.collectAsState()

    Column(modifier= Modifier
        .padding(Dimensions.PADDING_SHORT)
        .verticalScroll(rememberScrollState())) {
        FieldView(
            name = R.string.credit_card,
            value = creditCardName.value,
            modifier = ModifiersCustom.FieldFillMAxWidhtAndPaddingShort(),
            isMoney = false
        )

        FieldDatePicker(title = androidx.compose.material3.R.string.m3c_date_picker_headline
            ,value = dateBought.value
            , callable = viewModel.dateBought::onValueChange
            , isError = viewModel.dateBought.error
            , validation = {viewModel.validate()}
            , modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp))

        FieldText(title = stringResource(id = R.string.name_product),
            value= nameProduct.value,
            icon= Icons.Rounded.Cancel,
            hasErrorState = viewModel.nameProduct.error,
            validation = {viewModel.validate()},
            callback = viewModel.nameProduct::onValueChange,
            modifier= ModifiersCustom.FieldFillMAxWidhtAndPaddingShort())

        FieldText(title = stringResource(id = R.string.value_product),
            value=valueProduct.value,
            icon= Icons.Rounded.Cancel,
            hasErrorState = viewModel.valueProduct.error.value,
            validation = {viewModel.validate()},
            callback = viewModel.valueProduct::onValueChange,
            keyboardType = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
            currency = true,
            modifier=ModifiersCustom.FieldFillMAxWidhtAndPaddingShort())

        FieldView(name = stringResource(id = R.string.months),
            value=months.value,
            modifier=ModifiersCustom.FieldFillMAxWidhtAndPaddingShort()
            ,isMoney = false)

        Row {
            FieldView(
                name = R.string.credit_rate,
                value = (creditRate.value?.takeIf { it.isNotBlank() }?.let { "$it %" }
                    ?: "").toString(),
                modifier = Modifier.weight(2f),
                isMoney = false
            )

            FieldView(
                name = R.string.credit_rate,
                value = creditKindRate.value,
                modifier = Modifier.weight(1f),
                isMoney = false
            )
        }

        FieldView(
            name = R.string.capital_value,
            value = capitalValue.value,
            modifier = ModifiersCustom.FieldFillMAxWidhtAndPaddingShort(),
            isMoney = false
        )

        FieldView(
            name = R.string.interest_value,
            value = interestValue.value,
            modifier = ModifiersCustom.FieldFillMAxWidhtAndPaddingShort(),
            isMoney = false
        )

        FieldView(
            name = R.string.quote_value,
            value = quoteValue.value,
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
    val viewModel = AdvanceViewModel(null,0,0, LocalDateTime.now(),null,null,null,null,prefs)
    viewModel.loading.value = false
    return viewModel
}