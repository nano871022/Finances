package co.com.japl.module.paid.views.paid.form

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import co.com.japl.module.paid.R
import co.com.japl.module.paid.controllers.paid.form.PaidViewModel
import co.com.japl.ui.components.CheckBoxField
import co.com.japl.ui.components.FieldDatePicker
import co.com.japl.ui.components.FieldSelect
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun Paid(viewModel:PaidViewModel) {
    val progresStatus  = remember{viewModel.progressStatus}
    val loaderState = remember {viewModel.loading}

    CoroutineScope(Dispatchers.IO).launch {
        viewModel.main()
    }

    if(loaderState.value){
        LinearProgressIndicator(progress = progresStatus.value,modifier=Modifier.fillMaxWidth())
    }else{
        Body(viewModel = viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
private fun Body(viewModel:PaidViewModel){

    Scaffold (floatingActionButton = {
        Buttons(add={viewModel.save()}, clear = {viewModel.clean()})
    }){
        Form(viewModel = viewModel, modifier = Modifier.padding(it))
    }

}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable private fun Form(viewModel: PaidViewModel, modifier:Modifier){
    val account = remember {
        viewModel.account
    }
    val accountError = remember {
        viewModel.errorAccount
    }
    val accountList = remember {
        viewModel.accountListPair
    }
    val datePaidState = remember {
        viewModel.date
    }
    val errorDatePaidState = remember {
        viewModel.errorDate
    }
    val nameItemState = remember {
        viewModel.name
    }
    val errorNameItemState = remember {
        viewModel.errorName
    }
    val valueItemState = remember {
        viewModel.value
    }
    val errorValueItemState = remember {
        viewModel.errorValue
    }
    val recurrentState = remember {
        viewModel.recurrent
    }

Column (modifier=Modifier.padding(Dimensions.PADDING_SHORT)) {


    FieldSelect(
        title = stringResource(id = R.string.account),
        value = account.value?.name ?: "",
        isError = accountError,
        list = accountList,
        modifier = Modifier
            .fillMaxWidth().padding(bottom = Dimensions.PADDING_SHORT)
    ) { selected ->
        selected?.let {
            viewModel.accountList?.first { it.id == selected.first }?.let {
                account.value = it
            }
        } ?: account.let { it.value = null }
    }

    FieldDatePicker(title = R.string.date_paid,
        value = datePaidState.value?.let { DateUtils.localDateToStringDB(it) } ?: "",
        validation = { viewModel.validate() },
        isError = errorDatePaidState,
        callable = {
            it.takeIf {
                it.isNotBlank() &&
                        DateUtils.isDateValid(it)
            }?.let { datePaidState.value = DateUtils.toLocalDate(it) }
        },modifier = Modifier
            .fillMaxWidth().padding(bottom = Dimensions.PADDING_SHORT))

    FieldText(
        title = stringResource(id = R.string.name_item),
        value = nameItemState.value ?: "",
        icon= Icons.Rounded.Cancel,
        validation = { viewModel.validate() },
        hasErrorState = errorNameItemState,
        callback = {
            it.takeIf { it.isNotBlank() }?.let{
                nameItemState.value = it
            }?:nameItemState.let{it.value = "" }
        },
        modifier = Modifier
            .fillMaxWidth().padding(bottom = Dimensions.PADDING_SHORT)
    )

    FieldText(
        title = stringResource(id = R.string.value_item),
        value = valueItemState.value,
        validation = { viewModel.validate() },
        icon= Icons.Rounded.Cancel,
        currency = true,
        keyboardType = KeyboardOptions.Companion.Default.copy (keyboardType = KeyboardType.Decimal),
        hasErrorState = errorValueItemState,
        callback = {valueItemState.value = it},
        modifier = Modifier
            .fillMaxWidth().padding(bottom = Dimensions.PADDING_SHORT)
    )

    CheckBoxField(title = stringResource(id = R.string.recurrent), value = recurrentState.value,
        callback = {
            recurrentState.value = it
            viewModel.validate()
        },modifier = Modifier
            .fillMaxWidth())
}

}

@Composable
private fun Buttons(add:()->Unit,clear:()->Unit){
    Column {

        FloatButton(imageVector = Icons.Rounded.CleaningServices, descriptionIcon = co.com.japl.ui.R.string.clear, onClick = clear)

        FloatButton(imageVector = Icons.Rounded.Add, descriptionIcon = R.string.add_paid, onClick = add)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
internal fun PreviewPaidForm(){

    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        Form(viewModel = viewModel,modifier = Modifier.fillMaxWidth())
    }

}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
internal fun PreviewPaidFormDark(){

    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        Form(viewModel = viewModel,modifier = Modifier.fillMaxWidth())
    }

}

@Composable
private fun getViewModel():PaidViewModel{
    val viewModel = PaidViewModel(paidSvc = null, codeAccount = 0 , codePaid =0, accountSvc = null,navController = null)
    viewModel.loading.value = false


    return viewModel
}