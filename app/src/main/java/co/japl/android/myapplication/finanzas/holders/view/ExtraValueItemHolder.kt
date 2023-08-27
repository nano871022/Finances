package co.japl.android.myapplication.finanzas.holders.view

import android.view.View
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.DTO.AddAmortizationDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.ExtraValueAmortizationCreditDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.ExtraValueAmortizationQuoteCreditCardDTO
import co.japl.android.myapplication.finanzas.enums.MoreOptionesExtraValueItems
import co.japl.android.myapplication.finanzas.holders.interfaces.IItemHolder
import co.japl.android.myapplication.utils.NumbersUtil
import java.math.BigDecimal

class ExtraValueItemHolder(val view:View):IItemHolder<Any,MoreOptionesExtraValueItems>, RecyclerView.ViewHolder(view){
    private lateinit var period:TextView
    private lateinit var value:TextView
    private lateinit var more: AppCompatImageView

    override fun loadFields(){
        period = view.findViewById(R.id.tv_period_evil)
        value = view.findViewById(R.id.tv_value_evil)
        more = view.findViewById(R.id.iv_more_evil)
    }

    override fun setFields(values: Any, callback: (MoreOptionesExtraValueItems) -> Unit) {
        if(values is AddAmortizationDTO){
            period.text = values.nbrQuote.toString()
            value.text = NumbersUtil.toString(values.value)
        }

        if(values is ExtraValueAmortizationCreditDTO){
            period.text = values.nbrQuote.toString()
            value.text = NumbersUtil.toString(values.value)
        }

        if(values is ExtraValueAmortizationQuoteCreditCardDTO){
            period.text = values.nbrQuote.toString()
            value.text = NumbersUtil.toString(values.value)
        }

        more.setOnClickListener {
            callback.invoke(MoreOptionesExtraValueItems.DELETE)
        }
    }

}