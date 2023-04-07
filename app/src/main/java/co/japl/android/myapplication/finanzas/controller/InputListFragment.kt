package co.japl.android.myapplication.finanzas.controller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListInputAdapter
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.InputDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.InputImpl
import co.japl.android.myapplication.finanzas.putParams.InputListParams
import com.google.android.material.button.MaterialButton

class InputListFragment : Fragment(),OnClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAdd:MaterialButton
    private lateinit var service: SaveSvc<InputDTO>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_input_list, container, false)
        service = InputImpl(root,ConnectDB(root.context))
        val data = service.getAll().toMutableList()
        btnAdd = root.findViewById(R.id.btn_add_il)
        recyclerView = root.findViewById(R.id.rv_input_list)
        recyclerView.layoutManager = LinearLayoutManager(root.context,LinearLayoutManager.VERTICAL,false)
        ListInputAdapter(data)?.let {
            recyclerView.adapter = it
        }
        btnAdd.setOnClickListener(this)
        return root
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_add_il->InputListParams.newInstance(findNavController())
        }
    }

}