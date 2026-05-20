package co.com.japl.module.creditcard.views.email.list

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import co.com.japl.finances.iports.dtos.EmailCreditCardDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.controllers.emailcreditcard.list.EmailListCreditCardViewModel
import co.com.japl.module.creditcard.enums.MoreOptionsItemEmailListCC
import co.com.japl.ui.components.AlertDialogOkCancel
import co.com.japl.ui.components.IconButton
import co.com.japl.ui.components.MoreOptionsDialog
import co.com.japl.ui.enums.IMoreOptions
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions

@Composable
fun EmailList(viewModel: EmailListCreditCardViewModel){
    val load by viewModel.load

    LaunchedEffect (Unit){
        viewModel.main()
    }

    if(load){
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth()
        )
    }else{
        BodyMain(viewModel)
    }

}

@Composable
private fun BodyMain(viewModel: EmailListCreditCardViewModel){
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.create() }){
                Icon(imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.create))
            }
        }
    ) {
        Body(viewModel, modifier=Modifier.padding(it))
    }
}

@Composable
private fun Body(viewModel: EmailListCreditCardViewModel,modifier:Modifier){
    val deleteAlert = remember { mutableStateOf<Boolean>(false) }
    val idRecord = remember { mutableStateOf<Int>(0) }
    val list = viewModel.list
    LazyColumn (modifier = Modifier.fillMaxWidth().padding(Dimensions.PADDING_SHORT),
        userScrollEnabled = true) {
        items(list.size){
            Column {
                EmailItem(list[it], onClick = { opt,id ->
                    when(opt){
                        MoreOptionsItemEmailListCC.EDIT -> viewModel.edit(id)
                        MoreOptionsItemEmailListCC.DISABLED -> viewModel.activate(id,false)
                        MoreOptionsItemEmailListCC.ENABLED -> viewModel.activate(id,true)
                        MoreOptionsItemEmailListCC.DELETE -> {
                            idRecord.value = id
                            deleteAlert.value = true
                        }
                        MoreOptionsItemEmailListCC.CLONE -> viewModel.clone(id)
                    }
                })
            }
        }
    }

    if(deleteAlert.value){
        AlertDelete(onClick = {
            viewModel.delete(idRecord.value)
            deleteAlert.value = false
        }, onDismiss = {
            deleteAlert.value = false
        })
    }
}

@Composable
private fun EmailItem(dto: EmailCreditCardDTO,onClick:(IMoreOptions,Int)->Unit){
    val moreOptions = remember { mutableStateOf<Boolean>(false) }
    val id = dto.id

    Card (modifier = Modifier.fillMaxWidth().padding(Dimensions.PADDING_SHORT),
        backgroundColor = MaterialTheme.colorScheme.secondary){
        Row(modifier=Modifier.fillMaxWidth().padding(Dimensions.PADDING_SHORT)){

            Text(text=dto.nameCreditCard,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.weight(1f).padding(Dimensions.PADDING_TOP_MORE_VERT))

            Text(text=dto.sender,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.weight(1f).padding(Dimensions.PADDING_TOP_MORE_VERT))

            IconButton(
                imageVector = Icons.Rounded.MoreVert,
                descriptionContent = R.string.more_options,
                onClick = { moreOptions.value = true },
                modifier = Modifier.weight(1f)
            )
        }
    }
    if(moreOptions.value){
        MoreOptions(onClick = {
            onClick(it,id)
            moreOptions.value = false
        }, onDismiss = {
            moreOptions.value = false
        })
    }
}

@Composable
private fun MoreOptions(onClick: (IMoreOptions)->Unit, onDismiss: ()->Unit){
    MoreOptionsDialog(
        listOptions = MoreOptionsItemEmailListCC.entries,
        onClick = {  onClick(it) },
        onDismiss = onDismiss
    )
}

@Composable
private fun AlertDelete(onClick: () -> Unit, onDismiss: () -> Unit){
    AlertDialogOkCancel(
      title = R.string.delete_email_record,
        confirmNameButton = R.string.delete,
        onDismiss = onDismiss,
        onClick = onClick
    )
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
private fun EmailListPreview(){
    val vm = getViewModel()
    MaterialThemeComposeUI {
        EmailList(vm)
    }
}

@Composable
private fun getViewModel(): EmailListCreditCardViewModel {
   val vm =  EmailListCreditCardViewModel(svc = null, navController = null)
    vm.load.value = false
    vm.list.add(EmailCreditCardDTO(
        id = 1,
        sender = "sender@email.com",
        subjectPattern = "Bought .+",
        bodyPattern = "Bought .+ value .+ in .+",
        kindInterestRateEnum = KindInterestRateEnum.CREDIT_CARD,
        codeCreditCard = 10,
        nameCreditCard = "Bank 1",
        active = true

    ))
    vm.list.add(EmailCreditCardDTO(
        id = 2,
        sender = "sender@email.com",
        subjectPattern = "Bought .+",
        bodyPattern = "Bought .+ value .+ in .+",
        kindInterestRateEnum = KindInterestRateEnum.CREDIT_CARD,
        codeCreditCard = 10,
        nameCreditCard = "Bank 2",
        active = true

    ))
    return vm
}