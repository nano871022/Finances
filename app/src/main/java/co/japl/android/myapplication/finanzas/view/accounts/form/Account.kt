package co.japl.android.myapplication.finanzas.view.accounts.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import co.com.japl.ui.components.CheckBoxField
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.theme.values.Dimensions
import co.japl.android.myapplication.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AccountForm(viewModel: AccountViewModel){
    var stateLoader = remember {
        viewModel.loader
    }
    var stateProgress = remember {
        viewModel.progress
    }

    val scope = rememberCoroutineScope()
    scope.launch {
        withContext(Dispatchers.IO) {
            viewModel.main()
        }
    }

    if(stateLoader.value) {
        LinearProgressIndicator(      progress = stateProgress.value)
    }else {
        Scaffold(
            floatingActionButton = {
                IconButton(onClick = { viewModel.save() }) {
                    Icon(imageVector = Icons.Rounded.Add
                        , contentDescription = "Add input to account")
                }
            }
        ) {
            Body(viewModel = viewModel,modifier=Modifier.padding(it))
        }
    }
}

@Composable
private fun Body(viewModel: AccountViewModel,modifier:Modifier){
    val stateName = remember { viewModel.name }
    val stateActive = remember { viewModel.active }

    Column {
        FieldText(
            title = stringResource(id = R.string.name)
            , value = stateName.value
            , validation = {viewModel.validation()}
            , hasErrorState = viewModel.errorName
            , callback = { stateName.value = it }
            ,modifier=modifier.fillMaxWidth().padding(Dimensions.PADDING_SHORT))

        CheckBoxField(
            title = stringResource(id = R.string.Active)
            , value = stateActive.value
            , modifier = modifier.padding(Dimensions.PADDING_SHORT)
            , callback = { stateActive.value = it }
        )
    }

}