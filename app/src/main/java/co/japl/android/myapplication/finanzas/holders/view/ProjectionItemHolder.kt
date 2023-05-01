package co.japl.android.myapplication.holders.view

import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.DTO.AccountDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.ProjectionDTO
import co.japl.android.myapplication.finanzas.enums.KindofProjectionEnum
import co.japl.android.myapplication.finanzas.holders.interfaces.IItemHolder
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.button.MaterialButton
import java.time.LocalDate
import java.time.Period

class ProjectionItemHolder(itemView:View) : IItemHolder<ProjectionDTO>,RecyclerView.ViewHolder(itemView) {
    lateinit var date:TextView
    lateinit var name:TextView
    lateinit var value:TextView
    lateinit var month:TextView
    lateinit var saved:TextView
    lateinit var quote:TextView
    lateinit var delete:ImageButton
    lateinit var edit:ImageButton


    override fun loadFields(){
        date = itemView.findViewById(R.id.tv_dt_paid_lip)
        name = itemView.findViewById(R.id.tv_name_lip)
        value = itemView.findViewById(R.id.tv_tot_value_lip)
        month = itemView.findViewById(R.id.tv_months_lip)
        saved = itemView.findViewById(R.id.tv_saved_lip)
        quote = itemView.findViewById(R.id.tv_quote_lip)
        delete = itemView.findViewById(R.id.btn_delete_lip)
        edit = itemView.findViewById(R.id.btn_edit_lip)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setFields(values: ProjectionDTO, action:View.OnClickListener){
        Log.d(javaClass.name,"${values.end} ${values.quote} ${Period.between(LocalDate.now(),values.end).toTotalMonths()} ${Period.between(values.end,
            LocalDate.now()).toTotalMonths()}")
        val months = Period.between(LocalDate.now(),values.end).toTotalMonths()
        val index = itemView.resources.getStringArray(R.array.kind_of_projection_list).indexOf(values.type)
        val originMonths = KindofProjectionEnum.values()[index].months
        val month = originMonths - months
        name.text = values.name
        date.text = DateUtils.localDateToString(values.end)
        value.text = NumbersUtil.COPtoString(values.value)
        this.month.text = months.toString()
        saved.text = NumbersUtil.COPtoString(values.quote * month.toBigDecimal())
        quote.text = NumbersUtil.COPtoString(values.quote)
        delete.setOnClickListener(action)
        edit.setOnClickListener(action)
        editAllowed(values)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun editAllowed(values:ProjectionDTO){
        val first = LocalDate.now().withDayOfMonth(1)
        val last = first.plusMonths(1).minusDays(1)
        Log.d(javaClass.name,"$first $last ${values.create}")
        if(values.create >= first && values.create <= last){
            edit.visibility = View.VISIBLE
        }else{
            edit.visibility = View.GONE
        }
    }
}