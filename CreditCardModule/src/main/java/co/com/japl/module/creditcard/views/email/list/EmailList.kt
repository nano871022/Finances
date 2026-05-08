package co.com.japl.module.creditcard.views.email.list

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.com.japl.finances.iports.dtos.EmailCreditCardDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.module.creditcard.R
import co.com.japl.module.creditcard.controllers.emailcreditcard.list.EmailCreditCardViewModel
import co.com.japl.module.creditcard.enums.MoreOptionsItemEmailCreditCard
import co.com.japl.ui.components.AlertDialogOkCancel
import co.com.japl.ui.components.Carousel
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.components.HelpWikiButton
import co.com.japl.ui.components.IconButton
import co.com.japl.ui.components.MoreOptionsDialog
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.com.japl.ui.theme.values.ModifiersCustom.Weight1f
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun EmailList(viewModel: EmailCreditCardViewModel) {
    val loader = remember { viewModel.load }
    val progress = remember { viewModel.progress }

    LaunchedEffect(key1 = viewModel) {
        viewModel.main()
    }

    if (loader.value) {
        LinearProgressIndicator(
            progress = { progress.floatValue },
            modifier = Modifier.fillMaxWidth(),
        )
    } else {
        Body(viewModel = viewModel)
    }
}

@Composable
private fun Body(viewModel: EmailCreditCardViewModel) {
    Scaffold(
        floatingActionButton = {
            Buttons {
                viewModel.add()
            }
        }
    ) {
        Content(viewModel = viewModel, modifier = Modifier.padding(it))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Content(viewModel: EmailCreditCardViewModel, modifier: Modifier) {
    val list = remember { viewModel.list }
    val stateScroll = rememberScrollState(0)

    Column(modifier = modifier.verticalScroll(stateScroll)) {
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            HelpWikiButton(
                wikiUrl = R.string.wiki_sms_credit_card_url, // TODO: Update with email wiki if available
                descriptionContent = R.string.wiki_sms_credit_card_description
            )
        }
        if (list.isNotEmpty()) {
            Carousel(size = list.size) {
                Column(
                    modifier = modifier.padding(bottom = Dimensions.PADDING_BOTTOM_SPACE_FLOATING_BUTTON)
                ) {
                    list[it]?.values?.forEach {
                        for (i in it) {
                            Card(email = i, modifier = Modifier, edit = {
                                viewModel.edit(it)
                            }, delete = {
                                viewModel.delete(it)
                            }, enable = {
                                viewModel.enabled(it)
                            }, disable = {
                                viewModel.disabled(it)
                            }, duplicate = {
                                viewModel.duplicate(it)
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Card(
    email: EmailCreditCardDTO,
    modifier: Modifier = Modifier,
    edit: (Int) -> Unit,
    delete: (Int) -> Unit,
    enable: (Int) -> Unit,
    disable: (Int) -> Unit,
    duplicate: (Int) -> Unit
) {
    val popupStable = remember { mutableStateOf(false) }
    val popupDeleteDialog = remember { mutableStateOf(false) }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = Dimensions.PADDING_SHORT,
                end = Dimensions.PADDING_SHORT,
                top = Dimensions.PADDING_SHORT
            ), border = BorderStroke(width = 1.dp, color = if (email.active) Color.Unspecified else Color.Red)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(Dimensions.PADDING_SHORT)
        ) {
            Text(text = email.nameCreditCard, modifier = Weight1f())

            Text(text = email.kindInterestRateEnum.name, modifier = Weight1f())

            Text(text = email.sender, modifier = Weight1f(), textAlign = TextAlign.End)

            IconButton(painter = R.drawable.more_vertical,
                descriptionContent = R.string.see_more, onClick = {
                    popupStable.value = true
                })
        }
    }
    if (popupStable.value) {
        MoreOptionsDialog(listOptions = MoreOptionsItemEmailCreditCard.values().filter {
            (email.active && it != MoreOptionsItemEmailCreditCard.ENABLE) || (!email.active && it != MoreOptionsItemEmailCreditCard.DISABLE)
        }.toList(),
            onDismiss = { popupStable.value = false }) {
            when (it) {
                MoreOptionsItemEmailCreditCard.DELETE -> popupDeleteDialog.value = true
                MoreOptionsItemEmailCreditCard.EDIT -> edit.invoke(email.id)
                MoreOptionsItemEmailCreditCard.ENABLE -> enable.invoke(email.id)
                MoreOptionsItemEmailCreditCard.DISABLE -> disable.invoke(email.id)
                MoreOptionsItemEmailCreditCard.DUPLICATE -> duplicate.invoke(email.id)
            }
            popupStable.value = false
        }
    }
    if (popupDeleteDialog.value) {
        AlertDialogOkCancel(
            R.string.do_you_want_to_delete_this_record, R.string.delete, onDismiss = {
                popupDeleteDialog.value = false
            }, onClick = {
                delete.invoke(email.id)
                popupDeleteDialog.value = false
            })
    }
}

@Composable
private fun Buttons(create: () -> Unit) {
    FloatButton(imageVector = Icons.Rounded.Add, descriptionIcon = R.string.create) {
        create.invoke()
    }
}
