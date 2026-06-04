package co.com.japl.module.paid.views.email.list

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.EmailPaidDTO
import co.com.japl.module.paid.R
import co.com.japl.module.paid.controllers.emailpaid.list.EmailListPaidViewModel
import co.com.japl.module.paid.enums.MoreOptionsItemsEmail
import co.com.japl.ui.components.AlertDialogOkCancel
import co.com.japl.ui.components.Carousel
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.FieldViewCards
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.components.MoreOptionsDialog
import co.com.japl.ui.enums.IMoreOptions
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions


@Composable
fun EmailListPaid(viewModel: EmailListPaidViewModel) {
    val load by viewModel.load

    LaunchedEffect(Unit) {
        viewModel.main()
    }
    Column{
        if (load) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Text(text=stringResource(R.string.loading_data))
        } else {
            Body(viewModel)
        }
    }
}

@Composable
fun Body(viewModel: EmailListPaidViewModel){
    val list = viewModel.list.groupBy { it.nameAccount }
    Scaffold(
        floatingActionButton = {
            FloatButton(
                imageVector = Icons.Rounded.Add,
                descriptionIcon = R.string.go_to_add
            ) { viewModel.add() }
        }
    ) {
        Carousel(
            size=list.size,
            modifier=Modifier.padding(it)
        ) {
            val items = list.entries.elementAt(it)
            Column(modifier = Modifier.fillMaxWidth().padding(Dimensions.PADDING_SHORT)) {

                FieldView(
                    title=stringResource(R.string.account),
                    value=items.key,
                    modifier=Modifier.fillMaxWidth()
                )
                LazyColumn {
                    items(items.value) { item ->
                        EmailItem(
                            item,
                            edit = { viewModel.edit(it) },
                            clone = { viewModel.clone(it) },
                            delete = { viewModel.delete(it) },
                            activate = { id, active -> viewModel.activate(id, active) })
                    }
                    item { Spacer(modifier = Modifier.height(100.dp)) }
                }

            }
        }
    }
}

    @Composable
    fun EmailItem(item: EmailPaidDTO, edit:(Int)->Unit, clone:(Int)->Unit, delete:(Int)->Unit, activate:(Int,Boolean)->Unit){
        var moreOptions by remember { mutableStateOf(false) }
        var showPopup by remember { mutableStateOf(false) }
        var color = if(item.active) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.surface

        Card(
            border= BorderStroke(1.dp,color),
            modifier = Modifier.fillMaxWidth().padding(Dimensions.PADDING_SHORT)) {
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {

                    FieldViewCards(
                        name = R.string.sender,
                        value = item.sender,
                        modifier = Modifier.weight(1f).align(alignment = Alignment.CenterVertically)
                    )

                    IconButton(onClick = { moreOptions = true }) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = stringResource(co.com.japl.ui.R.string.see_more)
                        )
                    }
                }
                FieldViewCards(
                    name = R.string.subject_email,
                    value=item.subjectPattern,
                    modifier=Modifier
                )
            }
        }
        if(moreOptions){
            MoreOptions(item.active,onClick = { opt ->
                when(opt){
                    MoreOptionsItemsEmail.EDIT->edit(item.id)
                    MoreOptionsItemsEmail.CLONE->{
                        moreOptions = false
                        clone(item.id)
                    }
                    MoreOptionsItemsEmail.DISABLED->{
                        moreOptions = false
                        activate(item.id,false)
                    }
                    MoreOptionsItemsEmail.ENABLED->{
                        moreOptions = false
                        activate(item.id,true)
                    }
                    MoreOptionsItemsEmail.DELETE->{
                        moreOptions=false
                        showPopup=true
                    }
                }
            },
                onDismiss = {moreOptions=false})
        }

        if(showPopup){
            ConfirmDeletePopup(onClick = {
                delete(item.id)
                showPopup = false
            }, onDismiss = {
                showPopup = false
            })
        }
    }

@Composable
private fun ConfirmDeletePopup( onClick: () -> Unit, onDismiss: () -> Unit){
    AlertDialogOkCancel(
        title = R.string.do_you_wnat_to_delete_email_record,
        confirmNameButton = R.string.delete,
        onDismiss = onDismiss,
        onClick = onClick
    )

}

@Composable
private fun MoreOptions(enable:Boolean,onClick: (IMoreOptions)->Unit, onDismiss: ()->Unit){
    MoreOptionsDialog (
        listOptions = MoreOptionsItemsEmail.entries.filter{
            (enable && it == MoreOptionsItemsEmail.DISABLED) ||
            (enable.not() && it == MoreOptionsItemsEmail.ENABLED) ||
            (it != MoreOptionsItemsEmail.DISABLED && it != MoreOptionsItemsEmail.ENABLED)
        },
        onClick = {  onClick(it) },
        onDismiss = onDismiss
    )
}

@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun EmailListPaidPreview(){
    val vm = getViewModel()
    MaterialThemeComposeUI {
        EmailListPaid(vm)
    }
}

@Composable
private fun getViewModel(): EmailListPaidViewModel {
    val vm =  EmailListPaidViewModel(svc = null, navController = null)
    vm.list.add(
        EmailPaidDTO(
            id = 0,
            sender = "@asd.com",
            subjectPattern = "Notificaciones",
            bodyPattern = ".+",
            codeAccount = 0,
            nameAccount = "Account",
            active = false,
        )
    )
    vm.load.value = false
    return vm
}


