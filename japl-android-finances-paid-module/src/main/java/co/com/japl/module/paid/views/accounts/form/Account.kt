package co.com.japl.module.paid.views.accounts.form

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.module.paid.R
import co.com.japl.module.paid.controllers.accounts.form.AccountViewModel
import co.com.japl.module.paid.views.accounts.form.fakes.FakeAccountPort
import co.com.japl.ui.components.CheckBoxField
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions

@Composable
fun Account(viewModel: AccountViewModel, navController: NavController) {
    val stateLoader = remember { viewModel.loader }
    val stateProgress = remember { viewModel.progress }
    val context = LocalContext.current

    if (stateLoader.value) {
        LinearProgressIndicator(
            progress = { stateProgress.value },
        )
    } else {
        Scaffold(
            floatingActionButton = {
                FloatButton(
                    imageVector = Icons.Rounded.Add,
                    descriptionIcon = R.string.add_input_account
                ) {
                    viewModel.save(navController, context)
                }
            }
        ) {
            Body(viewModel = viewModel, modifier = Modifier.padding(it))
        }
    }
}

@Composable
private fun Body(viewModel: AccountViewModel, modifier: Modifier) {
    val stateName = remember { viewModel.name }
    val stateActive = remember { viewModel.active }

    Column(modifier = modifier.fillMaxWidth()) {
        FieldText(
            title = stringResource(id = R.string.name),
            value = stateName.value,
            validation = { viewModel.validation() },
            hasErrorState = viewModel.errorName,
            callback = { stateName.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.PADDING_SHORT)
        )

        CheckBoxField(
            title = stringResource(id = R.string.active),
            value = stateActive.value,
            modifier = Modifier.padding(Dimensions.PADDING_SHORT),
            callback = { stateActive.value = it }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AccountPreview() {
    val accountSvc: IAccountPort = FakeAccountPort()
    MaterialThemeComposeUI {
        Account(
            viewModel(
                factory = AccountViewModel.Companion.create(
                    extras = viewModel(),
                    accountSvc = accountSvc
                )
            ),
            navController = rememberNavController()
        )
    }
}