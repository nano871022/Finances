package co.japl.android.myapplication.finanzas.controller.simulators

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICalcSvc
import co.japl.android.myapplication.finanzas.holders.ListSaveHolder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListSave : Fragment(), LoaderManager.LoaderCallbacks<List<CalcDTO>> {
    lateinit var holder:ListSaveHolder
    @Inject lateinit var saveSvc: ICalcSvc

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_list_save, container, false)
        holder = ListSaveHolder(rootView)
        holder.setFields(null)
        loaderManager.initLoader(1, null, this)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        loaderManager.restartLoader(1, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<CalcDTO>> {
        return object:AsyncTaskLoader<List<CalcDTO>>(requireContext()){
            private var data:List<CalcDTO>? = null
            override fun onStartLoading() {
                super.onStartLoading()
                if(data != null){
                    deliverResult(data)
                }else{
                    forceLoad()
                }
            }
            override fun loadInBackground(): List<CalcDTO>? {
                data = saveSvc.getAll()
                return data
            }
        }
    }

    override fun onLoaderReset(loader: Loader<List<CalcDTO>>) {
    }

    override fun onLoadFinished(loader: Loader<List<CalcDTO>>, data: List<CalcDTO>?) {
        data?.let {
            holder.loadRecycler(it.toMutableList())
        }
    }


}