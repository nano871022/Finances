package co.japl.android.finanzas.controller

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.finanzas.R
import co.japl.android.finanzas.adapter.ListTaxAdapter
import co.japl.android.finanzas.bussiness.DB.connections.ConnectDB
import co.japl.android.finanzas.bussiness.DTO.CreditCardDTO
import co.japl.android.finanzas.bussiness.DTO.TaxDTO
import co.japl.android.finanzas.bussiness.impl.CreditCardImpl
import co.japl.android.finanzas.bussiness.impl.TaxImpl
import co.japl.android.finanzas.bussiness.interfaces.SaveSvc
import co.japl.android.finanzas.holders.TaxHolder
import java.util.stream.Collectors

class ListTaxCreditCard : Fragment() , AdapterView.OnItemSelectedListener{


    private lateinit var list:MutableList<TaxDTO>
    private lateinit var holder:TaxHolder
    private lateinit var searchCCSvc: SaveSvc<CreditCardDTO>
    private lateinit var searchTaxSvc: SaveSvc<TaxDTO>
    private lateinit var listCC:List<CreditCardDTO>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_tax_credit_card, container, false)
        val connect = ConnectDB(view.context)
        list = ArrayList<TaxDTO>()
        searchCCSvc = CreditCardImpl(connect)
        searchTaxSvc = TaxImpl(connect)
        setField(view)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun setField(view:View){
        view.let{
            holder = TaxHolder(it,parentFragmentManager)
            holder.setFields(null)
            listCC = searchCCSvc.getAll()
            val list = listCC.toMutableList().stream().map { it.name }.collect(Collectors.toList())
            list.add(0,"-- Seleccionar --")
            holder.lists{
                it.creditCard.adapter = ArrayAdapter(view.context,R.layout.spinner_simple,R.id.tvValueBigSp,list.toTypedArray())
                it.creditCard.onItemSelectedListener = this
                loadRecycleView(it.recyclerView)
            }
        }
    }

    fun loadRecycleView(recyclerView:RecyclerView){
        view.let {
            recyclerView.let { rv ->
                Log.d(this.javaClass.name,"Create RecyclerVire with $list")
                rv.layoutManager = LinearLayoutManager(it?.context,LinearLayoutManager.VERTICAL,false)
                rv.adapter = ListTaxAdapter(list)

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d(this.javaClass.name,"Selected item $position")
        if(position != 0)
        parent.let {
            val selected = it?.getItemAtPosition(position)
            val found = listCC.stream().filter{
                it.name == selected.toString()
            }.findAny()
            if(found.isPresent) {
                list = searchTaxSvc.getAll().stream().filter {
                    it.codCreditCard == found.get().id
                }.collect(Collectors.toList())
                //loadRecycleView(holder.recyclerView)
                holder.recyclerView.adapter = ListTaxAdapter(list)
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}