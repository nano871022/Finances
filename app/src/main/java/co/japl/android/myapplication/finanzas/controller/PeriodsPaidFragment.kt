package co.japl.android.myapplication.finanzas.controller

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListPeriodPaidAdapter
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.PaidImpl
import co.japl.android.myapplication.finanzas.holders.PeriodsPaidHolder

class PeriodsPaidFragment : Fragment() , LoaderManager.LoaderCallbacks<List<PaidDTO>> {
    private lateinit var holder:PeriodsPaidHolder
    private lateinit var service:SaveSvc<PaidDTO>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_periods_paid, container, false)
        service = PaidImpl(ConnectDB(root.context))
        holder = PeriodsPaidHolder(root,findNavController())
        holder.setFields(null)
        loaderManager.initLoader(1,null,this)
        return root
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<PaidDTO>> {
        return object : AsyncTaskLoader<List<PaidDTO>>(requireContext()) {
            private var data: List<PaidDTO>? = null
            override fun onStartLoading() {
                super.onStartLoading()
                if (data != null) {
                    deliverResult(data)
                } else {
                    forceLoad()
                }
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun loadInBackground(): List<PaidDTO>? {
                data = (service as PaidImpl).getPeriods()
                return data

            }
        }
    }

    override fun onLoaderReset(loader: Loader<List<PaidDTO>>) {
    }

    override fun onLoadFinished(loader: Loader<List<PaidDTO>>, data: List<PaidDTO>?) {
        data?.let { holder.loadRecycler(data.toMutableList())}
    }
}