package co.japl.android.myapplication.holders.view

import android.os.Build
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.AccountDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.InputDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import co.japl.android.myapplication.finanzas.utils.TaxEnum
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil

class PaidItemHolder(var view:View) : RecyclerView.ViewHolder(view) {
    lateinit var date:TextView
    lateinit var name:TextView
    lateinit var value:TextView
    lateinit var delete:ImageButton

    fun loadFields(){
        date = view.findViewById(R.id.date_paid_pil)
        name = view.findViewById(R.id.name_pil)
        value = view.findViewById(R.id.value_pil)
        delete = view.findViewById(R.id.btn_delete_pil)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setFields(values:PaidDTO, action:View.OnClickListener){
        date.text = DateUtils.localDateToString( values.date)
        name.text = values.name
        value.text = NumbersUtil.COPtoString(values.value)
        delete.setOnClickListener(action)
    }
}