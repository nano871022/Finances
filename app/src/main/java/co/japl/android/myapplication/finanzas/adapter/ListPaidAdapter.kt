package co.japl.android.myapplication.adapter

import android.app.AlertDialog
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.PaidImpl
import co.japl.android.myapplication.holders.view.PaidItemHolder
import com.google.android.material.snackbar.Snackbar

class ListPaidAdapter(var data:MutableList<PaidDTO>) : RecyclerView.Adapter<PaidItemHolder>() {
    private lateinit var saveSvc: SaveSvc<PaidDTO>
    private lateinit var view:View


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaidItemHolder {
        view = parent
        saveSvc = PaidImpl(ConnectDB(parent.context))
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_paid_item_list, parent, false)
        val viewHolder =  PaidItemHolder(view)
        viewHolder.loadFields()
        return viewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PaidItemHolder, position: Int) {
       holder.setFields(data[position]) {
           val dialog = AlertDialog.Builder(view.context).setTitle(R.string.do_you_want_to_delete_this_record).setNegativeButton(R.string.cancel,null).setPositiveButton(R.string.delete,null).create()
           dialog.show()
           dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
               if (saveSvc.delete(data[position].id)) {
                   dialog.dismiss()
                   Snackbar.make(view, R.string.delete_successfull, Snackbar.LENGTH_LONG)
                       .setAction(R.string.close) {

                       }
                       .show().also {
                           data.removeAt(position)
                           this.notifyItemRemoved(position)
                           this.notifyDataSetChanged()
                       }
               } else {
                   Snackbar.make(view, R.string.dont_deleted, Snackbar.LENGTH_LONG)
                       .setAction(R.string.close, null).show()
               }
           }
        }
    }
}