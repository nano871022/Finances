package co.japl.android.myapplication.holders.view

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.DTO.AccountDTO

class AccountItemHolder(var view:View) : RecyclerView.ViewHolder(view) {
    lateinit var name:TextView
    lateinit var search:AppCompatImageView
    lateinit var delete:AppCompatImageView

    fun loadFields(){
        name = view.findViewById(R.id.name_accil)
        search = view.findViewById(R.id.btn_search_accil)
        delete = view.findViewById(R.id.btn_delete_accil)
    }

    fun setFields(values:AccountDTO,action:View.OnClickListener){
        name.text = values.name
        search.setOnClickListener(action)
        delete.setOnClickListener(action)
    }
}