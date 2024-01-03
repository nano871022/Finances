package co.com.japl.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun CardValues(title:String, values:@Composable() ()->Unit){
    Card (modifier= Modifier
        .padding(10.dp)
        .fillMaxWidth()
        , elevation = CardDefaults.cardElevation(10.dp)) {
        Title(title)

        values.invoke()
    }
}



@Composable
private fun Title(title:String){
    Row(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()) {

        Text(
            text = title,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.fillMaxWidth()
        )
    }
}