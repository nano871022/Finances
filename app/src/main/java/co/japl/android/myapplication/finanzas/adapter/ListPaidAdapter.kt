package co.japl.android.myapplication.adapter

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.PaidImpl
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsPayments
import co.japl.android.myapplication.finanzas.putParams.PaidsParams
import co.japl.android.myapplication.holders.view.PaidItemHolder
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import java.time.LocalDateTime

class ListPaidAdapter(var data:MutableList<PaidDTO>,var inflater: LayoutInflater,val navController: NavController) : RecyclerView.Adapter<PaidItemHolder>() {
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
           when(it) {
               MoreOptionsItemsPayments.ENDING-> ending(position)
               MoreOptionsItemsPayments.UPDATE_VALUE->updateValue(position)
               MoreOptionsItemsPayments.DELETE -> deleteOption(position)
           }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateValue(position:Int){
        val customLayout = inflater.inflate(R.layout.dialog_update_value_cop,null)
        val dialog = AlertDialog.Builder(view.context)
            .setTitle(R.string.update_value_recurrent_paymner)
            .setView(customLayout)
            .setPositiveButton(R.string.ending,null)
            .setNegativeButton(R.string.cancel, null)
            .create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val value = customLayout.findViewById<EditText>(R.id.ed_value_dupv)
            val dtoNew = data[position].copy()
            dtoNew.end  = LocalDate.of(9999, 12, 31)
            dtoNew.id = 0
            dtoNew.value = NumbersUtil.toBigDecimal(value)
            dtoNew.date = dtoNew.date.withYear(LocalDate.now().year).withMonth(LocalDate.now().monthValue)
            val dto = data[position]
            dto.end = LocalDate.now().withDayOfMonth(1).minusDays(1)
            if(saveSvc.save(dtoNew) >0){
                if (saveSvc.save(dto)>0) {
                    dialog.dismiss()
                    Snackbar.make(
                        view,
                        R.string.ending_recurrent_payment_successfull,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.close) {}
                        .show().also {
                            this.notifyDataSetChanged()
                            this.notifyItemChanged(position)
                            this.notifyItemRemoved(position)
                            PaidsParams.toBack(navController)
                        }
                }else {
                    dialog.dismiss()
                    Snackbar.make(
                        view,
                        R.string.dont_ending_recurrent_payment,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.close, null).show()
                }
            } else {
                dialog.dismiss()
                Snackbar.make(
                    view,
                    R.string.dont_ending_recurrent_payment,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.close, null).show()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun ending(position:Int){
        val dialog = AlertDialog.Builder(view.context)
            .setTitle(R.string.do_you_want_to_ending_recurrent_payment)
            .setPositiveButton(R.string.ending, null)
            .setNegativeButton(R.string.cancel, null)
            .create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val dto = data[position]
            dto.end = LocalDate.now().withDayOfMonth(1).minusDays(1)
            if (saveSvc.save(dto)>0) {
                dialog.dismiss()
                Snackbar.make(
                    view,
                    R.string.ending_recurrent_payment_successfull,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.close) {}
                    .show().also {
                        this.notifyDataSetChanged()
                        this.notifyItemChanged(position)
                        this.notifyItemRemoved(position)
                        PaidsParams.toBack(navController)
                    }
            } else {
                dialog.dismiss()
                Snackbar.make(
                    view,
                    R.string.dont_ending_recurrent_payment,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.close, null).show()
            }
        }
    }

    private fun deleteOption(position:Int){
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