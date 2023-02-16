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
    private lateinit var tvCapital: TextView
    private lateinit var tvCurrentCapital: TextView
    private lateinit var tvQuoteCapital: TextView
    private lateinit var tvCurrentInterest: TextView
    private lateinit var tvQuoteInterest: TextView
    private lateinit var tvInterest: TextView
    private lateinit var tvPendingToPay: TextView
    private lateinit var tvTotalQuote: TextView
    lateinit var recyclerView: RecyclerView



    override fun setFields(actions: View.OnClickListener?) {
        tvCapital = view.findViewById(R.id.tvCapitalList)
        tvCurrentCapital = view.findViewById(R.id.tvCurrentCapitalList)
        tvQuoteCapital = view.findViewById(R.id.tvQuoteCapitalList)
        tvCurrentInterest = view.findViewById(R.id.tvCurrentInterestList)
        tvQuoteInterest = view.findViewById(R.id.tvQuoteInterestList)
        tvInterest = view.findViewById(R.id.tvInterestList)
        tvPendingToPay = view.findViewById(R.id.tvPendingToPayList)
        tvTotalQuote = view.findViewById(R.id.tvTotalQuoteList)
        recyclerView = view.findViewById(R.id.list_bought)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun loadFields(values: BoughtRecap) {
        tvCapital.text = NumbersUtil.COPtoString(values.capitalValue.orElse(BigDecimal.ZERO))
        tvCurrentCapital.text = NumbersUtil.COPtoString(values.currentValueCapital.orElse(BigDecimal.ZERO))
        tvQuoteCapital.text = NumbersUtil.COPtoString(values.quotesValueCapital.orElse(BigDecimal.ZERO))
        tvCurrentInterest.text = NumbersUtil.COPtoString(values.currentValueInterest.orElse(BigDecimal.ZERO))
        tvQuoteInterest.text = NumbersUtil.COPtoString(values.quotesValueInterest.orElse(BigDecimal.ZERO))
        tvInterest.text = NumbersUtil.COPtoString(values.interestValue.orElse(BigDecimal.ZERO))
        tvPendingToPay.text = NumbersUtil.COPtoString(values.pendingToPay.orElse(BigDecimal.ZERO))
        tvTotalQuote.text = NumbersUtil.COPtoString(values.totalValue.orElse(BigDecimal.ZERO))
    }

    override fun downLoadFields(): BoughtRecap {
        return BoughtRecap()
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