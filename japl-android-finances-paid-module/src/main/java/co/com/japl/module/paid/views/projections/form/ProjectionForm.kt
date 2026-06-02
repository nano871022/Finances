package co.com.japl.module.paid.views.projections.form

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import co.com.japl.finances.iports.enums.KindPaymentsEnums
import co.com.japl.module.paid.R
import co.com.japl.module.paid.controllers.projections.forms.ProjectionFormViewModel
import co.com.japl.ui.components.FieldDatePicker
import co.com.japl.ui.components.FieldSelect
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1f
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1fAndPaddintRightSpace
import co.com.japl.ui.utils.DateUtils
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import co.com.japl.finances.iports.inbounds.paid.IProjectionFormPort
import java.math.BigDecimal
import java.time.LocalDate

@Composable
fun ProjectionForm(viewModel: ProjectionFormViewModel){
    val snackbarState = remember { viewModel.hostState}
    Scaffold (
        floatingActionButton = {
            Buttons(viewModel)
        },
        snackbarHost = {
            SnackbarHost( hostState = snackbarState)
        }
    ){
        Column(modifier = Modifier.padding(it)) {
            Form(viewModel)
        }
    }
}

@Composable
private fun Buttons(viewModel: ProjectionFormViewModel){
    Column {
        FloatButton(imageVector = Icons.Rounded.CleaningServices, descriptionIcon = R.string.clean_form) {
            viewModel.clear()
        }
        if(viewModel.id == null) {
            FloatButton(imageVector = Icons.Rounded.Save, descriptionIcon = R.string.save) {
                viewModel.save()
            }
        }else {
            FloatButton(imageVector = Icons.Rounded.Update, descriptionIcon = R.string.update) {
                viewModel.update()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
private fun Form(viewModel: ProjectionFormViewModel){
    val name by viewModel.name.value.collectAsState()
    val value by viewModel.value.value.collectAsState()
    val quote by viewModel.quote.value.collectAsState()
    val datePayment by viewModel.datePayment.value.collectAsState()
    val period by viewModel.period.value.collectAsState()

    Column(modifier = Modifier.padding(Dimensions.PADDING_SHORT)) {
        FieldText(
            title = stringResource(id = R.string.name_projection),
            value = name,
            callback = { viewModel.name.onValueChange(it) },
            modifier = Modifier.fillMaxWidth(),
            validation = { viewModel.name.validate() }
        )

        FieldDatePicker(
            title = R.string.date_of_projection,
            value = DateUtils.localDateToString(datePayment),
            callable = { date -> viewModel.datePayment.onValueChange(DateUtils.toLocalDate(date)) },
            validation = { viewModel.datePayment.validate() },
            modifier = Modifier.padding(top= Dimensions.PADDING_SHORT)
        )

        FieldSelect(
            title = stringResource(id = R.string.period_projection),
            value = period.second,
            list = KindPaymentsEnums.entries.map { Pair(it.ordinal, it.name) }.toMutableStateList(),
            modifier = Modifier.padding(top= Dimensions.PADDING_SHORT),
            callAble = { pair -> pair?.let { viewModel.period.onValueChange(it) } }
        )

            FieldText(
                title = stringResource(id = R.string.value_projection),
                value = value.takeIf { it > BigDecimal.ZERO }?.toString()?:"",
                callback = { viewModel.value.onValueChangeStr(it) },
                validation = { viewModel.value.validate() },
                modifier = Modifier.fillMaxWidth().padding(top= Dimensions.PADDING_SHORT),
                currency = true
            )

            FieldText(
                title = stringResource(id = R.string.quote_projection),
                value = quote.takeIf { it > BigDecimal.ZERO }?.toString()?:"",
                callback = { viewModel.quote.onValueChangeStr(it) },
                validation = { viewModel.quote.validate() },
                modifier = Modifier.fillMaxWidth().padding(top= Dimensions.PADDING_SHORT),
                currency = true
            )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
fun ProjectionFormPreview(){
    MaterialThemeComposeUI {
        ProjectionForm(getViewModel())
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun ProjectionFormPreviewDark(){
    MaterialThemeComposeUI {
        ProjectionForm(getViewModel())
    }
}

@Composable
private fun getViewModel(): ProjectionFormViewModel{
    val vm = ProjectionFormViewModel(context= LocalContext.current, saveStateHandler = SavedStateHandle(), id = null, projectionSvc = null, navController = null)
    vm.datePayment.onValueChange(LocalDate.now())
    vm.period.onValueChange(Pair(KindPaymentsEnums.MONTHLY.month,KindPaymentsEnums.MONTHLY.name))
    vm.name.onValueChange("Salario")
    vm.value.onValueChange(BigDecimal.valueOf(1_000_000))
    vm.quote.onValueChange(BigDecimal.valueOf(100_000))
    return vm
}
