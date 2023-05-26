package co.japl.android.myapplication.holders.view

import android.app.AlertDialog
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.finanzas.enums.CalcEnum
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsQuoteSave
import co.japl.android.myapplication.utils.NumbersUtil

class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
    lateinit var tvQuoteCreditListSaveItem:TextView
    lateinit var tvPeriodListSaveItem:TextView
    lateinit var tvInterestListSaveItem:TextView
    lateinit var tvCreditValueListSaveItem:TextView
    lateinit var tvNameListSaveItem:TextView
    lateinit var tvTypeListSaveItem:TextView
    lateinit var btnMore:ImageButton
    lateinit var tvCapitalValue:TextView
    lateinit var tvInterestValue:TextView
    lateinit var llCapitalValue:LinearLayout
    lateinit var llInterestValue:LinearLayout
    val items = itemView.context.resources.getStringArray(R.array.quote_save_item_options)

    fun loadFields(view:View){
        tvCreditValueListSaveItem = view.findViewById(R.id.tvCreditValueListSaveItem)
        tvInterestListSaveItem = view.findViewById(R.id.tvInterestListSaveItem)
        tvNameListSaveItem = view.findViewById(R.id.tvNameListSaveItem)
        tvPeriodListSaveItem = view.findViewById(R.id.tvPeriodListSaveItem)
        tvQuoteCreditListSaveItem = view.findViewById(R.id.tvQuoteCreditListSaveItem)
        tvTypeListSaveItem = view.findViewById(R.id.tvTypeListSaveItem)
        tvCapitalValue = view.findViewById(R.id.tvCapitalValueList)
        tvInterestValue = view.findViewById(R.id.tvInterestValueList)
        llCapitalValue = view.findViewById(R.id.llCapitalBought)
        llInterestValue = view.findViewById(R.id.llInterestBought)
        btnMore = view.findViewById(R.id.btn_more_qcsil)
        llCapitalValue.visibility = View.INVISIBLE
        llInterestValue.visibility = View.INVISIBLE
    }

    fun load(data: CalcDTO, callback:(MoreOptionsItemsQuoteSave)->Unit){

        tvCreditValueListSaveItem.text = NumbersUtil.COPtoString(data.valueCredit)
        tvInterestListSaveItem.text = "${data.interest.toString()} % ${data.kindOfTax}"
        tvNameListSaveItem.text = data.name
        tvPeriodListSaveItem.text = data.period.toString()
        tvQuoteCreditListSaveItem.text = NumbersUtil.COPtoString(data.quoteCredit)
        tvTypeListSaveItem.text = data.type
        if(CalcEnum.VARIABLE.toString().contentEquals(data.type)) {
            tvCapitalValue.text = NumbersUtil.COPtoString(data.capitalValue)
            tvInterestValue.text = NumbersUtil.COPtoString(data.interestValue)
            llCapitalValue.visibility = View.VISIBLE
            llInterestValue.visibility = View.VISIBLE
        }

        val builder = AlertDialog.Builder(itemView.context)
        builder.apply {
            setTitle(itemView.resources.getString(R.string.pick_option))
            setItems(items) { _, which ->
                callback.invoke(MoreOptionsItemsQuoteSave.values()[which])
            }
        }
        btnMore.setOnClickListener{
            builder.create().show()
        }

    }
}