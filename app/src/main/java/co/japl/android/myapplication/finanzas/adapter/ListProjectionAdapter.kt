package co.japl.android.myapplication.adapter

import android.app.AlertDialog
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.ProjectionDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.ProjectionsImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IProjectionsSvc
import co.japl.android.myapplication.finanzas.enums.MoreOptionalItemsProjection
import co.japl.android.myapplication.finanzas.putParams.ProjectionsParams
import co.japl.android.myapplication.holders.view.ProjectionItemHolder
import com.google.android.material.snackbar.Snackbar

class ListProjectionAdapter(var data:MutableList<ProjectionDTO>,val navController: NavController) : RecyclerView.Adapter<ProjectionItemHolder>() {
    private lateinit var view:View
    private lateinit var svc:IProjectionsSvc

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectionItemHolder {
        view = parent
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_projection_item_list, parent, false)
        val viewHolder =  ProjectionItemHolder(view)
        svc = ProjectionsImpl(ConnectDB(view.context))
        viewHolder.loadFields()
        return viewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ProjectionItemHolder, position: Int) {
       holder.setFields(data[position]) {
           when (it) {
               MoreOptionalItemsProjection.DELETE -> {
                   val dialog = AlertDialog.Builder(view.context)
                       .setTitle(R.string.do_you_want_to_delete_this_record)
                       .setNegativeButton(R.string.cancel, null)
                       .setPositiveButton(R.string.delete, null).create()
                   dialog.show()
                   dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                       if (svc.delete(data[position].id)) {
                           dialog.dismiss()
                           Snackbar.make(view, R.string.delete_successfull, Snackbar.LENGTH_LONG)
                               .setAction(R.string.close) {}
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
               MoreOptionalItemsProjection.EDIT -> {
                   ProjectionsParams.newInstanceFromList(data[position].id.toLong(), navController)
               }
           }
       }
    }
}