package co.com.japl.module.creditcard.views.bought.forms

import android.app.Application
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.SaveAs
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.TagDTO
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.controllers.bought.forms.QuoteViewModel
import co.com.japl.module.creditcard.controllers.bought.forms.WalletViewModel
import co.com.japl.ui.Prefs
import co.com.japl.ui.components.CheckBoxField
import co.com.japl.ui.components.FieldDatePicker
import co.com.japl.ui.components.FieldSelect
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.components.IconButton
import co.com.japl.ui.components.Popup
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.TaxDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.inbounds.creditcard.IBuyCreditCardSettingPort
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardSettingPort
import co.com.japl.finances.iports.inbounds.creditcard.ITagPort
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtPort
import co.com.japl.ui.theme.values.ModifiersCustom
import co.com.japl.ui.utils.NumbersUtil
import co.com.japl.utils.NumbersUtil
import kotlinx.coroutines.CoroutineDispatcher
import java.math.BigDecimal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun Quote (viewModel:QuoteViewModel, navController: NavController){

    val isLoadingState = remember {viewModel.loading}
    val loadingState = remember { viewModel.progress }

    if(isLoadingState.value){
        LinearProgressIndicator(progress = loadingState.floatValue,modifier=Modifier.fillMaxWidth())
    }else {
        viewModel.creditRateEmpty(navController)
        Scaffold(
            floatingActionButton = {

                FloatingButtons(viewModel, navController)

            }
        ) {
            Body(viewModel,modifier = Modifier.padding(it))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
internal fun QuotePreviewDark(){
    val viewModel = viweModel()
    MaterialThemeComposeUI {
        Quote(viewModel, NavController(LocalContext.current))
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
internal fun QuotePreview(){
    val viewModel = viweModel()
    MaterialThemeComposeUI {
        Quote(viewModel, NavController(LocalContext.current))
    }
}

@Composable
private fun viweModel():QuoteViewModel{
    val context = LocalContext.current
    val prefs = Prefs(context)
    val savedStateHandle = SavedStateHandle()
    val boughtSvc = FakeBoughtPort()
    val creditRateSvc = FakeTaxPort()
    val creditCardSvc = FakeCreditCardPort()
    val tagSvc = FakeTagPort()
    val creditCardSettingSvc = FakeCreditCardSettingPort()
    val buyCreditCardSettingSvc = FakeBuyCreditCardSettingPort()
    val viewModel = QuoteViewModel(savedStateHandle, boughtSvc, creditRateSvc, creditCardSvc, tagSvc, creditCardSettingSvc, buyCreditCardSettingSvc, prefs, context)
    viewModel.loading.value = false
    return viewModel
}

@Composable
private fun FloatingButtons(viewModel: QuoteViewModel, navController: NavController) {
    val uiState = viewModel.uiState.collectAsState()
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
                onClick = {viewModel.create(navController)}
            )

            FloatButton(
                imageVector = Icons.Rounded.SaveAs,
                descriptionIcon = R.string.save,
                onClick = {viewModel.createAndBack()}
            )
        }else {

            FloatButton(
                imageVector = Icons.Rounded.Edit,
                descriptionIcon = R.string.edit,
                onClick = { viewModel.update(navController) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
private fun Body(viewModel: QuoteViewModel,modifier:Modifier){
     val tagPopupState = remember { mutableStateOf(false) }
    val settingNameShowState = remember { mutableStateOf(viewModel.settingName.value?.let{true}?:false) }
    val creditCardName = viewModel.creditCardName.value.collectAsState()
    val dateBought = viewModel.dateBought.value.collectAsState()
    val nameProduct = viewModel.nameProduct.value.collectAsState()
    val valueProduct = viewModel.valueProduct.value.collectAsState()
    val interestValue = viewModel.interestValue.value.collectAsState()
    val quoteValue = viewModel.quoteValue.value.collectAsState()
    val capitalValue = viewModel.capitalValue.value.collectAsState()
    val monthProduct = viewModel.monthProduct.value.collectAsState()
    val creditRate = viewModel.creditRate.value.collectAsState()
    val creditRateKind = viewModel.creditRateKind.value.collectAsState()
    val recurrent = viewModel.recurrent.value.collectAsState()
    val tagSelected = viewModel.tagSelected.value.collectAsState()
    val settingKind = viewModel.settingKind.value.collectAsState()
    val settingName = viewModel.settingName.value.collectAsState()
    val settingKindList = remember {viewModel.settingKind.list}
    val settingNameList = remember {viewModel.settingName.list}

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
            , callable = {viewModel.dateBought.onValueChange(it)}
            , isError = viewModel.dateBought.error
            , validation = {viewModel.validate()}
            , modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp))

        FieldText(title = stringResource(id = R.string.name_product),
            value=nameProduct.value,
            icon= Icons.Rounded.Cancel,
            hasErrorState = viewModel.nameProduct.error,
            validation = {viewModel.validate()},
            callback = {viewModel.nameProduct.onValueChange(it)},
            modifier= ModifiersCustom.FieldFillMAxWidhtAndPaddingShort())

        FieldText(title = stringResource(id = R.string.value_product),
            value=valueProduct.value,
            icon= Icons.Rounded.Cancel,
            hasErrorState = viewModel.valueProduct.error.value,
            validation = {viewModel.validate()},
            callback = {viewModel.valueProduct.onValueChange(it)},
            keyboardType = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
            currency = true,
            modifier=ModifiersCustom.FieldFillMAxWidhtAndPaddingShort())

        FieldText(title = stringResource(id = R.string.months),
            value=monthProduct.value,
            icon= Icons.Rounded.Cancel,
            hasErrorState = viewModel.monthProduct.error,
            validation = {viewModel.validate()},
            keyboardType = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            callback = { viewModel.monthProduct.onValueChange(it)},
            modifier=ModifiersCustom.FieldFillMAxWidhtAndPaddingShort())

        FieldView(name = R.string.tag,
            value = tagSelected.value?.name?:"",
            onClick = {tagPopupState.value = true},
            isMoney=false,
            modifier = ModifiersCustom.FieldFillMAxWidhtAndPaddingShort())

        if(viewModel.settingKind.list.isEmpty().not()) {
            FieldSelect(
                title = stringResource(id = R.string.setting_kind),
                value = settingKind.value?.second ?: "",
                list = settingKindList.toMutableStateList() as SnapshotStateList<Pair<Int, String>> ,
                modifier = ModifiersCustom.FieldFillMAxWidhtAndPaddingShort(),
                callAble = {
                    it?.let {
                        viewModel.settingKind.onValueChange(it)
                        viewModel.settingName(it.first)
                        settingNameShowState.value = true
                    } ?: settingNameShowState.let {
                        viewModel.settingKind.reset(null)
                        viewModel.settingName.reset(null)
                        it.value = false
                    }
                })
        }
        if(settingNameShowState.value) {
            FieldSelect(title = stringResource(id = R.string.setting_name),
                value = settingName.value?.second ?: "",
                list = settingNameList.toMutableStateList() as SnapshotStateList<Pair<Int, String>>,
                modifier = ModifiersCustom.FieldFillMAxWidhtAndPaddingShort(),
                callAble = viewModel.settingName::onValueChange
                )
        }

        CheckBoxField(title = stringResource(id = R.string.recurrent),
            value = recurrent.value,
            callback = viewModel.recurrent::onValueChange,
            modifier = ModifiersCustom.FieldFillMAxWidhtAndPaddingShort())

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
                value = creditRateKind.value,
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

        PopupTags(tagPopupState,viewModel)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PopupTags(tagPopupState:MutableState<Boolean>,viewModel: QuoteViewModel) {
    val tagName = remember {mutableStateOf("")}
    if(tagPopupState.value){
        Popup(title = R.string.tag,
            state = tagPopupState) {
            Scaffold (modifier= Modifier
                .fillMaxWidth()
            , bottomBar = {
                    Row {
                        FieldText(
                            title = stringResource(id = R.string.tag),
                            value = tagName.value,
                            icon= Icons.Rounded.Cancel,
                            callback={
                                tagName.value = it
                            },
                            modifier = Modifier.weight(2f)
                        )

                        IconButton(
                            imageVector = Icons.Rounded.AddCircleOutline,
                            descriptionContent = R.string.add_tag,
                            onClick = {
                                tagName.value.takeIf { it.isNotBlank() }?.let {
                                    viewModel.createTag(tagName.value)?.let {
                                        viewModel.tagSelected.list.add(it)
                                        viewModel.tagSelected.onValueChange(it)
                                        tagPopupState.value = false
                                    }
                                }
                            }, modifier = Modifier
                                .weight(1f)
                                .align(alignment = Alignment.CenterVertically))

                    }
                }){
                    Column (modifier= Modifier
                        .padding(bottom = 60.dp, start = Dimensions.PADDING_SHORT)
                        .verticalScroll(rememberScrollState())) {
                        for (it in viewModel.tagSelected.list) {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.tagSelected.onValueChange(it)
                                    tagPopupState.value = false
                                }) {
                                Text(text = it?.name?:"", modifier = Modifier.weight(2f))
                                IconButton(
                                    imageVector = Icons.Rounded.Delete,
                                    descriptionContent = R.string.delete,
                                    onClick = {
                                        viewModel.deleteTag(it?.id?:0)
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            HorizontalDivider()
                        }
                    }
            }
        }
    }
}
