package co.japl.android.myapplication.finanzas.controller.projections

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
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IProjectionsSvc
import co.japl.android.myapplication.finanzas.holders.ProjectionsHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.IRecapHolder
import co.japl.android.myapplication.finanzas.putParams.ProjectionsParams
import co.com.japl.ui.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class ProjectionsFragment : Fragment(),OnClickListener ,LoaderManager.LoaderCallbacks<Map<String, Any>>{
    private lateinit var holder:IRecapHolder<ProjectionsHolder>

    @Inject lateinit var svc: IProjectionsSvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_projections, container, false)
        holder = ProjectionsHolder(root)
        holder.setFields(this)
        loaderManager.initLoader(0,null,this)
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_detail_pjs->ProjectionsParams.newInstanceList(LocalDate.now(),findNavController())
            R.id.btn_add_pjs->ProjectionsParams.newInstance(findNavController())
        }
    }

    override fun onResume() {
        super.onResume()
        loaderManager.restartLoader(0,null,this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Map<String, Any>> {
        return object:AsyncTaskLoader<Map<String, Any>>(requireContext()){
            private var data: Map<String, Any>? = null
            override fun onStartLoading() {
                super.onStartLoading()
                if(data != null){
                    deliverResult(data)
                }else{
                    forceLoad()
                }
            }
            override fun loadInBackground(): Map<String, Any>? {
                val close = svc.getClose()
                val far = svc.getFar()
                val tot = svc.getTotal()
                data = mapOf(
                    "close" to close, "far" to far, "tot" to tot
                )
                return data
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Map<String, Any>>) {
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onLoadFinished(loader: Loader<Map<String, Any>>, data: Map<String, Any>?) {
        data?.let {
            holder.loadFields {
                val far = data["far"] as Triple<LocalDate,Int,BigDecimal>
                val close = data["close"] as Triple<LocalDate,Int,BigDecimal>
                val tot = data["tot"] as Pair<Int,BigDecimal>
                it.items.text = tot.first.toString()
                it.total.text = NumbersUtil.toString(tot.second)
                it.dtClose.text = DateUtils.localDateToString(close.first)
                it.monthClose.text = close.second.toString()
                it.valueClose.text = NumbersUtil.toString(close.third)
                it.dtFar.text = DateUtils.localDateToString(far.first)
                it.monthFar.text = far.second.toString()
                it.valueFar.text = NumbersUtil.toString(far.third)
            }
        }
    }
}