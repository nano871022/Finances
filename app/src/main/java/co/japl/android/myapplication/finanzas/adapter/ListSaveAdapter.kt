package co.japl.android.myapplication.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.bussiness.impl.SaveImpl
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.putParams.AmortizationTableParams
import co.japl.android.myapplication.holders.view.ViewHolder
import co.japl.android.myapplication.finanzas.enums.CalcEnum
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsQuoteSave
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat

class ListSaveAdapter(private val data:MutableList<CalcDTO>,val view:View) : RecyclerView.Adapter<ViewHolder>() {
    lateinit var dbConnect: ConnectDB
    lateinit var saveSvc: SaveSvc<CalcDTO>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        dbConnect = ConnectDB(parent.context)
        saveSvc = SaveImpl(dbConnect)
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.quote_credit_save_item_list, parent, false)
        val viewHolder =  ViewHolder(view)
        viewHolder.loadFields(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.load(data[position]) {
            when(it) {
                MoreOptionsItemsQuoteSave.DELETE-> {
                    val dialog = AlertDialog.Builder(view.context)
                        .setTitle(R.string.do_you_want_to_delete_this_record)
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.delete, null)
                        .create()
                    dialog.show()
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        dialog.dismiss()
                        if (saveSvc.delete(data[position].id)) {
                            Snackbar.make(
                                holder.itemView,
                                R.string.delete_successfull,
                                Snackbar.LENGTH_LONG
                            )
                                .setAction(R.string.close) {

                                }
                                .show().also {
                                    data.removeAt(position)
                                    this.notifyItemRemoved(position)
                                    this.notifyDataSetChanged()
                                }
                        } else {
                            Snackbar.make(
                                holder.itemView,
                                R.string.dont_deleted,
                                Snackbar.LENGTH_LONG
                            )
                                .setAction(R.string.close, null).show()
                        }
                    }
                }
                        MoreOptionsItemsQuoteSave.AMORTIZATION-> {
                    AmortizationTableParams.newInstance(data[position], view.findNavController())
                }
            }
        }
    }
}