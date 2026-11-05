package co.com.japl.module.paid.views.accounts.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import co.com.japl.module.paid.R
import co.com.japl.module.paid.controllers.accounts.form.AccountViewModel
import co.com.japl.ui.components.CheckBoxField
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.components.LoadingProgress
import co.com.japl.ui.components.OnResumeEffect
import co.com.japl.ui.theme.values.Dimensions


@Composable
fun AccountForm(viewModel: AccountViewModel){
    var stateLoader = remember {
        viewModel.loader
    }

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
                FloatButton(imageVector = Icons.Rounded.Add, descriptionIcon = R.string.add_input_account) {
                    viewModel.save()
                }
            }
        ) {
            Body(viewModel = viewModel,modifier=Modifier.padding(it))
        }
    }
}

@Composable
private fun Body(viewModel: AccountViewModel, modifier:Modifier){
    val stateName = remember { viewModel.name }
    val stateActive = remember { viewModel.active }

    Column {
        FieldText(
            title = stringResource(id = R.string.name)
            , value = stateName.value
            , validation = {viewModel.validation()}
            , hasErrorState = viewModel.errorName
            , callback = { stateName.value = it }
            ,modifier= modifier
                .fillMaxWidth()
                .padding(Dimensions.PADDING_SHORT))

        CheckBoxField(
            title = stringResource(id = R.string.active)
            , value = stateActive.value
            , modifier = modifier.padding(Dimensions.PADDING_SHORT)
            , callback = { stateActive.value = it }
        )
    }

}
