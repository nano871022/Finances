package co.japl.android.myapplication.finanzas.view.accounts.inputs.lists

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.japl.android.myapplication.R
import co.com.japl.ui.enums.IMoreOptions
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsInput
import co.com.japl.ui.components.AlertDialogOkCancel
import co.com.japl.ui.components.MoreOptionsDialog
import co.japl.android.myapplication.utils.NumbersUtil

@Composable
fun MoreOptionsItemsInputList(mutableStateId: MutableState<Int>,modelView:InputListModelView){
    val stateDialogOptionsMore = remember { modelView.stateDialogOptionsMore }
    val stateDialogDelete = remember { mutableStateOf(false) }
    val stateDialogUpdate = remember { mutableStateOf(false) }
    if(stateDialogDelete.value) {
        DeleteConfirm(stateDeleteDialog = stateDialogDelete, onDismiss = { stateDialogDelete.value = false }, onClick = {
            modelView.optionSelected(mutableStateId.value,option=MoreOptionsItemsInput.DELETE)
            stateDialogDelete.value = false
        })
    }
    if(stateDialogUpdate.value) {
        UpdateValueDialog(
            onDismiss = { stateDialogUpdate.value = false },
            onClick = {
                modelView.optionSelected(mutableStateId.value,it, MoreOptionsItemsInput.UPDATE_VALUE)
                stateDialogUpdate.value = false
            }
        )
    }
    if(stateDialogOptionsMore.value) {
        MoreOptionsDialog(
            listOptions = MoreOptionsItemsInput.values().toList() as List<IMoreOptions>,
            onDismiss = { stateDialogOptionsMore.value = false },
            onClick = {
                when(it) {
                    MoreOptionsItemsInput.DELETE -> {
                        stateDialogDelete.value = true
                    }
                    MoreOptionsItemsInput.UPDATE_VALUE -> {
                        stateDialogUpdate.value = true
                    }
                }
                stateDialogOptionsMore.value = false
            })
    }
}

@Composable
private fun DeleteConfirm(stateDeleteDialog:MutableState<Boolean>,onDismiss:()->Unit,onClick: (IMoreOptions) -> Unit){
    AlertDialogOkCancel (
        R.string.do_you_want_to_delete_this_record, R.string.delete
        ,onDismiss = {
            stateDeleteDialog.value = false
            onDismiss.invoke()
        },onClick = { onClick.invoke(MoreOptionsItemsInput.DELETE)
            stateDeleteDialog.value = false
            onDismiss.invoke()
        })
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UpdateValueDialog(onDismiss: () -> Unit, onClick: (Double) -> Unit) {
    var text by remember { mutableStateOf("") }
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss){
        Surface{
            Column(modifier = Modifier.fillMaxWidth()
                , horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(id = R.string.update_input_value), modifier = Modifier.padding(5.dp), fontSize = 18.sp)
                Divider()
                TextField(value = text
                    , onValueChange = {
                                        text = NumbersUtil.toString(NumbersUtil.toDouble(it)).toString()
                                      }
                    , label = { Text(text = stringResource(id = R.string.value)) }
                        , trailingIcon = {
                            IconButton(onClick = {
                                text = ""
                            }){
                                Icon(imageVector = Icons.Rounded.Cancel, contentDescription = "close")
                            }
                        }
                    , keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    , prefix = { Text(text = "$") })

                Row{
                    TextButton(onClick = { onDismiss.invoke() }) {
                        Text(text = stringResource(id = R.string.cancel),color= MaterialTheme.colorScheme.onSurface) }

                    TextButton(onClick = { onClick.invoke(NumbersUtil.toDouble(text)) }) {
                        Text(text = stringResource(id = R.string.update_value_paymnet),color= MaterialTheme.colorScheme.onSurface)

                    }
                }
            }
        }
    }
}