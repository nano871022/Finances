package co.japl.android.myapplication.controller

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.impl.QuoteCreditVariable
import co.japl.android.myapplication.bussiness.impl.QuoteCreditVariableInterest
import co.japl.android.myapplication.bussiness.interfaces.Calc
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.bussiness.mapping.CalcMap
import co.japl.android.myapplication.finanzas.holders.QuoteCreditVariableHolder
import co.japl.android.myapplication.finanzas.pojo.QuoteCreditCard
import co.japl.android.myapplication.finanzas.putParams.AmortizationTableParams
import co.japl.android.myapplication.finanzas.putParams.QuoteCreditVariablesParams
import co.japl.android.myapplication.finanzas.enums.KindOfTaxEnum
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.util.*

@AndroidEntryPoint
class QuoteCreditVariable : Fragment(), View.OnClickListener{

    private val calc: Calc = QuoteCreditVariable()
    private val calcInt: Calc = QuoteCreditVariableInterest()

    private lateinit var holder: IHolder<QuoteCreditCard>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.quote_credit_variable, container, false)
        holder = QuoteCreditVariableHolder(rootView)
        holder.setFields(this)
        holder.cleanField()
        return rootView

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(view: View){
     when(view.id){
         R.id.btnCalc->calc()
         R.id.btnClear->holder.cleanField()
         R.id.btnSaveVariable->save()
         R.id.btnAmortizationQCV->openAmortization()
     }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun openAmortization(){
        AmortizationTableParams.newInstanceVariable(CalcMap().mapping(holder.downLoadFields()),findNavController())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun save(){
        val quote = holder.downLoadFields()
        Log.d(this.tag,"${quote.capitalValue} ${quote.interestValue} ${quote.response}")
        QuoteCreditVariablesParams.newInstance(quote.value.orElse(BigDecimal.ZERO)
            ,quote.tax.orElse(0.0)
            ,quote.period.orElse(0)
            ,quote.response.orElse(BigDecimal.ZERO)
            ,quote.interestValue.orElse(BigDecimal.ZERO)
            ,quote.capitalValue.orElse(BigDecimal.ZERO)
            ,quote.kindOfTax.orElse(KindOfTaxEnum.EM.name),
            findNavController())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun calc() {
        if (holder.validate()) {

            val quote = holder.downLoadFields()
            val response = calc.calc(quote.value.get(), quote.period.get(), quote.tax.get(),
                KindOfTaxEnum.valueOf(quote.kindOfTax.get()))

            quote.capitalValue = Optional.ofNullable(response)
            val responseInt = calcInt.calc(quote.value.get(), quote.period.get(), quote.tax.get(),
                KindOfTaxEnum.valueOf(quote.kindOfTax.get()))
            quote.interestValue = Optional.ofNullable(responseInt)
            quote.response =
                Optional.ofNullable(quote.capitalValue.get().add(quote.interestValue.get()))
            holder.loadFields(quote)
        }
    }






}