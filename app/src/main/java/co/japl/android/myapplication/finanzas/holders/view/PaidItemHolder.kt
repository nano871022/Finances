package co.japl.android.myapplication.holders.view

import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil

class PaidItemHolder(var view:View) : RecyclerView.ViewHolder(view) {
    lateinit var date:TextView
    lateinit var name:TextView
    lateinit var value:TextView
    lateinit var delete:AppCompatImageView

    fun loadFields(){

        view.findViewById<TextView>(R.id.date_paid_pil)?.let{
            date = it
        }
        view.findViewById<TextView>(R.id.name_pil)?.let{
            name = it
        }
        view.findViewById<TextView>(R.id.value_pil)?.let {
            value = it
        }
        view.findViewById<AppCompatImageView>(R.id.btn_delete_pil)?.let {
            delete = it
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setFields(values:PaidDTO, action:View.OnClickListener){
        if(this::date.isInitialized) {
            date.text = DateUtils.localDateToString(values.date)
        }
        if(this::name.isInitialized) {
            name.text = values.name
        }
        if(this::value.isInitialized) {
            value.text = NumbersUtil.COPtoString(values.value)
        }
        if(this::delete.isInitialized) {
            delete.setOnClickListener(action)
        }
        if(values.recurrent == (1).toShort()){
            delete.setBackgroundColor(Color.RED)
        }
    }
}