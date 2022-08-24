package co.japl.android.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.impl.CreditCardImpl
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.controller.CreateCreditCard
import co.japl.android.myapplication.holders.ListCreditCardItemHolder
import co.japl.android.myapplication.putParams.CreditCardParams
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.snackbar.Snackbar

class ListCreditCardAdapter(var data:MutableList<CreditCardDTO>,var parentFragmentManager:FragmentManager,var navController: NavController) : RecyclerView.Adapter<ListCreditCardItemHolder>() {
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
            CreditCardParams.newInstance(data[position].id.toString(), navController)
        }
    }
}