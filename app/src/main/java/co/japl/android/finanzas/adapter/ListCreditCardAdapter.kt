package co.japl.android.finanzas.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.finanzas.R
import co.japl.android.finanzas.bussiness.DTO.CalcDTO
import co.japl.android.finanzas.bussiness.interfaces.SaveSvc
import co.japl.android.finanzas.bussiness.DB.connections.CalculationConnectDB
import co.japl.android.finanzas.bussiness.DB.connections.ConnectDB
import co.japl.android.finanzas.bussiness.DTO.CreditCardDB
import co.japl.android.finanzas.bussiness.DTO.CreditCardDTO
import co.japl.android.finanzas.bussiness.DTO.TaxDTO
import co.japl.android.finanzas.bussiness.impl.CreditCardImpl
import co.japl.android.finanzas.bussiness.impl.SaveImpl
import co.japl.android.finanzas.bussiness.impl.TaxImpl
import co.japl.android.finanzas.controller.CreateCreditCard
import co.japl.android.finanzas.holders.ListCreditCardItemHolder
import co.japl.android.finanzas.putParams.CreditCardParams
import co.japl.android.finanzas.utils.CalcEnum
import co.japl.android.finanzas.utils.NumbersUtil
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat

class ListCreditCardAdapter(var data:MutableList<CreditCardDTO>,var parentFragmentManager:FragmentManager) : RecyclerView.Adapter<ListCreditCardItemHolder>() {
    private lateinit var saveSvc: SaveSvc<CreditCardDTO>
    private lateinit var view:View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListCreditCardItemHolder {
        saveSvc = CreditCardImpl(ConnectDB(parent.context))
        view = parent
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.credit_card_item_list, parent, false)
        val viewHolder =  ListCreditCardItemHolder(view)
        viewHolder.loadFields()
        return viewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ListCreditCardItemHolder, position: Int) {
        holder.name.text =data[position].name
        holder.cutOffDay.text = data[position].cutOffDay.toString()
        holder.warningQuote.text = NumbersUtil.COPtoString(data[position].warningValue)
        holder.status.text = data[position].status.toString()
        holder.delete.setOnClickListener {
            if (saveSvc.delete(data[position].id)) {
                Snackbar.make(view, R.string.delete_successfull, Snackbar.LENGTH_LONG)
                    .setAction(R.string.close) {

                    }
                    .show().also { data.removeAt(position)
                        this.notifyDataSetChanged()
                        this.notifyItemRemoved(position) }
            } else {
                Snackbar.make(view, R.string.dont_deleted, Snackbar.LENGTH_LONG)
                    .setAction(R.string.close,null).show()
            }
        }
        holder.edit.setOnClickListener{
            parentFragmentManager.beginTransaction().replace(R.id.fragment_initial,
                CreateCreditCard()
            ).setTransition(
                FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
            CreditCardParams.newInstance(data[position].id.toString())
            parentFragmentManager.setFragmentResult(
                CreditCardParams.Params.ARG_PARAM_CODE, bundleOf(
                    CreditCardParams.Params.ARG_PARAM_CODE to data[position].id.toString())
            )
        }
    }
}