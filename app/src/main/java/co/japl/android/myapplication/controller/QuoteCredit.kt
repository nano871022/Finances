package co.japl.android.myapplication.controller

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.Calc
import co.japl.android.myapplication.bussiness.impl.QuoteCredit
import co.japl.android.myapplication.utils.Constants
import java.math.RoundingMode
import java.text.DecimalFormat
import android.content.Context as Context

class QuoteCredit : Fragment(), View.OnClickListener{
    private lateinit var etValueCredit: EditText
    private lateinit var etTax: EditText
    private lateinit var etMonths: EditText
    private lateinit var tvQuoteValue: TextView
    private lateinit var lyQuoteCredit: LinearLayout
    private val calc: Calc = QuoteCredit()
    private lateinit var contexto:Context
    private lateinit var btnSave:Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.quote_credit, container, false)
        contexto = rootView.context
        loadFields(rootView)
        clear()
        return rootView

    }

    override fun onClick(view: View){
     when(view.id){
         R.id.btnCalc->calc()
         R.id.btnClear->clear()
         R.id.btnSave->save()
     }
    }

    private fun save(){
        val value = etValueCredit.text.toString().toBigDecimal()
        val period = etMonths.text.toString().toLong()
        val tax = etTax.text.toString().toDouble()
        val response = calc.calc(value, period, tax)
        val quoteCreditSave:Intent = Intent(contexto,QuoteCreditSave::class.java)
        quoteCreditSave.putExtra(Constants.valueCredit,value)
        quoteCreditSave.putExtra(Constants.period,period)
        quoteCreditSave.putExtra(Constants.interest,tax)
        quoteCreditSave.putExtra(Constants.quoteCredit,response)
        startActivity(quoteCreditSave)
    }

    private fun calc(){
        if(validate()) {
            val value = etValueCredit.text.toString().toBigDecimal()
            val period = etMonths.text.toString().toLong()
            val tax = etTax.text.toString().toDouble()
            val response = calc.calc(value, period, tax)
            response.let {
                val format = DecimalFormat("#,###.00")
                tvQuoteValue.text = format.format(it.setScale(2, RoundingMode.HALF_EVEN))
                lyQuoteCredit.visibility = View.VISIBLE
                btnSave.visibility = View.VISIBLE
            }
        }
    }

    private fun validate():Boolean{
        var valid:Boolean = true
        if(etValueCredit.text.isBlank()){
            etValueCredit.setBackgroundColor(Color.RED)
            valid = valid && false
        }
        if(etTax.text.isBlank()){
            etTax.setBackgroundColor(Color.RED)
            valid = valid && false
        }
        if(etMonths.text.isBlank()){
            etMonths.setBackgroundColor(Color.RED)
            valid = valid && false
        }
        return valid
    }
    private fun clear(){
        etValueCredit.editableText.clear();
        etTax.editableText.clear()
        etMonths.editableText.clear()
        //tvQuoteValue.editableText.clear()
        lyQuoteCredit.visibility = View.INVISIBLE
        etValueCredit.setBackgroundColor(Color.WHITE)
        etTax.setBackgroundColor(Color.WHITE)
        etMonths.setBackgroundColor(Color.WHITE)
        btnSave.visibility = View.INVISIBLE
    }

    private fun loadFields( container: View){
            if(container != null) {
                etValueCredit = container.findViewById(R.id.etValueCredit)
                etTax = container?.findViewById(R.id.etTax)!!
                etMonths = container?.findViewById(R.id.etMonths)!!
                tvQuoteValue = container?.findViewById(R.id.tvQuoteValue)!!
                lyQuoteCredit = container?.findViewById(R.id.lyInterestValue)!!
                val btnClear:Button = container.findViewById(R.id.btnClear)
                btnClear.setOnClickListener(this)
                val btnCalc:Button = container.findViewById(R.id.btnCalc)
                btnCalc.setOnClickListener(this)
                btnSave  = container.findViewById(R.id.btnSave)
                btnSave.setOnClickListener(this)

            }

    }

}