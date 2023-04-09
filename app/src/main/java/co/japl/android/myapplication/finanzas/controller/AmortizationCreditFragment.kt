package co.japl.android.myapplication.finanzas.controller

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.AmortizationCreditFix
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.AdditionalCreditImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditFixImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ISaveSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ITableHolder
import co.japl.android.myapplication.finanzas.holders.AmortizationCreditTableHolder
import co.japl.android.myapplication.finanzas.putParams.CreditFixParams
import co.japl.android.myapplication.finanzas.utils.AmortizationCreditFixEnum
import co.japl.android.myapplication.finanzas.utils.KindOfTaxEnum
import java.math.BigDecimal
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class AmortizationCreditFragment : Fragment() {
    private lateinit var holder: ITableHolder<AmortizationCreditFix>
    private lateinit var credit:SaveSvc<CreditDTO>
    private lateinit var additionalCredit: ISaveSvc<AdditionalCreditDTO>
    private lateinit var data:CreditDTO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_amortization_credit, container, false)
        credit = CreditFixImpl(ConnectDB(root.context))
        additionalCredit = AdditionalCreditImpl(ConnectDB(root.context))
        holder = AmortizationCreditTableHolder(root)
        holder.setup{
            when(it?.id){
                R.id.btn_additional_acf->CreditFixParams.newInstanceAmortizationToAdditionalList(data.id.toLong(),findNavController())
            }
        }
        getData()
        val additional = additionalCredit.get(getAdditional()).map { it.value }.reduceOrNull { acc, bigDecimal ->  acc + bigDecimal} ?: BigDecimal.ZERO
        holder.add(AmortizationCreditFixEnum.ADDITIONAL.name,additional)
        holder.add(AmortizationCreditFixEnum.QUOTES_PAID.name,ChronoUnit.MONTHS.between(data.date,LocalDate.now()))
        holder.setData(getCalc())
        holder.create()
        holder.load()
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getData(){
        arguments?.let {
            val values = CreditFixParams.downloadAmortizationList(it)
            data = values.first
            holder.add(AmortizationCreditFixEnum.DATE_BILL.name,data.date)
        }
    }

    private fun getCalc():CalcDTO{
        val name = data.name
        val valueCredit = data.value
        val interest = data.tax
        val period = data.periods
        val quoteCredit = data.quoteValue
        val type = data.kindOf
        val id = 0
        val interestValue = BigDecimal.ZERO
        val capitalValue = BigDecimal.ZERO
        val kindOfTax = data.kindOfTax
        return CalcDTO(name,valueCredit,interest,period.toLong(),quoteCredit,type,id,interestValue,capitalValue,kindOfTax)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAdditional():AdditionalCreditDTO{
        val id = 0
        val name = ""
        val value = BigDecimal.ZERO
        val creditCode = data.id
        val startDate = LocalDate.now()
        val endDate = LocalDate.MAX
        return AdditionalCreditDTO(id, name ,value, creditCode.toLong() ,startDate,endDate )
    }

}