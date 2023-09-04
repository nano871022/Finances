package co.japl.android.myapplication.finanzas.holders

import android.app.Dialog
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.utils.NumbersUtil

class AmortizationGeneralDialogHolder(val view:View,val dialog:Dialog) : IHolder<CalcDTO>, OnClickListener{
    private lateinit var creditValue:EditText
    private lateinit var quoteValue:EditText
    private lateinit var capitalValue:EditText
    private lateinit var tax:EditText
    private lateinit var periods:EditText
    private lateinit var interestToPay:EditText
    private lateinit var calcDto:CalcDTO
    private lateinit var btnClose:Button
    override fun setFields(actions: View.OnClickListener?) {
        creditValue = view.findViewById(R.id.tv_credit_value_atd)
        quoteValue = view.findViewById(R.id.tv_quote_value_atd)
        capitalValue = view.findViewById(R.id.tv_capital_value_atd)
        tax = view.findViewById(R.id.tv_tax_atd)
        periods = view.findViewById(R.id.tv_periods_atd)
        interestToPay = view.findViewById(R.id.tv_interest_to_pay_atd)
        btnClose = view.findViewById(R.id.btn_close_atd)
        btnClose.setOnClickListener(this)
    }

    override fun downLoadFields(): CalcDTO {
        return calcDto
    }

    override fun cleanField() {

    }

    override fun validate(): Boolean {
        return true
    }

    override fun loadFields(values: CalcDTO) {
        calcDto = values
        creditValue.setText(NumbersUtil.toString(values.valueCredit))
        quoteValue.setText(NumbersUtil.toString(values.quoteCredit))
        capitalValue.setText(NumbersUtil.toString(values.capitalValue))
        tax.setText("${NumbersUtil.toString(values.interest)} ${values.kindOfTax}")
        periods.setText(NumbersUtil.toString(values.period))
        interestToPay.setText(NumbersUtil.toString(values.interestValue))
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_close_atd -> dialog.cancel()
        }
    }
}