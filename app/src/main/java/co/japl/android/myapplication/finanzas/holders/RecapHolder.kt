package co.japl.android.myapplication.finanzas.holders

import android.view.View
import android.widget.TextView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.holders.interfaces.IRecapHolder

class RecapHolder(val view:View):IRecapHolder<RecapHolder> {
    lateinit var inputs:TextView
    lateinit var saved:TextView
    lateinit var quoteSaved:TextView
    lateinit var totalPaid:TextView
    lateinit var totalCredits:TextView
    lateinit var totalFix:TextView
    lateinit var totalInputFix:TextView
    lateinit var quoteTC:TextView
    lateinit var totalPaids:TextView
    lateinit var totalInputsPaids:TextView

    override fun setFields(actions: View.OnClickListener?) {
        inputs = view.findViewById(R.id.tv_total_inputs_rec)
        saved = view.findViewById(R.id.tv_saved_rec)
        quoteSaved = view.findViewById(R.id.tv_quote_save_rec)
        totalPaid = view.findViewById(R.id.tv_total_paid_rec)
        totalCredits = view.findViewById(R.id.tv_total_credits_rec)
        totalFix = view.findViewById(R.id.tv_total_paids_credits_rec)
        totalInputFix = view.findViewById(R.id.tv_total_inputs_fix)
        quoteTC = view.findViewById(R.id.tv_total_quote_credit_card_rec)
        totalPaids = view.findViewById(R.id.tv_total_paids_rec)
        totalInputsPaids = view.findViewById(R.id.tv_total_input_paids_rec)
    }

    override fun loadFields(fn: ((RecapHolder) -> Unit)?) {
        fn?.let {
            it.invoke(this)
        }
    }

}