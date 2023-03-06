package co.japl.android.myapplication.adapter

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
import co.japl.android.myapplication.finanzas.bussiness.DTO.PeriodDTO
import co.japl.android.myapplication.finanzas.putParams.PeriodsQuotesParams
import co.japl.android.myapplication.finanzas.utils.TaxEnum
import co.japl.android.myapplication.holders.PeriodsHolder
import co.japl.android.myapplication.holders.view.PeriodItemHolder
import co.japl.android.myapplication.holders.view.TaxItemHolder
import com.google.android.material.snackbar.Snackbar
import java.time.Period

class ListPeriodAdapter(var data:MutableList<PeriodDTO>,var navController: NavController) : RecyclerView.Adapter<PeriodItemHolder>() {
    private lateinit var saveSvc: SaveSvc<TaxDTO>
    private lateinit var view:View


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeriodItemHolder {
        Log.d(this.javaClass.name,"on create view holder")
        saveSvc = TaxImpl(ConnectDB(parent.context))
        view = parent
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.quotes_paid_item_list, parent, false)
        val viewHolder =  PeriodItemHolder(view)
        viewHolder.loadFields()
        return viewHolder
    }

    override fun getItemCount(): Int {
        Log.d(this.javaClass.name,"get item count ${data.size}")
        return data.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PeriodItemHolder, position: Int) {
        Log.d(this.javaClass.name,"on binging view holder $position")
       holder.setFields(data[position]) {
            PeriodsQuotesParams.Companion.Historical.newInstance(data[position].creditCardId,data[position].periodStart.dayOfMonth.toShort(),data[position].periodEnd, navController)
        }
    }
}