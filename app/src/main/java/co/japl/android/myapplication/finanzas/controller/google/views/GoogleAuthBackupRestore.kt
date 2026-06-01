package co.japl.android.myapplication.finanzas.controller.google.views

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.japl.android.myapplication.finanzas.controller.google.GoogleAuthBackupRestoreViewModel
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.japl.android.myapplication.R
import coil.compose.AsyncImage

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun GoogleAuthBackupRestore(viewModel:GoogleAuthBackupRestoreViewModel) {
    Body(viewModel)
}

@Composable
fun Body(viewModel:GoogleAuthBackupRestoreViewModel){
    val selectedTabIndex = remember { viewModel.tabIndex }
    val isProcessing = remember { viewModel.isProcessing }
    val isLogged = remember { viewModel.isLogged }

    Scaffold(
        bottomBar = {

                BottomNav(isProcessing,isLogged,selectedTabIndex)

        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(top= 0.dp)
            .background(MaterialTheme.colorScheme.background)) {
                when (selectedTabIndex.value) {
                    0 -> LoginSpace(viewModel)
                    1 -> StatsSpace(viewModel)
                    2 -> DataTables(viewModel)
                }
        }
    }
}

@Composable
fun BottomNav(isProcessing: MutableState<Boolean>,isLogged: MutableState<Boolean>, selectedTabIndex: MutableIntState) {
    if(isProcessing.value.not()) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            tonalElevation = 8.dp
        ) {
            if(isLogged.value) {
                NavigationBarItem(
                    selected = selectedTabIndex.value == 1,
                    onClick = { selectedTabIndex.value = 1 },
                    icon = {
                        Icon(
                            Icons.Rounded.CloudSync,
                            contentDescription = stringResource(R.string.sync)
                        )
                    },
                    label = { Text(stringResource(R.string.sync)) }
                )
            }
            NavigationBarItem(
                selected = false,
                onClick = { selectedTabIndex.value = 2 },
                icon = { Icon(Icons.Rounded.Storage, contentDescription = stringResource(R.string.database)) },
                label = { Text(stringResource(R.string.database)) }
            )
            NavigationBarItem(
                selected = selectedTabIndex.value == 0,
                onClick = { selectedTabIndex.value = 0 },
                icon = { Icon(Icons.Rounded.Person, contentDescription = stringResource(R.string.profile)) },
                label = { Text(stringResource(R.string.profile)) }
            )
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
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
internal fun GoogleAuthBackupRestore2Preview(){
    val viewModel = getViewModel()
    viewModel.tabIndex.value = 1
    MaterialThemeComposeUI {
        GoogleAuthBackupRestore(viewModel)
    }
}

@Composable
private fun getViewModel():GoogleAuthBackupRestoreViewModel{
    return  GoogleAuthBackupRestoreViewModel(null,null,null)
}
