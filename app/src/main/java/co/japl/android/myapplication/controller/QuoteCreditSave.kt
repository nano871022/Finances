package co.japl.android.myapplication.controller

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.bussiness.SaveSvc
import co.japl.android.myapplication.bussiness.impl.DBConnect
import co.japl.android.myapplication.bussiness.impl.SaveImpl
import co.japl.android.myapplication.utils.CalcEnum
import co.japl.android.myapplication.utils.Constants
import com.google.android.material.snackbar.Snackbar
import java.math.BigDecimal
import java.text.DecimalFormat

class QuoteCreditSave :  AppCompatActivity(), View.OnClickListener{
    private lateinit var etName:EditText
    private lateinit var tvValueCredit:TextView
    private lateinit var tvInterest:TextView
    private lateinit var tvMonths:TextView
    private lateinit var tvQuoteCredit:TextView
    private lateinit var dbConnect: DBConnect
    private lateinit var saveSvc:SaveSvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setContentView(R.layout.quote_credit_save)
        dbConnect = DBConnect(this)
        saveSvc = SaveImpl(dbConnect)
        loadFields()
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        loadValues()
    }
    private fun loadValues(){
        if(intent.extras != null ) {
            val format = DecimalFormat(" $ #,###.##")
            val quoteCredit:BigDecimal = intent.extras!!.get(Constants.quoteCredit) as BigDecimal
            tvQuoteCredit.text = format.format(quoteCredit)
            val interest:Double = intent.extras!!.get(Constants.interest) as Double
            tvInterest.text = interest.toString()
            val period:Long = intent.extras!!.get(Constants.period) as Long
            tvMonths.text = period.toString()
            val valueCredit:BigDecimal = intent.extras!!.get(Constants.valueCredit) as BigDecimal
            tvValueCredit.text = format.format(valueCredit)
        }else{
            println("No se suministro extras")
        }
    }

    private fun valid():Boolean{
        return etName.text.isNotBlank()
    }

    private fun loadFields(){
        etName = findViewById(R.id.etName)
        tvValueCredit = findViewById(R.id.tvValueCreditSave)
        tvInterest = findViewById(R.id.tvInterestSave)
        tvMonths = findViewById(R.id.tvMonthsSave)
        tvQuoteCredit = findViewById(R.id.tvQuoteValueSave)
        val btnSave:Button = findViewById(R.id.btnSave)
        val btnCancel:Button = findViewById(R.id.btnCancel)
        btnSave.setOnClickListener(this)
        btnCancel.setOnClickListener(this)
    }

    private fun getCalcDTO():CalcDTO{
        return CalcDTO(etName.text.toString()
            ,tvValueCredit.text.toString().replace(" $ ","").replace(",","").toBigDecimal()
            ,tvInterest.text.toString().replace(",","").toDouble()
            ,tvMonths.text.toString().toLong()
            ,tvQuoteCredit.text.toString().replace(" $ ","").replace(",","").toBigDecimal()
            ,CalcEnum.FIX.toString()
            ,0
            ,BigDecimal(0)
            ,BigDecimal(0))
    }

    override fun onClick(view: View?) {
        println("IDVIEW ${view?.id}")
        when(view?.id){
            R.id.btnSave->{
                if(valid() && saveSvc.save(getCalcDTO())){
                    Snackbar.make(view,R.string.success_save_quote_credit,Snackbar.LENGTH_LONG).setAction(R.string.close,View.OnClickListener {  onBackPressed()}).show()
                }else{
                    Snackbar.make(view,R.string.dont_able_to_save,Snackbar.LENGTH_LONG).setAction(R.string.close,this).show()
                }
            }
            R.id.btnCancel->{
                onBackPressed()
            }

        }

    }

}