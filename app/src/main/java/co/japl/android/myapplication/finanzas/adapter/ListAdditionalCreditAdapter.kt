package co.japl.android.myapplication.finanzas.adapter

import android.app.AlertDialog
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
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.AdditionalCreditImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IAdditionalCreditSvc
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsAdditional
import co.japl.android.myapplication.finanzas.holders.view.AdditionalCreditItemHolder
import co.japl.android.myapplication.finanzas.putParams.AdditionalCreditParams
import co.japl.android.myapplication.finanzas.putParams.CreditCardQuotesParams
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import java.time.LocalDateTime

class ListAdditionalCreditAdapter(val data:MutableList<AdditionalCreditDTO>,val navController: NavController, val inflater:LayoutInflater): RecyclerView.Adapter<AdditionalCreditItemHolder>() {
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
            when (it) {
                MoreOptionsItemsAdditional.VIEW -> {
                    AdditionalCreditParams.newInstance(data[position], true,navController)
                }
                MoreOptionsItemsAdditional.UPDATE_VALUE -> {
                    updateRecurrentValue( position)
                }
                MoreOptionsItemsAdditional.DELETE -> {
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
             MoreOptionsItemsAdditional.EDIT-> AdditionalCreditParams.newInstance(data[position],false, navController)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateRecurrentValue( position:Int){
        val inflaterr = inflater.inflate(R.layout.dialog_update_value_cop,null)
        val dialog = AlertDialog.Builder(view.context)
            .setTitle(R.string.update_value_paymnet)
            .setView(inflaterr)
            .setPositiveButton(R.string.updating, null)
            .setNegativeButton(R.string.cancel, null)
            .create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val value = inflaterr.findViewById<EditText>(R.id.ed_value_dupv)

            if((additionalSvc as IAdditionalCreditSvc ).updateValue(data[position].id,NumbersUtil.toBigDecimal(value))){
                    dialog.dismiss()
                    Snackbar.make(
                        view,
                        R.string.ending_recurrent_payment_successfull,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.close) {}
                        .show().also {
                            CreditCardQuotesParams.Companion.CreateQuote.toBack(navController)
                        }
                }else {
                    dialog.dismiss()
                    Snackbar.make(
                        holder.view,
                        R.string.dont_ending_payment,
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