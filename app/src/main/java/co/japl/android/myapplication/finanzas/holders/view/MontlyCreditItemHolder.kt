package co.japl.android.myapplication.finanzas.holders.view

import android.app.AlertDialog
import android.os.Build
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.GracePeriodDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.AdditionalCreditImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.GracePeriodImpl
import co.japl.android.myapplication.finanzas.enums.MoreOptionalItemsCredit
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsCreditCard
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil

import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import java.math.BigDecimal
import java.time.LocalDate

class MonthlyCreditItemHolder(val view:View): ViewHolder(view) {
    val additionalSvc = AdditionalCreditImpl(ConnectDB(view.context))
    val gracePeriodSvc = GracePeriodImpl(ConnectDB(view.context))
    lateinit var date:TextView
    lateinit var name:TextView
    lateinit var value: TextView
    lateinit var more:ImageView
    val items = itemView.context.resources.getStringArray(R.array.credit_item_options)

    fun loadField(){
        view?.let {
            with(view) {
                date = findViewById(R.id.date_pay_quote_mcil)
                name = findViewById(R.id.name_mcil)
                value = findViewById(R.id.value_mcil)
                more = findViewById(R.id.btn_more_mcil)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setField(value:CreditDTO, callback:(MoreOptionalItemsCredit)->Unit){
        val gracePeriod = gracePeriodSvc.get(value.id, LocalDate.now())
        val additional = if(!gracePeriod.isPresent)additionalSvc
            .get(getAdditional(value.id.toLong()))
            .map { it.value }
            .reduceOrNull { acc, bigDecimal -> acc + bigDecimal } ?: BigDecimal.ZERO
        else BigDecimal.ZERO
        date.text = DateUtils.localDateToString(value.date)
        name.text = value.name
        this.value.text = NumbersUtil.toString(value.quoteValue + additional)
        val builder = AlertDialog.Builder(itemView.context)
        var itemsCredit = items
        if(gracePeriod.isPresent){
            itemsCredit = items.filterIndexed{ index,_-> index != MoreOptionalItemsCredit.GRACE_PERIOD.i }.toTypedArray()
        }else{
            itemsCredit = items.filterIndexed{ index,_-> index != MoreOptionalItemsCredit.DELETE_GRACE_PERIOD.i }.toTypedArray()
        }
        builder.apply {
            setTitle(view.resources.getString(R.string.pick_option))
            setItems(itemsCredit) { _, which ->
                val selected = itemsCredit[which]
                val index = items.indexOf(selected)
                callback.invoke(MoreOptionalItemsCredit.values().find { it.i == index }!!)
            }
        }
        more.setOnClickListener {
            builder.create().show()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAdditional(codeCredit:Long):AdditionalCreditDTO{
        return AdditionalCreditDTO(0,"", BigDecimal.ZERO,codeCredit, LocalDate.MIN,LocalDate.MAX)
    }
}