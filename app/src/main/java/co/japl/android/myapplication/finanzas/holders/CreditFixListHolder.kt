package co.japl.android.myapplication.finanzas.holders

import android.os.Build
import android.view.View
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditFixImpl
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import java.math.BigDecimal
import java.time.LocalDate

class CreditFixListHolder(val view:View){
    lateinit var creditSvc :SaveSvc<CreditDTO>
    lateinit var date:TextView
    lateinit var quote:TextView
    lateinit var interest:TextView
    lateinit var capital:TextView
    lateinit var pendingToPay: TextView
    lateinit var additional:TextView
    lateinit var detail:MaterialButton
    lateinit var period:MaterialButton
    lateinit var add:MaterialButton
    lateinit var progressBar:ProgressBar

    fun loadField(){
        creditSvc = CreditFixImpl(ConnectDB(view.context))
        date = view.findViewById(R.id.date_bill_cfl)
        quote = view.findViewById(R.id.value_cfl)
        interest = view.findViewById(R.id.interest_cfl)
        capital = view.findViewById(R.id.capital_cfl)
        pendingToPay = view.findViewById(R.id.pending_cfl)
        additional = view.findViewById(R.id.additional_cfl)
        detail = view.findViewById(R.id.btn_detail_cfl)
        period = view.findViewById(R.id.btn_periods_cfl)
        add = view.findViewById(R.id.btn_add_cfl)
        progressBar = view.findViewById(R.id.pb_load_cfl)

        detail.visibility = View.GONE
        period.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setField(date:LocalDate, quote:BigDecimal, interest:BigDecimal, capital:BigDecimal, pending:BigDecimal, additional:BigDecimal, actions:OnClickListener){
        val months = view.resources.getStringArray(R.array.Months)
        this.date.text = "${months[date.monthValue]} ${date.year}"
        this.quote.text = NumbersUtil.toString(quote)
        this.interest.text = NumbersUtil.toString(interest)
        this.capital.text = NumbersUtil.toString(capital)
        this.additional.text = NumbersUtil.toString(additional)
        this.pendingToPay.text = NumbersUtil.toString(pending)
        detail.setOnClickListener(actions)
        period.setOnClickListener(actions)
        add.setOnClickListener(actions)
        if(quote > BigDecimal.ZERO){
            detail.visibility = View.VISIBLE
            period.visibility = View.VISIBLE
        }
        progressBar.visibility = View.GONE
    }
}