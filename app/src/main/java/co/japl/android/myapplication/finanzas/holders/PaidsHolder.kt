package co.japl.android.myapplication.finanzas.holders

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.AccountDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.InputDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidsPOJO
import co.japl.android.myapplication.finanzas.bussiness.impl.AccountImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.InputImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.PaidImpl
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.google.api.client.util.StringUtils
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class PaidsHolder(val view:View): IHolder<PaidsPOJO> {
    private val service:SaveSvc<AccountDTO> = AccountImpl(ConnectDB(view.context))
    private val inputSvc:SaveSvc<InputDTO> = InputImpl(view,ConnectDB(view.context))
    private val paidSvc:SaveSvc<PaidDTO> = PaidImpl(ConnectDB(view.context))
        private lateinit var period:MaterialTextView
    private lateinit var paid:MaterialTextView
    private lateinit var count:MaterialTextView
    private lateinit var inputs:MaterialTextView
    private lateinit var inputLessOutput:MaterialTextView
    private lateinit var btnDetail:MaterialButton
    private lateinit var btnPeriods:MaterialButton
    private lateinit var btnAdd:MaterialButton

    override fun setFields(actions: View.OnClickListener?) {
        period = view.findViewById(R.id.period_ps)
        paid = view.findViewById(R.id.paid_ps)
        count = view.findViewById(R.id.count_ps)
        inputs = view.findViewById(R.id.tv_input_total_ps)
        inputLessOutput = view.findViewById(R.id.tv_input_less_paid_ps)
        btnAdd = view.findViewById(R.id.btn_add_ps)
        btnDetail = view.findViewById(R.id.btn_detail_ps)
        btnPeriods = view.findViewById(R.id.btn_periods_ps)
        btnAdd.setOnClickListener(actions)
        btnPeriods.setOnClickListener(actions)
        btnDetail.setOnClickListener(actions)
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

        val periodStr = values.period.toString()
        val year = periodStr.substring(0,4).toString()
        val month = periodStr.substring(5).toString()
        val date = LocalDate.of(year.toInt(),month.toInt(),1)
        period.text = "${date.getMonth().getDisplayName(TextStyle.FULL,Locale("es","CO"))} ${date.year}"
        paid.text = NumbersUtil.COPtoString(values.paid)
        count.text = values.count.toString()
        val list = service.getAll()
        if(valueInputs > BigDecimal.ZERO){
            inputs.text = NumbersUtil.COPtoString(valueInputs)
            inputLessOutput.text = NumbersUtil.COPtoString(valueInputs - values.paid.toBigDecimal())
        }
        if(list.isEmpty()){
            btnDetail.visibility = View.GONE
            btnAdd.visibility = View.GONE
        }
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
}