package co.japl.android.myapplication.finanzas.controller.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.DTO.AccountDTO
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IAccountSvc
import co.japl.android.myapplication.finanzas.holders.AccountListHolder
import co.japl.android.myapplication.finanzas.putParams.AccountListParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountListFragment : Fragment() , OnClickListener,LoaderManager.LoaderCallbacks<List<AccountDTO>>{
    @Inject lateinit var service:IAccountSvc
    private lateinit var holder:AccountListHolder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_account_list, container, false)
        holder = AccountListHolder(root)
        holder.setFields(this)
        loaderManager.initLoader(1,null,this)
        return root
    }

    override fun onResume() {
        super.onResume()
        loaderManager.restartLoader(1,null,this)
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
        data?.let {
            holder.loadRecycler(data.toMutableList())
        }
    }

}