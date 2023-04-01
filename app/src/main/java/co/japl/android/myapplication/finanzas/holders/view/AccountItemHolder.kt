package co.japl.android.myapplication.holders.view

import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.AccountDTO
import co.japl.android.myapplication.finanzas.utils.TaxEnum

class AccountItemHolder(var view:View) : RecyclerView.ViewHolder(view) {
    lateinit var name:TextView
    lateinit var delete:ImageButton

    fun loadFields(){
        name = view.findViewById(R.id.name_acil)
        delete = view.findViewById(R.id.btn_delete_acil)
    }

    fun setFields(values:AccountDTO,action:View.OnClickListener){
        name.text = values.name
        delete.setOnClickListener(action)
    }
}