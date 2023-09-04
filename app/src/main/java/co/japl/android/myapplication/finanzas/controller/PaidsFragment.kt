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
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidsPOJO
import co.japl.android.myapplication.finanzas.bussiness.impl.PaidImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IPaidSvc
import co.japl.android.myapplication.finanzas.holders.PaidsHolder
import co.japl.android.myapplication.finanzas.putParams.PaidsParams
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class PaidsFragment : Fragment(), OnClickListener ,LoaderManager.LoaderCallbacks<PaidsPOJO>{
    private lateinit var holder: IHolder<PaidsPOJO>
    @RequiresApi(Build.VERSION_CODES.O)
    private val date = LocalDate.now().withDayOfMonth(1)

    @Inject lateinit var service:IPaidSvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_paids, container, false)
        holder = PaidsHolder(root)
        holder.setFields(this)
        loaderManager.initLoader(0,null,this)
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_add_ps->PaidsParams.newInstance(findNavController())
            R.id.btn_detail_ps->PaidsParams.newInstanceList(date,findNavController())
            R.id.btn_periods_ps->PaidsParams.newInstancePeriods(findNavController())
        }
    }

    override fun onResume() {
        super.onResume()
        loaderManager.restartLoader(0,null,this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<PaidsPOJO> {
    return object:AsyncTaskLoader<PaidsPOJO>(requireContext()){
        private var data:PaidsPOJO? = null
        override fun onStartLoading() {
            super.onStartLoading()
            if(data != null){
                deliverResult(data)
            }else{
                forceLoad()
            }
        }
        @RequiresApi(Build.VERSION_CODES.O)
        override fun loadInBackground(): PaidsPOJO? {
            data = (service as PaidImpl).getPaids(date)
            return data
        }

    }
    }

    override fun onLoaderReset(loader: Loader<PaidsPOJO>) {
    }

    override fun onLoadFinished(loader: Loader<PaidsPOJO>, data: PaidsPOJO?) {
        data?.let{holder.loadFields(data)}
    }

}