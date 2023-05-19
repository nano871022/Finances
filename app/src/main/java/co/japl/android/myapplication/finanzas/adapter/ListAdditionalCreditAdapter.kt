package co.japl.android.myapplication.finanzas.adapter

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
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.AdditionalCreditImpl
import co.japl.android.myapplication.finanzas.holders.view.AdditionalCreditItemHolder
import co.japl.android.myapplication.finanzas.putParams.AdditionalCreditParams
import com.google.android.material.snackbar.Snackbar

class ListAdditionalCreditAdapter(val data:MutableList<AdditionalCreditDTO>,val navController: NavController): RecyclerView.Adapter<AdditionalCreditItemHolder>() {
    private lateinit var additionalSvc:SaveSvc<AdditionalCreditDTO>
    private lateinit var view:View
    private lateinit var holder: AdditionalCreditItemHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdditionalCreditItemHolder {
        view = parent
        additionalSvc = AdditionalCreditImpl(ConnectDB(view.context))
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_additional_credit_item_list,parent,false)
        holder = AdditionalCreditItemHolder(view)
        holder.loadFields()
        return holder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: AdditionalCreditItemHolder, position: Int) {
        holder.setField(data[position]) {
            when (it.id) {
                R.id.btn_delete_acil -> {
                    val dialog = AlertDialog
                        .Builder(view.context)
                        .setTitle(R.string.do_you_want_to_delete_this_record)
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.delete, null)
                        .create()
                    dialog.show()
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener {
                            if (additionalSvc.delete(data[position].id)) {
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
             R.id.btn_edit_acil-> AdditionalCreditParams.newInstance(data[position], navController)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}