package co.com.japl.module.creditcard.views.email.list

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.EmailCreditCardDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.controllers.emailcreditcard.list.EmailListCreditCardViewModel
import co.com.japl.module.creditcard.enums.MoreOptionsItemEmailListCC
import co.com.japl.ui.components.AlertDialogOkCancel
import co.com.japl.ui.components.Carousel
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.FieldViewCards
import co.com.japl.ui.components.IconButton
import co.com.japl.ui.components.LoadingProgress
import co.com.japl.ui.components.MoreOptionsDialog
import co.com.japl.ui.enums.IMoreOptions
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions

@Composable
fun EmailList(viewModel: EmailListCreditCardViewModel){
    val load = remember { viewModel.load}

    LoadingProgress(
        message = R.string.loading_data,
        showProgress = load,
        execute = viewModel::execution
    ) {
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
        Banks(viewModel,Modifier.padding(it))
    }
}


@Composable
private fun Banks(viewModel: EmailListCreditCardViewModel,modifier:Modifier){
    val deleteAlert = remember { mutableStateOf<Boolean>(false) }
    val idRecord = remember { mutableStateOf<Int>(0) }
    val list = viewModel.list.groupBy { it.nameCreditCard }

    Carousel(
        size=list.size
    ){
        val list2 = list.entries.elementAt(it)

        Column (modifier=Modifier.fillMaxWidth().padding(Dimensions.PADDING_TOP)) {
            FieldView(
                title=stringResource(R.string.credit_card),
                value=list2.key,
                modifier=Modifier.fillMaxWidth().padding(Dimensions.PADDING_SHORT))
            for(item in list2.value) {
                EmailItem(item){opt,id->
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
                }
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
    val color = if(dto.active) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface
    Card (
        border=BorderStroke(1.dp,color),
        modifier = Modifier.fillMaxWidth().padding(Dimensions.PADDING_SHORT)){
        Column(modifier=Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth().padding(Dimensions.PADDING_SHORT)) {

                Text(
                    text = stringResource(dto.kindInterestRateEnum.getName()),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f).padding(Dimensions.PADDING_SHORT).align(alignment = Alignment.CenterVertically)
                )

                IconButton(
                    imageVector = Icons.Rounded.MoreVert,
                    descriptionContent = R.string.more_options,
                    onClick = { moreOptions.value = true },
                    modifier = Modifier.weight(1f)
                )
            }

            FieldViewCards(
                name=R.string.sender,
                value=dto.sender,
                modifier=Modifier.fillMaxWidth().padding(start=Dimensions.PADDING_SHORT)
            )
        }
    }
    if(moreOptions.value){
        MoreOptions(enable=dto.active,
            onClick = {
            onClick(it,id)
            moreOptions.value = false
        }, onDismiss = {
            moreOptions.value = false
        })
    }
}

@Composable
private fun MoreOptions(enable:Boolean,onClick: (IMoreOptions)->Unit, onDismiss: ()->Unit){
    MoreOptionsDialog(
        listOptions = MoreOptionsItemEmailListCC.entries.filter{
            (enable && it == MoreOptionsItemEmailListCC.DISABLED) ||
            (enable.not() && it == MoreOptionsItemEmailListCC.ENABLED) ||
            (it != MoreOptionsItemEmailListCC.DISABLED && it != MoreOptionsItemEmailListCC.ENABLED)
        },
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
        id = 1,
        sender = "sender@email.com",
        subjectPattern = "Bought .+",
        bodyPattern = "Bought .+ value .+ in .+",
        kindInterestRateEnum = KindInterestRateEnum.CREDIT_CARD,
        codeCreditCard = 10,
        nameCreditCard = "Bank 1",
        active = false

    ))
    vm.list.add(EmailCreditCardDTO(
        id = 2,
        sender = "sender@email.com",
        subjectPattern = "Bought .+",
        bodyPattern = "Bought .+ value .+ in .+",
        kindInterestRateEnum = KindInterestRateEnum.CREDIT_CARD,
        codeCreditCard = 11,
        nameCreditCard = "Bank 2",
        active = true

    ))
    return vm
}