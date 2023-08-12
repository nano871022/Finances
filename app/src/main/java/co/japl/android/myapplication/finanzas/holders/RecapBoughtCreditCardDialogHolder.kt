package co.japl.android.myapplication.finanzas.holders

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import co.japl.android.myapplication.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class RecapBoughtCreditCardDialogHolder(context:Context, private val inflater:LayoutInflater, private val values:Bundle?) : Dialog(context), OnClickListener {
    lateinit var totalItem:TextInputEditText
    lateinit var recurrentItem:TextInputEditText
    lateinit var quotesItem:TextInputEditText
    lateinit var quoteItem:TextInputEditText
    lateinit var currentcapital:TextInputEditText
    lateinit var currentInterest:TextInputEditText
    lateinit var quoteCapital:TextInputEditText
    lateinit var quoteInterest:TextInputEditText
    lateinit var totalCapital:TextInputEditText
    lateinit var totalInterest:TextInputEditText
    lateinit var totalQuote:TextInputEditText
    lateinit var pendingValue:TextInputEditText
    lateinit var btnClose:MaterialButton

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val dialog = inflater.inflate(R.layout.dialog_bought_credit_card_recap, null)
        totalItem = dialog.findViewById(R.id.tv_total_item_dbccr)
        recurrentItem = dialog.findViewById(R.id.tv_recurrent_item_dbccr)
        quotesItem = dialog.findViewById(R.id.tv_quotes_item_dbccr)
        quoteItem = dialog.findViewById(R.id.tv_quote_1_item_dbccr)
        currentcapital = dialog.findViewById(R.id.tv_current_capital_dbccr)
        currentInterest = dialog.findViewById(R.id.tv_current_interest_dbccr)
        quoteCapital = dialog.findViewById(R.id.tv_quote_capital_dbccr)
        quoteInterest = dialog.findViewById(R.id.tv_quote_interest_dbccr)
        totalCapital = dialog.findViewById(R.id.tv_total_capital_dbccr)
        totalInterest = dialog.findViewById(R.id.tv_total_interest_dbccr)
        totalQuote = dialog.findViewById(R.id.tv_total_quote_dbccr)
        pendingValue = dialog.findViewById(R.id.tv_pending_dbccr)
        btnClose = dialog.findViewById(R.id.btn_close_dbccr)
        loadValues()
        setContentView(dialog)
    }

    private fun loadValues(){
        btnClose.setOnClickListener(this)
        totalItem.setText(values?.getString("items"))
        recurrentItem.setText(values?.getString("recurrentItems"))
        quotesItem.setText(values?.getString("quotesItems"))
        quoteItem.setText(values?.getString("quoteItems"))
        currentcapital.setText(values?.getString("currentCapital"))
        currentInterest.setText(values?.getString("currentInterest"))
        quoteCapital.setText(values?.getString("quoteCapital"))
        quoteInterest.setText(values?.getString("quoteInterest"))
        totalCapital.setText(values?.getString("totalCapital"))
        totalInterest.setText(values?.getString("totalInterest"))
        totalQuote.setText(values?.getString("totalQuote"))
        pendingValue.setText(values?.getString("pendingValue"))
    }

    override fun onClick(p0: View?) {
        cancel()
    }

}