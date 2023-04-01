package co.japl.android.myapplication.finanzas.controller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListAccountAdapter
import co.japl.android.myapplication.bussiness.interfaces.IHolder

class AccountListFragment : Fragment() {
    private lateinit var holder:IHolder<ListAccountAdapter>
    private lateinit var recycler:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_account_list, container, false)
        recycler = root.findViewById(R.id.rv_account_list)
        return root
    }

}