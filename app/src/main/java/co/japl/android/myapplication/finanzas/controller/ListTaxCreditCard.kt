package co.japl.android.myapplication.controller

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListTaxAdapter
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.bussiness.impl.CreditCardImpl
import co.japl.android.myapplication.bussiness.impl.TaxImpl
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.impl.ScriptService
import co.japl.android.myapplication.holders.TaxHolder
import com.google.android.material.progressindicator.CircularProgressIndicator
import java.util.stream.Collectors

class ListTaxCreditCard : Fragment() {


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
        val progressBar = view?.findViewById(R.id.pbTaxTCC) as CircularProgressIndicator
        progressBar.isEnabled = true
        progressBar.isVisible = true
        view.findViewById<LinearLayout>(R.id.llMainTTCC).isVisible = false
        val connect = ConnectDB(view.context)
        list = ArrayList<TaxDTO>()
        searchCCSvc = CreditCardImpl(connect)
        searchTaxSvc = TaxImpl(connect)

        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setField(view)
        (view?.findViewById(R.id.pbTaxTCC) as CircularProgressIndicator).isVisible = false
        view.findViewById<LinearLayout>(R.id.llMainTTCC).isVisible = true
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun setField(view:View){
        view.let{
            listCC = searchCCSvc.getAll()
            holder = TaxHolder(view,parentFragmentManager,findNavController(),listCC)
            holder.setFields(){
                val builder = AlertDialog.Builder(view.context)
                with(builder) {
                    setItems(holder.list.map { "${it.id}. ${it.name}" }
                        .toTypedArray()) { _, position ->
                        holder.creditCard.setText(holder.list [position].name )
                        search(holder.list[position])
                    }
                }
                val dialog = builder.create()
                dialog.show()
            }
            holder.lists{
                it.lbNameTCC.setEndIconOnClickListener { _->
                    it.creditCard.setText("")
                    clearList()
                }
                loadRecycleView(it.recyclerView)
                if(listCC.isNotEmpty() && listCC.size == 1){
                    it.creditCard.setText(listCC.first().name)
                    search(listCC.first())
                }
            }
        }
    }

    fun loadRecycleView(recyclerView:RecyclerView){
        view.let {
            recyclerView.let { rv ->
                rv.layoutManager = LinearLayoutManager(it?.context,LinearLayoutManager.VERTICAL,false)
                rv.adapter = ListTaxAdapter(list)

            }
        }
    }

    private fun search(creditCard:CreditCardDTO){
        this.list = searchTaxSvc.getAll().filter {
            it.codCreditCard == creditCard.id
        }.sortedByDescending{ String.format("%04d%02d",it.year,it.month).toInt() }
            .toMutableList()
        holder.recyclerView.adapter = ListTaxAdapter(this.list)
    }

    private fun clearList(){
        this.list.clear()
        holder.recyclerView.adapter?.notifyDataSetChanged()
    }
}