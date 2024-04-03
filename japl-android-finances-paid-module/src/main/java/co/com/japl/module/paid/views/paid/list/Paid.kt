package co.com.japl.module.paid.views.paid.list

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.BrightnessHigh
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.PaidDTO
import co.com.japl.module.paid.R
import co.com.japl.module.paid.controllers.paid.list.PaidViewModel
import co.com.japl.module.paid.enums.PaidListOptions
import co.com.japl.ui.Prefs
import co.com.japl.ui.components.AlertDialogOkCancel
import co.com.japl.ui.components.CheckBoxField
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.FieldViewCards
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.components.IconButton
import co.com.japl.ui.components.MoreOptionsDialog
import co.com.japl.ui.components.Popup
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1f
import co.com.japl.ui.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun Paid(viewModel:PaidViewModel) {
    val progressState = remember {
        viewModel.progressStatus
    }
    val loaderState = remember {
        viewModel.loaderState
    }

    CoroutineScope(Dispatchers.IO).launch{
        viewModel.main()
    }

    if(loaderState.value){
        LinearProgressIndicator(progress = progressState.value,modifier=Modifier.fillMaxWidth())
    }else{
        Body(viewModel)
    }
}

@Composable
private fun Body(viewModel: PaidViewModel){
    Scaffold (floatingActionButton = {
        Buttons(newOne = {viewModel.newOne()})
    }){

        Column{
            Header(viewModel)
            List(viewModel,modifier = Modifier.padding(it))
        }

    }
}

@Composable
private fun Buttons(newOne:()->Unit){
    Column {
        FloatButton(imageVector = Icons.Rounded.Add, descriptionIcon = R.string.add_paid, onClick = newOne)
    }
}

@Composable private fun Header(viewModel: PaidViewModel){
    val settingState = remember {
        mutableStateOf(false)
    }
    Row{

        FieldView(name = stringResource(id = R.string.period),value=viewModel.periodOfList.value, modifier=Weight1f(), isMoney = false)

        FieldView(name = stringResource(id = R.string.value),value=NumbersUtil.toString(viewModel.allValues.value), modifier=Weight1f())

        IconButton(imageVector = Icons.Rounded.BrightnessHigh,
            descriptionContent = R.string.setting,
            onClick = {
                settingState.value = !settingState.value
            },modifier=Weight1f())
    }
    if(settingState.value){
        Settings(settingState = settingState,prefs= viewModel.prefs)
    }
}

@Composable private fun Settings( settingState: MutableState<Boolean>,prefs: Prefs?){
    val daysSmsReadState = remember {
        mutableStateOf(prefs?.paidSMSDaysRead?.toString()?:"0")
    }
    Popup(title = R.string.setting, state = settingState) {
        Scaffold (floatingActionButton = {
            FloatButton(imageVector = Icons.Rounded.Add, descriptionIcon = R.string.save) {
                daysSmsReadState.value?.takeIf { it.isNotBlank() }?.let{
                    prefs?.paidSMSDaysRead = it.toInt()
                }
                settingState.value = false
            }
        }) {
            Column (modifier = Modifier
                .padding(it)
                .padding(Dimensions.PADDING_SHORT)) {
                FieldText(title = stringResource(id = R.string.msm_read_num)
                , value = daysSmsReadState.value
                    , keyboardType = KeyboardOptions(keyboardType = KeyboardType.Number)
                    , callback = {
                        it.takeIf { it.isNotBlank() }?.let {
                            daysSmsReadState.value = it
                        }
                    }
                    ,modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable private fun List(viewModel: PaidViewModel,modifier: Modifier){
    val list = remember {
        viewModel.list
    }
    Column(modifier = modifier){
            list.keys.sortedByDescending { it }.forEach{
                Items(list[it],viewModel)
            }

    }
}

@Composable private fun Items(list:List<PaidDTO>?,viewModel:PaidViewModel){
    val visibleContentState = remember {
        mutableStateOf(false)
    }
    list?.takeIf { it.isNotEmpty() }?.let {
        Surface (
            border = BorderStroke(1.dp,color= MaterialTheme.colorScheme.onPrimaryContainer),
            shadowElevation = 10.dp,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(Dimensions.PADDING_SHORT)){
            Column (modifier = Modifier.padding(Dimensions.PADDING_SHORT)) {

                HeaderMonthly(period = YearMonth.of(list[0].datePaid.year,
                    list[0].datePaid.monthValue), value = list.sumOf { it.itemValue },
                    onClick = { visibleContentState.value = !visibleContentState.value })

                if(visibleContentState.value) {
                    LazyColumn {
                        items(list.size) { index ->
                            Item(dto = list[index], viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable private fun HeaderMonthly(period:YearMonth,value:Double, onClick:()->Unit){
    Row (
        modifier = Modifier.clickable { onClick.invoke() }
    ){
        FieldView(
            name = R.string.period,
            isMoney = false,
            value = period.format(
                    DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es", "CO"))
                ),
            modifier = Weight1f(),onClick =  onClick
        )

        FieldView(
            name = R.string.value,
            value = NumbersUtil.toString(value),
            modifier = Weight1f(), onClick=  onClick
        )
    }
}

@Composable private fun Item(dto:PaidDTO,viewModel:PaidViewModel){
    val menuState = remember { mutableStateOf(false) }
    val dialogState = remember { mutableStateOf(false) }

    Card(
        border = BorderStroke(1.dp,color= if(dto.recurrent) Color.Red else Color.Unspecified)
        ,modifier=Modifier.padding(Dimensions.PADDING_SHORT)) {
        Column (modifier = Modifier.padding(Dimensions.PADDING_SHORT)) {
            Row {
                Text(
                    text = stringResource(id = R.string.date_paid),
                    modifier = Weight1f().align(
                        alignment = Alignment.CenterVertically
                    )
                )

                Text(
                    text = DateUtils.localDateTimeToStringDate(dto.datePaid),
                    modifier = Weight1f().align(
                        alignment = Alignment.CenterVertically
                    )
                )

                IconButton(
                    imageVector = Icons.Rounded.MoreVert,
                    descriptionContent = co.com.japl.ui.R.string.see_more,
                    onClick = {
                        menuState.value = true
                    }, modifier = Weight1f()
                )
            }

                FieldViewCards(
                    name = R.string.name_item,
                    value = dto.itemName,
                    modifier = Modifier
                )

                FieldViewCards(
                    name = R.string.value_item,
                    value = NumbersUtil.COPtoString(dto.itemValue),
                    modifier = Modifier
                )

        }
    }

    if(menuState.value){
        MoreOptionsDialog(listOptions = viewModel.listOptions(dto)
            , onDismiss = { menuState.value = false }) {
            menuState.value = false
            when(it){
                PaidListOptions.EDIT->{
                    viewModel.edit(dto.id)
                }
                PaidListOptions.DELETE->{
                    dialogState.value = true
                }
                PaidListOptions.COPY->{
                    viewModel.copy(dto.id)
                }
                PaidListOptions.END->{
                    viewModel.endRecurrent(dto.id)
                }
            }
        }
    }
    if(dialogState.value) {
        AlertDialogOkCancel(title = R.string.dialog_delete,
            confirmNameButton = R.string.delete,
            onDismiss = { dialogState.value = false }) {
            dialogState.value = false
            viewModel.delete(dto.id)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
internal fun PaidPreview() {
    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        Paid(viewModel = viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
internal fun PaidPreviewDark() {
    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        Paid(viewModel = viewModel)
    }
}

@Composable
private fun getViewModel():PaidViewModel{
    val viewModel = PaidViewModel(paidSvc = null, accountCode = 0 , period = YearMonth.now(),prefs= null,navController = null)
    viewModel.loaderState.value = false
    viewModel.allValues.value = 1000.0
    viewModel.periodOfList.value = "June 2022"
    viewModel.list[YearMonth.now()] = arrayListOf(PaidDTO(
    id=0,
    itemName="Item 1",
    itemValue=10000.0,
    datePaid = LocalDateTime.now(),
        account = 1,
        recurrent = false,
        end=LocalDateTime.MAX
))
    viewModel.list[YearMonth.now().minusMonths(1)] = arrayListOf(PaidDTO(
        id=0,
        itemName="Item 2",
        itemValue=20000.0,
        datePaid = LocalDateTime.now().minusMonths(1),
        account = 1,
        recurrent = false,
        end=LocalDateTime.MAX
    ))
    return viewModel
}