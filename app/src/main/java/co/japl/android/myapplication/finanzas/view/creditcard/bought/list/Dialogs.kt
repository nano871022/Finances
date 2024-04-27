package co.japl.android.myapplication.finanzas.view.creditcard.bought.list

import android.content.res.Configuration
import android.os.Build
import android.text.InputType
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsCreditCard
import co.com.japl.ui.components.AlertDialogOkCancel
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.utils.NumbersUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreOptionsDialog(valueToPay:Double, creditRate:Double,listOptions:List<MoreOptionsItemsCreditCard>, onDismiss:()->Unit, onClick:(MoreOptionsItemsCreditCard,Double) -> Unit) {
    val stateDeleteDialog = remember { mutableStateOf(false) }
    val stateEndingDialog = remember { mutableStateOf(false) }
    val stateUpdateDialog = remember { mutableStateOf(false) }
    val stateDifferDialog = remember { mutableStateOf(false) }
    val stateRestoreDialog = remember { mutableStateOf(false) }
    when{
        stateDeleteDialog.value -> {
            AlertDialogOkCancel (
                R.string.do_you_want_to_delete_this_record, R.string.delete
                ,onDismiss = {
                    stateDeleteDialog.value = false
                    onDismiss.invoke()
                },onClick = { onClick.invoke(MoreOptionsItemsCreditCard.DELETE,0.0)
                    stateDeleteDialog.value = false
                    onDismiss.invoke()
                })
        }
        stateEndingDialog.value -> {
            AlertDialogOkCancel (
                R.string.do_you_want_to_ending_recurrent_payment, R.string.ending
                ,onDismiss = {
                    stateEndingDialog.value = false
                    onDismiss.invoke()
                },onClick = { onClick.invoke(MoreOptionsItemsCreditCard.ENDING,0.0)
                    stateEndingDialog.value = false
                    onDismiss.invoke()
                })
        }
        stateUpdateDialog.value -> {
            UpdateValueDialog(
                onDismiss = {
                    stateUpdateDialog.value = false
                    onDismiss.invoke()
                },
                onClick = {
                    onClick.invoke(MoreOptionsItemsCreditCard.UPDATE_VALUE,it)
                    stateUpdateDialog.value = false
                    onDismiss.invoke()
                }
            )
        }

        stateDifferDialog.value -> {
            DifferInstallmentDialog(
                  value = valueToPay,
                creditRate = creditRate
                , onDismiss = {  stateDifferDialog.value = false}
                , onClick = {
                    onClick.invoke( MoreOptionsItemsCreditCard.DIFFER_INSTALLMENT, it.toDouble())
                    stateDifferDialog.value = false
                    onDismiss.invoke()
                }
            )
        }
        stateRestoreDialog.value -> {
            AlertDialogOkCancel (
                R.string.do_you_want_to_restore_this_record, R.string.restore
                ,onDismiss = {
                    stateRestoreDialog.value = false
                    onDismiss.invoke()
                },onClick = { onClick.invoke(MoreOptionsItemsCreditCard.RESTORE,0.0)
                    stateRestoreDialog.value = false
                    onDismiss.invoke()
                })
        }
    }
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss){
        Surface{
            Column(modifier = Modifier.fillMaxWidth()
                , horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(id = R.string.see_more), modifier = Modifier.padding(5.dp), fontSize = 18.sp)
                Divider()
                for ( item in listOptions) {
                    TextButton(onClick = {
                        when(item){
                            MoreOptionsItemsCreditCard.DELETE -> stateDeleteDialog.value = true
                            MoreOptionsItemsCreditCard.ENDING -> stateEndingDialog.value = true
                            MoreOptionsItemsCreditCard.UPDATE_VALUE -> stateUpdateDialog.value = true
                            MoreOptionsItemsCreditCard.DIFFER_INSTALLMENT -> stateDifferDialog.value = true
                            MoreOptionsItemsCreditCard.RESTORE->stateRestoreDialog.value = true
                            else->onClick.invoke(item,0.0)
                        }

                    },modifier= Modifier.fillMaxWidth()) {
                        Text(text = stringResource(id = item.title),color= MaterialTheme.colorScheme.onSurface) }
                    Divider()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UpdateValueDialog(onDismiss: () -> Unit, onClick: (Double) -> Unit) {
    var text by remember { mutableDoubleStateOf(0.0) }
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss){
        Surface{
            Column(modifier = Modifier.fillMaxWidth()
                , horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(id = R.string.update_value_recurrent_paymner), modifier = Modifier.padding(5.dp), fontSize = 18.sp)
                Divider()
                TextField(value = NumbersUtil.toString(text)
                    , onValueChange = { text = NumbersUtil.toDouble(it) }
                    , label = { Text(text = stringResource(id = R.string.value)) }
                    , prefix = { Text(text = "$") })

                Row{
                   TextButton(onClick = { onDismiss.invoke() }) {
                       Text(text = stringResource(id = R.string.cancel),color= MaterialTheme.colorScheme.onSurface) }

                   TextButton(onClick = { onClick.invoke(text) }) {
                       Text(text = stringResource(id = R.string.update_value_paymnet),color= MaterialTheme.colorScheme.onSurface)

                   }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DifferInstallmentDialog(value:Double,creditRate: Double,onDismiss: () -> Unit, onClick: (Long) -> Unit) {
    val stateInstallment = remember { mutableStateOf("") }
    val newValueInstallment = remember { mutableDoubleStateOf(0.0) }
    val interest = remember {mutableDoubleStateOf(0.0)}
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss) {
        Surface {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.differ_installment),
                    modifier = Modifier.padding(5.dp),
                    fontSize = 18.sp
                )

                FieldView(
                    name = R.string.pending_to_pay,
                    value = NumbersUtil.toString(value),
                    modifier = Modifier.defaultMinSize(
                        minWidth = TextFieldDefaults.MinWidth,
                        minHeight = TextFieldDefaults.MinHeight
                    )
                )

                TextField(value = stateInstallment.value.toString(),
                    onValueChange = {
                        stateInstallment.value = it
                        newValueInstallment.doubleValue = value / NumbersUtil.toDouble(it)
                        interest.doubleValue = value * creditRate
                    },
                    placeholder = { Text(text = "0")},
                    label = { Text(text = stringResource(id = R.string.periods)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    trailingIcon = {
                        IconButton(onClick = {
                            stateInstallment.value = ""
                        }){
                            Icon(imageVector = Icons.Rounded.Cancel,
                                contentDescription = stringResource(id = R.string.cancel))
                        }
                    }

                )

                FieldView(
                    name = R.string.capital_quotes,
                    value = NumbersUtil.toString(newValueInstallment.doubleValue),
                    modifier = Modifier.defaultMinSize(
                        minWidth = TextFieldDefaults.MinWidth,
                        minHeight = TextFieldDefaults.MinHeight
                    )
                )

                FieldView(
                    name = R.string.interest_value,
                    value = NumbersUtil.toString(interest.doubleValue),
                    modifier = Modifier.defaultMinSize(
                        minWidth = TextFieldDefaults.MinWidth,
                        minHeight = TextFieldDefaults.MinHeight
                    )
                )

                FieldView(
                    name = R.string.total_quote,
                    value = NumbersUtil.toString(newValueInstallment.doubleValue + interest.doubleValue),
                    modifier = Modifier.defaultMinSize(
                        minWidth = TextFieldDefaults.MinWidth,
                        minHeight = TextFieldDefaults.MinHeight
                    )
                )

                Row {
                    TextButton(onClick = { onDismiss.invoke() }) {
                        Text(text = stringResource(id = R.string.cancel), color = MaterialTheme.colorScheme.onSurface)
                    }

                    TextButton(onClick = { onClick.invoke(stateInstallment.value?.toLong()?:0) }) {
                        Text(text = stringResource(id = R.string.ccio_differ_quotes), color = MaterialTheme.colorScheme.onSurface)

                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
    private fun DifferInstallmentDialogPreview(){
        MaterialThemeComposeUI {
            DifferInstallmentDialog(100.0,0.0,{},{})
        }
}
@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun DifferInstallmentDialogPreviewDark(){
    MaterialThemeComposeUI {
        DifferInstallmentDialog(100.0,0.0,{},{})
    }
}