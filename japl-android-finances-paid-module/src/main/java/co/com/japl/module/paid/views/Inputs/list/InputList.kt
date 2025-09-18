package co.com.japl.module.paid.views.Inputs.list

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.module.paid.R
import co.com.japl.module.paid.controllers.Inputs.list.InputListModelView
import co.com.japl.ui.components.Carousel
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1f
import co.com.japl.utils.NumbersUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

@Composable
fun InputList(modelView: InputListModelView){
    val stateLoader = remember { modelView.stateLoader }
    val stateProgress = remember { modelView.progress }
    val scope = rememberCoroutineScope()
    scope.launch {
        withContext(Dispatchers.IO) {
            modelView.main()
        }
    }
        if(stateLoader.value) {
            LinearProgressIndicator(
                progress = { stateProgress.value },
            )
        }else {
            Scaffold (
                floatingActionButton = {
                    FloatingActionButton(onClick = {
                        modelView.goToInputForm()
                    }, elevation = FloatingActionButtonDefaults.elevation(10.dp),
                        backgroundColor =  MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        modifier=Modifier.padding(bottom=Dimensions.PADDING_BOTTOM_SPACE_FLOATING_BUTTON)){
                        Icon(imageVector = Icons.Rounded.AttachMoney, contentDescription = "Add input to account")
                    }
                }
            ){
                Content(modelView = modelView,modifier=Modifier.padding(it))
            }

        }
}

@Composable
private fun Content(modelView: InputListModelView, modifier:Modifier) {
    val stateOptions = remember { modelView.stateDialogOptionsMore }
    val stateOptionsId = remember { mutableIntStateOf(0) }
    val stateList = remember { modelView._items}

    Column (modifier=Modifier.verticalScroll(rememberScrollState())) {
        InputListHeader(
            modelView._items.size.toLong(),
            modelView._items.sumOf { it.value.toDouble() },
            modelView._items.map{
                when(it.kindOf ) {
                    "Mensual" -> it.value.toDouble() * 6
                    "Semestral"-> it.value.toDouble() * 1
                    else -> 0.0
                }
            }.sum(),
            modelView._items.map{
                when(it.kindOf ) {
                    "Mensual" -> it.value.toDouble() * 12
                    "Semestral"-> it.value.toDouble() * 2
                    else -> it.value.toDouble()
                }
            }.sum())

        stateList.forEach{item->
            InputItem(
                date = item.date,
                nameInput = item.name,
                valueInput = item.value.toDouble()
            ) {
                stateOptionsId.value = item.id
                stateOptions.value = !stateOptions.value
            }
        }

            MoreOptionsItemsInputList(stateOptionsId, modelView = modelView)
    }
}

@Composable
private fun InputItem(date:LocalDate,nameInput:String,valueInput:Double,onClick:()->Unit){
    Card(modifier = Modifier.padding(bottom=Dimensions.PADDING_SHORT)) {
        Row(verticalAlignment = Alignment.CenterVertically
        , modifier = Modifier.padding(5.dp)){

            Column (modifier = Modifier.weight(1f)){
                Text(text = stringArrayResource(id = R.array.months_short)[date.monthValue])
                Text(text = "${date.dayOfMonth} ${date.year}")
            }

            Column (modifier = Modifier.weight(1f)) {
                Text(text = stringResource(id = R.string.name))

                Text(text = nameInput)
            }

            Column (modifier = Modifier.weight(1f)) {
                Text(text = stringResource(id = R.string.value))

                Text(text = NumbersUtil.COPtoString(valueInput))
            }

            IconButton(onClick = onClick) {
                Icon( imageVector = Icons.Rounded.MoreVert, contentDescription = "more options")
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun InputListHeader(numInputs:Long,totalInputs:Double,totalMonthly:Double,totalYear:Double){
    Carousel(modifier=Modifier.padding(bottom = Dimensions.PADDING_BOTTOM_CAROUSEL_SPACE),size = 2) {
        if(it == 0){
            Row{
                FieldView(name = R.string.num_inputs, value = numInputs.toString(), isMoney=false, modifier = Weight1f())

                FieldView(name = R.string.input_total, value = NumbersUtil.toString(totalInputs), modifier = Weight1f())
            }
        }else{
            Row {
                FieldView(
                    name = R.string.monthly_input_total,
                    value = NumbersUtil.COPtoString(totalMonthly),
                    modifier = Weight1f()
                )
                FieldView(
                    name = R.string.yearly_input_total,
                    value = NumbersUtil.COPtoString(totalYear),
                    modifier = Weight1f()
                )
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
internal fun InputListPreview(){
    MaterialThemeComposeUI {
        InputList(getViewModel())
    }
}

@Composable
private fun getViewModel():InputListModelView{
    val context = LocalContext.current
    val viewModel = InputListModelView(context = context, accountCode = 0,null,null)
    viewModel.stateDialogOptionsMore.value = false
    viewModel.stateLoader.value = false
    return viewModel
}
