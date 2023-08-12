package co.japl.android.myapplication.adapter

import android.app.AlertDialog
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.CheckCreditDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.CheckPaymentsDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.CheckQuoteDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.ICheck
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.PaidImpl
import co.japl.android.myapplication.finanzas.enums.CheckPaymentsEnum
import co.japl.android.myapplication.finanzas.holders.CheckPaymentsHolder
import co.japl.android.myapplication.finanzas.holders.validations.COPToBigDecimal
import co.japl.android.myapplication.finanzas.pojo.CheckPaymentsPOJO
import co.japl.android.myapplication.holders.view.PaidItemHolder
import co.japl.android.myapplication.holders.view.PaymentsItemHolder
import co.japl.android.myapplication.utils.DateUtils
import com.google.android.material.snackbar.Snackbar
import java.math.BigDecimal
import java.time.LocalDateTime

class ListPaymentsAdapter(var data:MutableList<CheckPaymentsPOJO>,
                          private val checkPaymentList:MutableList<ICheck>, val fn: ((ListPaymentsAdapter, PaymentsItemHolder) -> Unit)?) :
    RecyclerView.Adapter<PaymentsItemHolder>() {
    private lateinit var view:View
    var paid:BigDecimal = BigDecimal.ZERO
    var listPaid:ArrayList<CheckPaymentsPOJO> = arrayListOf()
    var listPaidRemove:ArrayList<CheckPaymentsPOJO> = arrayListOf()
    var checkEnable = true


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentsItemHolder {
        view = parent
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_check_payments_item_list, parent, false)
        val viewHolder =  PaymentsItemHolder(view,checkPaymentList,checkEnable)
        viewHolder.loadFields {
            when (it?.id) {
                R.id.tv_check_cip -> {
                    if (viewHolder.check.isChecked) {
                       viewHolder.isCheck(LocalDateTime.now())
                        paid = paid.plus(viewHolder.value.COPToBigDecimal())
                        val paidDTO = data.first { it.codPaid == viewHolder.codPaid && it.name == viewHolder.name.text }
                        checkPaymentList.filterIsInstance<CheckPaymentsDTO>().firstOrNull{ it.codPaid == viewHolder.codPaid.toInt() && paidDTO.type == CheckPaymentsEnum.PAYMENTS}?.let {
                            paidDTO.id = it.id.toLong()
                        }
                        checkPaymentList.filterIsInstance<CheckCreditDTO>().firstOrNull{ it.codCredit == viewHolder.codPaid.toInt() && paidDTO.type == CheckPaymentsEnum.CREDITS}?.let {
                            paidDTO.id = it.id.toLong()
                        }
                        checkPaymentList.filterIsInstance<CheckQuoteDTO>().firstOrNull{ it.codQuote == viewHolder.codPaid.toInt() && paidDTO.type == CheckPaymentsEnum.QUOTE_CREDIT_CARD}?.let {
                            paidDTO.id = it.id.toLong()
                        }
                        listPaid.add(paidDTO)
                    } else {
                        viewHolder.isNotCheck()
                        paid = paid.subtract(viewHolder.value.COPToBigDecimal())
                        listPaid.remove(data.first { it.codPaid == viewHolder.codPaid })
                        listPaidRemove.add(data.first { it.codPaid == viewHolder.codPaid })
                    }
                    fn?.let { it.invoke(this,viewHolder) }
                }
            }
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PaymentsItemHolder, position: Int) {
       holder.setFields(data[position])
    }
}