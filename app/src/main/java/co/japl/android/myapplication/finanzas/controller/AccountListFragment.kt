package co.japl.android.myapplication.finanzas.controller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListAccountAdapter
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.AccountDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.AccountImpl
import co.japl.android.myapplication.finanzas.putParams.AccountListParams
import com.google.android.material.button.MaterialButton

class AccountListFragment : Fragment() , OnClickListener,LoaderManager.LoaderCallbacks<List<AccountDTO>>{
    private lateinit var recycler:RecyclerView
    private lateinit var btnAdd:MaterialButton
    private lateinit var progressBar:ProgressBar
    private lateinit var service:SaveSvc<AccountDTO>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_account_list, container, false)
        progressBar = root.findViewById(R.id.pb_load_al)
        recycler = root.findViewById(R.id.rv_account_list)
        btnAdd = root.findViewById(R.id.btn_add_al)
        service = AccountImpl(ConnectDB(root.context))
        recycler.layoutManager = LinearLayoutManager(root.context,LinearLayoutManager.VERTICAL,false)
        btnAdd.setOnClickListener(this)
        progressBar.visibility = View.VISIBLE
        loaderManager.initLoader(1,null,this)
        return root
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_add_al->AccountListParams.newInstance(findNavController())
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<AccountDTO>> {
        return object: AsyncTaskLoader<List<AccountDTO>>(requireContext()){
            private var data : List<AccountDTO>? = null
            override fun onStartLoading() {
                super.onStartLoading()
                if(data != null){
                    deliverResult(data)
                }else {
                    forceLoad()
                }
            }
            override fun loadInBackground(): List<AccountDTO>? {
                data = service.getAll()
                return data
            }
        }
    }

    override fun onLoaderReset(loader: Loader<List<AccountDTO>>) {
    }

    override fun onLoadFinished(loader: Loader<List<AccountDTO>>, data: List<AccountDTO>?) {
        data?.let { list->
            ListAccountAdapter(list.toMutableList())?.let {
                recycler.adapter = it
            }
            progressBar.visibility = View.GONE
        }
    }

}