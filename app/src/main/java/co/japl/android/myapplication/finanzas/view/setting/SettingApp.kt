package co.japl.android.myapplication.finanzas.view.setting

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import co.com.japl.ui.Prefs
import co.com.japl.ui.components.CheckBoxField
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.controller.setting.SettingsAppViewModel

@Composable
fun SettingsApp(viewModel:SettingsAppViewModel){
    val msgStatus = remember {viewModel.msgInitial}
    Scaffold (
        floatingActionButton = {
            Buttons(viewModel)
        }, modifier = Modifier.padding(Dimensions.PADDING_SHORT)
    ) {
        Column(modifier = Modifier.padding(it)) {
            CheckBoxField(
                title = stringResource(id = R.string.msg_checkbox),
                value = msgStatus.value,
                callback = { msgStatus.value = it })
        }
    }
}

@Composable
private fun Buttons(viewModel: SettingsAppViewModel){
    Column{
        FloatButton(imageVector = Icons.Rounded.Save,
            descriptionIcon = R.string.save) {
            viewModel.save()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
internal fun SettingsAppPreview(){
    MaterialThemeComposeUI {
        SettingsApp(getViewModel())
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
internal fun SettingsAppPreviewDark(){
    MaterialThemeComposeUI {
        SettingsApp(getViewModel())
    }
}

@Composable
fun getViewModel():SettingsAppViewModel{
    return  SettingsAppViewModel(prefs=Prefs(LocalContext.current), context = LocalContext.current)
}