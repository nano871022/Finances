package co.japl.android.myapplication.finanzas.controller

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListAccountAdapter
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.AccountDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.AccountImpl
import co.japl.android.myapplication.finanzas.putParams.AccountListParams
import co.japl.android.myapplication.finanzas.putParams.AccountParams
import com.google.android.material.button.MaterialButton

class AccountListFragment : Fragment() , OnClickListener{
    private lateinit var recycler:RecyclerView
    private lateinit var btnAdd:MaterialButton
    private lateinit var service:SaveSvc<AccountDTO>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_account_list, container, false)
        recycler = root.findViewById(R.id.rv_account_list)
        btnAdd = root.findViewById(R.id.btn_add_al)
        service = AccountImpl(ConnectDB(root.context))
        val data = service.getAll().toMutableList()
        recycler.layoutManager = LinearLayoutManager(root.context,LinearLayoutManager.VERTICAL,false)
        ListAccountAdapter(data)?.let{
            recycler.adapter = it
        }
        btnAdd.setOnClickListener(this)
        return root
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_add_al->AccountListParams.newInstance(findNavController())
        }
    }

}