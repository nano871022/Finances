package co.japl.android.myapplication.finanzas.controller

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.ProjectionDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.ProjectionsImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IProjectionsSvc
import co.japl.android.myapplication.finanzas.enums.KindofProjectionEnum
import co.japl.android.myapplication.finanzas.holders.ListProjectionHolder
import co.japl.android.myapplication.finanzas.holders.ProjectionsHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.IListHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.IRecapHolder
import co.japl.android.myapplication.utils.NumbersUtil
import java.time.LocalDate
import java.time.Period

class ListProjectFragment : Fragment(),LoaderManager.LoaderCallbacks<List<ProjectionDTO>>{
    private lateinit var holder: IListHolder<ListProjectionHolder,ProjectionDTO>
    private lateinit var svc:IProjectionsSvc
    private lateinit var kindOfList:Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_list_projection, container, false)
        svc = ProjectionsImpl(ConnectDB(root.context),root)
        kindOfList = root.resources.getStringArray(R.array.kind_of_projection_list)
        holder = ListProjectionHolder(root,findNavController())
        holder.setFields(null)
        loaderManager.initLoader(0,null,this)
        return root
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<ProjectionDTO>> {
        return object:AsyncTaskLoader<List<ProjectionDTO>>(requireContext()){
            private var data:List<ProjectionDTO>? = null
            override fun onStartLoading() {
                super.onStartLoading()
                if(data != null){
                    deliverResult(data)
                }else{
                    forceLoad()
                }
            }
            override fun loadInBackground(): List<ProjectionDTO>? {
                data = svc.getAllActive()
                return data
            }
        }
    }

    override fun onLoaderReset(loader: Loader<List<ProjectionDTO>>) {
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onLoadFinished(loader: Loader<List<ProjectionDTO>>, data: List<ProjectionDTO>?) {
        data?.let {
            holder.loadFields {
                val tot = data.sumOf {
                    val index = kindOfList.indexOf(it.type)
                    val months = KindofProjectionEnum.values()[index].months
                    it.quote * (months - Period.between(LocalDate.now(), it.end)
                        .toTotalMonths()).toBigDecimal()
                }
                it.items.text = data.count().toString()
                it.total.text = NumbersUtil.toString(tot)
            }
            holder.loadRecycler(it.toMutableList())
        }
    }
}