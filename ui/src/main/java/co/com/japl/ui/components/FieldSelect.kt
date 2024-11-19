package co.com.japl.ui.components

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import co.com.alameda181.ui.R
//import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.R
import co.com.japl.ui.enums.IMoreOptions
import co.com.japl.ui.theme.MaterialThemeComposeUI

@Composable
fun FieldSelect(title:String,value:String?,list:List<IMoreOptions>?,isError:MutableState<Boolean> = mutableStateOf(false),modifier: Modifier,callable:(IMoreOptions?)->Unit){
    val context = LocalContext.current
    val state = remember { mutableStateOf(false) }
    val stateValue = remember { mutableStateOf("") }
    val stateClean = remember { mutableStateOf(false) }

    value?.takeIf { it.isNotEmpty() }?.let {
        stateValue.value = it
        stateClean.value = true
    }


    Box(modifier = modifier
        .background(color = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant)
        .border(1.dp, Color.Unspecified, shape = ShapeDefaults.ExtraLarge)
        .clickable { state.value = true }){
        Text(text = title,
            modifier
                .padding(top = 1.dp, start = 10.dp)
                .fillMaxWidth()
            , fontSize = if(stateClean.value){12.sp}else{TextUnit.Unspecified}
            ,color = if(isError.value){MaterialTheme.colorScheme.error}else{MaterialTheme.colorScheme.onSurface}
        )

        Row (modifier=Modifier.padding(top = 11.dp, start = 10.dp)){
            Text(text = stateValue.value,
                modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ,color = if(isError.value){MaterialTheme.colorScheme.error}else{MaterialTheme.colorScheme.onSurface})

            if(stateClean.value){
            IconButton(onClick = {
                stateValue.value = ""
                stateClean.value = false
                callable.invoke(null)
            }) {
                Icon(
                    imageVector = Icons.Rounded.Cancel,
                    contentDescription = stringResource(id = R.string.clear),
                    modifier = Modifier
                    ,tint = if(isError.value){MaterialTheme.colorScheme.error}else{MaterialTheme.colorScheme.onSurface}
                )
            }
            }
        }
        if(stateClean.value) {
            Divider(
                modifier = Modifier.padding(top = 60.dp),
                color = if (isError.value) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
    if(state.value){
        MoreOptionsDialog(list!!, onDismiss = { state.value = false }) {
            stateValue.value = context.getString(it.getName())
            callable.invoke(it)
            stateClean.value = true
            state.value = false }
    }
}

@Composable
fun FieldSelect(title: String, value:String, @StringRes cleanTitle:Int = R.string.clear, isError:MutableState<Boolean> = mutableStateOf(false), list:List<Pair<Int,String>>?, modifier: Modifier, callable:(Pair<Int,String>?)->Unit){
    val context = LocalContext.current
    val state = remember { mutableStateOf(false) }
    val stateValue = remember { mutableStateOf("") }
    val stateClean = remember { mutableStateOf(false) }

    value?.takeIf { it.isNotEmpty() }?.let {
        stateValue.value = it
        stateClean.value = true
    }


    Box(modifier = modifier
        .defaultMinSize(minHeight = 48.dp)
        .background(color = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant)
        .border(1.dp, Color.Unspecified, shape = ShapeDefaults.ExtraLarge)
        .clickable { state.value = true }){
        Text(text = title,
            modifier
                .padding(top = 1.dp, start = 10.dp)
                .fillMaxWidth()
            , fontSize = if(stateClean.value){12.sp}else{TextUnit.Unspecified}
            ,color = if(isError.value){MaterialTheme.colorScheme.error}else{MaterialTheme.colorScheme.onSurface}
        )

        Row (modifier=Modifier.padding(top = 11.dp, start = 10.dp)){
            Text(text = stateValue.value,
                modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                ,color = if(isError.value){MaterialTheme.colorScheme.error}else{MaterialTheme.colorScheme.onSurface})

            if(stateClean.value){
                IconButton(onClick = {
                    stateValue.value = ""
                    stateClean.value = false
                    callable.invoke(null)
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Cancel,
                        contentDescription = stringResource(id = cleanTitle),
                        modifier = Modifier
                        ,tint = if(isError.value){MaterialTheme.colorScheme.error}else{MaterialTheme.colorScheme.onSurface}
                    )
                }
            }
        }
        if(stateClean.value) {
            Divider(
                modifier = Modifier.padding(top = 60.dp),
                color = if (isError.value) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
    if(state.value){

        MoreOptionsDialogPair(list!!, onDismiss = { state.value = false }) {
            stateValue.value = it.second
            callable.invoke(it)
            stateClean.value = true
            state.value = false }

    }
}

@Composable
fun FieldSelect(title: String,
                value:String,
                isError:MutableState<Boolean> = mutableStateOf(false),
                list:SnapshotStateList<Pair<Int,String>>,
                modifier: Modifier,
                callAble:(Pair<Int,String>?)->Unit){
    val context = LocalContext.current
    val state = remember { mutableStateOf(false) }
    val stateValue = remember { mutableStateOf("") }
    val stateClean = remember { mutableStateOf(false) }

    value?.takeIf { it.isNotEmpty() }?.let {
        stateValue.value = it
        stateClean.value = true
    }


    Box(modifier = modifier
        .background(color = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant)
        .border(1.dp, Color.Unspecified, shape = ShapeDefaults.ExtraLarge)
        .clickable { state.value = true }){
        Text(text = title,
            modifier
                .padding(top = 1.dp, start = 10.dp)
                .fillMaxWidth()
            , fontSize = if(stateClean.value){12.sp}else{TextUnit.Unspecified}
            ,color = if(isError.value){MaterialTheme.colorScheme.error}else{MaterialTheme.colorScheme.onSurface}
        )

        Row (modifier=Modifier.padding(top = 11.dp, start = 10.dp)){
            Text(text = stateValue.value,
                modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                ,color = if(isError.value){MaterialTheme.colorScheme.error}else{MaterialTheme.colorScheme.onSurface})

            if(stateClean.value){
                IconButton(onClick = {
                    stateValue.value = ""
                    stateClean.value = false
                    callAble.invoke(null)
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Cancel,
                        contentDescription = stringResource(id = R.string.clear),
                        modifier = Modifier
                        ,tint = if(isError.value){MaterialTheme.colorScheme.error}else{MaterialTheme.colorScheme.onSurface}
                    )
                }
            }
        }
        if(stateClean.value) {
            Divider(
                modifier = Modifier.padding(top = 60.dp),
                color = if (isError.value) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
    if(state.value){

        MoreOptionsDialogPair(list, onDismiss = { state.value = false }) {
            stateValue.value = it.second
            callAble.invoke(it)
            stateClean.value = true
            state.value = false }

    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewFieldSelect(){
    MaterialThemeComposeUI {
        FieldSelect(title = "Title",value = "Value", list = arrayListOf(Pair(1,"Value1"),Pair(2,"Value2")), modifier = Modifier,callable={})
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewFieldSelectWithoutValue(){
    MaterialThemeComposeUI {
        FieldSelect(title = "Title",value = "", list = arrayListOf(Pair(1,"Value1"),Pair(2,"Value2")), modifier = Modifier,callable={})
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewFieldSelectDark(){
    MaterialThemeComposeUI {
        FieldSelect(title = "Title",value = "Value", list = arrayListOf(Pair(1,"Value1"),Pair(2,"Value2")), modifier = Modifier,callable={})
    }
}
