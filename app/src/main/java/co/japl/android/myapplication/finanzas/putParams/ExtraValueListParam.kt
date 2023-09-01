package co.japl.android.myapplication.finanzas.putParams

import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.enums.AmortizationKindOfEnum

class ExtraValueListParam {

    object PARAMS {
        val PARAMS_AMORTIZATION_CREDIT_ID = "amortization_credit_id"
        val PARAMS_KIND_OF_EXTRA_VALUE = "kind_of_extra_value"
    }
        companion object {

            fun newInstance(id:Int,kindOf:AmortizationKindOfEnum,navController: NavController) {
                val parameters = bundleOf(PARAMS.PARAMS_AMORTIZATION_CREDIT_ID to id
                    , PARAMS.PARAMS_KIND_OF_EXTRA_VALUE to kindOf)
                navController.navigate(R.id.action_amortizationTableFragment_to_extraValueListController,parameters)
            }

            fun newInstance(id:Int,navController: NavController){
                val parameters = bundleOf(PARAMS.PARAMS_AMORTIZATION_CREDIT_ID to id
                    , PARAMS.PARAMS_KIND_OF_EXTRA_VALUE to AmortizationKindOfEnum.EXTRA_VALUE_AMORTIZATION_CREDIT)
                navController.navigate(R.id.action_amortizationCreditFragment_to_extraValueListController,parameters)
            }


            fun download(parameters: Bundle):Pair<Int,AmortizationKindOfEnum> {
                val id = parameters.getInt(PARAMS.PARAMS_AMORTIZATION_CREDIT_ID)?:0
                val kindOf = parameters.get(PARAMS.PARAMS_KIND_OF_EXTRA_VALUE)?.let{it as AmortizationKindOfEnum}?:AmortizationKindOfEnum.EXTRA_VALUE_AMORTIZATION
                return Pair(id,kindOf)
            }

            fun toBack(navController: NavController)=navController.popBackStack()

        }
}