package co.com.japl.module.paid.views.accounts.list

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.AccountDTO
import co.com.japl.module.paid.R
import co.com.japl.module.paid.controllers.Inputs.list.InputListModelView
import co.com.japl.module.paid.controllers.accounts.list.AccountViewModel
import co.com.japl.module.paid.enums.MoreOptionsItemsAccount
import co.com.japl.module.paid.views.Inputs.list.InputList
import co.com.japl.ui.components.AlertDialogOkCancel
import co.com.japl.ui.components.Carousel
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.HelpWikiButton
import co.com.japl.ui.components.MoreOptionsDialog
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

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
        LinearProgressIndicator(      progress = stateProgress.value, modifier = Modifier.fillMaxWidth())
    }else {

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = { viewModel.add() },
                    elevation =  FloatingActionButtonDefaults.elevation(
                        defaultElevation = 10.dp
                    ),
                    backgroundColor =  MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)) {
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
private fun Body(viewModel: AccountViewModel, modifier:Modifier) {
    val listState = remember { viewModel.list}
    val state = remember { mutableStateOf(false) }
    val stateDelete = remember { mutableStateOf(false) }
    Column {
        Row(horizontalArrangement = Arrangement.End, modifier=Modifier.fillMaxWidth()) {
            HelpWikiButton(wikiUrl = R.string.wiki_account_url,
                descriptionContent = R.string.wiki_account_description)
        }
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
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    )
                    IconButton(onClick = { state.value = true }) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = "Add input to account"
                        )
                    }


                }

                Column {
                    InputList(
                        modelView = InputListModelView(
                            LocalContext.current,
                            item.id,
                            viewModel.navController,
                            viewModel.inputSvc
                        )
                    )
                }
                if (stateDelete.value) {
                    AlertDialogOkCancel(
                        title = R.string.toast_do_you_want_to_delete_this_record,
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
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, showSystemUi = true)
fun AccountListPreview() {
    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        AccountList(viewModel = viewModel)
    }
}

fun getViewModel(): AccountViewModel {
    val viewModel = AccountViewModel(accountSvc = null, inputSvc = null,navController = null)
    viewModel.loading.value = false
    viewModel.list.add(AccountDTO(1, LocalDate.now(),"",true))
    return viewModel
}