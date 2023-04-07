package co.japl.android.myapplication.adapter

import android.app.AlertDialog
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.bussiness.impl.TaxImpl
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.AccountDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.InputDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.AccountImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.InputImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.PaidImpl
import co.japl.android.myapplication.finanzas.putParams.PaidsParams
import co.japl.android.myapplication.finanzas.utils.TaxEnum
import co.japl.android.myapplication.holders.view.*
import com.google.android.material.snackbar.Snackbar

class ListPeriodPaidAdapter(var data:MutableList<PaidDTO>,val navController: NavController) : RecyclerView.Adapter<PeriodPaidItemHolder>() {
    private lateinit var view:View


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeriodPaidItemHolder {
        view = parent
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_periods_paid_item_list, parent, false)
        val viewHolder =  PeriodPaidItemHolder(view)
        viewHolder.loadFields()
        return viewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PeriodPaidItemHolder, position: Int) {
        holder.setFields(data[position]){
            when(it?.id){
                R.id.btn_search_ppil->{
                    PaidsParams.newInstanceDetail(data[position].date, navController)
                }
            }
        }
    }
}