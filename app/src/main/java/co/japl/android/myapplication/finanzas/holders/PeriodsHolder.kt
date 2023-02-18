package co.japl.android.myapplication.holders

import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.ISpinnerHolder
import co.japl.android.myapplication.finanzas.bussiness.DTO.PeriodDTO
import co.japl.android.myapplication.putParams.TaxesParams
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PeriodsHolder (var view:View, var parentFragmentManager:FragmentManager, var navController: NavController): IHolder<PeriodDTO>,ISpinnerHolder<PeriodsHolder>, View.OnClickListener {

    lateinit var period: TextView
    lateinit var interest: TextView
    lateinit var capital: TextView
    lateinit var total: TextView
    lateinit var paid: Button
    lateinit var periodDTO: PeriodDTO


    override fun setFields(actions: View.OnClickListener?) {
        period = view.findViewById(R.id.tvNameLCCS)
        interest = view.findViewById(R.id.tvValueLCCS)
        capital = view.findViewById(R.id.tvStatusLCCS)
        total = view.findViewById(R.id.tvCreditCardLCCS)
        paid = view.findViewById(R.id.btnShowPaid)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun loadFields(values: PeriodDTO) {
        period.text = values.periodStart.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")).plus(" - ").plus(values.periodEnd.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
        interest.text = values.interest.toString()
        capital.text = values.capital.toString()
        total.text = values.total.toString()
        periodDTO = values
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun downLoadFields(): PeriodDTO {
        val period:List<String> = this.period.text.split(" - ")
        val startPeriod: LocalDateTime = LocalDateTime.parse(period.get(0),DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        val endPeriod: LocalDateTime = LocalDateTime.parse(period.get(1),DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        val interest: BigDecimal = this.interest.text.toString().toBigDecimal()
        val capital: BigDecimal = this.capital.text.toString().toBigDecimal()
        val total: BigDecimal = this.total.text.toString().toBigDecimal()
        return PeriodDTO(periodDTO.creditCardId,startPeriod,endPeriod,interest,capital,total)
    }

    override fun cleanField() {
        period.text = ""
        interest.text = ""
        capital.text = ""
        total.text = ""
    }

    override fun validate(): Boolean {
        return true
    }

    override fun lists(fn: ((PeriodsHolder) -> Unit)?) {
        fn?.invoke(this)
    }

    override fun onClick(v: View?) {
        TaxesParams.newInstance(navController)
    }
}