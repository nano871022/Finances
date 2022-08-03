package co.japl.android.finanzas.putParams

import android.os.Bundle
import co.japl.android.finanzas.controller.Taxes
import co.japl.android.finanzas.putParams.TaxesParams.Params.ARG_PARAM1
import co.japl.android.finanzas.putParams.TaxesParams.Params.ARG_PARAM2

class TaxesParams {
    object Params {
        val ARG_PARAM1 = "param1"
        val ARG_PARAM2 = "param2"
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Taxes().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}