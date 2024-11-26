package co.japl.android.myapplication.finanzas.view.google

import android.app.Activity
import android.content.res.Configuration
import android.icu.text.DecimalFormat
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Login
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Restore
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProvider
import co.com.japl.ui.components.AlertDialogOkCancel
import co.com.japl.ui.components.DataTable
import co.com.japl.ui.model.datatable.Header
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1f
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.holders.validations.notNull
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun GoogleAuthBackupRestore(viewModel:GoogleAuthBackupRestoreViewModel) {
    val selectedTabIndex = remember { viewModel.tabIndex}


    Column(modifier = Modifier.fillMaxWidth()) {

        Tabs(selectedTabIndex)

        when (selectedTabIndex.value) {
            0 -> LoginSpace(viewModel)
            1 -> StatsSpace(viewModel)
        }
    }

}

@Composable
private fun Tabs(selectedTabIndex: MutableIntState){
    TabRow(selectedTabIndex = selectedTabIndex.value) {
        Tab( selected = true,  onClick={ selectedTabIndex.value = 0}) {
            Text(text = "Login", color=MaterialTheme.colorScheme.onBackground)
        }
        Tab( selected = true,  onClick={ selectedTabIndex.value = 1}) {
            Text(text = "stats", color=MaterialTheme.colorScheme.onBackground)
        }
    }
}

@Composable
private fun LoginSpace(viewModel:GoogleAuthBackupRestoreViewModel){
    val context = LocalContext.current
    val statusRestoreDialog = remember { mutableStateOf(false) }
    val stateResultActivity = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        viewModel.isProcessing.value = true
        viewModel.responseConnection(it)
    }
    val loginValue = remember { viewModel.loginValue }
    val result  = remember { viewModel.result }
    val isLogged = remember { viewModel.isLogged }
    val isProcessing = remember { viewModel.isProcessing }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.PADDING_SHORT)
            .verticalScroll(rememberScrollState())
    ) {
        if (isProcessing.value) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        Row {
            Text(
                text = loginValue.value,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .weight(2f)
            )

            if (!isLogged.value) {
                Column {
                    Button(
                        onClick = {
                            viewModel.login()?.let { stateResultActivity.launch(it) }

                        },
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Login,
                            contentDescription = stringResource(id = R.string.login)
                        )
                        Text(text = stringResource(id = R.string.login))
                    }
                }
            } else {
                Button(
                    onClick = {
                            viewModel.logout()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Logout,
                        contentDescription = stringResource(id = R.string.logout)
                    )
                    Text(text = stringResource(id = R.string.logout))
                }
            }
        }
        if (isLogged.value) {
            Logged(viewModel, statusRestoreDialog)
        }

        Text(
            text = result.value,
            minLines = 20,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.fillMaxWidth()
        )

    }
    AlertRestore(status = statusRestoreDialog,action={
            viewModel.restore()
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatsSpace(viewModel:GoogleAuthBackupRestoreViewModel){
    val progress by remember {viewModel.statsLocalProgess}
    val stateStats = remember {viewModel.statsLocal }
    val color = MaterialTheme.colorScheme.onBackground
    val columnNameWeight = 2f
    val columnCountWeight = 1f

    viewModel.onload()

    if(progress) {
        LinearProgressIndicator()
    }else {
        DataTable(listHeader = listOf(
            Header(title = "Nombre", tooltip = "Nombre de la tabla", weight = columnNameWeight),
            Header(title = "Cantidad", tooltip = "Cantidad de registros en la tabla",weight = columnCountWeight)
            ),
            sizeBody = stateStats.size,
            footer = {
                Text(
                    text = "Total Registros",
                    color = color,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(columnNameWeight)
                )
                Text(
                    text = DecimalFormat("#,###").format(
                        stateStats.sumOf { it.second }),
                    color = color,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(columnCountWeight)
                )
            }) { index ->
            Text(
                text = stateStats[index].first,
                color = color,
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .weight(columnNameWeight)
                    .padding(end = 5.dp)
            )
            Text(
                text = DecimalFormat("#,###").format(stateStats[index].second),
                color = color,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(columnCountWeight)
            )
        }
    }
}

@Composable
private fun Logged(viewModel:GoogleAuthBackupRestoreViewModel,statusRestoreDialog:MutableState<Boolean>){

    Row {
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.backup()
            }
        }, modifier = Weight1f()) {
            Icon(
                imageVector = Icons.Rounded.Upload,
                contentDescription = stringResource(id = R.string.backup)
            )
            Text(text = stringResource(id = R.string.backup))

        }

        Button(onClick = { statusRestoreDialog.value = true }, modifier = Weight1f()) {
            Icon(
                imageVector = Icons.Rounded.Restore,
                contentDescription = stringResource(id = R.string.restore)
            )
            Text(text = stringResource(id = R.string.restore))
        }


    }
}

@Composable
private fun AlertRestore(status:MutableState<Boolean>,action:()->Unit){
    if(status.value) {
        AlertDialogOkCancel(title = R.string.dialog_restore,
            confirmNameButton = R.string.restore,
            onDismiss = { status.value = false },) {
                action.invoke()
                status.value = false
        }
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
internal fun GoogleAuthBackupRestorePreview(){
    val viewModel = getViewModel()
    viewModel.tabIndex.value = 0
    MaterialThemeComposeUI {
        GoogleAuthBackupRestore(viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
internal fun GoogleAuthBackupRestorePreviewDark(){
    val viewModel = getViewModel()
    repeat(30) { index ->
        viewModel.statsLocal.add(Pair("Table $index", (index + 1) * 1000L))
    }


    viewModel.tabIndex.value = 1
    viewModel.statsLocalProgess.value = false
    MaterialThemeComposeUI {
        GoogleAuthBackupRestore(viewModel)
    }
}

@Composable
private fun getViewModel():GoogleAuthBackupRestoreViewModel{
    return  GoogleAuthBackupRestoreViewModel(null,null,null,null,null)
}