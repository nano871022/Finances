package co.japl.android.myapplication.finanzas.view.accounts.inputs.lists

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.view.components.FieldView
import co.japl.android.myapplication.utils.NumbersUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

@Composable
fun InputList(modelView: InputListModelView){
    val stateLoader = remember { modelView.stateLoader }
    val scope = rememberCoroutineScope()
    scope.launch {
        withContext(Dispatchers.IO) {
            modelView.main()
        }
    }
        if(stateLoader.value.not()) {
            LinearProgressIndicator()
        }else {
            Scaffold (
                floatingActionButton = {
                    IconButton(onClick = {
                        modelView.goToInputForm()
                    }){
                        Icon(painter = painterResource(id = R.drawable.ic_baseline_attach_money_24), contentDescription = "Add input to account")
                    }
                }
            ){
                Content(modelView = modelView,modifier=Modifier.padding(it))
            }

        }
}

@Composable
private fun Content(modelView: InputListModelView,modifier:Modifier) {
    val stateOptions = remember { modelView.stateDialogOptionsMore }
    val stateOptionsId = remember { mutableIntStateOf(0) }

    Column {
        InputListHeader(
            modelView.items.size.toLong(),
            modelView.items.sumOf { it.value.toDouble() })

        for (item in modelView.items) {
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
    Card(modifier = Modifier.padding(2.dp), elevation = CardDefaults.elevatedCardElevation(defaultElevation = 15.dp)) {
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
                Icon( painter = painterResource(id = R.drawable.more_vertical), contentDescription = "more options")
            }
        }
    }

}

@Composable
private fun InputListHeader(numInputs:Long,totalInputs:Double){
    Row{
        FieldView(name = R.string.num_inputs, value = numInputs.toString(), modifier = Modifier.weight(1f))

        FieldView(name = R.string.input_total, value = NumbersUtil.COPtoString(totalInputs), modifier = Modifier.weight(1f))
    }
}
