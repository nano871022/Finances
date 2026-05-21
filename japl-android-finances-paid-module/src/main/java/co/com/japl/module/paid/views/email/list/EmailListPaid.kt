package co.com.japl.module.paid.views.email.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.com.japl.module.paid.R
import co.com.japl.module.paid.controllers.emailpaid.list.EmailListPaidViewModel
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.components.LabelValue
import co.com.japl.ui.theme.values.Dimensions

@Composable
fun EmailListPaid(viewModel: EmailListPaidViewModel) {
    val load by viewModel.load
    val list = viewModel.list

    LaunchedEffect(Unit) {
        viewModel.main()
    }

    Scaffold(
        floatingActionButton = { FloatButton(imageVector = Icons.Rounded.Add, descriptionIcon = R.string.go_to_add) { viewModel.add() } }
    ) {
        Column(modifier = Modifier.padding(it).fillMaxWidth()) {
            if (load) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            } else {
                LazyColumn {
                    items(list) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(Dimensions.PADDING_SHORT),
                            onClick = { viewModel.edit(item.id) }
                        ) {
                            Column(modifier = Modifier.padding(Dimensions.PADDING_SHORT)) {
                                LabelValue(label = stringResource(R.string.account), value = item.nameAccount)
                                LabelValue(label = stringResource(R.string.sender), value = item.sender)
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                    IconButton(onClick = { viewModel.clone(item.id) }) {
                                        Icon(imageVector = Icons.Rounded.ContentCopy, contentDescription = stringResource(R.string.copy))
                                    }
                                    IconButton(onClick = { viewModel.delete(item.id) }) {
                                        Icon(imageVector = Icons.Rounded.Delete, contentDescription = stringResource(R.string.delete))
                                    }
                                    Switch(checked = item.active, onCheckedChange = { active -> viewModel.activate(item.id, active) })
                                }
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(100.dp)) }
                }
            }
        }
    }
}
