package co.com.japl.ui.components

import android.icu.text.DecimalFormat
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProvider
import co.com.japl.ui.model.datatable.Header

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataTable(textColor:Color=MaterialTheme.colorScheme.onBackground,
              listHeader:List<Header>?,
              sizeBody:Int,
              footer:@Composable RowScope.()->Unit?,
              listBody:@Composable RowScope.(Int)->Unit){

    val tooltipStateName = remember {mutableStateListOf<TooltipState>()}
    val tooltipPosition = remember {mutableStateListOf<PopupPositionProvider>()}

    if( listHeader != null) {
        listHeader.forEachIndexed { index, _ ->
            tooltipStateName.add(index, TooltipState(false))
            tooltipPosition.add(index,TooltipDefaults.rememberPlainTooltipPositionProvider())
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(text = "#", color = textColor, modifier = Modifier.padding(5.dp))

            listHeader.forEachIndexed { index, it ->

                Row(modifier = Modifier.weight(it.weight)) {
                    Text(
                        text = it.title,
                        color = textColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
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
                        state = tooltipStateName[index]
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
        HorizontalDivider()
    }


    LazyColumn(
        state = rememberLazyListState(), modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        items(sizeBody) { index ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Text(
                    text = "${index + 1}",
                    color = textColor,
                    modifier = Modifier.padding(end = 5.dp).align(alignment = Alignment.CenterVertically)
                )

                listBody(index)
            }
            HorizontalDivider()
        }
        if(footer != null) {
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    footer()
                }
            }
        }
    }
}