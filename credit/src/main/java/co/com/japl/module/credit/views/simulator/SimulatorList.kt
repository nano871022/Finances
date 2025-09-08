package co.com.japl.module.credit.views.simulator

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.module.credit.R
import co.com.japl.module.credit.controllers.simulator.SimulatorListItemViewModel
import co.com.japl.ui.components.MoreOptionsDialogPair
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.utils.WindowWidthSize
import co.com.japl.utils.NumbersUtil

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun SimulatorList(viewModel: SimulatorListItemViewModel){
    if(viewModel.item.isEmpty){
        return
    }
    BoxWithConstraints {
        val size = WindowWidthSize.fromDp(maxWidth)
        MainBody(viewModel,size)
    }
}

@Composable
private fun MainBody(viewModel: SimulatorListItemViewModel,size: WindowWidthSize){
    Surface(
        modifier= Modifier.padding(Dimensions.PADDING_VIEW_SPACE)
    ){
        Column {
            Header(viewModel,size)
            BodyHeader(size)
            Body(viewModel,size)
        }
    }
}

@Composable
private fun Header(viewModel: SimulatorListItemViewModel,size: WindowWidthSize){
    val stateOptions = remember { mutableStateOf(false) }
    Row{
        Text(
            text = stringResource(R.string.name),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align (Alignment.CenterVertically).weight(0.6f)
        )
        Text(
            text = "Name of comparetion record",
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align (Alignment.CenterVertically).weight(2f)
        )

        IconButton(onClick = {stateOptions.value = true}){
            Icon(imageVector = Icons.Rounded.MoreVert,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = stringResource(R.string.see_more))
        }
    }
    Row{

        Text(
            text = stringResource(R.string.months),
            color=MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
                .align (alignment = Alignment.CenterVertically)
        )
        Text(
            text = NumbersUtil.toString(12.0),
            color=MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
                .align (alignment = Alignment.CenterVertically)
        )
        Text(
            text = stringResource(R.string.interest_short),
            color=MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
                .align (alignment = Alignment.CenterVertically)
        )
        Text(
            text = NumbersUtil.toString(25.5),
            color=MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Right,
            modifier = Modifier.weight(1f)
                .align (alignment = Alignment.CenterVertically)
                .padding(end=Dimensions.PADDING_SHORT)
        )
        Text(
            text = KindOfTaxEnum.MONTHLY_EFFECTIVE.value,
            color=MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
                .align (alignment = Alignment.CenterVertically)
        )

    }
    if(size == WindowWidthSize.COMPACT){
        Row {
            Text(
                text = stringResource(R.string.credit_value),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
                    .align(alignment = Alignment.CenterVertically)
            )
            Text(
                text = NumbersUtil.COPtoString(1200000.toDouble()),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
                    .align(alignment = Alignment.CenterVertically)
            )
        }
    }
    Options(viewModel,stateOptions)
}

@Composable
private fun Options(viewModel: SimulatorListItemViewModel,state: MutableState<Boolean>){
    if(state.value) {
        val amortizationTitle = stringResource(R.string.amortization)
        val list = remember { mutableListOf<Pair<Int, String>>(
            Pair(1,amortizationTitle)
        ) }
        MoreOptionsDialogPair(
            listOptions = list,
            onDismiss = {state.value = false},
            onClick = { (code, key) ->
                state.value = false
                when(code){
                    1 -> viewModel.gotoAmortization(viewModel.item.get())
                }

            }
        )
    }
}

@Composable
private fun BodyHeader(size: WindowWidthSize){
    Row(modifier = Modifier.padding(top= Dimensions.PADDING_TOP)) {
        if(size != WindowWidthSize.COMPACT) {
            Text(
                text = stringResource(R.string.credit_value),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
                    .align(alignment = Alignment.CenterVertically)
            )
        }
        Text(
            text = stringResource(R.string.quote_value_short),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
                .align(alignment = Alignment.CenterVertically)
        )
        Text(
            text = stringResource(R.string.first_capital),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
                .align(alignment = Alignment.CenterVertically)
        )
        Text(
            text = stringResource(R.string.first_interest),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
                .align(alignment = Alignment.CenterVertically)
        )
    }
    HorizontalDivider()
}

@Composable
private fun Body(viewModel: SimulatorListItemViewModel,size: WindowWidthSize){
    Row {
        if(size != WindowWidthSize.COMPACT) {
            Text(
                text = NumbersUtil.COPtoString(viewModel.item.get().value),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Right,
                modifier = Modifier.weight(1f)
                    .align(alignment = Alignment.CenterVertically)
            )
        }
        Text(
            text = NumbersUtil.COPtoString(viewModel.item.get().quoteValue?.toDouble()?:0.0),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Right,
            modifier = Modifier.weight(1f)
                .align(alignment = Alignment.CenterVertically)
        )
        Text(
            text = NumbersUtil.COPtoString(viewModel.item.get().capitalValue?.toDouble()?:0.0),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Right,
            modifier = Modifier.weight(1f)
                .align(alignment = Alignment.CenterVertically)
        )
        Text(
            text = NumbersUtil.COPtoString(viewModel.item.get().interestValue?.toDouble()?:0.0),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Right,
            modifier = Modifier.weight(1f)
                .align(alignment = Alignment.CenterVertically)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, backgroundColor = 0xffffff, uiMode = Configuration.UI_MODE_NIGHT_NO)
private fun SimulatorListPreviewLight(){
    val viewModel = getViewModelItem()
    MaterialThemeComposeUI {
        SimulatorList(
            viewModel
        )
    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, backgroundColor = 0xffffff, uiMode = Configuration.UI_MODE_NIGHT_NO,device=Devices.FOLDABLE)
private fun SimulatorListPreviewLightFold(){
    val viewModel = getViewModelItem()
    MaterialThemeComposeUI {
        SimulatorList(
            viewModel
        )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, backgroundColor = 0x000000, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun SimulatorListPreviewDark(){
    val viewModel = getViewModelItem()
    MaterialThemeComposeUI {
        SimulatorList(
            viewModel
        )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, backgroundColor = 0x000000, uiMode = Configuration.UI_MODE_NIGHT_YES, device = Devices.TABLET)
private fun SimulatorListPreviewDarkTablet(){
    val viewModel = getViewModelItem()
    MaterialThemeComposeUI {
        SimulatorList(
            viewModel
        )
    }
}

private fun getViewModelItem(): SimulatorListItemViewModel{
    val dto = SimulatorCreditDTO(
        code = 1,
        name = "name large test for record",
        value = 10000000.toBigDecimal(),
        periods = 12,
        tax = 25.5,
        kindOfTax = KindOfTaxEnum.ANUAL_EFFECTIVE,
        isCreditVariable = false,
        interestValue = 205645.toBigDecimal(),
        capitalValue = 833333.toBigDecimal(),
        quoteValue = 1038978.toBigDecimal(),
    )
    return SimulatorListItemViewModel(
        dto
    )
}