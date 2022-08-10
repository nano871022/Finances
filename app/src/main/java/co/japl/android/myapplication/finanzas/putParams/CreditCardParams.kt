package co.japl.android.myapplication.putParams

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import co.japl.android.myapplication.controller.CreateCreditCard
import co.japl.android.myapplication.putParams.CreditCardParams.Params.ARG_PARAM_CODE

class CreditCardParams(var parentFragmentManagers: FragmentManager) {
    object Params {
        val ARG_PARAM_CODE = "code_credit_card"
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            CreateCreditCard().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_CODE, param1)
                }
            }
    }
}