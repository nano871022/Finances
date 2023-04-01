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
import co.japl.android.myapplication.finanzas.utils.TaxEnum
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil

class InputItemHolder(var view:View) : RecyclerView.ViewHolder(view) {
    lateinit var date:TextView
    lateinit var name:TextView
    lateinit var value:TextView
    lateinit var delete:ImageButton

    fun loadFields(){
        date = view.findViewById(R.id.date_input_iil)
        name = view.findViewById(R.id.name_iil)
        value = view.findViewById(R.id.value_iil)
        delete = view.findViewById(R.id.btn_delete_iil)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setFields(values:InputDTO, action:View.OnClickListener){
        date.text = DateUtils.localDateToString( values.date)
        name.text = values.name
        value.text = NumbersUtil.COPtoString(values.value)
        delete.setOnClickListener(action)
    }
}