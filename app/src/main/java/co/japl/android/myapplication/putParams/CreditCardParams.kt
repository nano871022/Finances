package co.japl.android.myapplication.putParams

import android.os.Bundle
import co.japl.android.myapplication.controller.CreateCreditCard
import co.japl.android.myapplication.putParams.CreditCardParams.Params.ARG_PARAM1
import co.japl.android.myapplication.putParams.CreditCardParams.Params.ARG_PARAM2

class CreditCardParams {
    object Params {
        val ARG_PARAM1 = "param1"
        val ARG_PARAM2 = "param2"
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateCreditCard().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}