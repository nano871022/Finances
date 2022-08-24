package co.japl.android.myapplication.controller

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.impl.QuoteCredit
import co.japl.android.myapplication.bussiness.interfaces.Calc
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.finanzas.holders.QuoteCreditHolder
import co.japl.android.myapplication.finanzas.pojo.QuoteCreditCard
import co.japl.android.myapplication.finanzas.putParams.QuoteCreditParams
import java.math.BigDecimal
import java.util.*

class QuoteCredit : Fragment(), View.OnClickListener{
    private lateinit var navController:NavController
    private val calc: Calc = QuoteCredit()
    private lateinit var holder:IHolder<QuoteCreditCard>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.quote_credit, container, false)
        holder = QuoteCreditHolder(rootView)
        holder.setFields(this)
        holder.cleanField()
        return rootView

    }

    override fun onViewCreated(view:View,saveInstanceState:Bundle?){
        navController = Navigation.findNavController(view)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(view: View){
     when(view.id){
         R.id.btnCalc->calc()
         R.id.btnClear->holder.cleanField()
         R.id.btnSave-> save()
     }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun save(){
        val quote = holder.downLoadFields()
        QuoteCreditParams.newInstance(quote.value.orElse(BigDecimal.ZERO),quote.tax.orElse(0.0),quote.period.orElse(0),quote.response.orElse(
            BigDecimal.ZERO),navController)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun calc() {
        if (holder.validate()) {
            val quote = holder.downLoadFields()
            val response = calc.calc(quote.value.get(), quote.period.get(), quote.tax.get())
            response.let {
                quote.response = Optional.ofNullable(response)
                holder.loadFields(quote)
            }
        }
    }
}