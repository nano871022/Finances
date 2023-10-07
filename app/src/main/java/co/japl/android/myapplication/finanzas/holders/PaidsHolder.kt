package co.japl.android.myapplication.finanzas.holders

import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.AccountDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.InputDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidsPOJO
import co.japl.android.myapplication.finanzas.bussiness.impl.AccountImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.InputImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.PaidImpl
import co.japl.android.myapplication.finanzas.bussiness.mapping.InputMap
import co.japl.android.myapplication.finanzas.holders.interfaces.ICallerHolder
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class PaidsHolder(val view:View): IHolder<PaidsPOJO>, ICallerHolder<PaidsHolder> {
    private val service:SaveSvc<AccountDTO> = AccountImpl(ConnectDB(view.context))
    private val inputSvc:SaveSvc<InputDTO> = InputImpl(ConnectDB(view.context), InputMap(view.context))
    private val paidSvc:SaveSvc<PaidDTO> = PaidImpl(ConnectDB(view.context))
        private lateinit var period:TextView
    private lateinit var paid:TextView
    private lateinit var count:TextView
    private lateinit var inputs:TextView
    private lateinit var inputLessOutput:TextView
    private lateinit var btnDetail:MaterialButton
    private lateinit var btnPeriods:MaterialButton
    private lateinit var btnAdd:MaterialButton
    private lateinit var progressBar:ProgressBar
    lateinit var customDraw: CustomDraw

    override fun setFields(actions: View.OnClickListener?) {
        period = view.findViewById(R.id.period_ps)
        paid = view.findViewById(R.id.paid_ps)
        count = view.findViewById(R.id.count_ps)
        inputs = view.findViewById(R.id.tv_input_total_ps)
        inputLessOutput = view.findViewById(R.id.tv_input_less_paid_ps)
        btnAdd = view.findViewById(R.id.btn_add_ps)
        btnDetail = view.findViewById(R.id.btn_detail_ps)
        btnPeriods = view.findViewById(R.id.btn_periods_ps)
        progressBar = view.findViewById(R.id.pb_load_ps)
        customDraw = view.findViewById(R.id.cv_canvas_ps)
        btnAdd.setOnClickListener(actions)
        btnPeriods.setOnClickListener(actions)
        btnDetail.setOnClickListener(actions)
        progressBar.visibility = View.VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun loadFields(values: PaidsPOJO) {
        val valueInputs = inputSvc.getAll()?.let{
            if(it.isNotEmpty()){
                it.filter { it.kindOf == view.resources.getStringArray(R.array.kind_of_pay_list)[0] }.map{it.value}.reduceOrNull{acc,value->acc+value} ?: BigDecimal.ZERO
            }else{
                BigDecimal.ZERO
            }
        }?:BigDecimal.ZERO
        (paidSvc as PaidImpl).getPeriods()?.let {
            if(it.isNotEmpty()){
                btnPeriods.visibility = View.VISIBLE
            }else{
                btnPeriods.visibility = View.GONE
            }
        }

        val periods = YearMonth.parse(values.period.toString(), DateTimeFormatter.ofPattern("yyyyMM"))
        val date =  LocalDate.of(periods.year,periods.month,1)
        period.text = "${date.getMonth().getDisplayName(TextStyle.FULL,Locale("es","CO"))} ${date.year}"
        paid.text = NumbersUtil.toString(values.paid)
        count.text = values.count.toString()
        val list = service.getAll()
        if(valueInputs > BigDecimal.ZERO){
            inputs.text = NumbersUtil.toString(valueInputs)
            inputLessOutput.text = NumbersUtil.toString(valueInputs - values.paid.toBigDecimal())
        }
        if(list.isEmpty()){
            btnDetail.visibility = View.GONE
            btnAdd.visibility = View.GONE
        }
        progressBar.visibility = View.GONE
    }

    override fun downLoadFields(): PaidsPOJO {
        TODO("Not yet implemented")
    }

    override fun cleanField() {
        TODO("Not yet implemented")
    }

    override fun validate(): Boolean {
        TODO("Not yet implemented")
    }

    override fun execute(fn: ((PaidsHolder) -> Unit)?) {
        fn?.invoke(this)
    }
}