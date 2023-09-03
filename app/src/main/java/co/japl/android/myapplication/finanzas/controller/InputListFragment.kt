package co.japl.android.myapplication.finanzas.controller

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListInputAdapter
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.InputDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.InputImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IInputSvc
import co.japl.android.myapplication.finanzas.holders.InputListHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.IListHolder
import co.japl.android.myapplication.finanzas.putParams.AccountParams
import co.japl.android.myapplication.finanzas.putParams.InputListParams
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class InputListFragment : Fragment(),OnClickListener,LoaderManager.LoaderCallbacks<List<InputDTO>> {
    private lateinit var holder:IListHolder<InputListHolder,InputDTO>
    private var accountCode:Int = 0

    @Inject lateinit var service: IInputSvc
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_input_list, container, false)
        accountCode = arguments?.let{AccountParams.download(it)}?:0
        holder = InputListHolder(root)
        holder.setFields(this)
        loaderManager.initLoader(1,null,this)
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getData():MutableList<InputDTO>{
        val input = InputDTO(0, LocalDate.now(),accountCode,"","", BigDecimal.ZERO, LocalDate.now(),
            LocalDate.now())
        return (service as InputImpl).get(input).toMutableList()
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_add_il->InputListParams.newInstance(accountCode,findNavController())
        }
    }

    override fun onResume() {
        super.onResume()
        loaderManager.restartLoader(1,null,this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<InputDTO>> {
        return object:AsyncTaskLoader<List<InputDTO>>(requireContext()){
             var data:List<InputDTO> ?= null
            override fun onStartLoading() {
                super.onStartLoading()
                Log.d(javaClass.name,"onStartLoading $data")
                if(data != null){
                    deliverResult(data)
                }else{
                    forceLoad()
                }
            }
            @RequiresApi(Build.VERSION_CODES.O)
            override fun loadInBackground(): List<InputDTO>? {
                Log.d(javaClass.name,"loadInBackground $data")
                data = getData()
                return data
            }
        }.also { it.data = null }
    }

    override fun onLoaderReset(loader: Loader<List<InputDTO>>) {
    }

    override fun onLoadFinished(loader: Loader<List<InputDTO>>, data: List<InputDTO>?) {
        data?.let {
            holder.loadRecycler(it.toMutableList())
        }
    }

}