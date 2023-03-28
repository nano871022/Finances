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
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.PeriodCreditDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditFixImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.PeriodCreditImpl
import co.japl.android.myapplication.finanzas.holders.view.MonthlyCreditItemHolder
import co.japl.android.myapplication.finanzas.holders.view.PeriodCreditItemHolder
import com.google.android.material.snackbar.Snackbar

class ListPeriodCreditAdapter(val data:MutableList<PeriodCreditDTO>,val view:View,val navController: NavController): RecyclerView.Adapter<PeriodCreditItemHolder>() {
    private lateinit var periodSvc:PeriodCreditImpl
    private lateinit var holder: PeriodCreditItemHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeriodCreditItemHolder {
        periodSvc = PeriodCreditImpl(ConnectDB(view.context))
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_period_credit_item_list,parent,false)
        holder = PeriodCreditItemHolder(view, navController)
        holder.loadField()
        return holder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PeriodCreditItemHolder, position: Int) {
        holder.setField(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

}