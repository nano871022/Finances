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
import co.japl.android.myapplication.utils.CalcEnum
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
        val format = DecimalFormat("$ #,###.00")
        holder.tvCreditValueListSaveItem.text = format.format(data[position].valueCredit)
        holder.tvInterestListSaveItem.text = "${data[position].interest.toString()} % ${data[position].kindOfTax}"
        holder.tvNameListSaveItem.text = data[position].name
        holder.tvPeriodListSaveItem.text = data[position].period.toString()
        holder.tvQuoteCreditListSaveItem.text = format.format(data[position].quoteCredit)
        holder.tvTypeListSaveItem.text = data[position].type
        if(CalcEnum.VARIABLE.toString().contentEquals(data[position].type)) {
            holder.tvCapitalValue.text = format.format(data[position].capitalValue)
            holder.tvInterestValue.text = format.format(data[position].interestValue)
            holder.llCapitalValue.visibility = View.VISIBLE
            holder.llInterestValue.visibility = View.VISIBLE
        }
        holder.btnDelete.setOnClickListener {
            val dialog = AlertDialog.Builder(view.context)
                .setTitle(R.string.do_you_want_to_delete_this_record)
                .setNegativeButton(R.string.cancel, null).setPositiveButton(R.string.delete, null)
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
                    Snackbar.make(holder.itemView, R.string.dont_deleted, Snackbar.LENGTH_LONG)
                        .setAction(R.string.close, null).show()
                }
            }
        }
        holder.btnAmortization.setOnClickListener{
            AmortizationTableParams.newInstance(data[position],view.findNavController())
        }
    }
}