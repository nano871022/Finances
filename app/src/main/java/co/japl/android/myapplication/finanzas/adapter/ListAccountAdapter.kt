package co.japl.android.myapplication.adapter

import android.app.AlertDialog
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
import co.japl.android.myapplication.finanzas.bussiness.DTO.AccountDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.AccountImpl
import co.japl.android.myapplication.finanzas.utils.TaxEnum
import co.japl.android.myapplication.holders.view.AccountItemHolder
import co.japl.android.myapplication.holders.view.TaxItemHolder
import com.google.android.material.snackbar.Snackbar

class ListAccountAdapter(var data:MutableList<AccountDTO>) : RecyclerView.Adapter<AccountItemHolder>() {
    private lateinit var saveSvc: SaveSvc<AccountDTO>
    private lateinit var view:View


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountItemHolder {
        Log.d(this.javaClass.name,"on create view holder")
        saveSvc = AccountImpl(ConnectDB(parent.context))
        view = parent
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_account_item_list, parent, false)
        val viewHolder =  AccountItemHolder(view)
        viewHolder.loadFields()
        return viewHolder
    }

    override fun getItemCount(): Int {
        Log.d(this.javaClass.name,"get item count ${data.size}")
        return data.size
    }

    override fun onBindViewHolder(holder: AccountItemHolder, position: Int) {
        Log.d(this.javaClass.name,"on binging view holder $position")
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