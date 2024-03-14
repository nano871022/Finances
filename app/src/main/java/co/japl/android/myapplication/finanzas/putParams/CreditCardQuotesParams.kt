package co.japl.android.myapplication.finanzas.putParams

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.com.japl.ui.utils.DateUtils
import java.time.LocalDateTime

class CreditCardQuotesParams {
    object Params{
        val PARAM_CREDIT_CARD_CODE = "CreditCardCode"
        val PARAM_CREDIT_CARD_NAME = "CreditCardName"
        val PARAM_CREDIT_CARD_CUTOFF = "CreditCardCutoff"
        val PARAM_CREDIT_CARD_CUTOFF_DAY = "CreditCardCutOffDay"
        val PARAM_BOUGHT_ID = "bought_id_credit_card"
        val PARAM_CODE_CREDIT_CARD = "codeCreditCard"
        val PARAM_CUTOFF_DAY = "cutOffDay"
        val PARAM_CUTOFF = "cutOff"
        val PARAM_DEEPLINK = "android-support-nav:controller:deepLinkIntent"
    }
    companion object {
        object ListBought{
            fun newInstanceQuote(quoteId:Int,creditCard:Int,navController: NavController){
                val parameters = bundleOf(Params.PARAM_CREDIT_CARD_CODE  to creditCard.toString(),Params.PARAM_BOUGHT_ID to quoteId)
                navController.navigate(R.id.action_list_bought_to_buy_credit_card,parameters)
            }

            fun newInstanceAdvance(quoteId:Int,creditCard:Int,navController: NavController){
                val parameters = bundleOf(Params.PARAM_CREDIT_CARD_CODE  to creditCard.toString(),Params.PARAM_BOUGHT_ID to quoteId)
                navController.navigate(R.id.action_list_bought_to_cash_advance_fragment,parameters)
            }

            fun newInstanceWallet(quoteId:Int,creditCard:Int,navController: NavController){
                val parameters = bundleOf(Params.PARAM_CREDIT_CARD_CODE  to creditCard.toString(),Params.PARAM_BOUGHT_ID to quoteId)
                navController.navigate(R.id.action_list_bought_to_boughWalletController,parameters)
            }

            fun newInstanceFloat(quoteId:Int,creditCard:Int,navController: NavController){
                val parameters = bundleOf(Params.PARAM_CREDIT_CARD_CODE  to creditCard.toString(),Params.PARAM_BOUGHT_ID to quoteId)
                navController.navigate(R.id.action_list_bought_to_buy_credit_card2,parameters)
            }

            fun toBack(navController: NavController) {
                navController.popBackStack()
            }
        }

        object Historical {
            @RequiresApi(Build.VERSION_CODES.O)
            fun newInstance(code: Int, cutOffDay:Short,cutOff: LocalDateTime, navController: NavController) {
                val parameter = bundleOf(
                    Params.PARAM_CREDIT_CARD_CODE to code,
                    Params.PARAM_CREDIT_CARD_CUTOFF to DateUtils.localDateTimeToString(cutOff),
                    Params.PARAM_CREDIT_CARD_CUTOFF_DAY to cutOffDay
                )
                navController.navigate(R.id.action_list_cc_quote_to_list_bought, parameter)
            }

            @RequiresApi(Build.VERSION_CODES.O)
            fun download(argument: Bundle): Triple<Int, LocalDateTime,Short> {
                argument.let {
                    var code = 0
                    var cutOff = LocalDateTime.now()
                    var cutoffDay: Short = 0
                    if(it.containsKey(Params.PARAM_CREDIT_CARD_CODE)){
                        code = it.get(Params.PARAM_CREDIT_CARD_CODE).toString().toInt()
                    }
                    if(it.containsKey(Params.PARAM_CREDIT_CARD_CUTOFF)) {
                         cutOff = DateUtils.toLocalDateTime(it.get(Params.PARAM_CREDIT_CARD_CUTOFF).toString())
                    }
                    if(it.containsKey(Params.PARAM_CREDIT_CARD_CUTOFF_DAY)) {
                         cutoffDay = it.get(Params.PARAM_CREDIT_CARD_CUTOFF_DAY).toString().toShort()
                    }

                    if(it.containsKey(PeriodsParams.Params.PARAM_DEEPLINK)) {
                        val intent = it.get(PeriodsParams.Params.PARAM_DEEPLINK) as Intent
                        val uri = Uri.parse(intent.dataString)
                            uri.getQueryParameters(Params.PARAM_CODE_CREDIT_CARD).let {
                            code = it[0]!!.toInt()
                            }
                        uri.getQueryParameters(Params.PARAM_CUTOFF).let {
                            cutOff = DateUtils.toLocalDateTime(it[0]!!)
                        }
                        uri.getQueryParameters(Params.PARAM_CUTOFF_DAY).let {
                            cutoffDay = it[0]!!.toShort()
                        }
                    }
                    return Triple(code, cutOff,cutoffDay)
                }
            }

            fun toBack(navController: NavController) {
                navController.popBackStack()
            }
        }
        object CreateQuote {
            @RequiresApi(Build.VERSION_CODES.O)
            fun newInstance(code: Int, name: String, navController: NavController) {
                val parameter = bundleOf(
                    Params.PARAM_CREDIT_CARD_CODE to code,
                    Params.PARAM_CREDIT_CARD_NAME to name
                )
                navController.navigate(R.id.action_item_menu_side_boughtmade_to_buy_credit_card, parameter)
            }


            @RequiresApi(Build.VERSION_CODES.O)
            fun download(argument: Bundle): Triple<Int, String,Int> {
                argument.let {
                    if(it.containsKey(Params.PARAM_DEEPLINK)) {
                        val intent = it.get(Params.PARAM_DEEPLINK) as Intent
                        val code = if(Uri.parse(intent.dataString).getQueryParameter(Params.PARAM_CREDIT_CARD_CODE) != null){
                             Uri.parse(intent.dataString).getQueryParameter(Params.PARAM_CREDIT_CARD_CODE)!!.toInt()
                        }else{
                            0
                        }
                        return Triple(code, "",0)
                    }else {
                        val code = it.get(Params.PARAM_CREDIT_CARD_CODE).toString().toInt()
                        val name = it.get(Params.PARAM_CREDIT_CARD_NAME).toString()
                        val id = it.get(Params.PARAM_BOUGHT_ID).toString().toInt()
                        return Triple(code, name, id)
                    }
                }
            }

            fun toBack(navController: NavController) {
                navController.popBackStack()
            }
        }
    }


}