package co.japl.android.myapplication.finanzas.view.recap

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import co.com.japl.finances.iports.dtos.RecapDTO
import co.com.japl.ui.Prefs
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.controller.RecapViewModel
import co.com.japl.ui.components.Carousel
import co.com.japl.ui.theme.values.Dimensions
import co.japl.android.myapplication.finanzas.view.components.PieceOfPieDraw

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Recap(model:RecapViewModel= viewModel()) {
    model.main()
    val currentProgress = remember { model.currentProgress }
    val loading = remember {model.loading }

    Column {
        if(loading) {
            LinearProgressIndicator(
                progress = currentProgress,
                modifier = Modifier.fillMaxWidth()
            )
        }else{
            CarouselView(model = model)

            PieceOfPieDraw(model.recap)
        }

    }

    DialogStart(model)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CarouselView(model:RecapViewModel){
    Carousel(
        size = 4, modifier = Modifier.height(250.dp)
    ) {
        when (it) {
            0 -> CardRecap(
                title = stringResource(id = R.string.recap),
                model.totalInbound,
                model.totalPayed + model.totalCredits + model.totalCreditCard
            )

            1 -> CardProjections(
                title = stringResource(id = R.string.projections),
                totalSaved = model.totalSaved,
                projectionValue = model.projectionsValue
            )

            2 -> CardPaymentAndCredits(
                title = stringResource(id = R.string.cash_paids_credits),
                totalInbound = model.totalInbound,
                totalPayed = model.totalPayed,
                totalCredits = model.totalCredits,
            )

            3 -> CardCredits(
                title = stringResource(id = R.string.credit_cards),
                totalCC = model.totalCreditCard,
                warningValue = model.warningValue
            )
        }
    }
}

@Composable
private fun DialogStart(model:RecapViewModel){
    val status = remember {model.msgInitial }
    val context= LocalContext.current
    val wikiUrl = stringResource(id = R.string.wiki_url).toUri()
    if(false /*status.value*/){
   Dialog(onDismissRequest = { status.value = false }) {

       Surface(
           shape = RoundedCornerShape(10.dp), modifier = Modifier.padding(Dimensions.PADDING_SHORT)
       ) {
           Column(modifier = Modifier.padding(Dimensions.PADDING_SHORT)) {
               Row {
                   Text(
                       text = stringResource(id = R.string.welcome_app),
                       modifier = Modifier.fillMaxWidth().weight(2f).align(Alignment.CenterVertically)
                   )

                   Button(onClick = { status.value = false }) {
                       Icon(imageVector = Icons.Rounded.Close,
                           contentDescription = stringResource(id = R.string.close))
                   }
               }
               Divider(
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(top = Dimensions.PADDING_SHORT, bottom = Dimensions.PADDING_SHORT)
               )
               Text(text = stringResource(id = R.string.app_msg))
               Text(text = stringResource(id = R.string.simulators))
               TextButton(onClick = {

               }){Text(text = stringResource(id = R.string.simulator_vary_quote),color = MaterialTheme.colorScheme.onSurface)}
               TextButton(onClick = {

               }){Text(text = stringResource(id = R.string.simulator_fix_quote),color = MaterialTheme.colorScheme.onSurface)}
               Divider(modifier = Modifier.fillMaxWidth())
               TextButton(onClick = {

               }){Text(text = stringResource(id = R.string.projection_payment),color = MaterialTheme.colorScheme.onSurface)}
               TextButton(onClick = {}){Text(text = stringResource(id = R.string.payment_cash),color = MaterialTheme.colorScheme.onSurface)}
               TextButton(onClick = {}){Text(text = stringResource(id = R.string.payment_credits),color = MaterialTheme.colorScheme.onSurface)}
               TextButton(onClick = {}){Text(text = stringResource(id = R.string.payment_credit_card),color = MaterialTheme.colorScheme.onSurface)}
                Button(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, wikiUrl)
                    ContextCompat.startActivity(context,intent,null)

                }) {
                    Text(text = stringResource(id = R.string.wiki_sms))
                }
           }
       }
   }
   }

}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewRecap() {
    val prefs = Prefs(LocalContext.current)

    val model = RecapViewModel(null,null,prefs,null)
    val recap = RecapDTO(
      projectionSaved = 20000.0,
     projectionNext = 30000.0,
     totalPaid = 40000.0,
     totalQuoteCredit = 50000.0,
     totalInputs = 60000.0,
     totalQuoteCreditCard = 70000.0,
     warningValueCreditCard = 80000.0
    )
    model.setRecap(recap)
    co.com.japl.ui.theme.MaterialThemeComposeUI {
        Recap(model)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewRecapDark() {
    val prefs = Prefs(LocalContext.current)

    val model = RecapViewModel(null,null,prefs,null)
    val recap = RecapDTO(
        projectionSaved = 20000.0,
        projectionNext = 30000.0,
        totalPaid = 40000.0,
        totalQuoteCredit = 50000.0,
        totalInputs = 60000.0,
        totalQuoteCreditCard = 70000.0,
        warningValueCreditCard = 80000.0
    )
    model.setRecap(recap)
    co.com.japl.ui.theme.MaterialThemeComposeUI {
        Recap(model)
    }
}