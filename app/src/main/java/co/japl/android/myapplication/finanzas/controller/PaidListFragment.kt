package co.japl.android.myapplication.finanzas.controller

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.PaidImpl
import co.japl.android.myapplication.finanzas.holders.interfaces.IRecyclerView
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ISaveSvc
import co.japl.android.myapplication.finanzas.holders.PaidListHolder
import co.japl.android.myapplication.finanzas.putParams.PaidsParams
import java.math.BigDecimal
import java.time.LocalDate

class PaidListFragment : Fragment() , LoaderManager.LoaderCallbacks<List<PaidDTO>> {
    private lateinit var service: ISaveSvc<PaidDTO>
    private lateinit var date:LocalDate
    private lateinit var holder: IHolder<PaidDTO>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_paid_list, container, false)
        date = getDate(arguments)
        holder = PaidListHolder(root)
        service = PaidImpl(ConnectDB(root.context))
        holder.setFields(null)
        loaderManager.initLoader(1,null,this)
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDate(arguments:Bundle?):LocalDate{
        return arguments?.let {
            PaidsParams.downloadList(it)
        }?:LocalDate.now().withDayOfMonth(1)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getData():MutableList<PaidDTO>{
        val data = service.get(getPaids()).toMutableList()
        (service as PaidImpl).getRecurrent(date)?.let {
            if(it.isNotEmpty()){
                data.addAll(it)
            }
        }
        data.sortByDescending { it.date }
        return data
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPaids():PaidDTO{
        val id = 0
        val account = ""
        val name = ""
        val value = BigDecimal.ZERO
        val recurrent = 0
        return PaidDTO(id, date,account,name,value,recurrent.toShort())
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<PaidDTO>> {
        return object:AsyncTaskLoader<List<PaidDTO>>(requireContext()){
            private var data:List<PaidDTO>? = null
            override fun onStartLoading() {
                super.onStartLoading()
                if(data != null){
                    deliverResult(data)
                }else{
                    forceLoad()
                }
            }
            @RequiresApi(Build.VERSION_CODES.O)
            override fun loadInBackground(): List<PaidDTO>? {
                data = getData()
                return data
            }
        }
    }

    override fun onLoaderReset(loader: Loader<List<PaidDTO>>) {
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onLoadFinished(loader: Loader<List<PaidDTO>>, data: List<PaidDTO>?) {
        data?.let{
        val dto = getPaids()
        dto.value = if(data.isNotEmpty()) data.map{ it.value }.reduce{ acc,value-> acc+value} else BigDecimal.ZERO
        holder.loadFields(dto)
        (holder as IRecyclerView<PaidDTO>).loadRecycler(data.toMutableList())
    }
    }

}