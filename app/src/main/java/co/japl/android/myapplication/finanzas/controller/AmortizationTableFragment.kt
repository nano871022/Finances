package co.japl.android.myapplication.finanzas.controller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.loader.content.AsyncTaskLoader
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.DTO.Amortization
import co.japl.android.myapplication.finanzas.holders.AmortizationTableHolder
import co.japl.android.myapplication.finanzas.putParams.AmortizationTableParams

class AmortizationTableFragment : Fragment() {
    lateinit var holder: AmortizationTableHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_amortization_table, container, false)
        holder = AmortizationTableHolder(view)
        holder.setup(null)
        val data = AmortizationTableParams.download(requireArguments())
        holder.setData(data.first)
        holder.add("QuotesPaid",data.second)
        holder.create()
        holder.load()
        return view
    }


}