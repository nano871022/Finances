package co.com.japl.module.creditcard.views.simulator

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.controllers.simulator.SimulatorListItemViewModel
import co.com.japl.module.creditcard.views.fakeSvc.SimulatorCreditVariableFake
import co.com.japl.ui.components.MoreOptionsDialogPair
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.utils.WindowWidthSize
import co.com.japl.utils.NumbersUtil

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun SimulatorList(viewModel: SimulatorListItemViewModel) {
    if(viewModel.item.isEmpty){
        return
    }
    BoxWithConstraints {
        val size = WindowWidthSize.fromDp(maxWidth)
       MainBody(viewModel,size)
    }
}

@Composable
private fun MainBody(viewModel: SimulatorListItemViewModel,size:WindowWidthSize){
    Surface (modifier = Modifier.fillMaxWidth().padding(Dimensions.PADDING_VIEW_SPACE)) {
        Column {
            Header(viewModel,size)
            BodyDes(size)
            Body(viewModel.item.get(),size)
        }
    }
}

@Composable
private fun Header(viewModel: SimulatorListItemViewModel, size:WindowWidthSize){
    val navController = rememberNavController()
    val state = remember { viewModel.showOptions }
    Row {
        Text(
            text = stringResource(R.string.name),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
                .weight(0.3f)
                .align (alignment = Alignment.CenterVertically)
                .padding(end= Dimensions.PADDING_SHORT)
        )

        Text(
            text = viewModel.item.get().name?:"Not Found",
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
                .weight(1f)
                .align (alignment = Alignment.CenterVertically)
        )

        IconButton(
            onClick = {state.value = true},
            modifier =Modifier
        ){
            Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = "More Vertical")
        }
    }
    Row{
        Text(
            text=stringResource(R.string.months)
            ,textAlign = TextAlign.Left,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
                .align(alignment = Alignment.CenterVertically)
        )
        Text(
            text= NumbersUtil.toString(viewModel.item.get().periods.toLong()),
            textAlign = TextAlign.Left,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
                .align(alignment = Alignment.CenterVertically)
        )
        Text(
            text=stringResource(R.string.interest),
            textAlign = TextAlign.Left,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
                .align(alignment = Alignment.CenterVertically)

        )
        Text(
            text= NumbersUtil.toString(viewModel.item.get().tax),
            textAlign = TextAlign.Right,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
                .align(alignment = Alignment.CenterVertically)
                .padding(end= Dimensions.PADDING_SHORT)

        )
        if(size != WindowWidthSize.COMPACT) {
            Text(
                text = stringResource(viewModel.item.get().kindOfTax.title),
                textAlign = TextAlign.Left,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(2f)

            )
        }else{
            Text(
                text = viewModel.item.get().kindOfTax.value,
                textAlign = TextAlign.Left,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)

            )
        }
    }
    if(size == WindowWidthSize.COMPACT) {
        Row{
            Text(
                text = stringResource(R.string.credit_value),
                textAlign = TextAlign.Left,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
                    .align(alignment = Alignment.CenterVertically)
            )

            Text(
                text = NumbersUtil.COPtoString(viewModel.item.get().value),
                textAlign = TextAlign.Left,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1.5f)
                    .align(alignment = Alignment.CenterVertically)
            )
        }
    }
    Options(state=state,actionAmortization={
      viewModel.gotoAmortization(viewModel.item.get().code,navController)
    })
}

@Composable
private fun Options(state: MutableState<Boolean>, actionAmortization:()->Unit){
    if(state.value) {
        val amortizationText = stringResource(R.string.amortization)
        val list = remember {
            mutableListOf<Pair<Int, String>>(
                Pair(1, amortizationText)
            )
        }
        MoreOptionsDialogPair(
            listOptions = list,
            onDismiss = { state.value = false },
            onClick = { (code, value) ->
                when (code) {
                    1 -> actionAmortization.invoke()
                }
            }
        )
    }
}

@Composable
private fun BodyDes(size:WindowWidthSize){
    Row {
        if(size != WindowWidthSize.COMPACT) {
            Text(
                text = stringResource(R.string.credit_value),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
                    .align(alignment = Alignment.CenterVertically)
            )
        }

        Text(
            text = stringResource(R.string.first_quote_value),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
                .align (alignment = Alignment.CenterVertically)
        )

        Text(
            text = stringResource(R.string.capital_value),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
                .align (alignment = Alignment.CenterVertically)
        )

        Text(
            text = stringResource(R.string.first_interest),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
                .align (alignment = Alignment.CenterVertically)
        )
    }
    HorizontalDivider()
}

@Composable
private fun Body(dto: SimulatorCreditDTO, size:WindowWidthSize){

            Row {
                if(size != WindowWidthSize.COMPACT) {
                    Text(
                        text = NumbersUtil.COPtoString(dto.value),
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                            .align(alignment = Alignment.CenterVertically)
                    )
                }

                Text(
                    text = NumbersUtil.COPtoString(dto.quoteValue?.toDouble()?:0.0),
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                        .align (alignment = Alignment.CenterVertically)
                )

                Text(
                    text = NumbersUtil.COPtoString(dto.capitalValue?.toDouble()?:0.0),
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                        .align (alignment = Alignment.CenterVertically)
                )

                Text(
                    text = NumbersUtil.COPtoString(dto.interestValue?.toDouble()?:0.0),
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                        .align (alignment = Alignment.CenterVertically)
                )
            }
}



@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, backgroundColor = 0xffffff, uiMode = Configuration.UI_MODE_NIGHT_NO)
fun SimulatorListPreviewLight(){
    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        SimulatorList(viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, backgroundColor = 0xffffff, uiMode = Configuration.UI_MODE_NIGHT_NO,device=Devices.PIXEL)
fun SimulatorListPreviewLightPixel(){
    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        SimulatorList(viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, backgroundColor = 0xffffff, uiMode = Configuration.UI_MODE_NIGHT_NO, widthDp = 550)
fun SimulatorListPreviewLightLandscape(){
    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        SimulatorList(viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, backgroundColor = 0x000000, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun SimulatorListPreviewDark(){
    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        SimulatorList(viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, backgroundColor = 0x000000, uiMode = Configuration.UI_MODE_NIGHT_YES, device = Devices.TABLET)
fun SimulatorListPreviewDarkTablet(){
    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        SimulatorList(viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, backgroundColor = 0x000000, uiMode = Configuration.UI_MODE_NIGHT_YES, device = Devices.FOLDABLE)
fun SimulatorListPreviewDarkFold(){
    val viewModel = getViewModel()
    MaterialThemeComposeUI {
        SimulatorList(viewModel)
    }
}
@Composable
private fun getViewModel(): SimulatorListItemViewModel{
    val dto = SimulatorCreditDTO(
        code=1,
        name="Value of credito to compare",
        value = 10000000.toBigDecimal(),
        tax= 25.2,
        kindOfTax = KindOfTaxEnum.ANUAL_EFFECTIVE,
        isCreditVariable = true,
        periods = 12,
        interestValue = 205645.toBigDecimal(),
        quoteValue = 1038978.toBigDecimal(),
        capitalValue = 833333.toBigDecimal()
    )
    return SimulatorListItemViewModel(
        itemValue = dto,
        simulatorVariableSvc = SimulatorCreditVariableFake()
    )
}