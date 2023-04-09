package co.japl.android.myapplication.holders.view

import android.os.Build
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.myapplication.finanzas.utils.KindOfTaxEnum
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import java.math.BigDecimal
import java.util.*

class BoughtViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
    lateinit var tvBoughtName:TextView
    lateinit var tvCreditValueBought:TextView
    lateinit var tvQuotesBought:TextView
    lateinit var tvMonthBought:TextView
    lateinit var tvCapitalBought:TextView
    lateinit var tvInterestBought:TextView
    lateinit var btnBoughtDelete:ImageButton
    lateinit var btnAmortization:ImageButton
    lateinit var tvTotalQuoteBought:TextView
    lateinit var tvBoughtDate:TextView
    lateinit var tvPendingToPay:TextView
    lateinit var tvTaxBoughtICC:TextView

    fun loadFields(view:View){
        tvBoughtName = view.findViewById(R.id.tvNameLCCS)
        tvCreditValueBought = view.findViewById(R.id.tvCreditValueBought)
        tvQuotesBought = view.findViewById(R.id.tvValueLCCS)
        tvMonthBought = view.findViewById(R.id.tvStatusLCCS)
        tvCapitalBought = view.findViewById(R.id.tvCreditCardLCCS)
        tvInterestBought = view.findViewById(R.id.tvInterestBought)
        btnBoughtDelete = view.findViewById(R.id.btnDeleteItemLCCS)
        tvTotalQuoteBought = view.findViewById(R.id.tvTotalQuoteBought)
        tvBoughtDate = view.findViewById(R.id.tvBoughtDate)
        tvPendingToPay = view.findViewById(R.id.tvPendingToPay)
        tvTaxBoughtICC = view.findViewById(R.id.tvTaxBoughtICC)
        btnAmortization = view.findViewById(R.id.btnAmortizationItemLCCS)
        btnAmortization.visibility = View.INVISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setFields(values:CreditCardBoughtDTO, capital:BigDecimal, interest:BigDecimal, quotesBought:Long, pendintToPay:BigDecimal,tax:Optional<Double>, action:View.OnClickListener){
        tvBoughtName.text = values.nameItem
        tvCapitalBought.text = NumbersUtil.COPtoString(capital)
        tvMonthBought.text = values.month.toString()
        tvQuotesBought.text = quotesBought.toString()
        tvCreditValueBought.text = NumbersUtil.COPtoString(values.valueItem!!)
        tvTotalQuoteBought.text = NumbersUtil.COPtoString(capital.plus(interest))
        tvBoughtDate.text = DateUtils.localDateTimeToString(values.boughtDate!!)
        tvInterestBought.text = NumbersUtil.COPtoString(interest)
        tvPendingToPay.text = NumbersUtil.COPtoString(pendintToPay)
        tvTaxBoughtICC.text = "${tax.orElse(values.interest)} % ${KindOfTaxEnum.NM.name}"
        btnBoughtDelete.setOnClickListener(action)
        btnAmortization.setOnClickListener(action)
        if(values.month > 1){
            btnAmortization.visibility = View.VISIBLE
        }
    }
}