package co.com.japl.module.paid.views.projections.list

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.RemoveRedEye
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.ProjectionRecap
import co.com.japl.module.paid.R
import co.com.japl.module.paid.controllers.projections.list.ProjectionsViewModel
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1f
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1fAndPaddintRightSpace
import co.com.japl.ui.utils.NumbersUtil
import androidx.lifecycle.SavedStateHandle
import java.math.BigDecimal
import java.time.LocalDate

@Composable
fun Projections(viewModel: ProjectionsViewModel){
    Scaffold (
        floatingActionButton = {
            Buttons(viewModel)
        }
    ){
        Column(modifier = Modifier.padding(it)) {
            Header(viewModel)
            Body(viewModel)
        }
    }
}

@Composable
private fun Buttons(viewModel: ProjectionsViewModel){
    Column {
        FloatButton(imageVector = Icons.Rounded.RemoveRedEye, descriptionIcon = R.string.go_to_list) {
            viewModel.goToList()
        }
        FloatButton(imageVector = Icons.Rounded.Add, descriptionIcon = R.string.add_projection) {
            viewModel.goToCreate()
        }
    }
}

@Composable
private fun Header(viewModel: ProjectionsViewModel){
    val totalCount = viewModel.totalCount.value.collectAsState()
    val totalSaved = viewModel.totalSaved.value.collectAsState()

    Row(modifier = Modifier.padding(Dimensions.PADDING_SHORT)) {
        FieldView(
            name = stringResource(id = R.string.count_projection),
            value = totalCount.value.toString(),
            modifier = Weight1fAndPaddintRightSpace(),
            isMoney = false
        )

        FieldView(
            name = stringResource(id = R.string.total_saved),
            value = NumbersUtil.toString(totalSaved.value?.toDouble()?:0.0),
            modifier = Weight1f()
        )
    }
}

@Composable
private fun Body(viewModel: ProjectionsViewModel){
    val list = viewModel.projectionsList

    Column(modifier = Modifier.padding(Dimensions.PADDING_SHORT)) {
        list.forEach {
            ProjectionItem(it)
        }
    }
}

@Composable
private fun ProjectionItem(recap: ProjectionRecap){
    androidx.compose.material3.Card(modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth()) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = recap.limitDate.toString(), fontWeight = FontWeight.Bold)
            Row {
                Text(text = "Saved: ", modifier = Modifier.weight(1f))
                Text(text = NumbersUtil.toString(recap.savedCash.toDouble()))
            }
            Row {
                Text(text = "Months left: ", modifier = Modifier.weight(1f))
                Text(text = recap.monthsLeft.toString())
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
fun ProjectionsPreview(){
    MaterialThemeComposeUI {
        Projections(getViewModel())
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun ProjectionsPreviewDark(){
    MaterialThemeComposeUI {
        Projections(getViewModel())
    }
}

@Composable
fun getViewModel(): ProjectionsViewModel{
    val vm =  ProjectionsViewModel(SavedStateHandle(), null, null)
    vm.projectionsList.add(ProjectionRecap(
        limitDate = LocalDate.now(),
        savedCash = BigDecimal.TEN,
        monthsLeft = 10
    ))
    vm.projectionsList.add(ProjectionRecap(
        limitDate = LocalDate.now().minusMonths(1),
        savedCash = BigDecimal.valueOf(500_000),
        monthsLeft = 5
    ))
    vm.totalCount.onValueChange(10)
    vm.totalSaved.onValueChange(BigDecimal.valueOf(1_000_000))

    return vm
}
