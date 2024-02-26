package co.japl.android.myapplication.finanzas.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.interfaces.ITagSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.TagDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.TagsImpl
import co.japl.android.myapplication.finanzas.enums.TagItemEnum
import co.japl.android.myapplication.finanzas.holders.view.TagItemHolder
import com.google.android.material.snackbar.Snackbar

class TagAdapter(val data:MutableList<TagDTO>,val action:(TagDTO)->Unit): RecyclerView.Adapter<TagItemHolder>() {
    var view:View? = null
    var tagSvc:ITagSvc?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagItemHolder {
        view = parent
        tagSvc = TagsImpl(ConnectDB(parent.context))
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tag_item_list,parent,false)
        val holder = TagItemHolder(view){action(it)}
        holder.loadFields()
        return holder
    }

    override fun onBindViewHolder(holder: TagItemHolder, position: Int) {
        holder.setFields(data[position]){
            when(it){
                TagItemEnum.DELETE -> delete(holder,position)
            }
        }
    }
    fun delete(holder: TagItemHolder, position:Int){
        val dialog = AlertDialog.Builder(view?.context)
            .setTitle(R.string.do_you_want_to_delete_this_record)
            .setPositiveButton(R.string.delete, null)
            .setNegativeButton(R.string.cancel, null)
            .create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if (tagSvc?.delete(data[position].id) == true) {
                dialog.dismiss()
                Snackbar.make(
                    view!!,
                    R.string.delete_successfull,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.close) {}
                    .show().also{
                        data.removeAt(position)
                        this.notifyDataSetChanged()
                        this.notifyItemRemoved(position)
                    }
            } else {
                dialog.dismiss()
                Snackbar.make(
                    holder.itemView,
                    R.string.dont_deleted,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.close, null).show()
            }
        }
    }
    override fun getItemCount(): Int {
        return data.size
    }
}