package co.japl.android.myapplication.holders.view

import android.app.AlertDialog
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.finanzas.enums.KindOfTaxEnum
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsCreditCard
import co.japl.android.myapplication.finanzas.enums.TaxEnum
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.util.*

class BoughtViewHolder(val itemView:View) : RecyclerView.ViewHolder(itemView) {
    lateinit var tvBoughtName:TextView
    lateinit var tvCreditValueBought:TextView
    lateinit var tvQuotesBought:TextView
    lateinit var tvMonthBought:TextView
    lateinit var tvCapitalBought:TextView
    lateinit var tvInterestBought:TextView
    lateinit var btnMore:ImageButton
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
        btnMore = view.findViewById(R.id.btn_more_bil)
        tvTotalQuoteBought = view.findViewById(R.id.tvTotalQuoteBought)
        tvBoughtDate = view.findViewById(R.id.tvBoughtDate)
        tvPendingToPay = view.findViewById(R.id.tvPendingToPay)
        tvTaxBoughtICC = view.findViewById(R.id.tvTaxBoughtICC)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setFields(values:CreditCardBoughtDTO, capital:BigDecimal, interest:BigDecimal, quotesBought:Long, pendintToPay:BigDecimal,tax:Pair<Double,KindOfTaxEnum>, callback:(MoreOptionsItemsCreditCard)->Unit) {
        tvBoughtName.text = values.nameItem
        tvCapitalBought.text = NumbersUtil.COPtoString(capital)
        tvMonthBought.text = values.month.toString()
        tvQuotesBought.text = quotesBought.toString()
        tvCreditValueBought.text = NumbersUtil.COPtoString(values.valueItem!!)
        tvTotalQuoteBought.text = NumbersUtil.COPtoString(capital.plus(interest))
        tvBoughtDate.text = DateUtils.localDateTimeToString(values.boughtDate!!)
        tvInterestBought.text = NumbersUtil.COPtoString(interest)
        tvPendingToPay.text = NumbersUtil.COPtoString(pendintToPay)
        tvTaxBoughtICC.text = "${(tax.first * 100).toBigDecimal().setScale(3,RoundingMode.CEILING)} % ${tax.second.name}"
        loadAlert(values,callback)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun loadAlert(values:CreditCardBoughtDTO, callback:(MoreOptionsItemsCreditCard)->Unit){
        val builder = AlertDialog.Builder(itemView.context)
        builder.apply {
            setTitle(R.string.pick_option)
            val itemsOrigin = itemView.context.resources.getStringArray(R.array.credit_card_item_options)
            var items = itemsOrigin
            val dateFirst = LocalDate.now().withDayOfMonth(1)
            val dateLast = dateFirst.plusMonths(1).minusDays(1)
            Log.d(javaClass.name,"$dateFirst $dateLast ${values.createDate}")
            if(values.month <= 1){
                items = items.filterIndexed { index, _ -> index != 0 }.toTypedArray()
            }
            if(values.createDate.toLocalDate() !in dateFirst..dateLast){
                items = items.filterIndexed{ index, _ -> index != 1 }.toTypedArray()
            }
            setItems(items) { dialog, which ->
                val itemSelect = (dialog as AlertDialog).listView.getItemAtPosition(which)
                val item = itemsOrigin.indexOf(itemSelect)
                Log.d(javaClass.name,"Item select $item $itemSelect ${items} ")
                callback.invoke(MoreOptionsItemsCreditCard.values().find { it.i == item }!!)
            }
        }
        btnMore.setOnClickListener {
            builder.create().show()
        }
    }
}