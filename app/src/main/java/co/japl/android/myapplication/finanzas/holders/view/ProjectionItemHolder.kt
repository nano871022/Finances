package co.japl.android.myapplication.holders.view

import android.app.AlertDialog
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.DTO.ProjectionDTO
import co.japl.android.myapplication.finanzas.enums.KindofProjectionEnum
import co.japl.android.myapplication.finanzas.enums.MoreOptionalItemsProjection
import co.japl.android.myapplication.finanzas.holders.interfaces.IItemHolder
import co.com.japl.ui.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import java.time.LocalDate
import java.time.Period

class ProjectionItemHolder(val itemView:View) : IItemHolder<ProjectionDTO,MoreOptionalItemsProjection>,RecyclerView.ViewHolder(itemView) {
    lateinit var date:TextView
    lateinit var name:TextView
    lateinit var value:TextView
    lateinit var month:TextView
    lateinit var saved:TextView
    lateinit var quote:TextView
    lateinit var more:ImageButton
    val itemsMain = itemView.context.resources.getStringArray(R.array.projection_item_options)


    override fun loadFields(){
        date = itemView.findViewById(R.id.tv_dt_paid_lip)
        name = itemView.findViewById(R.id.tv_name_lip)
        value = itemView.findViewById(R.id.tv_tot_value_lip)
        month = itemView.findViewById(R.id.tv_months_lip)
        saved = itemView.findViewById(R.id.tv_saved_lip)
        quote = itemView.findViewById(R.id.tv_quote_lip)
        more = itemView.findViewById(R.id.btn_more_lip)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setFields(values: ProjectionDTO, callback:(MoreOptionalItemsProjection)->Unit){
        Log.d(javaClass.name,"${values.end} ${values.quote} ${Period.between(LocalDate.now(),values.end).toTotalMonths()} ${Period.between(values.end,
            LocalDate.now()).toTotalMonths()}")
        val months = Period.between(LocalDate.now(),values.end).toTotalMonths()
        val index = itemView.resources.getStringArray(R.array.kind_of_projection_list).indexOf(values.type)
        val originMonths = KindofProjectionEnum.values()[index].months
        val month = originMonths - months
        name.text = values.name
        date.text = DateUtils.localDateToString(values.end)
        value.text = NumbersUtil.toString(values.value)
        this.month.text = months.toString()
        saved.text = NumbersUtil.toString(values.quote * month.toBigDecimal())
        quote.text = NumbersUtil.toString(values.quote)
        editAllowed(values,callback)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun editAllowed(values:ProjectionDTO,callback:(MoreOptionalItemsProjection)->Unit){
        val first = LocalDate.now().withDayOfMonth(1)
        val last = first.plusMonths(1).minusDays(1)
        val builder = AlertDialog.Builder(itemView.context)
        builder.apply {
            setTitle(itemView.resources.getString(R.string.pick_option))
            var items = itemsMain
            if(!(values.create >= first && values.create <= last)){
                items = items.filterIndexed { index,_ -> index != 0 }.toTypedArray()
            }
            setItems(items) { dialog, which ->
                val index = itemsMain.indexOf(items[which] )
                callback.invoke(MoreOptionalItemsProjection.values()[index])
            }
        }
        more.setOnClickListener{
            builder.create().show()
        }
    }
}