package co.com.japl.module.creditcard.views.setting.forms

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.controllers.setting.CreditCardSettingViewModel
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.module.creditcard.enums.MoreOptionsItemsTypeSettings
import co.com.japl.ui.components.CheckBoxField
import co.com.japl.ui.components.FieldSelect
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.components.MoreOptionsDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CreditCardSetting(viewModel: CreditCardSettingViewModel){
    val stateProgres = remember {viewModel.showProgress}
    val progress = remember {viewModel.progress}
    
    CoroutineScope(Dispatchers.IO).launch { 
        viewModel.main()       
    }
    
    if(stateProgres.value){
        LinearProgressIndicator(
            progress = { progress.value },
        )
    }else{
        Scaffold (
            floatingActionButton = {
                Buttons(viewModel = viewModel)
            }   
        ){
            Body(viewModel = viewModel,modifier = Modifier.padding(it))
        }
    }
        
}

@Composable
private fun Body(viewModel: CreditCardSettingViewModel, modifier:Modifier) {
    val stateType = remember { mutableStateOf(false) }
    val nameState = remember { viewModel.name }
    val valueState = remember { viewModel.value }
    val typeState = remember {viewModel.type}
    val activeState = remember{viewModel.active}

    val nameIsErrorState = remember { viewModel.nameIsError }
    val valueIsErrorState = remember { viewModel.valueIsError }
    val typeIsErrorState = remember { viewModel.typeIsError }
    val context = LocalContext.current

    Column {

        FieldView(title = stringResource(id = R.string.credit_card)
            , value = viewModel.creditCard?.name?:""
            , modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp))

        FieldSelect(title = stringResource(id = R.string.credit_card_setting_type),
            value = typeState.value,
            list = MoreOptionsItemsTypeSettings.values().toList(),
            isError = typeIsErrorState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            callable = {
                typeState.value =  it?.let { context.getString(it.getName()) }?:""

                viewModel.validate()
            })

        FieldText(title = stringResource(id = R.string.name)
            , value=nameState.value
            , hasErrorState =  nameIsErrorState
            , validation = {viewModel.validate()}
            , icon=Icons.Rounded.Cancel
            , callback = {nameState.value = it}
            , modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp))


        FieldText(title = stringResource(id = R.string.value)
                , value = valueState.value
                , icon =  Icons.Rounded.Cancel
                , hasErrorState = valueIsErrorState
                , callback = { valueState.value = it}
                , validation = {viewModel.validate()}
                , keyboardType = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                , modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp))


        CheckBoxField(title = stringResource(id = R.string.active),
            value = activeState.value,
            callback = {
                activeState.value = it
                viewModel.validate()
                       },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp))


        if (stateType.value) {
            MoreOptionsDialog(listOptions = MoreOptionsItemsTypeSettings.values().toList(),
                onDismiss = { stateType.value = false },
                onClick = {
                    viewModel.dto?.type = context.getString(it.getName())
                    stateType.value = false
                })
        }
    }
}

@Composable
private fun Buttons(viewModel: CreditCardSettingViewModel) {
    val state = remember {viewModel.showButtons}
    val newOneState = remember {viewModel.newOne}
        if (!newOneState.value) {
            FloatButton(imageVector = Icons.Rounded.Update,
                descriptionIcon = R.string.save) {
                viewModel.update()
            }
        } else {
            FloatButton(imageVector = Icons.Rounded.Create,
                descriptionIcon =R.string.save) {
                viewModel.create()
            }
        }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun CreditCardSettingPreview(){
    val viewModel = CreditCardSettingViewModel(codeCreditCard = null,codeCreditCardSetting = null, creditCardSvc = null,creditCardSettingSvc = null, navController=null)
    viewModel.showProgress.value = false
    MaterialThemeComposeUI {
        CreditCardSetting(viewModel = viewModel)
    }
}