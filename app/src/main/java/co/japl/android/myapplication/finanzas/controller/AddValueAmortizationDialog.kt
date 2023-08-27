package co.japl.android.myapplication.finanzas.controller

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.finanzas.bussiness.impl.AddAmortizationImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IAddAmortizationSvc
import co.japl.android.myapplication.finanzas.holders.validations.firstInvalid
import co.japl.android.myapplication.finanzas.holders.validations.COPToBigDecimal
import co.japl.android.myapplication.finanzas.holders.validations.COPtoBigDecimal
import co.japl.android.myapplication.finanzas.holders.validations.notNull
import co.japl.android.myapplication.finanzas.holders.validations.set
import co.japl.android.myapplication.finanzas.holders.validations.setNumberToField
import co.japl.android.myapplication.finanzas.holders.validations.text
import co.japl.android.myapplication.finanzas.holders.validations.`when`
import co.japl.android.myapplication.finanzas.putParams.ExtraValueListParam
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.textfield.TextInputEditText
import java.math.BigDecimal

class AddValueAmortizationDialog(context:Context, private val inflater: LayoutInflater,private val code:Int,val navController: NavController) : Dialog(context), OnClickListener {
    private lateinit var nbrQuote:TextInputEditText
    private lateinit var value:TextInputEditText
    private lateinit var btnClose:Button
    private lateinit var btnSave:Button
    private lateinit var svc:IAddAmortizationSvc

    private val validations  by lazy{
        arrayOf(
            nbrQuote set R.string.error_empty `when` { text().isEmpty() },
            value set R.string.error_empty `when` { text().isEmpty() },
            value set R.string.error_less_or_equal_than_0_value `when` { NumbersUtil.stringCOPToBigDecimal( text()) < BigDecimal.ZERO },
        )
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        svc = AddAmortizationImpl(ConnectDB(context))
        val view = inflater.inflate(R.layout.dialog_add_value_amortization, null)
        nbrQuote = view.findViewById(R.id.tv_num_quote_dava)
        value = view.findViewById(R.id.tv_value_dava)
        btnSave = view.findViewById(R.id.btn_add_dava)
        btnClose = view.findViewById(R.id.btn_close_dava)
        btnClose.setOnClickListener(this)
        btnSave.setOnClickListener(this)
        btnSave.visibility = View.GONE

        value.addTextChangedListener (object: TextWatcher {
            private val handler = Handler(Looper.getMainLooper())
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    value.removeTextChangedListener(this)
                    value.setNumberToField()
                    value.addTextChangedListener (this)
                    validate()
                },1000)
            }
        })

        nbrQuote.addTextChangedListener (object: TextWatcher {
            private val handler = Handler(Looper.getMainLooper())
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                    validate()
            }
        })

        setContentView(view)
    }

    fun validate():Boolean{
        var valid = false
        validations.firstInvalid{ requestFocus() }.notNull { valid = true }
        return valid.also { if(it) btnSave.visibility = View.VISIBLE }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_close_dava -> cancel()
            R.id.btn_add_dava -> {
                val nbr = nbrQuote.text?.toString()?.let {it.toInt()} ?: 0
                val value =  value.COPtoBigDecimal()
                    if(svc.createNew(code, nbr.toLong(), value.toDouble())){
                        Toast.makeText(context, R.string.add_value_amortization_success, Toast.LENGTH_LONG).show()
                        cancel()
                        ExtraValueListParam.toBack(navController)
                    }else{
                        Toast.makeText(context, R.string.add_value_amortization_error, Toast.LENGTH_LONG).show()
                    }
            }
        }

    }

}