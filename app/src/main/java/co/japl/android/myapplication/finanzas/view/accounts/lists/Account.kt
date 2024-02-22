package co.japl.android.myapplication.finanzas.view.accounts.lists

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import co.com.japl.ui.components.AlertDialogOkCancel
import co.com.japl.ui.components.Carousel
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.MoreOptionsDialog
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsAccount
import co.japl.android.myapplication.finanzas.view.accounts.inputs.lists.InputList
import co.japl.android.myapplication.finanzas.view.accounts.inputs.lists.InputListModelView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AccountList(viewModel: AccountViewModel) {
    val stateProgress = remember {
        viewModel.progress
    }
    val stateLoader = remember {
        viewModel.loading
    }
    val scope = rememberCoroutineScope()
    scope.launch {
        withContext(Dispatchers.IO) {
            viewModel.main()
        }
    }

    if(stateLoader.value) {
        LinearProgressIndicator(      progress = stateProgress.value)
    }else {

        Scaffold(
            floatingActionButton = {
                IconButton(onClick = { viewModel.add() }) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = stringResource(id = R.string.create)
                    )
                }
            }
        ) {
            Body(viewModel = viewModel, modifier = Modifier.padding(it))
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Body(viewModel: AccountViewModel,modifier:Modifier) {
    val listState = remember { viewModel.list}
    val state = remember { mutableStateOf(false) }
    val stateDelete = remember { mutableStateOf(false) }
 Carousel(size = listState.size) {
     val item = listState[it]
     Card(
         modifier = Modifier
             .padding(Dimensions.PADDING_SHORT)
             .fillMaxWidth()
     ) {
         Row {
             FieldView(
                 title = stringResource(id = R.string.name),
                 value = item.name,
                 modifier = Modifier.weight(1f).align(Alignment.CenterVertically)
             )
             IconButton(onClick = { state.value = true }) {
                 Icon(
                     painter = painterResource(id = R.drawable.more_vertical),
                     contentDescription = "Add input to account"
                 )
             }
         }

         InputList(
             modelView = InputListModelView(
                 LocalContext.current,
                 item.id,
                 viewModel.navController!!,
                 viewModel.inputSvc
             )
         )
         if (stateDelete.value) {
             AlertDialogOkCancel(
                 title = R.string.do_you_want_to_delete_this_record,
                 confirmNameButton = R.string.delete,
                 onDismiss = { stateDelete.value = false }) {
                 viewModel.delete(item.id)
                 stateDelete.value = false
             }
         }
     }

     if (state.value) {
         MoreOptionsDialog(listOptions = MoreOptionsItemsAccount.values().toList(),
             onDismiss = { state.value = false },
             onClick = {
                 when (it) {
                     MoreOptionsItemsAccount.DELETE -> stateDelete.value = true
                     MoreOptionsItemsAccount.EDIT -> viewModel.edit(item.id)
                 }
                 state.value = false
             })
     }
 }
}

