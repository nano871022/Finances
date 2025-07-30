package co.com.japl.ui.components

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.icu.text.DecimalFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProvider
import co.com.japl.ui.model.datatable.Header
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions

@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataTable(
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    listHeader: (Dp) -> List<Header>?,
    sizeBody: Int,
    splitPos: Int = 0,
    highlightPos:Int = -1,
    footer: @Composable RowScope.(Dp) -> Unit?,
    split: @Composable (Int,Dp) -> Unit? = {_,_->},
    listBody: @Composable RowScope.(Int, Dp) -> Unit

) {
    BoxWithConstraints {
        val width = maxWidth
        Column {
            Header(textColor, listHeader(width))
            Body(textColor, sizeBody, width, splitPos,highlightPos,footer, split,listBody)
        }
    }
}

@Composable
private fun Body(
        textColor: Color,
        sizeBody: Int,
        maxWidth:Dp,
        splitPos: Int,
        highlightPos:Int,
        footer: @Composable RowScope.(Dp) -> Unit?,
        splitView: @Composable (Int,Dp) -> Unit?,
        listBody: @Composable RowScope.(Int,Dp) -> Unit){
    LazyColumn(
        state = rememberLazyListState(), modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        items(sizeBody) { index ->
            if(splitPos > 0 && (index % splitPos ) == 0 ) {

                Surface(modifier=Modifier.fillMaxWidth().padding(top= Dimensions.PADDING_TOP)
                    .border(border = BorderStroke(1.dp, textColor),
                        shape = RoundedCornerShape(topStart =  10.dp, topEnd = 10.dp) ).padding(Dimensions.PADDING_SHORT)) {
                    splitView(index, maxWidth)
                }
            }
            BodyRow(index,textColor,maxWidth,highlightPos,listBody)
        }
        item {
            Row(modifier = Modifier.fillMaxWidth().padding(bottom=Dimensions.PADDING_BOTTOM)) {
                footer(maxWidth)
            }
        }
    }
}

@Composable
private fun BodyRow( index:Int,textColor: Color,maxWidth:Dp,highlightPos:Int,listBody: @Composable RowScope.(Int,Dp) -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .background(color=if(index == highlightPos) MaterialTheme.colorScheme.surfaceContainer else Color.Transparent)
    ) {
        Text(
            text = "${index + 1}",
            color = textColor,
            modifier = Modifier
                .padding(end = 5.dp)
                .align(alignment = Alignment.CenterVertically)
        )

        listBody(index,maxWidth)
    }
    HorizontalDivider()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Header( textColor: Color,listHeader: List<Header>?){
    val tooltipStateName = remember { mutableStateListOf<TooltipState>() }
    val tooltipPosition = remember { mutableStateListOf<PopupPositionProvider>() }
    if (listHeader != null) {
        listHeader.forEachIndexed { index, _ ->
            tooltipStateName.add(index, TooltipState(false))
            tooltipPosition.add(index, TooltipDefaults.rememberPlainTooltipPositionProvider())
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            HeaderRow(
                textColor = textColor,
                listHeader = listHeader,
                tooltipStateName = tooltipStateName,
                tooltipPosition = tooltipPosition
            )
        }
        HorizontalDivider()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RowScope.HeaderRow(
    textColor: Color,
    listHeader:List<Header>,
    tooltipStateName: MutableList<TooltipState>,
    tooltipPosition: MutableList<PopupPositionProvider>){
    Text(text = "#",
        color = textColor,
        modifier = Modifier.padding(5.dp)
            .align (alignment = Alignment.CenterVertically))

    listHeader.forEachIndexed { index, it ->

        Row(modifier = Modifier.weight(it.weight)) {
            Text(
                text = it.title,
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
                    .align (alignment = Alignment.CenterVertically)
            )
            TooltipBox(
                positionProvider = tooltipPosition[index],
                tooltip = {
                    ElevatedCard {
                        Text(
                            text = it.tooltip,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                },
                state = tooltipStateName[index],
                modifier = Modifier.align (alignment = Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = it.tooltip,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview( showBackground = true, showSystemUi = true, backgroundColor = 0xFFFFFFFF, uiMode = 0x1)
private fun DatatableLight(){
    val listHeader = getHeaders()
    val listValue = getBody()
    MaterialThemeComposeUI {
        Column {
            DataTable(
                listHeader = { _->listHeader },
                sizeBody = listValue.size,
                splitPos = 12,
                split = {v1,v2->
                    Text(text=" Split Space $v1 $v2")
                },
                footer = {}
            ) { pos, maxWidth ->

                Text(text = listValue[pos].first, modifier = Modifier.weight(1f))
                Text(text = listValue[pos].second, modifier = Modifier.weight(1f))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview( showBackground = true, showSystemUi = true, backgroundColor = 0x00000000, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun DatatableDark(){
    val listHeader = getHeaders()
    val listValue = getBody()
    MaterialThemeComposeUI {
        Column {
            DataTable(
                listHeader = { _->listHeader },
                sizeBody = listValue.size,
                splitPos = 12,
                split = {v1,v2->
                    Text(text=" Split Space $v1 $v2")
                },
                footer = {}
            ) { pos, maxWidth ->

                Text(text = listValue[pos].first, modifier = Modifier.weight(1f))
                Text(text = listValue[pos].second, modifier = Modifier.weight(1f))
            }
        }
    }
}

private fun getHeaders():List<Header>{
    return listOf(Header(
        title="Colum1",
        tooltip = "Column 1",
        weight=1f
    ),Header(
        title="Colum2",
        tooltip = "Column 2",
        weight=1f
    ))
}
private fun getBody():List<Pair<String,String>>{
    return listOf<Pair<String,String>>(
        Pair("Value 1","value 2"),
        Pair("Value 3","value 4"),
        Pair("Value 5","value 6"),
        Pair("Value 7","value 8"),
        Pair("Value 7","value 8"),
        Pair("Value 7","value 8"),
        Pair("Value 7","value 8"),
        Pair("Value 7","value 8"),
        Pair("Value 7","value 8"),
        Pair("Value 7","value 8"),
        Pair("Value 7","value 8"),
        Pair("Value 7","value 8"),
        Pair("Value 7","value 8"),
        Pair("Value 7","value 8"),
        Pair("Value 7","value 8"),
        Pair("Value 7","value 8"),
        Pair("Value 7","value 8"),
        Pair("Value 7","value 8"),
        Pair("Value 7","value 8"),
        Pair("Value 7","value 8")

    )
}