package co.com.japl.module.paid.views.projections.list

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.RemoveRedEye
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.ProjectionRecap
import co.com.japl.module.paid.R
import co.com.japl.module.paid.controllers.projections.list.ProjectionsViewModel
import co.com.japl.ui.components.CardValues
import co.com.japl.ui.components.Carousel
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import java.math.BigDecimal
import java.time.LocalDate

@Composable
fun Projections(viewModel: ProjectionsViewModel){
    val progressStatus = remember { viewModel.loadingStatus }

    if (progressStatus.value) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }else {
        Scafold(viewModel=viewModel)
    }
}

@Composable
private fun Scafold(viewModel: ProjectionsViewModel){
    Scaffold (
        floatingActionButton = {
            FloatButton(viewModel)
        }
    ) {
        Body(viewModel=viewModel,modifier = Modifier.padding(it))
    }
}

@Composable
private fun Body(viewModel: ProjectionsViewModel,modifier: Modifier){
    val list = remember { viewModel.projectionsList }
  Column (modifier = modifier.padding(Dimensions.PADDING_SHORT)){
      Header(viewModel = viewModel)

      Carousel(
          size = list.size,
          modifier = Modifier.height(200.dp)
      ) { pos ->
          list[pos].let {
              when (pos) {
                  0 -> Card(titleCard = R.string.projection_closed,it)
                  1 -> Card(titleCard = R.string.projection_far,it)
              }
          }
      }

  }
}

@Composable
private fun Card(@StringRes titleCard:Int, projection: ProjectionRecap){
    CardValues (title = stringResource(titleCard)){
            Column {
                Row {
                    FieldView(
                        title = stringResource(R.string.limite_date),
                        value = DateUtils.localDateToStringDate(projection.limitDate),
                        modifier = Modifier.weight(1f).padding(end = Dimensions.PADDING_SHORT)
                    )

                    FieldView(
                        title = stringResource(R.string.months_left_to_pay),
                        value = projection.monthsLeft.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }

                FieldView(
                    title = stringResource(R.string.saved_cash),
                    value = NumbersUtil.COPtoString(projection.savedCash),
                    modifier = Modifier.padding(top = Dimensions.PADDING_SHORT).fillMaxWidth()
                )
            }

    }
}

@Composable
private fun Header(viewModel: ProjectionsViewModel){
    Row (modifier = Modifier.fillMaxWidth()){

        FieldView(
            title = stringResource(R.string.products_count),
            value = viewModel.totalCount.valueStr,
            modifier = Modifier.weight(1f).padding(end  = Dimensions.PADDING_SHORT)
        )

        FieldView(
            title = stringResource(R.string.products_cost),
            value = NumbersUtil.COPtoString(viewModel.totalSaved.value),
            modifier = Modifier.weight(1f)
        )

    }
}

@Composable
private fun FloatButton(viewModel: ProjectionsViewModel){
    Column(){
        FloatButton(
            imageVector = Icons.Rounded.RemoveRedEye,
            descriptionIcon = R.string.list_projection
        ) {
            viewModel.goToList()
        }

        FloatButton(
            imageVector = Icons.Rounded.Add,
            descriptionIcon = R.string.add_projection
        ) {
            viewModel.goToCreate()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO, backgroundColor = 0x000000)
fun PreviewLight(){
    MaterialThemeComposeUI {
        Projections(getViewModel())
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES, backgroundColor = 0xffffff)
fun PreviewDark(){
    MaterialThemeComposeUI {
        Projections(getViewModel())
    }
}

@Composable
fun getViewModel(): ProjectionsViewModel{
    val vm =  ProjectionsViewModel()
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