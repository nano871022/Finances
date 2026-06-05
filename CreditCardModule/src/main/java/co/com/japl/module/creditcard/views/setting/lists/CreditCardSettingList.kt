package co.com.japl.module.creditcard.views.setting.lists

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import co.com.japl.finances.iports.dtos.CreditCardSettingDTO
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardSettingPort
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.controllers.setting.CreditCardSettingListViewModel
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1f
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1fAndAlightCenterVertical
import co.com.japl.module.creditcard.enums.MoreOptionsItemsSettingsCreditCard
import co.com.japl.ui.utils.WindowWidthSize
import co.com.japl.ui.components.AlertDialogOkCancel
import co.com.japl.ui.components.FieldViewCards
import co.com.japl.ui.components.MoreOptionsDialog
import java.time.LocalDateTime

@Composable
 fun CreditCardSettingList(viewModel: CreditCardSettingListViewModel){
    val stateShow = remember {viewModel.showProgress}

    if(stateShow.value) {
        viewModel.execute()
        Column {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
            )
            Text(text=stringResource(R.string.loading_data),
                textAlign = TextAlign.Center,
                color= MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth())
        }
    }else {
        Body(viewModel = viewModel)
    }
}

@Composable
private fun Body(viewModel: CreditCardSettingListViewModel){
    val listState = remember { viewModel.list }
    Scaffold( floatingActionButton = {
        FloatingActionButton(onClick = {
            viewModel.onClick()
        }, elevation = FloatingActionButtonDefaults.elevation(10.dp),
            backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = stringResource(id = R.string.add_credit_card_setting)
            )
        }
    },modifier=Modifier.padding(5.dp)) {
            Column(modifier = Modifier.padding(it)) {
                listState.forEach{item->
                    Item(
                        item = item,
                        edit = { id -> viewModel.edit(id) },
                        delete = { id -> viewModel.delete(id) })
                }
        }
    }

}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun Item(item:CreditCardSettingDTO,edit:(Int)->Unit,delete:(Int)->Unit){
    val stateShowOptions = remember { mutableStateOf(false) }
    val stateShowDelete = remember { mutableStateOf(false) }

     Card (modifier = Modifier.padding(5.dp)
     ) {
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
        Column (modifier=Modifier.scrollable(rememberScrollState(), Orientation.Vertical)){
            Row(modifier = Modifier.padding(8.dp)) {
                Text(text = item.name, modifier = Weight1fAndAlightCenterVertical())

                Text(text = stringResource(id = R.string.value), modifier = Weight1fAndAlightCenterVertical())
                Text(text = item.value, modifier = Weight1fAndAlightCenterVertical())

                Text(text = stringResource(id = R.string.status), modifier = Weight1fAndAlightCenterVertical())
                Text(text = (item.active > 0).toString(), modifier = Weight1fAndAlightCenterVertical())

                FieldViewCards(
                    name = R.string.credit_card_setting_type,
                    value = item.type,
                    modifier =  Weight1fAndAlightCenterVertical(),
                )

                IconButton(onClick = { statusShowOptions.value = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.more_vertical),
                        contentDescription = stringResource(id = R.string.see_more)
                    )
                }
            }
        }
}

@SuppressLint("SuspiciousIndentation")
@Composable
private fun ItemCompact(item:CreditCardSettingDTO,statusShowOptions:MutableState<Boolean>){
    val active by remember { mutableStateOf(item.active)}
    val color = if(active>0) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.error
    Column (modifier = Modifier.padding(5.dp).scrollable(rememberScrollState(), Orientation.Vertical)){
            Row {
                Text(text = item.name,
                    color=color,
                    modifier = Modifier
                    .weight(1f)
                    .align(alignment = Alignment.CenterVertically))

                Text(text = item.value, modifier = Modifier.align(alignment = Alignment.CenterVertically))

                IconButton(onClick = { statusShowOptions.value = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.more_vertical),
                        contentDescription = stringResource(id = R.string.see_more)
                    )
                }
            }

                FieldViewCards(
                    name = R.string.credit_card_setting_type,
                    value = item.type,
                    modifier =  Modifier,
                )
        }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
fun CreditCardSettingListPreview(){
    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        CreditCardSettingList(viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun CreditCardSettingListPreviewDARK(){
    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        CreditCardSettingList(viewModel)
    }
}

fun getViewModel():CreditCardSettingListViewModel{
    val viewModel = CreditCardSettingListViewModel(
        savedStateHandle = SavedStateHandle(),
        creditCardSettingSvc = object:ICreditCardSettingPort{
            override fun getAll(codeCreditCard: Int): List<CreditCardSettingDTO> {
                TODO("Not yet implemented")
            }

            override fun get(
                codeCreditCard: Int,
                codeCreditCardSetting: Int
            ): CreditCardSettingDTO? {
                TODO("Not yet implemented")
            }

            override fun delete(
                codeCreditCard: Int,
                codeCreditCardSetting: Int
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun update(dto: CreditCardSettingDTO): Boolean {
                TODO("Not yet implemented")
            }

            override fun create(dto: CreditCardSettingDTO): Int {
                TODO("Not yet implemented")
            }
        }
    )
    viewModel.showProgress.value = false
    viewModel.list.add(CreditCardSettingDTO(1,2,"test","test"," cambios", LocalDateTime.now(),Short.MIN_VALUE))
    viewModel.list.add(CreditCardSettingDTO(1,2,"test","test","cambios", LocalDateTime.now(),0))
    viewModel.list.add(CreditCardSettingDTO(1,2,"test","test","cambios", LocalDateTime.now(),Short.MIN_VALUE))
    viewModel.list.add(CreditCardSettingDTO(1,2,"test","test","cambios", LocalDateTime.now(),Short.MIN_VALUE))
    viewModel.list.add(CreditCardSettingDTO(1,2,"test","test","cambios", LocalDateTime.now(),Short.MIN_VALUE))
    viewModel.list.add(CreditCardSettingDTO(1,2,"test","test","cambios", LocalDateTime.now(),Short.MIN_VALUE))
    viewModel.list.add(CreditCardSettingDTO(1,2,"test","test","cambios", LocalDateTime.now(),0))
    return viewModel
}