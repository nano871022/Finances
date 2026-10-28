package co.com.japl.module.creditcard.views.bought

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.BoughtCreditCardPeriodDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.dtos.CreditCardBoughtListDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.controllers.bought.lists.AllBoughtRecordsViewModel
import co.com.japl.ui.components.CheckBoxField
import co.com.japl.ui.components.FieldSelect
import co.com.japl.ui.components.FieldView
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.utils.NumbersUtil
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun AllBoughtRecordsList(viewModel: AllBoughtRecordsViewModel, initialExpanded: Boolean = false) {
    val loader by remember { viewModel.loader }
    val searchQuery by remember { viewModel.searchQuery }
    val showOnlyRestorable by remember { viewModel.showOnlyRestorable }
    val filteredList by remember { viewModel.filteredList }
    val banksList by remember { viewModel.banksList }
    val selectedBank by remember { viewModel.selectedBank }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        FieldSelect(
            title = stringResource(id = R.string.credit_card),
            value = selectedBank?.second ?: "",
            list = banksList,
            modifier = Modifier.fillMaxWidth()
        ) { bank ->
            viewModel.onBankSelected(bank)
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (selectedBank == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Por favor seleccione un banco para ver los registros",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else if (loader) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        } else if (filteredList.isEmpty()) {
            Text(
                text = stringResource(id = R.string.no_data),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else {
            Row {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = viewModel::onSearchQueryChange,
                    label = { Text(text = stringResource(id = R.string.search_placeholder)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().weight(2f),
                    singleLine = true
                )

                CheckBoxField(
                    title = stringResource(R.string.restore),
                    value = showOnlyRestorable,
                    callback = { viewModel.onShowOnlyRestorableChange(it) },
                    modifier = Modifier.fillMaxWidth().weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${stringResource(id = R.string.total_records)}: ${filteredList.size}",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
            )

            val groupedByYear = remember(filteredList) {
                filteredList.sortedByDescending { it.boughtDate }
                    .groupBy { it.boughtDate.year }
                    .entries
                    .sortedByDescending { it.key }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(groupedByYear, key = { it.key }) { (year, yearItems) ->
                    YearSection(year = year, yearItems = yearItems, initialExpanded = initialExpanded, viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
private fun YearSection(year: Int, yearItems: List<CreditCardBoughtItemDTO>, initialExpanded: Boolean = false, viewModel: AllBoughtRecordsViewModel) {
    val expanded = remember { mutableStateOf(initialExpanded) }
    val totalYearValue = remember(yearItems) { yearItems.sumOf { it.valueItem } }
    val monthsGroup = remember(yearItems) {
        yearItems.sortedByDescending { it.boughtDate }
            .groupBy { YearMonth.of(it.boughtDate.year, it.boughtDate.month) }
            .entries
            .sortedByDescending { it.key }
    }

    Surface(
        onClick = { expanded.value = !expanded.value },
        modifier = Modifier
            .padding(vertical = 2.dp)
            .fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$year (${yearItems.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = NumbersUtil.COPtoString(totalYearValue),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            if (expanded.value) {
                Spacer(modifier = Modifier.height(6.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for ((yearMonth, monthItems) in monthsGroup) {
                        MonthSection(yearMonth = yearMonth, monthItems = monthItems, initialExpanded = initialExpanded, viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthSection(yearMonth: YearMonth, monthItems: List<CreditCardBoughtItemDTO>, initialExpanded: Boolean = false, viewModel: AllBoughtRecordsViewModel) {
    val expanded = remember { mutableStateOf(initialExpanded) }

    Surface(
        onClick = { expanded.value = !expanded.value },
        modifier = Modifier
            .padding(top = 2.dp, bottom = 2.dp, start = 8.dp, end = 2.dp)
            .fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            MonthHeader(key = yearMonth, list = monthItems)

            if (expanded.value) {
                Spacer(modifier = Modifier.height(6.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val sortedMonthItems = remember(monthItems) {
                        monthItems.sortedByDescending { it.boughtDate }
                    }
                    for (item in sortedMonthItems) {
                        RecordCard(item = item, viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthHeader(key: YearMonth, list: List<CreditCardBoughtItemDTO>) {
    val totalMonthValue = remember(list) { list.sumOf { it.valueItem } }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(1f)) {
            Text(
                text = key.format(DateTimeFormatter.ofPattern("MMMM")).replaceFirstChar{it.uppercase()},
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = " (${list.size})",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = NumbersUtil.COPtoString(totalMonthValue),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun RecordCard(item: CreditCardBoughtItemDTO, viewModel: AllBoughtRecordsViewModel) {
    val remainingMonths = (item.month - item.monthPaid).coerceAtLeast(0)
    val showRestoreMenu = item.restorable
    val menuExpanded = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = item.boughtDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.nameItem,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "${item.monthPaid}/${item.month}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(start = 4.dp)
                )

                if (showRestoreMenu) {
                    Box {
                        IconButton(onClick = { menuExpanded.value = true }) {
                            Icon(
                                painter = painterResource(id = R.drawable.more_vertical),
                                contentDescription = stringResource(id = R.string.see_more)
                            )
                        }
                        DropdownMenu(
                            expanded = menuExpanded.value,
                            onDismissRequest = { menuExpanded.value = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(text = stringResource(id = R.string.ccio_restore)) },
                                onClick = {
                                    menuExpanded.value = false
                                    viewModel.restore(item)
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FieldView(
                    name = stringResource(id = R.string.original_value),
                    value = NumbersUtil.COPtoString(item.valueItem),
                    modifier = Modifier.weight(1f)
                )

                FieldView(
                    name = stringResource(id = R.string.pending_value),
                    value = NumbersUtil.COPtoString(item.pendingToPay),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun AllBoughtRecordsListPreviewLight() {
    val port = remember {
        object : IBoughtListPort {
            override fun getBoughtList(creditCardDTO: CreditCardDTO, cutOff: LocalDateTime, cache: Boolean) = TODO()
            override fun getBoughtPeriodList(idCreditCard: Int, cache: Boolean) = emptyList<BoughtCreditCardPeriodDTO>()
            override fun delete(codeBought: Int, cache: Boolean) = true
            override fun restore(codeBought: Int, cache: Boolean) = true
            override fun restoreEndDate(codeBought: Int, cache: Boolean) = true
            override fun endingRecurrentPayment(codeBought: Int, cutOff: LocalDateTime) = true
            override fun endingPayment(codeBought: Int, message: String, cutOff: LocalDateTime) = true
            override fun updateRecurrentValue(codeBought: Int, value: Double, cutOff: LocalDateTime, cache: Boolean) = true
            override fun differntInstallment(codeBought: Int, value: Long, cutOff: LocalDateTime, cache: Boolean) = true
            override fun clone(codeBought: Int, cache: Boolean) = true
            override fun getAllRecurrent(idCreditCard: Int) = emptyList<CreditCardBoughtItemDTO>()
            override fun reactivateRecurrent(codeBought: Int) = true
            override fun changeQuotas(codeBought: Int, months: Int, cache: Boolean) = true
            override fun getAllBoughts(): List<CreditCardBoughtItemDTO> {
                return listOf(
                    CreditCardBoughtItemDTO(
                        id = 1,
                        nameItem = "Laptop Gamer",
                        valueItem = 4500000.0,
                        interest = 2.5,
                        month = 12,
                        interestValue = 112500.0,
                        quoteValue = 375000.0,
                        capitalValue = 375000.0,
                        settings = 0.0,
                        monthPaid = 2,
                        pendingToPay = 3750000.0,
                        tagName = "Tecnología",
                        settingCode = 1,
                        boughtDate = LocalDateTime.now(),
                        cutOutDate = LocalDateTime.now(),
                        createDate = LocalDateTime.now(),
                        endDate = LocalDateTime.now().plusMonths(12),
                        codeCreditCard = 1,
                        nameCreditCard = "Visa",
                        recurrent = false,
                        kind = KindInterestRateEnum.CREDIT_CARD,
                        kindOfTax = KindOfTaxEnum.MONTHLY_EFFECTIVE
                    )
                )
            }
        }
    }

    val ccPort = remember {
        object : ICreditCardPort {
            override fun getCreditCard(codeCreditCard: Int) = CreditCardDTO(1, "Visa", 1.toShort(), 29.toShort(), 1000.toBigDecimal(), LocalDateTime.now(), true, false, false, null)
            override fun getAll() = listOf(
                CreditCardDTO(1, "Visa", 1.toShort(), 29.toShort(), 1000.toBigDecimal(), LocalDateTime.now(), true, false, false, null),
                CreditCardDTO(2, "Mastercard", 1.toShort(), 15.toShort(), 1000.toBigDecimal(), LocalDateTime.now(), true, false, false, null)
            )
            override fun delete(id: Int) = true
            override fun create(dto: CreditCardDTO) = 1
            override fun update(dto: CreditCardDTO) = true
        }
    }

    val vm = remember {
        AllBoughtRecordsViewModel(port, ccPort).apply {
            banksList.value = listOf(Pair(1, "Visa"), Pair(2, "Mastercard"))
            selectedBank.value = Pair(1, "Visa")
            loader.value = false
            list.value = listOf(
                CreditCardBoughtItemDTO(
                    id = 1,
                    nameItem = "Laptop Gamer",
                    valueItem = 4500000.0,
                    interest = 2.5,
                    month = 12,
                    interestValue = 112500.0,
                    quoteValue = 375000.0,
                    capitalValue = 375000.0,
                    settings = 0.0,
                    monthPaid = 2,
                    pendingToPay = 3750000.0,
                    tagName = "Tecnología",
                    settingCode = 1,
                    boughtDate = LocalDateTime.now(),
                    cutOutDate = LocalDateTime.now(),
                    createDate = LocalDateTime.now(),
                    endDate = LocalDateTime.now().plusMonths(12),
                    codeCreditCard = 1,
                    nameCreditCard = "Visa",
                    recurrent = false,
                    kind = KindInterestRateEnum.CREDIT_CARD,
                    kindOfTax = KindOfTaxEnum.MONTHLY_EFFECTIVE,
                    restorable = true
                ),
                CreditCardBoughtItemDTO(
                    id = 2,
                    nameItem = "Supermercado",
                    valueItem = 250000.0,
                    interest = 0.0,
                    month = 1,
                    interestValue = 0.0,
                    quoteValue = 250000.0,
                    capitalValue = 250000.0,
                    settings = 0.0,
                    monthPaid = 1,
                    pendingToPay = 0.0,
                    tagName = "Hogar",
                    settingCode = 1,
                    boughtDate = LocalDateTime.now().minusDays(15),
                    cutOutDate = LocalDateTime.now(),
                    createDate = LocalDateTime.now(),
                    endDate = LocalDateTime.now(),
                    codeCreditCard = 1,
                    nameCreditCard = "Visa",
                    recurrent = false,
                    kind = KindInterestRateEnum.CREDIT_CARD,
                    kindOfTax = KindOfTaxEnum.MONTHLY_EFFECTIVE,
                    restorable = false
                )
            )
            filteredList.value = list.value
        }
    }

    MaterialThemeComposeUI {
        AllBoughtRecordsList(viewModel = vm, initialExpanded = false)
    }
}

@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AllBoughtRecordsListPreviewDark() {
    AllBoughtRecordsListPreviewLight()
}

@Preview(showSystemUi = true, showBackground = true, name = "Expanded View Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun AllBoughtRecordsListExpandedPreviewLight() {
    val port = remember {
        object : IBoughtListPort {
            override fun getBoughtList(creditCardDTO: CreditCardDTO, cutOff: LocalDateTime, cache: Boolean) = TODO()
            override fun getBoughtPeriodList(idCreditCard: Int, cache: Boolean) = emptyList<BoughtCreditCardPeriodDTO>()
            override fun delete(codeBought: Int, cache: Boolean) = true
            override fun restore(codeBought: Int, cache: Boolean) = true
            override fun restoreEndDate(codeBought: Int, cache: Boolean) = true
            override fun endingRecurrentPayment(codeBought: Int, cutOff: LocalDateTime) = true
            override fun endingPayment(codeBought: Int, message: String, cutOff: LocalDateTime) = true
            override fun updateRecurrentValue(codeBought: Int, value: Double, cutOff: LocalDateTime, cache: Boolean) = true
            override fun differntInstallment(codeBought: Int, value: Long, cutOff: LocalDateTime, cache: Boolean) = true
            override fun clone(codeBought: Int, cache: Boolean) = true
            override fun getAllRecurrent(idCreditCard: Int) = emptyList<CreditCardBoughtItemDTO>()
            override fun reactivateRecurrent(codeBought: Int) = true
            override fun changeQuotas(codeBought: Int, months: Int, cache: Boolean) = true
            override fun getAllBoughts(): List<CreditCardBoughtItemDTO> {
                return listOf(
                    CreditCardBoughtItemDTO(
                        id = 1,
                        nameItem = "Laptop Gamer",
                        valueItem = 4500000.0,
                        interest = 2.5,
                        month = 12,
                        interestValue = 112500.0,
                        quoteValue = 375000.0,
                        capitalValue = 375000.0,
                        settings = 0.0,
                        monthPaid = 2,
                        pendingToPay = 3750000.0,
                        tagName = "Tecnología",
                        settingCode = 1,
                        boughtDate = LocalDateTime.now(),
                        cutOutDate = LocalDateTime.now(),
                        createDate = LocalDateTime.now(),
                        endDate = LocalDateTime.now().plusMonths(12),
                        codeCreditCard = 1,
                        nameCreditCard = "Visa",
                        recurrent = false,
                        kind = KindInterestRateEnum.CREDIT_CARD,
                        kindOfTax = KindOfTaxEnum.MONTHLY_EFFECTIVE,
                        restorable = true
                    )
                )
            }
        }
    }

    val ccPort = remember {
        object : ICreditCardPort {
            override fun getCreditCard(codeCreditCard: Int) = CreditCardDTO(1, "Visa", 1.toShort(), 29.toShort(), 1000.toBigDecimal(), LocalDateTime.now(), true, false, false, null)
            override fun getAll() = listOf(
                CreditCardDTO(1, "Visa", 1.toShort(), 29.toShort(), 1000.toBigDecimal(), LocalDateTime.now(), true, false, false, null),
                CreditCardDTO(2, "Mastercard", 1.toShort(), 15.toShort(), 1000.toBigDecimal(), LocalDateTime.now(), true, false, false, null)
            )
            override fun delete(id: Int) = true
            override fun create(dto: CreditCardDTO) = 1
            override fun update(dto: CreditCardDTO) = true
        }
    }

    val vm = remember {
        AllBoughtRecordsViewModel(port, ccPort).apply {
            banksList.value = listOf(Pair(1, "Visa"), Pair(2, "Mastercard"))
            selectedBank.value = Pair(1, "Visa")
            loader.value = false
            list.value = listOf(
                CreditCardBoughtItemDTO(
                    id = 1,
                    nameItem = "Laptop Gamer",
                    valueItem = 4500000.0,
                    interest = 2.5,
                    month = 12,
                    interestValue = 112500.0,
                    quoteValue = 375000.0,
                    capitalValue = 375000.0,
                    settings = 0.0,
                    monthPaid = 2,
                    pendingToPay = 3750000.0,
                    tagName = "Tecnología",
                    settingCode = 1,
                    boughtDate = LocalDateTime.now(),
                    cutOutDate = LocalDateTime.now(),
                    createDate = LocalDateTime.now(),
                    endDate = LocalDateTime.now().plusMonths(12),
                    codeCreditCard = 1,
                    nameCreditCard = "Visa",
                    recurrent = false,
                    kind = KindInterestRateEnum.CREDIT_CARD,
                    kindOfTax = KindOfTaxEnum.MONTHLY_EFFECTIVE,
                    restorable = true
                ),
                CreditCardBoughtItemDTO(
                    id = 2,
                    nameItem = "Supermercado",
                    valueItem = 250000.0,
                    interest = 0.0,
                    month = 1,
                    interestValue = 0.0,
                    quoteValue = 250000.0,
                    capitalValue = 250000.0,
                    settings = 0.0,
                    monthPaid = 1,
                    pendingToPay = 0.0,
                    tagName = "Hogar",
                    settingCode = 1,
                    boughtDate = LocalDateTime.now().minusDays(15),
                    cutOutDate = LocalDateTime.now(),
                    createDate = LocalDateTime.now(),
                    endDate = LocalDateTime.now(),
                    codeCreditCard = 1,
                    nameCreditCard = "Visa",
                    recurrent = false,
                    kind = KindInterestRateEnum.CREDIT_CARD,
                    kindOfTax = KindOfTaxEnum.MONTHLY_EFFECTIVE,
                    restorable = false
                )
            )
            filteredList.value = list.value
        }
    }

    MaterialThemeComposeUI {
        AllBoughtRecordsList(viewModel = vm, initialExpanded = true)
    }
}
