package co.com.japl.module.creditcard.views.bought

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.controllers.bought.lists.RecurrentBoughtViewModel
import co.com.japl.module.creditcard.enums.MoreOptionsRecurrentBought
import co.com.japl.ui.components.MoreOptionsDialog
import co.com.japl.ui.components.AlertDialogOkCancel
import co.japl.android.myapplication.utils.NumbersUtil
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun RecurrentBoughtList(
    codeCreditCard: Int,
    navController: NavController,
    viewModel: RecurrentBoughtViewModel,
    onEdit: (CreditCardBoughtItemDTO) -> Unit,
    onAlter: (CreditCardBoughtItemDTO) -> Unit
) {
    LaunchedEffect(codeCreditCard, viewModel.loader.value) {
        if (!viewModel.loader.value) {
            viewModel.load(codeCreditCard)
        }
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text(
                    text = stringResource(id = R.string.total_active_recurrent) + ": ${viewModel.totalActive.value}",
                    style = MaterialTheme.typography.headlineSmall
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(onClick = { viewModel.setFilter(null) }) { Text(text = stringResource(id = R.string.all)) }
                    Button(onClick = { viewModel.setFilter(true) }) { Text(text = stringResource(id = R.string.active)) }
                    Button(onClick = { viewModel.setFilter(false) }) { Text(text = stringResource(id = R.string.inactive)) }
                }
            }
        }
    ) { paddingValues ->
        val groupedList = viewModel.filteredList.groupBy { YearMonth.of(it.createDate.year, it.createDate.month) }

        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            groupedList.forEach { (month, items) ->
                item {
                    Text(
                        text = month.format(DateTimeFormatter.ofPattern("MMM yyyy")),
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
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
fun RecurrentBoughtItem(
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
            listOptions = options,
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
                Text(text = item.boughtDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), style = MaterialTheme.typography.bodySmall)
                Text(text = item.nameItem, style = MaterialTheme.typography.titleMedium)
                Text(text = NumbersUtil.COPtoString(item.valueItem), style = MaterialTheme.typography.bodyLarge)
            }
            IconButton(onClick = { showMenu = true }) {
                Icon(painter = painterResource(id = co.com.japl.ui.R.drawable.more_vertical), contentDescription = null)
            }
        }
    }
}
