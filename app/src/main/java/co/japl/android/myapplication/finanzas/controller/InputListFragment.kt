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
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListInputAdapter
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.InputDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.InputImpl
import co.japl.android.myapplication.finanzas.putParams.AccountParams
import co.japl.android.myapplication.finanzas.putParams.InputListParams
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.button.MaterialButton
import java.math.BigDecimal
import java.time.LocalDate

class InputListFragment : Fragment(),OnClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAdd:MaterialButton
    private lateinit var service: SaveSvc<InputDTO>
    private lateinit var numInputs:TextView
    private lateinit var totInputs:TextView

    private var accountCode:Int = 0
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
        Log.d(javaClass.name,"account code: ${accountCode}")
        val data = getData(root)
        loadFields(root,data)
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getData(root:View):MutableList<InputDTO>{
        service = InputImpl(root,ConnectDB(root.context))
        val input = InputDTO(0, LocalDate.now(),accountCode,"","", BigDecimal.ZERO, LocalDate.now(),
            LocalDate.now())
        return (service as InputImpl).get(input).toMutableList()
    }

    private fun loadFields(root:View,data:MutableList<InputDTO>){
        numInputs = root.findViewById(R.id.tv_num_inputs_il)
        totInputs = root.findViewById(R.id.tv_tot_inputs_il)
        btnAdd = root.findViewById(R.id.btn_add_il)
        recyclerView = root.findViewById(R.id.rv_input_list)
        val countInputs = data.count()
        val totalInputs = data.sumOf { it.value }
        numInputs.text = countInputs.toString()
        totInputs.text = NumbersUtil.toString(totalInputs)
        recyclerView.layoutManager = LinearLayoutManager(root.context,LinearLayoutManager.VERTICAL,false)
        ListInputAdapter(data)?.let {
            recyclerView.adapter = it
        }
        btnAdd.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_add_il->InputListParams.newInstance(accountCode,findNavController())
        }
    }

}