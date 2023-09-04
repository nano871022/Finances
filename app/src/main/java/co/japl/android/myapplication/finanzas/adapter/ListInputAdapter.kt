package co.japl.android.myapplication.adapter

import android.app.AlertDialog
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.InputDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.InputImpl
import co.japl.android.myapplication.finanzas.bussiness.mapping.InputMap
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsInput
import co.japl.android.myapplication.holders.view.InputItemHolder
import com.google.android.material.snackbar.Snackbar

class ListInputAdapter(var data:MutableList<InputDTO>) : RecyclerView.Adapter<InputItemHolder>() {
    private lateinit var saveSvc: SaveSvc<InputDTO>
    private lateinit var view:View


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InputItemHolder {
        Log.d(this.javaClass.name,"on create view holder")
        view = parent
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_input_item_list, parent, false)
        saveSvc = InputImpl(ConnectDB(parent.context), InputMap(parent.context))
        val viewHolder =  InputItemHolder(view)
        viewHolder.loadFields()
        return viewHolder
    }

    override fun getItemCount(): Int {
        Log.d(this.javaClass.name,"get item count ${data.size}")
        return data.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: InputItemHolder, position: Int) {
        Log.d(this.javaClass.name,"on binging view holder $position")
       holder.setFields(data[position]) {
           when (it) {
               MoreOptionsItemsInput.DELETE -> {
                   val dialog = AlertDialog.Builder(view.context)
                       .setTitle(R.string.do_you_want_to_delete_this_record)
                       .setNegativeButton(R.string.cancel, null)
                       .setPositiveButton(R.string.delete, null).create()

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
    }
}