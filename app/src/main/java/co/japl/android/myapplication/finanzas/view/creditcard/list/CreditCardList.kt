package co.japl.android.myapplication.finanzas.view.creditcard.list

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.theme.values.ModifiersCustom.AlignCenterVerticalAndPaddingRightSpace
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1f
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1fAndAlightCenterVertical
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1fAndAlightCenterVerticalAndPaddingRightSpace
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1fAndPaddintRightSpace
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsCreditCardList
import co.japl.android.myapplication.finanzas.utils.WindowWidthSize
import co.com.japl.ui.components.AlertDialogOkCancel
import co.com.japl.ui.components.MoreOptionsDialog
import co.japl.android.myapplication.utils.NumbersUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
internal fun CreditCardList(creditCardViewModel:CreditCardViewModel){
    val progress = remember {
        creditCardViewModel.progress
    }
    val status = remember{
        creditCardViewModel.showProgress
    }

    CoroutineScope(Dispatchers.IO).launch {
        creditCardViewModel.main()
    }

    if(status.value) {
        LinearProgressIndicator(progress = progress.floatValue, modifier = Modifier.fillMaxWidth())
    }else {
        Body(creditCardViewModel = creditCardViewModel)
    }
}

@Composable
private fun Body(creditCardViewModel:CreditCardViewModel){
    Scaffold(floatingActionButton = {
        IconButton(onClick = { creditCardViewModel.onClick() }) {
            Icon(
                imageVector = Icons.Rounded.AddCircleOutline,
                contentDescription = stringResource(R.string.add_credit_card)
            )
        }
    },modifier = Modifier.padding(Dimensions.PADDING_SHORT)) {
        Column(modifier = Modifier.padding(it)) {
        for(item in creditCardViewModel.list) {
                Item(item,{creditCardViewModel.edit(it)},{creditCardViewModel.delete(it)},{creditCardViewModel.goToSettings(it)})
            }
        }
    }
}

@Composable
private fun Item(dto:CreditCardDTO,edit:(Int)->Unit,delete:(Int)->Unit,goToSettings:(Int)->Unit){
    val state = remember{ mutableStateOf(false) }
    val stateOptions = remember{ mutableStateOf(false) }

    Card(modifier=Modifier.padding(start=Dimensions.PADDING_SHORT,end=Dimensions.PADDING_SHORT,top=Dimensions.PADDING_SHORT)) {
        BoxWithConstraints {
            when(WindowWidthSize.fromDp(maxWidth)){
                WindowWidthSize.MEDIUM ->
                    ItemLarge(dto = dto, state = state, edit = edit, delete = delete)
            else ->
                ItemCompact(dto = dto, state = state, edit = edit, delete = delete)
            }
        }
    }

    if(state.value){
        MoreOptionsDialog(listOptions = MoreOptionsItemsCreditCardList.values().toList()
            , onDismiss = { state.value = false }
            , onClick = {
                when(it){
                    MoreOptionsItemsCreditCardList.EDIT->edit(dto.id)
                    MoreOptionsItemsCreditCardList.DELETE->stateOptions.value = true
                    MoreOptionsItemsCreditCardList.SETTINGS->goToSettings.invoke(dto.id)
                }
                state.value = false
            })
    }

    if(stateOptions.value) {
        AlertDialogOkCancel(
            title = R.string.do_you_want_to_delete_this_record,
            confirmNameButton = R.string.delete,
            onDismiss = { stateOptions.value = false }
            , onClick = {
                delete(dto.id)
                stateOptions.value = false
            })
    }
}

@Composable
private fun ItemCompact(dto:CreditCardDTO,state:MutableState<Boolean>,edit:(Int)->Unit,delete:(Int)->Unit){
    Column (modifier = Modifier.padding(start=Dimensions.PADDING_SHORT,end=Dimensions.PADDING_SHORT)){
        Row {

            Text(text = dto.name,modifier = Weight1fAndAlightCenterVertical())

            IconButton(onClick = {
                state.value = true
            }) {
                Icon(painter = painterResource(id = R.drawable.more_vertical), contentDescription = stringResource(R.string.see_more))
            }
        }

        Divider()

        Row (modifier=Modifier.padding(top=Dimensions.PADDING_TOP)){
            Text(text = stringResource(id = R.string.cut_off_day),modifier = Weight1fAndPaddintRightSpace())

            Text(text = dto.cutOffDay.toString(),modifier = Weight1f(), textAlign = TextAlign.End)


            Text(text = stringResource(id = R.string.status), modifier = Weight1fAndPaddintRightSpace())

            Text(text=dto.status.toString(),modifier = Weight1f(), textAlign = TextAlign.End)
        }
        Row{
            Text(text = stringResource(id = R.string.warning_quote_value),modifier = Weight1fAndPaddintRightSpace())

            Text(text = NumbersUtil.COPtoString(dto.warningValue),modifier = Weight1f(), textAlign = TextAlign.End)
        }
    }
}

@Composable
private fun ItemLarge(dto:CreditCardDTO,state:MutableState<Boolean>,edit:(Int)->Unit,delete:(Int)->Unit){
    Column (modifier = Modifier.padding(start=5.dp,end=5.dp)){
        Row {

            Text(text = dto.cutOffDay.toString(),modifier = AlignCenterVerticalAndPaddingRightSpace(), textAlign = TextAlign.End)

            Text(text = dto.name,modifier = Weight1fAndAlightCenterVertical())

            Text(text = stringResource(id = R.string.status), modifier = AlignCenterVerticalAndPaddingRightSpace())

            Text(text=dto.status.toString(),modifier = AlignCenterVerticalAndPaddingRightSpace(), textAlign = TextAlign.End)

            Text(text = stringResource(id = R.string.warning_quote_value),modifier = Weight1fAndAlightCenterVerticalAndPaddingRightSpace())

            Text(text = NumbersUtil.COPtoString(dto.warningValue)
                ,modifier = Weight1fAndAlightCenterVertical()
                , textAlign = TextAlign.End)

            IconButton(onClick = {
                state.value = true
            }) {
                Icon(painter = painterResource(id = R.drawable.more_vertical), contentDescription = stringResource(R.string.see_more))
            }

        }
    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true)
fun CreditCardListPreview(){

    val creditCardViewModel = CreditCardViewModel(creditCardSvc = null,navController = null)
    MaterialThemeComposeUI {
        CreditCardList(creditCardViewModel)
    }
}