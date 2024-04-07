package co.japl.android.myapplication.finanzas.view.google

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Login
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Restore
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import co.com.japl.ui.components.AlertDialogOkCancel
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1f
import co.japl.android.myapplication.R

@Composable
fun GoogleAuthBackupRestore(viewModel:GoogleAuthBackupRestoreViewModel) {
    val loginValue = remember { viewModel.loginValue }
    val result  = remember { viewModel.result }
    val isLogged = remember { viewModel.isLogged }
    val isProcessing = remember { viewModel.isProcessing }
    val statusRestoreDialog = remember { mutableStateOf(false) }
    val stateResultActivity = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        viewModel.responseConnection(it)
    }
        Column (modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.PADDING_SHORT)) {
            if(isProcessing.value) {
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
                    Button(
                        onClick = {
                            stateResultActivity.launch(viewModel.login())
                            //viewModel.login()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Login,
                            contentDescription = stringResource(id = R.string.login)
                        )
                        Text(text = stringResource(id = R.string.login))
                    }
                } else {
                    Button(onClick = { viewModel.logout() },) {
                        Icon(
                            imageVector = Icons.Rounded.Logout,
                            contentDescription = stringResource(id = R.string.logout)
                        )
                        Text(text = stringResource(id = R.string.logout))
                    }
                }
            }
            if (isLogged.value) {
                Row {
                    Button(onClick = { viewModel.backup() }, modifier = Weight1f()) {
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
                Text(
                    text = result.value,
                    minLines = 20,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
            }

    AlertRestore(status = statusRestoreDialog,action={viewModel.restore()})

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

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
internal fun GoogleAuthBackupRestorePreview(){
    MaterialThemeComposeUI {
        GoogleAuthBackupRestore(getViewModel())
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
internal fun GoogleAuthBackupRestorePreviewDark(){
    MaterialThemeComposeUI {
        GoogleAuthBackupRestore(getViewModel())
    }
}

@Composable
private fun getViewModel():GoogleAuthBackupRestoreViewModel{
    return  GoogleAuthBackupRestoreViewModel(null,null)
}