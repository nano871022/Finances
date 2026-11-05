package co.com.japl.module.paid.views.Inputs.form

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import co.com.japl.module.paid.R
import co.com.japl.module.paid.controllers.Inputs.form.InputViewModel
import co.com.japl.module.paid.enums.MoreOptionsKindPaymentInput
import co.com.japl.ui.components.FieldDatePicker
import co.com.japl.ui.components.FieldSelect
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.components.LoadingProgress
import co.com.japl.ui.components.MoreOptionsDialog
import co.com.japl.ui.components.OnResumeEffect
import co.com.japl.ui.theme.values.Dimensions


@Suppress("LocalContextGetResourceValueCall")
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun InputForm(viewModel: InputViewModel){
    val stateLoader = remember { viewModel.loader }

    OnResumeEffect {
        viewModel.reload()
    }

    LoadingProgress(
        message = R.string.loading_data,
        showProgress = stateLoader,
        validateData = { true },
        messageNoData = R.string.no_data
    ) {
        Scaffold(
            floatingActionButton = {
                FloatButton(imageVector = Icons.Rounded.Add, descriptionIcon = R.string.add_account) {
                    viewModel.save()
                }
            }
        ) {
            Body(viewModel = viewModel, Modifier.padding(it))
        }
    }
}

@Suppress("LocalContextGetResourceValueCall")
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
private fun Body(viewModel: InputViewModel, modifier: Modifier) {
    val stateDate = remember { viewModel.date }
    val stateKindOfPayment = remember { viewModel.kindOfPayment }
    val stateName = remember { viewModel.name }
    val stateValue = remember { viewModel.value }

    val stateErrorDate = remember { viewModel.errorDate }
    val stateErrorKindOfPayment = remember { viewModel.errorKindOfPayment }
    val stateErrorName = remember { viewModel.errorName }
    val stateErrorValue = remember { viewModel.errorValue }

    val stateOptionsKindOfPayment = remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column {

        FieldDatePicker(title = R.string.date_input
            ,value = stateDate.value
            , callable = {stateDate.value = it}
            , isError = stateErrorDate
            , validation = {viewModel.validation()}
            , modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.PADDING_SHORT)
            )

        FieldSelect(title = stringResource(id = R.string.kind_of_pay)
            ,value = stateKindOfPayment.value
            , list = MoreOptionsKindPaymentInput.values().toList()
             , isError = stateErrorKindOfPayment
            , modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.PADDING_SHORT)
            , callable = {
                it?.let {
                    stateKindOfPayment.value = context.getString(it.getName())
                }
            })

        FieldText(title = stringResource(id = R.string.name)
            ,value = stateName.value
            , validation = {viewModel.validation()}
            , callback = {stateName.value = it}
            , hasErrorState = stateErrorName
            , icon = Icons.Rounded.Cancel
            , modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.PADDING_SHORT))

        FieldText(title = stringResource(id = R.string.value)
            ,value = stateValue.value
            , validation = {viewModel.validation()}
            , callback = {stateValue.value = it}
            , hasErrorState = stateErrorValue.value
            , currency = true
            , icon = Icons.Rounded.Cancel
            , modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.PADDING_SHORT))

    }

    if(stateOptionsKindOfPayment.value) {
        MoreOptionsDialog(listOptions = MoreOptionsKindPaymentInput.values().toList(),
            onDismiss = { stateOptionsKindOfPayment.value = false },
            onClick = {

                stateOptionsKindOfPayment.value = false
            })
    }
}
