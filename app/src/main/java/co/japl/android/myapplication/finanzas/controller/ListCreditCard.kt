package co.japl.android.myapplication.controller

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListCreditCardAdapter
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.impl.CreditCardImpl
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICreditCardSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ISaveSvc
import co.japl.android.myapplication.finanzas.holders.ListCreditCardHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.IListHolder
import co.japl.android.myapplication.putParams.CreditCardParams
import dagger.hilt.android.AndroidEntryPoint
import java.util.Arrays
import javax.inject.Inject

@AndroidEntryPoint
class ListCreditCard : Fragment() , View.OnClickListener, LoaderManager.LoaderCallbacks<List<CreditCardDTO>> {
    private lateinit var holder:IListHolder<ListCreditCardHolder,CreditCardDTO>

    @Inject lateinit var saveSvc:ICreditCardSvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_list_credit_card, container, false)
        holder = ListCreditCardHolder(view,parentFragmentManager,findNavController())
        holder.setFields(this)
        loaderManager.initLoader(1,null,this)
        return view
    }

    private fun add(){
        CreditCardParams.newInstance(findNavController())
    }

    override fun onResume() {
        super.onResume()
        loaderManager.restartLoader(1,null,this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnAddNewCCS ->add()
            else->
                view.let{
                Toast.makeText(it!!.context,"Invalid Option",Toast.LENGTH_LONG).show()}}

    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<CreditCardDTO>> {
        return object:AsyncTaskLoader<List<CreditCardDTO>>(requireContext()){
            private var data:List<CreditCardDTO>? = null
            override fun onStartLoading() {
                super.onStartLoading()
                if(data != null){
                    deliverResult(data)
                }else{
                    forceLoad()
                }
            }
            override fun loadInBackground(): List<CreditCardDTO>? {
                data = saveSvc.getAll()
                return data
            }

        }
    }

    override fun onLoaderReset(loader: Loader<List<CreditCardDTO>>) {
    }

    override fun onLoadFinished(loader: Loader<List<CreditCardDTO>>, data: List<CreditCardDTO>?) {
        data?.let {
            holder.loadRecycler(it.toMutableList())
            saveSvc.backup("CreditCard.dat")
            saveSvc.restoreBackup("CreditCard.dat")
        }
    }


}