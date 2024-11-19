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
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
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
import co.com.japl.ui.theme.values.ModifiersCustom
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun Quote (viewModel:QuoteViewModel){
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
private fun FloatingButtons(viewModel: QuoteViewModel) {
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

            FloatButton(
                imageVector = Icons.Rounded.SaveAs,
                descriptionIcon = R.string.save,
                onClick = {viewModel.createAndBack()}
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

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
private fun Body(viewModel: QuoteViewModel,modifier:Modifier){
    val creditCardState = remember { viewModel.creditCardName }
    val nameState = remember { viewModel.nameProduct }
    val errorNameState = remember { viewModel.errorNameProduct }
    val valueState = remember { viewModel.valueProduct }
    val errorValueState = remember { viewModel.errorValueProduct }
    val creditRateState = remember { viewModel.creditRate }
    val monthsState = remember { viewModel.monthProduct }
    val errorMonthsState = remember { viewModel.errorMonthProduct }
    val valueCapitalState = remember { viewModel.capitalValue }
    val dateBoughtState = remember { viewModel.dateBought }
    val errorDateBoughtState = remember { viewModel.errorDateBought}
    val quoteValueState = remember { viewModel.quoteValue }
    val interestValueState = remember { viewModel.interestValue }
    val creditRateKindState = remember { viewModel.creditRateKind }
    val tagState = remember { viewModel.tagSelected }
    val tagPopupState = remember { mutableStateOf(false) }
    val settingkindState = remember { viewModel.settingKind }
    val settingKinListSate = remember { viewModel.settingKindListState }
    val settingNameState = remember { viewModel.settingName }
    val settingNameListSate = remember { viewModel.settingNameListState}
    val settingNameShowState = remember { mutableStateOf(viewModel.settingName.value?.let{true}?:false) }
    val recurrentState = remember { viewModel.recurrent }


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

        FieldText(title = stringResource(id = R.string.months),
            value=monthsState.value,
            icon= Icons.Rounded.Cancel,
            hasErrorState = errorMonthsState,
            validation = {viewModel.validate()},
            callback = {monthsState.value = it},
            modifier=ModifiersCustom.FieldFillMAxWidhtAndPaddingShort())

        FieldView(name = R.string.tag,
            value = tagState.value?.name?:"",
            onClick = {
                tagPopupState.value = true
            },
            isMoney=false,
            modifier = ModifiersCustom.FieldFillMAxWidhtAndPaddingShort())

        FieldSelect(title = stringResource(id = R.string.setting_kind),
                value = settingkindState.value?.second?:"",
                list = settingKinListSate,
                modifier = ModifiersCustom.FieldFillMAxWidhtAndPaddingShort(),
                callAble={
                    it?.let {
                        settingkindState.value = it
                        viewModel.settingName(it.first)
                        settingNameShowState.value = true
                    }?:settingNameShowState.let {
                        settingkindState.value = null
                        settingNameState.value = null
                        it.value = false
                    }
                })

        if(settingNameShowState.value) {
            FieldSelect(title = stringResource(id = R.string.setting_name),
                value = settingNameState.value?.second ?: "",
                list = settingNameListSate,
                modifier = ModifiersCustom.FieldFillMAxWidhtAndPaddingShort(),
                callAble = {
                    settingNameState.value = it
                })
        }

        CheckBoxField(title = stringResource(id = R.string.recurrent),
            value = recurrentState.value,
            callback = {
                        recurrentState.value = it
                        viewModel.validate()
                       },
            modifier = ModifiersCustom.FieldFillMAxWidhtAndPaddingShort())

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

        PopupTags(tagPopupState,tagState,viewModel)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PopupTags(tagPopupState:MutableState<Boolean>,tagState:MutableState<TagDTO?>,viewModel: QuoteViewModel) {
    val tagListState = remember { viewModel.tagList }
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
                                        tagListState.add(it)
                                        tagState.value = it
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
                        for (it in tagListState) {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    tagState.value = it
                                    tagPopupState.value = false
                                }) {
                                Text(text = it.name, modifier = Modifier.weight(2f))
                                IconButton(
                                    imageVector = Icons.Rounded.Delete,
                                    descriptionContent = R.string.delete,
                                    onClick = {
                                        viewModel.deleteTag(it.id)
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Divider()
                        }
                    }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
internal fun QuotePreviewDark(){
    val viewModel = viweModel()
    MaterialThemeComposeUI {
        Quote(viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
internal fun QuotePreview(){
    val viewModel = viweModel()
    MaterialThemeComposeUI {
        Quote(viewModel)
    }
}

@Composable
private fun viweModel():QuoteViewModel{
    val prefs = Prefs(LocalContext.current)
    val viewModel = QuoteViewModel(0,0, LocalDateTime.now(),null,null,null,null,null,null,null,prefs)
    viewModel.loading.value = false
    return viewModel
}