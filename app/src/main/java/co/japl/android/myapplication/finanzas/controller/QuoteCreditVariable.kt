package co.japl.android.myapplication.controller

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.impl.QuoteCreditVariable
import co.japl.android.myapplication.bussiness.impl.QuoteCreditVariableInterest
import co.japl.android.myapplication.bussiness.impl.QuoteCreditVariableInterestQuote
import co.japl.android.myapplication.bussiness.interfaces.Calc
import co.japl.android.myapplication.bussiness.interfaces.CalcInterest
import co.japl.android.myapplication.utils.Constants
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

class QuoteCreditVariable : Fragment(), View.OnClickListener,AdapterView.OnItemSelectedListener{
    private lateinit var etValueCredit: EditText
    private lateinit var etTax: EditText
    private lateinit var etMonths: EditText
    private lateinit var tvCapitalValue: TextView
    private lateinit var tvInterestValue: TextView
    private lateinit var tvTotalValue: TextView
    private lateinit var lyCapitalCredit: LinearLayout
    private lateinit var lyInterestCredit: LinearLayout
    private lateinit var lyMontlyPay: LinearLayout
    private lateinit var lyTotalValue: LinearLayout
    private lateinit var spMonth:Spinner
    private lateinit var listMonths:MutableList<String>
    private lateinit var contexto: Context

    private val calc: Calc = QuoteCreditVariable()
    private val calcInt: Calc = QuoteCreditVariableInterest()
    private val calcQuoteInt: CalcInterest = QuoteCreditVariableInterestQuote()
    private lateinit var context: View
    private lateinit var btnSave:Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.quote_credit_variable, container, false)
        context = rootView
        contexto = rootView.context
        loadFields(rootView)
        clear()
        return rootView

    }

    override fun onClick(view: View){
     when(view.id){
         R.id.btnCalc->calc()
         R.id.btnClear->clear()
         R.id.btnSaveVariable->save()
     }
    }

    private fun save(){
        val value = etValueCredit.text.toString().toBigDecimal()
        val period = etMonths.text.toString().toLong()
        val tax = etTax.text.toString().toDouble()
        val response = calc.calc(value, period, tax)
        val responseInt = calcInt.calc(value, period, tax)
        val quoteCreditSave: Intent = Intent(contexto,QuoteCreditSaveVariable::class.java)
        quoteCreditSave.putExtra(Constants.valueCredit,value)
        quoteCreditSave.putExtra(Constants.period,period)
        quoteCreditSave.putExtra(Constants.interest,tax)
        quoteCreditSave.putExtra(Constants.quoteCredit,response.plus(responseInt))
        quoteCreditSave.putExtra(Constants.interestValue,responseInt)
        quoteCreditSave.putExtra(Constants.capitalValue,response)
        startActivity(quoteCreditSave)
    }

    private fun calc(){
        if(validate()) {
            listMonths = mutableListOf()
            val format = DecimalFormat("#,###.00")
            val value = etValueCredit.text.toString().toBigDecimal()
            val period = etMonths.text.toString().toLong()
            val tax = etTax.text.toString().toDouble()
            val response = calc.calc(value, period, tax)
            var totalValue = BigDecimal(0)

            for ( i in 1 .. period){
                listMonths.add(i.toString())
            }

            val arrayAdapter:ArrayAdapter<String> = ArrayAdapter<String>(context.context,R.layout.spinner1,listMonths)
            spMonth.adapter = arrayAdapter

            response.let {

                tvCapitalValue.text = format.format(it.setScale(2, RoundingMode.HALF_EVEN))
                try {
                    lyCapitalCredit.visibility = View.VISIBLE
                    lyMontlyPay.visibility = View.VISIBLE
                }catch(e:Exception){}
                totalValue = totalValue.add(it)
            }
            val responseInt = calcInt.calc(value, period, tax)
            responseInt.let {

                tvInterestValue.text = format.format(it.setScale(2, RoundingMode.HALF_EVEN))
                try {
                    lyInterestCredit.visibility = View.VISIBLE
                    lyMontlyPay.visibility = View.VISIBLE
                }catch(e:Exception){}
                totalValue = totalValue.add(it)
            }
            if(totalValue.compareTo(BigDecimal(0))>0){
                try {
                    lyTotalValue.visibility = View.VISIBLE
                    lyMontlyPay.visibility = View.VISIBLE
                }catch(e:Exception){}
                tvTotalValue.text = format.format(totalValue.setScale(2, RoundingMode.HALF_EVEN))
            }
        }
        btnSave.visibility = View.VISIBLE
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
        etValueCredit.setBackgroundColor(Color.WHITE)
        etTax.setBackgroundColor(Color.WHITE)
        etMonths.setBackgroundColor(Color.WHITE)
        btnSave.visibility = View.INVISIBLE
        try{
            lyCapitalCredit.visibility = View.INVISIBLE
            lyInterestCredit.visibility = View.INVISIBLE
            lyTotalValue.visibility = View.INVISIBLE
            lyMontlyPay.visibility = View.INVISIBLE
        }catch (e:Exception){}
    }

    private fun loadFields( container: View){
            if(container != null) {
                etValueCredit = container.findViewById(R.id.etNameItem)
                etTax = container.findViewById(R.id.etTax)!!
                etMonths = container.findViewById(R.id.etMonths)!!
                tvCapitalValue = container.findViewById(R.id.tvCapitalValue)!!
                lyCapitalCredit = container.findViewById(R.id.lyCapitalValue)!!
                tvInterestValue = container.findViewById(R.id.tvInterestValueList)!!
                tvTotalValue = container.findViewById(R.id.tvTotalValue)!!
                spMonth = container.findViewById(R.id.months)
                val btnClear:Button = container.findViewById(R.id.btnClear)
                btnClear.setOnClickListener(this)
                val btnCalc:Button = container.findViewById(R.id.btnCalc)
                btnCalc.setOnClickListener(this)
                btnSave = container.findViewById(R.id.btnSaveVariable)
                btnSave.setOnClickListener(this)
                btnSave.visibility = View.INVISIBLE
                spMonth.onItemSelectedListener = this
                try{
                    lyInterestCredit = container.findViewById(R.id.lyInterestValue)!!
                    lyTotalValue = container.findViewById(R.id.lyTotalValue)!!
                    lyMontlyPay = container.findViewById(R.id.lyMonthPay)

                }catch(e:Exception){

                }
            }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        print("In item selected")
        val format = DecimalFormat("#,###.00")
        val value = etValueCredit.text.toString().toBigDecimal()
        val period = etMonths.text.toString().toLong()
        val tax = etTax.text.toString().toDouble()
        val response = calc.calc(value, period, tax)
        var totalValue = BigDecimal(0)

        val option = listMonths.get(pos)
        val interest = calcQuoteInt.calc(value,period,tax,option.toLong())
        response.let {

            tvCapitalValue.text = format.format(it.setScale(2, RoundingMode.HALF_EVEN))
            try {
                lyCapitalCredit.visibility = View.VISIBLE
                lyMontlyPay.visibility = View.VISIBLE
            }catch(e:Exception){}
            totalValue = totalValue.add(it)
        }
        interest.let {

            tvInterestValue.text = format.format(it.setScale(2, RoundingMode.HALF_EVEN))
            try {
                lyInterestCredit.visibility = View.VISIBLE
                lyMontlyPay.visibility = View.VISIBLE
            }catch(e:Exception){}
            totalValue = totalValue.add(it)
        }
        if(totalValue.compareTo(BigDecimal(0))>0){
            try {
                lyTotalValue.visibility = View.VISIBLE
                lyMontlyPay.visibility = View.VISIBLE
            }catch(e:Exception){}
            tvTotalValue.text = format.format(totalValue.setScale(2, RoundingMode.HALF_EVEN))
        }




    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}