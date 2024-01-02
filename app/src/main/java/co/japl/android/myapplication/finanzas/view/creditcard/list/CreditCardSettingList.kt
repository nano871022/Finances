package co.japl.android.myapplication.finanzas.view.creditcard.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.CreditCardSettingDTO
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsSettingsCreditCard
import co.japl.android.myapplication.finanzas.utils.WindowWidthSize
import co.japl.android.myapplication.finanzas.view.components.AlertDialogOkCancel
import co.japl.android.myapplication.finanzas.view.components.MoreOptionsDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
internal fun CreditCardSettingList(viewModel: CreditCardSettingViewModel){
    val stateShow = remember {viewModel.showProgress}
    val stateProgress = remember {viewModel.progress}

    CoroutineScope(Dispatchers.IO).launch {
        viewModel.main()
    }

    if(stateShow.value) {
        LinearProgressIndicator(progress = stateProgress.floatValue, modifier = Modifier.fillMaxWidth())
    }else {
        Body(viewModel = viewModel)
    }
}

@Composable
private fun Body(viewModel: CreditCardSettingViewModel){
    Scaffold( floatingActionButton = {
        IconButton(onClick = {
            viewModel.onClick()
        }){
            Icon(
                imageVector = Icons.Rounded.AddCircleOutline,
                contentDescription = stringResource(id = R.string.add_credit_card_setting)
            )
        }
    },modifier=Modifier.padding(5.dp)) {
        MaterialThemeComposeUI {
            Column(modifier = Modifier.padding(it)) {
                for (item in viewModel.list) {
                    Item(
                        item = item,
                        edit = { id -> viewModel.edit(id) },
                        delete = { id -> viewModel.delete(id) })
                }
            }
        }
    }

}

@Composable
private fun Item(item:CreditCardSettingDTO,edit:(Int)->Unit,delete:(Int)->Unit){
    val stateShowOptions = remember { mutableStateOf(false) }
    val stateShowDelete = remember { mutableStateOf(false) }
     Card (modifier = Modifier.padding(5.dp)) {
        BoxWithConstraints {
            when (WindowWidthSize.fromDp(maxWidth)) {
                WindowWidthSize.MEDIUM -> ItemMedium(item, stateShowOptions)
                else -> ItemCompact(item, stateShowOptions)
            }
        }
     }
    if(stateShowOptions.value){
        MoreOptionsDialog(listOptions = MoreOptionsItemsSettingsCreditCard.values().toList(), onDismiss = { stateShowOptions.value = false }, onClick = {
            when(it){
                MoreOptionsItemsSettingsCreditCard.EDIT->edit(item.id)
                MoreOptionsItemsSettingsCreditCard.DELETE->stateShowDelete.value = true
            }
            stateShowOptions.value = false
        })
    }
    if(stateShowDelete.value){
        AlertDialogOkCancel(title = R.string.do_you_want_to_delete_this_record, confirmNameButton = R.string.delete, onDismiss = { stateShowDelete.value = false }) {
            delete(item.id)
            stateShowDelete.value = false
        }
    }
}

@Composable
private fun ItemMedium(item:CreditCardSettingDTO, statusShowOptions: MutableState<Boolean>){
        Column {
            Row(modifier = Modifier.padding(8.dp)) {
                Text(text = item.name, modifier = Modifier
                    .weight(1f)
                    .align(alignment = Alignment.CenterVertically))

                Text(text = stringResource(id = R.string.value), modifier = Modifier
                    .weight(1f)
                    .align(alignment = Alignment.CenterVertically))
                Text(text = item.value, modifier = Modifier
                    .weight(1f)
                    .align(alignment = Alignment.CenterVertically))

                Text(text = stringResource(id = R.string.status), modifier = Modifier
                    .weight(1f)
                    .align(alignment = Alignment.CenterVertically))
                Text(text = (item.active > 0).toString(), modifier = Modifier
                    .weight(1f)
                    .align(alignment = Alignment.CenterVertically))

                Text(
                    text = stringResource(id = R.string.credit_card_setting_type),
                    modifier = Modifier
                        .weight(1f)
                        .align(alignment = Alignment.CenterVertically)
                )
                Text(text = item.type, modifier = Modifier
                    .weight(1f)
                    .align(alignment = Alignment.CenterVertically))

                IconButton(onClick = { statusShowOptions.value = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.more_vertical),
                        contentDescription = stringResource(id = R.string.see_more)
                    )
                }
            }
        }
}

@Composable
private fun ItemCompact(item:CreditCardSettingDTO,statusShowOptions:MutableState<Boolean>){
        Column (modifier = Modifier.padding(5.dp)){
            Row {
                Text(text = item.name, modifier = Modifier
                    .weight(1f)
                    .align(alignment = Alignment.CenterVertically))

                IconButton(onClick = { statusShowOptions.value = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.more_vertical),
                        contentDescription = stringResource(id = R.string.see_more)
                    )
                }
            }
            Divider()
            Row {
                Text(text = stringResource(id = R.string.value), modifier = Modifier.weight(1f))
                Text(text = item.value, modifier = Modifier.weight(1f))

                Text(text = stringResource(id = R.string.status), modifier = Modifier.weight(1f))
                Text(text = (item.active > 0).toString(), modifier = Modifier.weight(1f))

            }

            Row {
                Text(
                    text = stringResource(id = R.string.credit_card_setting_type),
                    modifier = Modifier.weight(1f)
                )
                Text(text = item.type, modifier = Modifier.weight(1f))
            }
        }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun CreditCardSettingListPreview(){
    val viewModel = CreditCardSettingViewModel(0, null,null)
    MaterialThemeComposeUI {
        CreditCardSettingList(viewModel)
    }
}