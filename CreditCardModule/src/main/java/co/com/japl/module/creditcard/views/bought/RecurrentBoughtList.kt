package co.com.japl.module.creditcard.views.bought

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1f
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.BoughtCreditCardPeriodDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtListDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.controllers.bought.lists.RecurrentBoughtViewModel
import co.com.japl.module.creditcard.enums.MoreOptionsRecurrentBought
import co.com.japl.ui.Prefs
import co.com.japl.ui.enums.IMoreOptions
import co.com.japl.ui.components.MoreOptionsDialog
import co.com.japl.ui.components.AlertDialogOkCancel
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.japl.android.myapplication.utils.NumbersUtil
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun RecurrentBoughtList(
    codeCreditCard: Int,
    viewModel: RecurrentBoughtViewModel,
    onEdit: (CreditCardBoughtItemDTO) -> Unit,
    onAlter: (CreditCardBoughtItemDTO) -> Unit
) {
    var progressStatus by remember {viewModel!!.progress}

    LaunchedEffect(codeCreditCard, viewModel.loader.value) {
        if (!viewModel.loader.value) {
            viewModel.load(codeCreditCard)
        }
    }

    if(viewModel.loader.value && viewModel.filteredList.isNotEmpty()) {
        Body(codeCreditCard, viewModel, onEdit, onAlter)
    }else if(viewModel.filteredList.isEmpty() && viewModel.progress.value < 1f){
        Column(modifier=Modifier.fillMaxWidth()) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text=stringResource(R.string.no_data),
                textAlign =  TextAlign.Center,
                color=MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth().padding(Dimensions.PADDING_TOP)
            )
        }
    }else{
        Text(
            text=stringResource(id = R.string.no_data),
            modifier=Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }

}

@Composable
private fun Body( codeCreditCard: Int,
                viewModel: RecurrentBoughtViewModel,
                onEdit: (CreditCardBoughtItemDTO) -> Unit,
                onAlter: (CreditCardBoughtItemDTO) -> Unit ){
    Scaffold(
        topBar = { TopBar(viewModel)   }
    ) { paddingValues ->
        val groupedList = viewModel.filteredList.groupBy { YearMonth.of(it.boughtDate.year, it.boughtDate.month) }

        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            groupedList.forEach { (month, items) ->
                item {
                    Row(modifier=Modifier.fillMaxWidth()) {
                        Text(
                            text = month.format(DateTimeFormatter.ofPattern("MMM yyyy")),
                            modifier = Weight1f().padding(8.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = NumbersUtil.COPtoString(items.sumOf { it.valueItem }),
                            modifier = Weight1f().padding(8.dp),
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Right
                        )
                    }
                }
                items(items) { item ->
                    RecurrentBoughtItem(
                        item = item,
                        viewModel = viewModel,
                        onEdit = onEdit,
                        onAlter = onAlter
                    )
                }
            }
        }
    }
}

@Composable
private fun TopBar(viewModel: RecurrentBoughtViewModel){
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Text(
                text = stringResource(id = R.string.total_active_recurrent),
                textAlign = TextAlign.Left
            )
            Text(
                text=  viewModel.totalActive.value,
                textAlign = TextAlign.Right
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(
                onClick = { viewModel.setFilter(null) }
            )
            { Text(text = stringResource(id = R.string.filter_all)) }
            Button(
                onClick = { viewModel.setFilter(true) }
            )
            { Text(text = stringResource(id = R.string.filter_active)) }
            Button(
                onClick = { viewModel.setFilter(false) }
            )
            { Text(text = stringResource(id = R.string.filter_inactive)) }
        }
    }
}

@Composable
private fun RecurrentBoughtItem(
    item: CreditCardBoughtItemDTO,
    viewModel: RecurrentBoughtViewModel,
    onEdit: (CreditCardBoughtItemDTO) -> Unit,
    onAlter: (CreditCardBoughtItemDTO) -> Unit
) {
    val isActive = item.endDate == LocalDateTime.MAX || item.endDate.isAfter(LocalDateTime.now())
    val borderColor = if (isActive) Color.Blue else Color.Unspecified
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    if (showDeleteConfirmation) {
        AlertDialogOkCancel(
            title = R.string.do_you_want_to_delete_this_record,
            confirmNameButton = R.string.delete,
            onDismiss = { showDeleteConfirmation = false },
            onClick = {
                viewModel.handleAction(item, MoreOptionsRecurrentBought.DELETE)
                showDeleteConfirmation = false
            }
        )
    }

    if (showMenu) {
        val options = MoreOptionsRecurrentBought.values().toMutableList()
        if (isActive) {
            options.remove(MoreOptionsRecurrentBought.ACTIVATE)
        } else {
            options.remove(MoreOptionsRecurrentBought.DEACTIVATE)
            options.remove(MoreOptionsRecurrentBought.ALTER)
        }

        MoreOptionsDialog(
            listOptions = options as List<IMoreOptions>,
            onDismiss = { showMenu = false },
            onClick = { action ->
                showMenu = false
                when (action) {
                    MoreOptionsRecurrentBought.DELETE -> showDeleteConfirmation = true
                    MoreOptionsRecurrentBought.EDIT -> onEdit(item)
                    MoreOptionsRecurrentBought.ALTER -> onAlter(item)
                    else -> viewModel.handleAction(item, action as MoreOptionsRecurrentBought)
                }
            }
        )
    }

    Card(
        modifier = Modifier.padding(8.dp).fillMaxWidth(),
        border = if (isActive) BorderStroke(2.dp, borderColor) else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(modifier=Modifier.fillMaxWidth()) {
                    Text(
                        text = item.boughtDate.format(DateTimeFormatter.ofPattern("dd"))
                        , style = androidx.compose.ui.text.TextStyle.Default
                        , modifier=Modifier.padding(end=8.dp).padding(top=4.dp)
                    )
                    Text(text = item.nameItem,
                        modifier=Modifier.weight(2f))
                    Text(
                        text = NumbersUtil.COPtoString(item.valueItem)
                        , style = androidx.compose.ui.text.TextStyle.Default
                        , textAlign = TextAlign.Right
                        ,modifier=Weight1f().padding(top=4.dp)
                    )
                }
            }
            IconButton(onClick = { showMenu = true }) {
                Icon(painter = painterResource(id = R.drawable.more_vertical), contentDescription = null)
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun RecurrentPreviewLight() {
    val context = LocalContext.current
    val listPort = getBoughtListPort()
    val vm = remember {
        recurrentViewModel(
            listPort,
            Prefs(context)
        )
    }
    MaterialThemeComposeUI {
        RecurrentBoughtList(
            codeCreditCard = 1,
            viewModel = vm,
            onEdit = {},
            onAlter = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun RecurrentPreviewNoDataLight() {
    val context = LocalContext.current
    val listPort = getBoughtListPort()
    val vm = remember {
        recurrentViewModel(
            listPort,
            Prefs(context)
        )
    }
    vm.filteredList.clear()
    vm.progress.value = 1f
    MaterialThemeComposeUI {
        RecurrentBoughtList(
            codeCreditCard = 1,
            viewModel = vm,
            onEdit = {},
            onAlter = {}
        )
    }
}

@Composable
private fun getBoughtListPort(): IBoughtListPort{
    val listPort = remember {
        object : IBoughtListPort {
            override fun getBoughtList(creditCardDTO: CreditCardDTO, cutOff: LocalDateTime, cache: Boolean): CreditCardBoughtListDTO = TODO()
            override fun getBoughtPeriodList(idCreditCard: Int, cache: Boolean): List<BoughtCreditCardPeriodDTO> = emptyList()
            override fun delete(codeBought: Int, cache: Boolean): Boolean = true
            override fun restore(codeBought: Int, cache: Boolean): Boolean = true
            override fun endingRecurrentPayment(codeBought: Int, cutOff: LocalDateTime): Boolean = true
            override fun endingPayment(codeBought: Int, message: String, cutOff: LocalDateTime): Boolean = true
            override fun updateRecurrentValue(codeBought: Int, value: Double, cutOff: LocalDateTime, cache: Boolean): Boolean = true
            override fun differntInstallment(codeBought: Int, value: Long, cutOff: LocalDateTime, cache: Boolean): Boolean = true
            override fun clone(codeBought: Int, cache: Boolean): Boolean = true
            override fun getAllRecurrent(idCreditCard: Int): List<CreditCardBoughtItemDTO> = emptyList()
            override fun reactivateRecurrent(codeBought: Int): Boolean = true
        }
    }
    return listPort
}

private fun recurrentViewModel(listPort: IBoughtListPort, prefs: Prefs): RecurrentBoughtViewModel {
    var vm = RecurrentBoughtViewModel(listPort, prefs)
    vm.totalActive.value = "20.000"
    vm.loader.value=true
    vm.progress.value = 1f
    vm.filteredList.add(
        CreditCardBoughtItemDTO(
            codeCreditCard= 0,
            nameCreditCard="Tarjeta 2",
            nameItem="Compra 2",
            valueItem=10000.0,
            interest= 0.22,
            month=1,
            boughtDate= LocalDateTime.now().minusMonths(1),
            cutOutDate= LocalDateTime.now(),
            createDate= LocalDateTime.now(),
            endDate= LocalDateTime.now(),
            id=0,
            recurrent=true,
            kind= KindInterestRateEnum.CREDIT_CARD,
            kindOfTax= KindOfTaxEnum.ANUAL_EFFECTIVE,
            monthPaid= 0,
            capitalValue=20000.0,
            interestValue= 0.0,
            settings= 0.0,
            settingCode=0,
            pendingToPay= 20000.0,
            quoteValue= 20000.0,
            tagName= ""
        )
    )
    vm.filteredList.add(
        CreditCardBoughtItemDTO(
             codeCreditCard= 0,
             nameCreditCard="Tarjeta 2",
            nameItem="Compra 1",
            valueItem=20000.0,
            interest= 0.22,
            month=1,
            boughtDate= LocalDateTime.now(),
            cutOutDate= LocalDateTime.now(),
            createDate= LocalDateTime.now(),
            endDate= LocalDateTime.now(),
            id=0,
            recurrent=true,
            kind= KindInterestRateEnum.CREDIT_CARD,
            kindOfTax= KindOfTaxEnum.ANUAL_EFFECTIVE,
            monthPaid= 0,
            capitalValue=20000.0,
            interestValue= 0.0,
            settings= 0.0,
            settingCode=0,
            pendingToPay= 20000.0,
            quoteValue= 20000.0,
            tagName= ""
        )
    )


    return vm
}
