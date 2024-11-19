package co.com.japl.ui.components

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
//import co.com.alameda181.ui.R
//import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.R
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Carousel(size:Int,delayMills:Long=20_000,modifier:Modifier=Modifier,pagerSize: PageSize=PageSize.Fill,components:@Composable() (Int)->Unit){
    if(size > 0) {
        val state = rememberPagerState(pageCount = { size })
        val isDragged by state.interactionSource.collectIsDraggedAsState()
        if (isDragged.not()) {
            with(state) {
                var currentPage by remember { mutableIntStateOf(0) }
                LaunchedEffect(key1 = currentPage) {
                    delay(delayMills)
                    val nextPage = (currentPage + 1) % pageCount
                    animateScrollToPage(nextPage)
                    currentPage = nextPage
                }
            }
        }

        Box {
            HorizontalPager(
                state = state, pageSize = pagerSize, modifier = modifier
            ) {

                components.invoke(it)
            }
            DotIndicator(
                pageCount = size,
                pagerState = state,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }else{
        Column(modifier= Modifier
            .padding(Dimensions.PADDING_SHORT)
            .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(id = R.string.any_data_to_show))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DotIndicator(pageCount: Int, pagerState: PagerState, modifier: Modifier) {

    Row(modifier=modifier){
        repeat(pageCount){
            val color = if(pagerState.currentPage == it) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primaryContainer

            Box(
                modifier = modifier
                    .padding(top = 10.dp, start = 2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .padding(8.dp)
            ){

            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
internal fun CarouselPreview(){
    MaterialThemeComposeUI {
        Carousel(size=6, delayMills = 3_000){pos->
            Column (modifier = Modifier.padding(Dimensions.PADDING_SHORT).fillMaxWidth())  {
                when (pos) {
                    0 -> Text(text = "Example 1")
                    1 -> Text(text = "Example 2")
                    2 -> Text(text = "Example 3")
                    3 -> Text(text = "Example 4")
                    4 -> Text(text = "Example 5")
                    5 -> Text(text = "Example 6")
                    else -> Text(text = "Example 7 $pos")
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
internal fun CarouselPreviewDark(){
        MaterialThemeComposeUI (darkTheme = true){
            Column {
                Carousel(size = 6, delayMills = 3_000) { pos ->
                Column(modifier = Modifier.padding(Dimensions.PADDING_SHORT).fillMaxWidth()) {
                    when (pos) {
                        0 -> Text(text = "Example 1")
                        1 -> Text(text = "Example 2")
                        2 -> Text(text = "Example 3")
                        3 -> Text(text = "Example 4")
                        4 -> Text(text = "Example 5")
                        5 -> Text(text = "Example 6")
                        else -> Text(text = "Example 7 $pos")
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
internal fun CarouselPreviewSize0(){
    MaterialThemeComposeUI {
        Carousel(size=0, delayMills = 3_000){pos->
            Column (modifier = Modifier.padding(Dimensions.PADDING_SHORT).fillMaxWidth())  {
                when (pos) {
                    0 -> Text(text = "Example 1")
                    1 -> Text(text = "Example 2")
                    2 -> Text(text = "Example 3")
                    3 -> Text(text = "Example 4")
                    4 -> Text(text = "Example 5")
                    5 -> Text(text = "Example 6")
                    else -> Text(text = "Example 7 $pos")
                }
            }
        }
    }
}