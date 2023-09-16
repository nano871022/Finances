package co.japl.android.myapplication.holders.view

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.provider.CalendarContract.Colors
import android.transition.Scene
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.toColor
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.interfaces.ITagQuoteCreditCardSvc
import co.japl.android.myapplication.finanzas.bussiness.impl.TagQuoteCreditCardImpl
import co.japl.android.myapplication.finanzas.enums.KindOfTaxEnum
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsCreditCard
import co.japl.android.myapplication.finanzas.enums.TaxEnum
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.gms.common.SignInButton.ColorScheme
import com.google.android.material.color.utilities.ColorUtils
import com.google.common.primitives.Shorts
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

class BoughtViewHolder(val itemView:View) : RecyclerView.ViewHolder(itemView) {
    lateinit var tvBoughtName:TextView
    lateinit var tvCreditValueBought:TextView
    lateinit var tvQuotesBought:TextView
    lateinit var tvMonthBought:TextView
    lateinit var tvCapitalBought:TextView
    lateinit var tvInterestBought:TextView
    lateinit var btnMore:ImageView
    lateinit var tvTotalQuoteBought:TextView
    lateinit var tvBoughtDate:TextView
    lateinit var tvPendingToPay:TextView
    lateinit var tvTaxBoughtICC:TextView
    lateinit var llTitleBought:LinearLayout
    var tag:ImageView? = null
    var tagSvc : ITagQuoteCreditCardSvc = TagQuoteCreditCardImpl(ConnectDB(itemView.context))

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
        llTitleBought = view.findViewById(R.id.llTitleBought)
        tag = view.findViewById(R.id.img_tag_bil)
        tag?.visibility = View.GONE
        llTitleBought.setOnClickListener {
            (llTitleBought.parent as LinearLayout).children.filter { it.id != R.id.llTitleBought }
                .forEach {
                    val state = if(it.visibility == View.GONE){
                        View.VISIBLE
                    }else{
                        View.GONE
                    }
                    ObjectAnimator.ofInt(it, "visibility", state).setDuration(700).start()
                    ObjectAnimator.ofInt(it, "height", if(state == View.GONE)0 else Int.MAX_VALUE ).setDuration(700).start()
                }
        }
        llTitleBought.callOnClick()
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
        tagSvc.getTags(values.id).takeIf { it.isNotEmpty() }?.let{
            tag?.tooltipText = it.first().name
            tag?.visibility = View.VISIBLE
        }
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
            val months = DateUtils.getMonths(values.boughtDate, LocalDateTime.of(dateLast,
                LocalTime.MAX))

            if(values.month <= 1){
                items = items.filter { it != itemsOrigin.filterIndexed { index, _ -> index == MoreOptionsItemsCreditCard.AMORTIZATION.i}[0] }.toTypedArray()
                items = items.filter { it != itemsOrigin.filterIndexed { index, _ -> index == MoreOptionsItemsCreditCard.DIFFER_INSTALLMENT.i}[0] }.toTypedArray()
            }
            if(values.createDate.toLocalDate() !in dateFirst..dateLast){
                items = items.filter{ it != itemsOrigin.filterIndexed { index, _ -> index == MoreOptionsItemsCreditCard.EDIT.i }[0] }.toTypedArray()
            }
            if(values.recurrent != (1).toShort() || (values.recurrent == (1).toShort() && values.createDate.toLocalDate() in dateFirst..dateLast)){
                items = items.filter{ it != itemsOrigin.filterIndexed { index, _ ->  index == MoreOptionsItemsCreditCard.ENDING.i }[0] }.toTypedArray()
                items = items.filter{ it != itemsOrigin.filterIndexed{ index,_-> index == MoreOptionsItemsCreditCard.UPDATE_VALUE.i}[0] }.toTypedArray()
            }
            if(values.month > 1 && months < 1 ){
                items = items.filter{ it != itemsOrigin.filterIndexed { index, _ ->  index == MoreOptionsItemsCreditCard.DIFFER_INSTALLMENT.i }[0] }.toTypedArray()
            }

            if(values.recurrent == (1).toShort() ){
                items = items.filter{ it != itemsOrigin.filterIndexed { index, _ ->  index == MoreOptionsItemsCreditCard.DIFFER_INSTALLMENT.i }[0] }.toTypedArray()
            }
            setItems(items) { dialog, which ->
                val itemSelect = (dialog as AlertDialog).listView.getItemAtPosition(which)
                val item = itemsOrigin.indexOf(itemSelect)
                callback.invoke(MoreOptionsItemsCreditCard.findByOrdinal(item))
            }
        }
        btnMore.setOnClickListener {
            builder.create().show()
        }
    }

    fun changeColor(color:Int){
        itemView?.findViewById<LinearLayout>(R.id.llTitleBought)?.parent?.let {
            (it as LinearLayout).background = itemView.context.resources.getColor(color).toDrawable()
        }

    }
}