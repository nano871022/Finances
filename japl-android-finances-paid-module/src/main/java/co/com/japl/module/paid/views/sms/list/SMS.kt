package co.com.japl.module.paid.views.sms.list

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.compose.rememberNavController
import co.com.japl.finances.iports.dtos.SMSPaidDTO
import co.com.japl.module.paid.R
import co.com.japl.module.paid.controllers.sms.list.SmsViewModel
import co.com.japl.module.paid.enums.MoreOptionsItemSms
import co.com.japl.module.paid.views.fakeSvc.AccountPortFake
import co.com.japl.module.paid.views.fakeSvc.SMSPaidPortFake
import co.com.japl.ui.components.AlertDialogOkCancel
import co.com.japl.ui.components.Carousel
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.components.HelpWikiButton
import co.com.japl.ui.components.IconButton
import co.com.japl.ui.components.MoreOptionsDialog
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1f
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SMS(viewModel: SmsViewModel) {
    val loader = remember { viewModel.load }
    val progress = remember { viewModel.progress }

    CoroutineScope(Dispatchers.IO).launch {
        viewModel.main()
    }

    if (loader.value) {
        LinearProgressIndicator(
            progress = { progress.floatValue },
            modifier = Modifier.fillMaxWidth(),
        )
    } else {
        Body(viewModel = viewModel)
    }

}

@Composable
private fun Body(viewModel: SmsViewModel) {
    val navController = rememberNavController()
    Scaffold(
        floatingActionButton = {
            Buttons {
                viewModel.add(navController)
            }
        }
    ) {
        Content(viewModel = viewModel, modifier = Modifier.padding(it))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Content(viewModel: SmsViewModel,modifier: Modifier){
    val list = remember { viewModel.list}
    Column {
        Row (horizontalArrangement = Arrangement.End, modifier=Modifier.fillMaxWidth()) {
            HelpWikiButton(wikiUrl = R.string.wiki_sms_paid_url,
                descriptionContent = R.string.wiki_sms_paid_description)
        }
    if(list.isNotEmpty()) {
        Carousel(size = list.size) {
            Column(
                modifier = modifier.padding(bottom = Dimensions.PADDING_BOTTOM_SPACE_FLOATING_BUTTON)
            ) {
                list[it]?.values?.forEach {
                    for (i in it) {
                        Card(sms = i, modifier = Modifier, edit = {
                            viewModel.edit(it)
                        }, delete = {
                            viewModel.delete(it)
                        }, enable = {
                            viewModel.enabled(it)
                        }, disable = {
                            viewModel.disabled(it)
                        })
                    }
                }
            }
            }
        }
    }
}

@Composable
private fun Card(sms:SMSPaidDTO,modifier:Modifier=Modifier,edit:(Int)->Unit,delete:(Int)->Unit,enable:(Int)->Unit,disable:(Int)->Unit) {
    val popupStable = remember { mutableStateOf(false) }
    val popupDeleteDialog = remember { mutableStateOf(false) }
    Card( modifier = modifier
        .fillMaxWidth()
        .padding(
            start = Dimensions.PADDING_SHORT,
            end = Dimensions.PADDING_SHORT,
            top = Dimensions.PADDING_SHORT
        ), border = BorderStroke(width= 1.dp, color = if(sms.active)Color.Unspecified else Color.Red)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
            ,modifier = Modifier.padding(Dimensions.PADDING_SHORT)
        ) {
            Text(text = sms.nameAccount,modifier=Weight1f())

            Text(text = sms.phoneNumber,modifier=Weight1f(), textAlign = TextAlign.End)

            IconButton(imageVector = Icons.Rounded.MoreVert,
                descriptionContent = co.com.japl.ui.R.string.see_more, onClick = {
                    popupStable.value = true
                })
        }
    }
    if (popupStable.value) {
        MoreOptionsDialog(listOptions = MoreOptionsItemSms.values().filter{
            (sms.active && it != MoreOptionsItemSms.ENABLE) || (!sms.active && it != MoreOptionsItemSms.DISABLE)
        }.toList(),
            onDismiss = { popupStable.value = false }) {
            when (it) {
                MoreOptionsItemSms.DELETE -> popupDeleteDialog.value = true
                MoreOptionsItemSms.EDIT -> edit.invoke(sms.id)
                MoreOptionsItemSms.ENABLE -> enable.invoke(sms.id)
                MoreOptionsItemSms.DISABLE -> disable.invoke(sms.id)
            }
            popupStable.value = false
        }
    }
    if (popupDeleteDialog.value) {
        AlertDialogOkCancel(
            R.string.dialog_delete, R.string.delete, onDismiss = {
                popupDeleteDialog.value = false
            }, onClick = {
                delete.invoke(sms.id)
                popupDeleteDialog.value = false
            })
    }
}

@Composable
private fun Buttons(create:()->Unit){
    FloatButton(imageVector = Icons.Rounded.Add, descriptionIcon = R.string.create) {
        create.invoke()
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
internal fun SMSPreviewNight(){
    MaterialThemeComposeUI {

        SMS(getViewModel())
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
internal fun SMSPreview(){
    MaterialThemeComposeUI {

        SMS(getViewModel())
    }
}

@Composable
private fun getViewModel():SmsViewModel {
  val viewModel = SmsViewModel(null,null, null)
    viewModel.load.value = false
    listOf(
        SMSPaidDTO(
            id = 1,
            codeAccount = 1,
            nameAccount = "Visa",
            phoneNumber = "123456789",
            pattern = "123456789",
            active = false
        )
    ,
        SMSPaidDTO(
            id = 1,
            codeAccount = 1,
            nameAccount = "Visa",
            phoneNumber = "123456789",
            pattern = "123456789",
            active = true
        ),
        SMSPaidDTO(
            id = 1,
            codeAccount = 2,
            nameAccount = "Master",
            phoneNumber = "123456789",
            pattern = "123456789",
            active = true
        ),
        SMSPaidDTO(
            id = 1,
            codeAccount = 2,
            nameAccount = "Master",
            phoneNumber = "123456789",
            pattern = "123456789",
            active = true
        )
    ).groupBy { it.codeAccount }?.let {
        it.map{
            it
        }?.let{
            it.map {
                mapOf(it.key to it.value)
            }?.let{
                viewModel.list.addAll(it)
            }

        }

    }
  return viewModel
}