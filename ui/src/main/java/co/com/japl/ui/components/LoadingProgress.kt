package co.com.japl.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.R
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun LoadingProgress(
    @StringRes message:Int,
    showProgress: MutableState<Boolean>,
    execute:()->Unit,
    validateData:()->Boolean={true},
    @StringRes messageNoData:Int?=null,
    body:@Composable ()->Unit){

    if(showProgress.value) {
        execute()
        Column(modifier = Modifier.fillMaxWidth()) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = stringResource(message),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }else if(!validateData() && messageNoData!=null){
        Text(
            text = stringResource(messageNoData),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth()
        )
    }else{
        body()
    }
}