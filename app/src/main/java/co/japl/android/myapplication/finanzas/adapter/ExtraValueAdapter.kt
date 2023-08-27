package co.japl.android.myapplication.finanzas.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.AddAmortizationDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.ExtraValueAmortizationCreditDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.ExtraValueAmortizationQuoteCreditCardDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.InputDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.AddAmortizationImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IAddAmortizationSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ISaveSvc
import co.japl.android.myapplication.finanzas.enums.MoreOptionesExtraValueItems
import co.japl.android.myapplication.finanzas.holders.view.ExtraValueItemHolder
import com.google.android.material.snackbar.Snackbar

class ExtraValueAdapter(val data:MutableList<Any>): RecyclerView.Adapter<ExtraValueItemHolder>() {
    private lateinit var view:View
    private lateinit var svc:IAddAmortizationSvc
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExtraValueItemHolder {
        view = parent
        svc = AddAmortizationImpl(ConnectDB(view.context))
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_extra_value_item_list, parent, false)
        val holder = ExtraValueItemHolder(view)
        holder.loadFields()
        return holder

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ExtraValueItemHolder, position: Int) {
        holder.setFields(data[position]){
            when(it){
                MoreOptionesExtraValueItems.DELETE-> alertDialog(position)
            }
        }
    }

    private fun alertDialog(position:Int){
        val dialog = AlertDialog.Builder(view.context)
            .setTitle(R.string.do_you_want_to_delete_this_record)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.delete, null).create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val value = data[position]
            val id = if(value is AddAmortizationDTO){
                value.id
            }else if(value is ExtraValueAmortizationCreditDTO){
                value.id
            }else if(value is ExtraValueAmortizationQuoteCreditCardDTO){
                value.id
            }else 0

            if (svc.delete(id)) {
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