package co.com.japl.module.creditcard.views.simulator

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import android.util.Log
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
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.controllers.simulator.SimulatorListItemViewModel
import co.com.japl.ui.components.MoreOptionsDialogPair
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.utils.WindowWidthSize
import co.com.japl.ui.utils.NumbersUtil
import java.math.BigDecimal

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun SimulatorList(viewModel: SimulatorListItemViewModel) {
    val dto by remember { viewModel.item }
    if(dto == null){
        return
    }
    BoxWithConstraints {
        val size = WindowWidthSize.fromDp(maxWidth)
       MainBody(viewModel,size)
    }
}

@Composable
private fun MainBody(viewModel: SimulatorListItemViewModel,size:WindowWidthSize){
    val dto by remember { viewModel.item }
    Card (modifier = Modifier.fillMaxWidth().padding(Dimensions.PADDING_VIEW_SPACE)) {
        Column (modifier = Modifier.padding(Dimensions.PADDING_TOP)){
            Header(viewModel,size)
            BodyDes(size)
            Body(dto,size)
        }
    }
}

@Composable
private fun Header(viewModel: SimulatorListItemViewModel, size:WindowWidthSize){
    val state = remember { viewModel.showOptions }
    val dto by remember { viewModel.item }
    Log.d("CreditCard.SimulatorList","Item/DTO: $dto")
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
            text = dto?.name?:"Not Found",
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
                .weight(1f)
                .align (alignment = Alignment.CenterVertically)
        )

        IconButton(
            onClick = {state.value = true},
            modifier =Modifier
        ){
            Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = stringResource(R.string.see_more))
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
            text= NumbersUtil.toString(dto?.periods?.toLong()?:0),
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
            text= NumbersUtil.toString(dto?.tax?:0.0),
            textAlign = TextAlign.Right,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
                .align(alignment = Alignment.CenterVertically)
                .padding(end= Dimensions.PADDING_SHORT)

        )
        if(size != WindowWidthSize.COMPACT) {
            Text(
                text = stringResource(dto?.kindOfTax?.title!!),
                textAlign = TextAlign.Left,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(2f)

            )
        }else{
            Text(
                text = dto?.kindOfTax?.value?: KindOfTaxEnum.ANUAL_EFFECTIVE.value,
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
                text = NumbersUtil.COPtoString(dto?.value?: BigDecimal.ZERO),
                textAlign = TextAlign.Left,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1.5f)
                    .align(alignment = Alignment.CenterVertically)
            )
        }
    }
    Options(state=state,actionAmortization={
      viewModel.gotoAmortization(dto?.code?:0)
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
            onClick = { (code, _) ->
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
private fun Body(dto: SimulatorCreditDTO?, size:WindowWidthSize){

            Row {
                if(size != WindowWidthSize.COMPACT) {
                    Text(
                        text = NumbersUtil.COPtoString(dto?.value?: BigDecimal.ZERO),
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                            .align(alignment = Alignment.CenterVertically)
                    )
                }

                Text(
                    text = NumbersUtil.COPtoString(dto?.quoteValue?.toDouble()?:0.0),
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                        .align (alignment = Alignment.CenterVertically)
                )

                Text(
                    text = NumbersUtil.COPtoString(dto?.capitalValue?.toDouble()?:0.0),
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                        .align (alignment = Alignment.CenterVertically)
                )

                Text(
                    text = NumbersUtil.COPtoString(dto?.interestValue?.toDouble()?:0.0),
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
    return SimulatorListItemViewModel(LocalContext.current,itemValue = dto)
}