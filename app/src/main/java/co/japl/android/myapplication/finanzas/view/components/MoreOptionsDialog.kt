package co.japl.android.myapplication.finanzas.view.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.enums.IMoreOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreOptionsDialog(listOptions:List<IMoreOptions>,onDismiss:()->Unit,onClick: (IMoreOptions) -> Unit){
    AlertDialog(onDismissRequest = onDismiss) {
        Surface{
            Column(modifier = Modifier.fillMaxWidth()
                , horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(id = R.string.see_more), modifier = Modifier.padding(5.dp), fontSize = 18.sp)
                Divider()
                for ( item in listOptions) {
                    Options(name = item.getName(),onClick = { onClick(item) })
                }
            }
        }
    }
}

@Composable
private fun Options(@StringRes name:Int,onClick:()->Unit){
    TextButton(onClick = onClick,modifier= Modifier.fillMaxWidth()) {
        Text(text = stringResource(id = name),color= MaterialTheme.colorScheme.onSurface) }
    Divider()
}