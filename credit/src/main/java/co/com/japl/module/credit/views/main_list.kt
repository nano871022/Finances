package co.com.japl.module.credit.views

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.theme.MaterialThemeComposeUI

@Composable
fun MainList(){
    Column {
        FieldView(title = "test")
    }

}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showSystemUi = true, showBackground = true)
internal fun MainListPreview(){
    MaterialThemeComposeUI {
        MainList()
    }
}