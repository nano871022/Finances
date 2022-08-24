package co.japl.android.myapplication.finanzas.holders

import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.ISpinnerHolder
import co.japl.android.myapplication.finanzas.pojo.BoughtRecap
import co.japl.android.myapplication.finanzas.pojo.QuoteCreditCard
import co.japl.android.myapplication.utils.NumbersUtil
import java.math.BigDecimal

class ListBoughtHolder(var view:View):IHolder<BoughtRecap> , ISpinnerHolder<ListBoughtHolder>{
    lateinit var tvCapital: TextView
    lateinit var tvInterest: TextView
    lateinit var tvPendingToPay: TextView
    lateinit var tvTotalQuote: TextView
    lateinit var recyclerView: RecyclerView



    override fun setFields(actions: View.OnClickListener?) {
        tvCapital = view.findViewById(R.id.tvCapitalList)
        tvInterest = view.findViewById(R.id.tvInterestList)
        tvPendingToPay = view.findViewById(R.id.tvPendingToPayList)
        tvTotalQuote = view.findViewById(R.id.tvTotalQuoteList)
        recyclerView = view.findViewById(R.id.list_bought)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun loadFields(values: BoughtRecap) {
        tvCapital.text = NumbersUtil.COPtoString(values.capitalValue.orElse(BigDecimal.ZERO))
        tvInterest.text = NumbersUtil.COPtoString(values.interestValue.orElse(BigDecimal.ZERO))
        tvPendingToPay.text = NumbersUtil.COPtoString(values.pendingToPay.orElse(BigDecimal.ZERO))
        tvTotalQuote.text = NumbersUtil.COPtoString(values.totalValue.orElse(BigDecimal.ZERO))
    }

    override fun downLoadFields(): BoughtRecap {
        val recap = BoughtRecap()
        return recap
    }

    override fun cleanField() {
    }

    override fun validate(): Boolean {
        return true
    }

    override fun lists(fn: ((ListBoughtHolder) -> Unit)?) {
        fn?.invoke(this)
    }
}