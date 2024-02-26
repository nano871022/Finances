package co.japl.android.myapplication.finanzas.controller.paids

import android.app.AlertDialog
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
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidsPOJO
import co.japl.android.myapplication.finanzas.bussiness.impl.PaidImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IAccountSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGraph
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IPaidSvc
import co.japl.android.myapplication.finanzas.bussiness.response.GraphValuesResp
import co.japl.android.myapplication.finanzas.holders.PaidsHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.ICallerHolder
import co.japl.android.myapplication.finanzas.putParams.AccountParams
import co.japl.android.myapplication.finanzas.putParams.PaidsParams
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class PaidsFragment : Fragment(), OnClickListener ,LoaderManager.LoaderCallbacks<Pair<PaidsPOJO,List<GraphValuesResp>>>{
    private lateinit var holder: IHolder<PaidsPOJO>
    private lateinit var dialog:AlertDialog

    @RequiresApi(Build.VERSION_CODES.O)
    private val date = LocalDate.now().withDayOfMonth(1)

    @Inject lateinit var service:IPaidSvc
    @Inject lateinit var accountSvc: IAccountSvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LoaderManager.getInstance(this).initLoader(0,null,this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_paids, container, false)
        holder = PaidsHolder(root)
        holder.setFields(this)
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
        val list = accountSvc.getAll()
        if(list.isEmpty()){
            notExistAccountDialog()
        }
        LoaderManager.getInstance(this).restartLoader(0,null,this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Pair<PaidsPOJO,List<GraphValuesResp>>> {
    return object:AsyncTaskLoader<Pair<PaidsPOJO,List<GraphValuesResp>>>(requireContext()){
        private var data:Pair<PaidsPOJO,List<GraphValuesResp>>? = null
        override fun onStartLoading() {
            super.onStartLoading()
            if(data != null){
                deliverResult(data)
            }else{
                forceLoad()
            }
        }
        @RequiresApi(Build.VERSION_CODES.O)
        override fun loadInBackground(): Pair<PaidsPOJO,List<GraphValuesResp>>? {
            val paids = (service as PaidImpl).getPaids(date)
            val graph = (service as IGraph).getValues(date)
            data = Pair(paids,graph)
            return data
        }

    }
    }

    override fun onLoaderReset(loader: Loader<Pair<PaidsPOJO,List<GraphValuesResp>>>) {
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onLoadFinished(loader: Loader<Pair<PaidsPOJO,List<GraphValuesResp>>>, data: Pair<PaidsPOJO,List<GraphValuesResp>>?) {
        data?.let{
            holder.loadFields(it.first)
            (holder as ICallerHolder<PaidsHolder>).execute{holder->
                it.second?.forEach {
                    holder.customDraw.addPiece(it.name,it.value)
                }
                holder.customDraw.invalidate()
            }
        }
    }

    private fun notExistAccountDialog() {
        if(!this::dialog.isInitialized || !dialog.isShowing) {
            dialog = AlertDialog.Builder(view?.context)
                .setTitle(R.string.title_account_doesnt_create_yet)
                .setMessage(R.string.message_account_doesnt_create_yet)
                .setPositiveButton(R.string.go, null)
                .setNegativeButton(R.string.cancel, null)
                .create()
            dialog.show()
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                dialog.cancel()
                AccountParams.newInstance(findNavController())
            }
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
                dialog.cancel()
            }
        }
    }

}