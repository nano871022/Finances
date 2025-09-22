package co.japl.android.myapplication.finanzas.view.checkpaids

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.lifecycle.SavedStateHandle
import co.com.japl.finances.iports.dtos.CheckPaymentDTO
import co.com.japl.finances.iports.enums.CheckPaymentsEnum
import co.com.japl.ui.components.AlertDialogOkCancel
import co.com.japl.ui.components.CheckBoxField
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.japl.android.graphs.utils.NumbersUtil
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.controller.paids.CheckListViewModel
import co.japl.finances.core.utils.DateUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun CheckList(viewModel:CheckListViewModel){
    val loaderStatus = remember { viewModel.loaderStatus }
    val loaderProgressStatus = remember { viewModel.loaderProgressStatus }
    val progressState = remember { viewModel.progression }

    CoroutineScope(Dispatchers.IO).launch {
        viewModel.main()
    }

    if(loaderStatus.value){
        LinearProgressIndicator(progress = progressState.value, modifier = Modifier.fillMaxWidth())
    }else if(loaderProgressStatus.value){
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }else{
        Body(viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
private fun Body(viewModel: CheckListViewModel){
    val listState = remember { viewModel.listState }
    val context = LocalContext.current
    Scaffold (
        floatingActionButton = {
            Buttons(save = { viewModel.save(context) })
        },
        topBar = {
            Headers(viewModel.listState.sumOf { it.amount }.toDouble(),viewModel.listState.filter { it.check }.sumOf { it.amount }.toDouble())
        }, modifier = Modifier.padding(Dimensions.PADDING_SHORT)
    ){
        LazyColumn(modifier = Modifier.padding(it)) {
            val list = listState.sortedBy { it.date }
            items(list.size) { item ->
                Item(list[item],viewModel::remove)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
private fun Item(item:CheckPaymentDTO,delete: (CheckPaymentDTO) -> Unit = {}){
    val status = remember { mutableStateOf(item.check) }
    val statusDelete = remember { mutableStateOf(false) }
    val stateDelete = remember { mutableStateOf(false) }
    CheckBoxField(customTitle = {color, modifier ->
                Row (modifier= modifier
                    .fillMaxWidth()
                    .padding(start = Dimensions.PADDING_SHORT)
                    .weight(1f).combinedClickable(
                        onLongClick = {
                            statusDelete.value = !statusDelete.value
                        }
                         , onClick = {}
                    )){
                    if(statusDelete.value){
                        Button(onClick = { stateDelete.value = true; statusDelete.value = false}) {
                            Icon(imageVector = Icons.Rounded.DeleteForever, contentDescription = "save")
                        }
                    }

                    Text(text = item.name, modifier = modifier.weight(1f), color = color,
                            textDecoration = if(item.check) TextDecoration.LineThrough else TextDecoration.None)

                    Text(text = NumbersUtil.COPtoString(item.amount), modifier = modifier, color = color,
                        textDecoration =  if(item.check) TextDecoration.LineThrough else TextDecoration.None)
                }
            }, value = status.value,
                description = { color, modifier ->
                    item.date?.let{
                        Text(text = "${DateUtils.localDateTimeToString(it)} - ${Period.between(LocalDate.now(),it.toLocalDate()).days}",
                            modifier = modifier.padding(start = Dimensions.PADDING_TOP),
                            fontSize = TextUnit(12f, TextUnitType.Sp),
                            color = MaterialTheme.colorScheme.error)
                    }

                },
                callback = {
                    status.value = it
                    item.check = status.value
                    item.update = true
                           },
        customModifier = Modifier
    )

    if (stateDelete.value) {
        AlertDialogOkCancel(
            title = co.com.japl.module.paid.R.string.toast_do_you_want_to_delete_this_record,
            confirmNameButton = co.com.japl.module.paid.R.string.delete,
            onDismiss = { stateDelete.value = false }) {
            CoroutineScope(Dispatchers.IO).launch {
                delete(item)
            }
            stateDelete.value = false
        }
    }
}

@Composable
private fun Headers(toPay:Double,paid:Double){
    Row {

        FieldView(name = R.string.to_pay, value = NumbersUtil.toString( toPay) , modifier = Modifier
            .weight(1f)
            .padding(end = Dimensions.PADDING_SHORT))

        FieldView(name = R.string.paid, value = NumbersUtil.toString( paid) , modifier = Modifier.weight(1f))
    }
}

@Composable
private fun Buttons(save:()->Unit){
    FloatButton(imageVector = Icons.Rounded.Save, descriptionIcon = R.string.save) {
        save.invoke()
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
internal fun CheckListPreview(){
    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        CheckList(viewModel = viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
internal fun CheckListPreviewDark(){
    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        CheckList(viewModel = viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
internal fun CheckListPreviewDarkV2(){
    val viewModel = getViewModel()
    viewModel.loaderStatus.value = true
    MaterialThemeComposeUI {
        CheckList(viewModel = viewModel)
    }
}

@Composable
private fun getViewModel():CheckListViewModel{
    val viewModel = CheckListViewModel(SavedStateHandle(),null)
    viewModel.loaderStatus.value = false
    viewModel.loaderProgressStatus.value = false
    viewModel.listState.add(
        CheckPaymentDTO(
        id = 0,
        codPaid = 0,
        name = "Semanal",
        period = YearMonth.now(),
        date = LocalDateTime.now().minusYears(2),
        amount = 1000.00,
        check = true,
        type = CheckPaymentsEnum.CREDITS
        )
    )
    viewModel.listState.add(
        CheckPaymentDTO(
            id = 0,
            codPaid = 0,
            name = "Semanal1",
            period = YearMonth.now(),
            date = LocalDateTime.now(),
            amount = 1000.00,
            check = true,
            type = CheckPaymentsEnum.CREDITS
        )
    )

    viewModel.listState.add(
        CheckPaymentDTO(
            id = 0,
            codPaid = 0,
            name = "Semanal2",
            period = YearMonth.now(),
            date = null,
            amount = 1000.00,
            check = false,
            type = CheckPaymentsEnum.CREDITS
        )
    )
    return viewModel
}