package co.japl.android.myapplication.controller

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListPeriodAdapter
import co.japl.android.myapplication.adapter.ListSaveAdapter
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.bussiness.impl.SaveCreditCardBoughtImpl
import co.japl.android.myapplication.bussiness.impl.SaveImpl
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.PeriodDTO
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGetPeriodsServices
import co.japl.android.myapplication.finanzas.putParams.PeriodsParams
import co.japl.android.myapplication.holders.view.PeriodItemHolder
import co.japl.android.myapplication.holders.view.ViewHolder
import java.util.Collections
import kotlin.properties.Delegates

class ListQuotesPaid : Fragment() {
    lateinit var recyclerView:RecyclerView
    lateinit var adapter:RecyclerView.Adapter<PeriodItemHolder>
    lateinit var list:List<PeriodDTO>
    lateinit var contexts:Context
    lateinit var dbConnect: ConnectDB
    lateinit var getPeriodsSvc: IGetPeriodsServices
    var creditCardId by Delegates.notNull<Int>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_list_periods, container, false)
        contexts = rootView.context
        recyclerView = rootView.findViewById(R.id.list_period)
        arguments?.let{
            creditCardId = PeriodsParams.Companion.Historical.download(it)
        }

        connectDB()
        loadRecyclerView()
        return rootView
    }

    private fun loadRecyclerView(){
        try {
            if (list.isEmpty()) {
                PeriodsParams.Companion.Historical.toBack(findNavController())
                Toast.makeText(
                    context,
                    resources.getString(R.string.there_are_not_data),
                    Toast.LENGTH_LONG
                ).show()
            }
            recyclerView.layoutManager =
                LinearLayoutManager(contexts, LinearLayoutManager.VERTICAL, false)
            adapter = ListPeriodAdapter(list as MutableList<PeriodDTO>, findNavController())
            recyclerView.adapter = adapter
        }catch(e:java.lang.ClassCastException){
            Toast.makeText(
                context,
                resources.getString(R.string.there_are_not_data),
                Toast.LENGTH_LONG
            ).show()
        }
    }
    private fun connectDB(){
        dbConnect = ConnectDB(contexts)
        getPeriodsSvc = SaveCreditCardBoughtImpl(dbConnect)
        list = getPeriodsSvc.getPeriods(creditCardId)
    }


}