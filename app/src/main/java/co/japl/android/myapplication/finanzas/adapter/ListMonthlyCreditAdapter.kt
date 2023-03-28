package co.japl.android.myapplication.finanzas.adapter

import android.app.AlertDialog
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.AdditionalCreditImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditFixImpl
import co.japl.android.myapplication.finanzas.holders.view.AdditionalCreditItemHolder
import co.japl.android.myapplication.finanzas.holders.view.MonthlyCreditItemHolder
import co.japl.android.myapplication.finanzas.putParams.CreditFixParams
import com.google.android.material.snackbar.Snackbar

class ListMonthlyCreditAdapter(val data:MutableList<CreditDTO>,val view:View): RecyclerView.Adapter<MonthlyCreditItemHolder>() {
    private lateinit var creditFixSvc:SaveSvc<CreditDTO>
    private lateinit var holder: MonthlyCreditItemHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlyCreditItemHolder {
        creditFixSvc = CreditFixImpl(ConnectDB(view.context))
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_monthly_credit_item_list,parent,false)
        holder = MonthlyCreditItemHolder(view)
        holder.loadField()
        return holder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MonthlyCreditItemHolder, position: Int) {
        holder.setField(data[position]) {
            when (it.id) {
                R.id.btn_delete_mcil -> {
                    val dialog = AlertDialog
                        .Builder(view.context)
                        .setTitle(R.string.do_you_want_to_delete_this_record)
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.delete, null)
                        .create()
                    dialog.show()
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener {
                            if (creditFixSvc.delete(data[position].id)) {
                                dialog.dismiss()
                                Snackbar.make(
                                    view,
                                    R.string.delete_successfull,
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction(R.string.close) {}
                                    .show().also {
                                        data.removeAt(position)
                                        notifyItemRemoved(position)
                                        notifyDataSetChanged()
                                    }
                            } else {
                                Snackbar.make(view, R.string.dont_deleted, Snackbar.LENGTH_LONG)
                                    .show()
                            }
                        }
                }
                R.id.btn_edit_mcil ->{
                     CreditFixParams.newInstanceAmortizationMonthlyList(data[position],view.findNavController())
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}