package co.com.japl.module.paid.views.monthly.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ForwardToInbox
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import co.com.japl.module.paid.R
import co.com.japl.module.paid.controllers.monthly.list.MonthlyViewModel
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.components.Popup
import co.com.japl.ui.theme.values.Dimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopupSetting(viewModel: MonthlyViewModel, state: MutableState<Boolean>) {
    val paidEmailDaysRead = remember { viewModel.paidEmailDaysRead }
    val errorPaidEmailDaysRead = remember { viewModel.errorPaidEmailDaysRead }
    val paidSMSDaysRead = remember { viewModel.paidSMSDaysRead }
    val errorPaidSMSDaysRead = remember { viewModel.errorPaidSMSDaysRead }
    val context = LocalContext.current

    Popup(title = R.string.setting, state = state) {
        Scaffold(
            floatingActionButton = {
                Row {
                    FloatButton(imageVector = Icons.AutoMirrored.Rounded.ForwardToInbox, descriptionIcon = R.string.email_read) {
                        viewModel.readEmail(context)
                    }
                    FloatButton(imageVector = Icons.Rounded.Save, descriptionIcon = R.string.save) {
                        viewModel.saveSettings(context)
                    }
                }
            },
            modifier = Modifier.padding(Dimensions.PADDING_SHORT)
        ) {
            Column(modifier = Modifier.padding(it)) {
                FieldText(title = stringResource(id = R.string.msm_read_num),
                    value = paidSMSDaysRead.value,
                    hasErrorState = errorPaidSMSDaysRead,
                    keyboardType = KeyboardOptions(keyboardType = KeyboardType.Number),
                    icon = Icons.Rounded.Cancel,
                    validation = { viewModel.validation() },
                    callback = {
                        paidSMSDaysRead.value = it
                    }, modifier = Modifier.padding(top = Dimensions.PADDING_SHORT).fillMaxWidth())

                FieldText(title = stringResource(id = R.string.email_read_num),
                    value = paidEmailDaysRead.value,
                    hasErrorState = errorPaidEmailDaysRead,
                    keyboardType = KeyboardOptions(keyboardType = KeyboardType.Number),
                    icon = Icons.Rounded.Cancel,
                    validation = { viewModel.validation() },
                    callback = {
                        paidEmailDaysRead.value = it
                    }, modifier = Modifier.padding(top = Dimensions.PADDING_SHORT).fillMaxWidth())
            }
        }
    }
}
