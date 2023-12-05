package co.japl.android.myapplication.finanzas.controller.projections

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.DTO.ProjectionDTO
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IProjectionsSvc
import co.japl.android.myapplication.finanzas.enums.KindofProjectionEnum
import co.japl.android.myapplication.finanzas.holders.ListProjectionHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.IListHolder
import co.japl.android.myapplication.utils.NumbersUtil
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.Period
import javax.inject.Inject

@AndroidEntryPoint
class ListProjectFragment : Fragment(),LoaderManager.LoaderCallbacks<List<ProjectionDTO>>{
    private lateinit var holder: IListHolder<ListProjectionHolder,ProjectionDTO>
    private lateinit var kindOfList:Array<String>

    @Inject lateinit var svc:IProjectionsSvc
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_list_projection, container, false)
        kindOfList = root.resources.getStringArray(R.array.kind_of_projection_list)
        holder = ListProjectionHolder(root,findNavController())
        holder.setFields(null)
        loaderManager.initLoader(0,null,this)
        return root
    }

    override fun onResume() {
        super.onResume()
        loaderManager.restartLoader(0,null,this)
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