package co.com.japl.module.creditcard.views.setting.lists

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
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
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.controllers.setting.CreditCardSettingListViewModel
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1f
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1fAndAlightCenterVertical
import co.com.japl.module.creditcard.enums.MoreOptionsItemsSettingsCreditCard
import co.com.japl.ui.utils.WindowWidthSize
import co.com.japl.ui.components.AlertDialogOkCancel
import co.com.japl.ui.components.MoreOptionsDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
 fun CreditCardSettingList(viewModel: CreditCardSettingListViewModel){
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
private fun Body(viewModel: CreditCardSettingListViewModel){
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
                Text(text = item.name, modifier = Weight1fAndAlightCenterVertical())

                Text(text = stringResource(id = R.string.value), modifier = Weight1fAndAlightCenterVertical())
                Text(text = item.value, modifier = Weight1fAndAlightCenterVertical())

                Text(text = stringResource(id = R.string.status), modifier = Weight1fAndAlightCenterVertical())

                Text(text = (item.active > 0).toString(), modifier = Weight1fAndAlightCenterVertical())

                Text(
                    text = stringResource(id = R.string.credit_card_setting_type),
                    modifier = Weight1fAndAlightCenterVertical()
                )
                Text(text = item.type, modifier = Weight1fAndAlightCenterVertical())

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
            Row (modifier=Modifier.padding(top=Dimensions.PADDING_TOP)) {
                Text(text = stringResource(id = R.string.value), modifier = Weight1f())
                Text(text = item.value, modifier = Weight1f())

                Text(text = stringResource(id = R.string.status), modifier = Weight1f())
                Text(text = if(item.active > 0){ stringResource(id = R.string.active_status)} else{ stringResource(id = R.string.disabled_status)}, modifier = Weight1f())

            }

            Row {
                Text(
                    text = stringResource(id = R.string.credit_card_setting_type),
                    modifier = Modifier.weight(1f)
                )
                Text(text = item.type, modifier = Modifier.weight(3f))
            }
        }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun CreditCardSettingListPreview(){
    val viewModel = CreditCardSettingListViewModel(0, null,null)
    MaterialThemeComposeUI {
        CreditCardSettingList(viewModel)
    }
}