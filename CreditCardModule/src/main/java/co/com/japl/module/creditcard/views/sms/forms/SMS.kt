package co.com.japl.module.creditcard.views.sms.forms

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import co.com.japl.module.creditcard.controllers.smscreditcard.form.SmsCreditCardViewModel
import co.com.japl.ui.theme.MaterialThemeComposeUI
import  androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import co.com.japl.module.creditcard.R
import co.com.japl.ui.components.FieldSelect
import co.com.japl.ui.components.FieldText
import co.com.japl.ui.components.FloatButton
import co.com.japl.ui.theme.values.Dimensions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun Sms(viewModel:SmsCreditCardViewModel){
    val load by remember {viewModel.load}
    var progress  by remember {viewModel.progress}

    CoroutineScope(Dispatchers.IO).launch {
        viewModel.main()
    }
    if(load){
        LinearProgressIndicator( progress = progress, modifier = Modifier.fillMaxWidth())
    }else{
        Form(viewModel = viewModel)
    }

}

@Composable
private fun Form(viewModel: SmsCreditCardViewModel){
    Scaffold (
        floatingActionButton = {
            Buttons({viewModel.clean()}, {viewModel.save()})
        }
    ) {
        Body(viewModel = viewModel, modifier = Modifier
            .padding(it)
            .fillMaxWidth()
            .padding(bottom = Dimensions.PADDING_SHORT))
    }
}

@Composable
private fun Body(viewModel: SmsCreditCardViewModel,modifier:Modifier){
    val listCreditCard = remember {viewModel.creditCardList}
    val creditCard = remember { viewModel.creditCard }
    val errorCreditCard =remember {viewModel.errorCreditCard}
    val kindInterest = remember { viewModel.kindInterestRate}
    val kinInterestList = remember{viewModel.kindInterestRateList}
    val errorKindInterest = remember{ viewModel.errorKindInterestRate}
    val phoneNumber =remember{viewModel.phoneNumber}
    val errorPhoneNumber = remember{viewModel.errorPhoneNumber}
    val pattern = remember{viewModel.pattern}
    val errorPattern = remember{viewModel.errorPattern}

  Column{
      FieldSelect(title = stringResource(id = R.string.credit_card),
          value = creditCard.value?.second?:"",
          list = listCreditCard,
          isError = errorCreditCard,
          modifier = modifier,
          callAble = {
              it?.let{
                  creditCard.value = it
                  viewModel.validate()
              }

          })

      FieldSelect(title = stringResource(id = R.string.kind_rate),
          value = kindInterest.value?.second?:"",
          list = kinInterestList,
          isError = errorKindInterest,
          modifier = modifier,
          callAble = {
              it?.let{
                  kindInterest.value = it
                  viewModel.validate()
              }
          })

      FieldText(title = stringResource(id = R.string.phone_number)
      , value = phoneNumber.value
              , hasErrorState = errorPhoneNumber,
          validation = {viewModel.validate()},
          callback = {
              phoneNumber.value = it
                     },
          modifier = modifier)

      FieldText(title = stringResource(id = R.string.pattern)
          , value = pattern.value
          , hasErrorState = errorPattern,
          validation = {viewModel.validate()},
          lines = 3,
          callback = {
              pattern.value = it
          },
          modifier = modifier)


      TextButton(onClick = { viewModel.validatePatternWithMessages() },
          modifier = modifier.align(alignment = Alignment.End)) {
          Text(text = stringResource(id = R.string.validate))
      }

    FieldText(title = stringResource(id = R.string.validate)
          , value = pattern.value
          , hasErrorState = errorPattern,
          validation = {viewModel.validate()},
          lines = 3,
          readOnly = true,
          callback = {
              pattern.value = it
          },
          modifier = modifier)


  }
}

@Composable
private fun Buttons(clean:()->Unit, save:()->Unit){
    Column {
        FloatButton(
            imageVector = Icons.Rounded.CleaningServices,
            descriptionIcon = co.com.japl.ui.R.string.clear
        ) {
            clean.invoke()
        }

        FloatButton(
            imageVector = Icons.Rounded.Save,
            descriptionIcon = R.string.save
        ) {
            save.invoke()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
internal fun SmsPreview(){
    MaterialThemeComposeUI {
        Sms(getViewModel())
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
internal fun SmsPreviewLight(){
    MaterialThemeComposeUI {
        Sms(getViewModel())
    }
}
@Composable
private fun getViewModel():SmsCreditCardViewModel{
    val viewModel = SmsCreditCardViewModel(null,null,null,null)
    viewModel.load.value = false
    return viewModel
}