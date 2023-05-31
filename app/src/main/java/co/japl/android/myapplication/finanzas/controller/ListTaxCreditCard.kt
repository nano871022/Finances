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
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
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

class ListTaxCreditCard : Fragment() ,LoaderManager.LoaderCallbacks<Pair<List<CreditCardDTO>,List<TaxDTO>>> {

    private lateinit var holder:TaxHolder
    private lateinit var searchCCSvc: SaveSvc<CreditCardDTO>
    private lateinit var searchTaxSvc: SaveSvc<TaxDTO>

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
        searchCCSvc = CreditCardImpl(connect)
        searchTaxSvc = TaxImpl(connect)
        holder = TaxHolder(view,parentFragmentManager,findNavController())
        holder.setFields(null)
        loaderManager.initLoader(1,null,this)
        return view
    }

    private fun search(creditCard:CreditCardDTO){
        val list = searchTaxSvc.getAll().filter {
            it.codCreditCard == creditCard.id
        }.sortedByDescending{ String.format("%04d%02d",it.year,it.month).toInt() }
            .toMutableList()
        holder.loadRecycler(list)

    }

    private fun clearList(){
        holder.recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Pair<List<CreditCardDTO>,List<TaxDTO>>> {
        return object:AsyncTaskLoader<Pair<List<CreditCardDTO>,List<TaxDTO>>>(requireContext()){
            private var data:Pair<List<CreditCardDTO>,List<TaxDTO>>? = null
            override fun onStartLoading() {
                super.onStartLoading()
                if(data != null){
                deliverResult(data)
                }else{
                    forceLoad()
                }
            }
            override fun loadInBackground(): Pair<List<CreditCardDTO>,List<TaxDTO>>? {
                var ccList = searchCCSvc.getAll()
                var taxList = searchTaxSvc.getAll()
                data = Pair(ccList,taxList)
                return data
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Pair<List<CreditCardDTO>,List<TaxDTO>>>) {
    }

    override fun onLoadFinished(loader: Loader<Pair<List<CreditCardDTO>,List<TaxDTO>>>, pair: Pair<List<CreditCardDTO>,List<TaxDTO>>?) {
        pair?.let {

            holder.setFields() {
                val builder = AlertDialog.Builder(requireContext())
                with(builder) {
                    setItems(pair.first?.map { "${it.id}. ${it.name}" }
                        ?.toTypedArray()) { _, position ->
                        holder.creditCard.setText(pair.first[position].name)
                        search(pair.first[position])
                    }
                }
                val dialog = builder.create()
                dialog.show()
            }

            holder.lists {
                it.lbNameTCC.setEndIconOnClickListener { _ ->
                    it.creditCard.setText("")
                    clearList()
                }
                holder.loadRecycler(pair.second.toMutableList())
                if (pair.first.isNotEmpty() && pair.first.size == 1) {
                    it.creditCard.setText(pair.first.first().name)
                    search(pair.first.first())
                }
            }
        }
    }
}