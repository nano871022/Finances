package co.japl.android.myapplication.finanzas.holders

import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListPaymentsAdapter
import co.japl.android.myapplication.finanzas.bussiness.DTO.CheckCreditDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.CheckPaymentsDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.CheckQuoteDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.ICheck
import co.japl.android.myapplication.finanzas.enums.CheckPaymentsEnum
import co.japl.android.myapplication.finanzas.holders.interfaces.IListHolder
import co.japl.android.myapplication.finanzas.pojo.CheckPaymentsPOJO
import co.japl.android.myapplication.utils.NumbersUtil
import java.math.BigDecimal

class CheckPaymentsHolder(val view:View) : IListHolder<CheckPaymentsHolder,CheckPaymentsPOJO>{
    lateinit var recyclerView:RecyclerView
    lateinit var paid:TextView
    lateinit var toPay:TextView
    lateinit var btnSave:Button
    lateinit var progressBar: ProgressBar
    var payment = BigDecimal.ZERO
    lateinit var checkPaymentsList:MutableList<ICheck>

    override fun setFields(actions: View.OnClickListener?) {
        recyclerView = view.findViewById(R.id.rv_item_cps)
        paid = view.findViewById(R.id.et_paid_cps)
        toPay = view.findViewById(R.id.et_payments_cps)
        btnSave = view.findViewById(R.id.btn_add_cps)
        progressBar = view.findViewById(R.id.pb_load_cps)
        btnSave.setOnClickListener(actions)
        btnSave.visibility = View.GONE
    }

    override fun loadFields(fn: ((CheckPaymentsHolder) -> Unit)?) { fn?.let{it.invoke(this)}}

    public fun  set(checkPaymentsList:MutableList<ICheck>){
        this.checkPaymentsList = checkPaymentsList
    }

    override fun loadRecycler(data: MutableList<CheckPaymentsPOJO>) {
        if(this::checkPaymentsList.isInitialized) {
            recyclerView.layoutManager = LinearLayoutManager(
                view.context,
                LinearLayoutManager.VERTICAL, false
            )
            payment = data.sumOf { it.value }
            toPay.text = NumbersUtil.toString(payment)
            ListPaymentsAdapter(data, checkPaymentsList) { adapter, holder ->
                paid.text = NumbersUtil.toString(adapter.paid)
                val sum = data.sumOf { it.value }
                val sumPaid =
                    data.filter{ it.type == CheckPaymentsEnum.PAYMENTS}
                        .filter { paid -> checkPaymentsList
                            .filterIsInstance<CheckPaymentsDTO>()
                            .firstOrNull { it.codPaid == paid.codPaid.toInt() } != null }
                        .sumOf { it.value } +
                 data.filter{ it.type == CheckPaymentsEnum.CREDITS}
                     .filter { paid -> checkPaymentsList
                         .filterIsInstance<CheckCreditDTO>()
                         .firstOrNull { it.codCredit == paid.codPaid.toInt() } != null }
                    .sumOf { it.value } +
                 data.filter{ it.type == CheckPaymentsEnum.QUOTE_CREDIT_CARD}
                     .filter { paid -> checkPaymentsList
                         .filterIsInstance<CheckQuoteDTO>()
                         .firstOrNull { it.codQuote == paid.codPaid.toInt() } != null }
                                .sumOf { it.value }

                if (sum == sumPaid) {
                    btnSave.visibility = View.GONE
                    adapter.checkEnable = false
                    holder.check.isEnabled = false
                }
            }.let { recyclerView.adapter = it }
            if (payment > BigDecimal.ZERO) {
                btnSave.visibility = View.VISIBLE
            }
            progressBar.visibility = View.GONE
        }
    }

}