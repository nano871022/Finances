package co.japl.android.myapplication.finanzas.view.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Carousel(size:Int,delayMills:Long=10_000,modifier:Modifier=Modifier,components:@Composable() (Int)->Unit){
    val state = rememberPagerState(pageCount = {size})
    val isDragged by state.interactionSource.collectIsDraggedAsState()
    if(isDragged.not()){
        with(state){
            var currentPage by remember { mutableIntStateOf(0) }
            LaunchedEffect(key1 = currentPage){
                delay(delayMills)
                val nextPage = (currentPage + 1) % pageCount
                animateScrollToPage(nextPage)
                currentPage = nextPage
            }
        }
    }

    Box {
        HorizontalPager(state = state
            ,modifier = modifier) {

            components.invoke(it)
        }
        DotIndicator(pageCount = size
            , pagerState = state
            , modifier = Modifier.align(Alignment.BottomCenter))
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
