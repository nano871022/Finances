package co.japl.android.myapplication.holders.view

import android.graphics.Paint
import android.os.Build
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.CheckBox
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.DTO.CheckCreditDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.CheckPaymentsDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.CheckQuoteDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.ICheck
import co.japl.android.myapplication.finanzas.enums.CheckPaymentsEnum
import co.japl.android.myapplication.finanzas.pojo.CheckPaymentsPOJO
import co.com.japl.ui.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import java.time.LocalDateTime

class PaymentsItemHolder(var view:View,val checkPaymentList:MutableList<ICheck>,val checkEnable:Boolean) : RecyclerView.ViewHolder(view){
    lateinit var name:TextView
    lateinit var value:TextView
    lateinit var date:TextView
    lateinit var check:CheckBox
    var currentColor:Int = 0
    var codPaid:Long = 0
    var id:Long = 0
    lateinit var checkPaid:ICheck

    fun loadFields(actions:OnClickListener){
        name = view.findViewById(R.id.tv_name_cip)
        value = view.findViewById(R.id.tv_amount_cip)
        date = view.findViewById(R.id.tv_date_cip)
        check = view.findViewById(R.id.tv_check_cip)
        check.setOnClickListener(actions)
        check.isChecked = checkEnable
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setFields(values:CheckPaymentsPOJO){
        Log.d(javaClass.name,"$values")
        name.text = values.name
        value.text = NumbersUtil.COPtoString(values.value)
        check.isChecked = values.checkValue
        date.visibility = View.GONE
        codPaid = values.codPaid
        id = values.id
        val paid = checkPaymentList.filterIsInstance<CheckPaymentsDTO>().firstOrNull { values.type == CheckPaymentsEnum.PAYMENTS && it.codPaid == codPaid.toInt() } ?:
                   checkPaymentList.filterIsInstance<CheckQuoteDTO>().firstOrNull { values.type == CheckPaymentsEnum.QUOTE_CREDIT_CARD && it.codQuote == codPaid.toInt() } ?:
                   checkPaymentList.filterIsInstance<CheckCreditDTO>().firstOrNull {  values.type == CheckPaymentsEnum.CREDITS && it.codCredit == codPaid.toInt() }
        paid?.let {
            checkPaid = it
            check.isChecked = true
            check.callOnClick()
            isCheck(
                when(it) {
                    is CheckPaymentsDTO -> it.date
                    is CheckQuoteDTO -> it.date
                    is CheckCreditDTO -> it.date
                    else -> LocalDateTime.MIN
                }
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isCheck(datePaid:LocalDateTime){
        date.text = DateUtils.localDateTimeToString(datePaid)
        date.visibility = View.VISIBLE
        currentColor = name.currentTextColor
        name.setTextColor(view.resources.getColor(R.color.grey_dark))
        value.setTextColor(view.resources.getColor(R.color.grey_dark))
        date.setTextColor(view.resources.getColor(R.color.brown))
        strikeText(name)
        strikeText(value)
    }

    private fun strikeText(textView:TextView){
        val paint = Paint()
        paint.isStrikeThruText = true
        textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    private fun unsStrikeText(textView:TextView){
        textView.paintFlags = 0
    }

    fun isNotCheck(){
        date.visibility = View.GONE
        val code = view.resources.getColor(androidx.media.R.color.secondary_text_default_material_light)
        name.setTextColor(code)
        value.setTextColor(code)
        unsStrikeText(name)
        unsStrikeText(value)
    }

    fun isCheckPayments():Boolean{
        return check.isChecked
    }
}