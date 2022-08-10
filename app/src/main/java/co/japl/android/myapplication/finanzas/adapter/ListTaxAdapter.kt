package co.japl.android.myapplication.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.bussiness.impl.TaxImpl
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.holders.view.TaxItemHolder
import com.google.android.material.snackbar.Snackbar

class ListTaxAdapter(var data:MutableList<TaxDTO>) : RecyclerView.Adapter<TaxItemHolder>() {
    private lateinit var saveSvc: SaveSvc<TaxDTO>
    private lateinit var view:View


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaxItemHolder {
        Log.d(this.javaClass.name,"on create view holder")
        saveSvc = TaxImpl(ConnectDB(parent.context))
        view = parent
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.tax_item_list, parent, false)
        val viewHolder =  TaxItemHolder(view)
        viewHolder.loadFields()
        return viewHolder
    }

    override fun getItemCount(): Int {
        Log.d(this.javaClass.name,"get item count ${data.size}")
        return data.size
    }

    override fun onBindViewHolder(holder: TaxItemHolder, position: Int) {
        Log.d(this.javaClass.name,"on binging view holder $position")
        val months = view.resources.getStringArray(R.array.Months)
        holder.month.text = months[data[position].month.toInt()]
        holder.year.text = data[position].year.toString()
        holder.tax.text = "${data[position].value.toString()} %"
        holder.delete.setOnClickListener {
            if (saveSvc.delete(data[position].id)) {
                Snackbar.make(view, R.string.delete_successfull, Snackbar.LENGTH_LONG)
                    .setAction(R.string.close) {

                    }
                    .show().also { data.removeAt(position)
                        this.notifyDataSetChanged()
                        this.notifyItemRemoved(position) }
            } else {
                Snackbar.make(view, R.string.dont_deleted, Snackbar.LENGTH_LONG)
                    .setAction(R.string.close,null).show()
            }
        }
    }
}