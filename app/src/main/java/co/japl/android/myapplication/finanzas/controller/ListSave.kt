package co.japl.android.myapplication.controller

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListSaveAdapter
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.bussiness.impl.SaveImpl
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.holders.view.ViewHolder

class ListSave : Fragment() {
    lateinit var recyclerView:RecyclerView
    lateinit var adapter:RecyclerView.Adapter<ViewHolder>
    lateinit var list:List<CalcDTO>
    lateinit var contexts:Context
    lateinit var dbConnect: ConnectDB
    lateinit var saveSvc: SaveSvc<CalcDTO>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_list_save, container, false)
        contexts = rootView.context
        recyclerView = rootView.findViewById(R.id.list_save)
        connectDB()
        loadRecyclerView()
        return rootView
    }

    private fun loadRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(contexts,LinearLayoutManager.VERTICAL,false)
        adapter = ListSaveAdapter(list)
        recyclerView.adapter = adapter
    }
    private fun connectDB(){
        dbConnect = ConnectDB(contexts)
        saveSvc = SaveImpl(dbConnect)
        list = saveSvc.getAll()
    }


}