package co.japl.android.myapplication.finanzas.holders.view

import android.os.Build
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.AdditionalCreditImpl
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil

import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import java.math.BigDecimal
import java.time.LocalDate

class MonthlyCreditItemHolder(val view:View): ViewHolder(view) {
    val additionalSvc = AdditionalCreditImpl(ConnectDB(view.context))
    lateinit var date:MaterialTextView
    lateinit var name:MaterialTextView
    lateinit var value:MaterialTextView
    lateinit var delete:ImageView
    lateinit var edit:ImageView

    fun loadField(){
        view?.let {
            with(view) {
                date = findViewById(R.id.date_pay_quote_mcil)
                name = findViewById(R.id.name_mcil)
                value = findViewById(R.id.value_mcil)
                delete = findViewById(R.id.btn_delete_mcil)
                edit = findViewById(R.id.btn_edit_mcil)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setField(value:CreditDTO, actions:OnClickListener){
        val additional = additionalSvc
            .get(getAdditional(value.id.toLong()))
            .map { it.value }
            .reduceOrNull { acc, bigDecimal -> acc + bigDecimal } ?: BigDecimal.ZERO
        date.text = DateUtils.localDateToString(value.date)
        name.text = value.name
        this.value.text = NumbersUtil.COPtoString(value.quoteValue + additional)
        delete.setOnClickListener(actions)
        edit.setOnClickListener(actions)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAdditional(codeCredit:Long):AdditionalCreditDTO{
        return AdditionalCreditDTO(0,"", BigDecimal.ZERO,codeCredit, LocalDate.MIN,LocalDate.MAX)
    }
}