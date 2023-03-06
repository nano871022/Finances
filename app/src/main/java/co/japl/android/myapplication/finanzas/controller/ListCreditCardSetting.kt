package co.japl.android.myapplication.controller

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListCreditCardAdapter
import co.japl.android.myapplication.adapter.ListCreditCardSettingAdapter
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.impl.CreditCardImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditCardSettingImpl
import co.japl.android.myapplication.putParams.CreditCardParams
import co.japl.android.myapplication.putParams.CreditCardSettingParams
import co.japl.android.myapplication.putParams.ListCreditCardSettingParams
import kotlin.properties.Delegates

class ListCreditCardSetting : Fragment() , View.OnClickListener{
    private lateinit var recycle:RecyclerView
    private lateinit var btnAdd:Button
    private lateinit var btnCancel:Button
    private var codeCreditCard : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_list_credit_card_setting, container, false)
        val map = ListCreditCardSettingParams.download(arguments)
        Log.v(this.javaClass.name,"CreateView  $map")
        if(map.containsKey(ListCreditCardSettingParams.Params.ARG_CODE_CREDIT_CARD)) {
            codeCreditCard = map[ListCreditCardSettingParams.Params.ARG_CODE_CREDIT_CARD]!!
        }
        setField(view)
        loadRecyclerView(view)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadRecyclerView(view:View){
        view?.let {
            recycle?.let { recycler->
                recycler.layoutManager = LinearLayoutManager(
                    it.context,
                    LinearLayoutManager.VERTICAL, false
                )
                val connect = ConnectDB(view.context)
                val saveSvc = CreditCardSettingImpl(connect)
                val data = saveSvc.getAll(codeCreditCard)
                recycler.adapter = ListCreditCardSettingAdapter(data.toMutableList(),parentFragmentManager,findNavController())
            }
        }
    }

    private fun add(){
        Log.d(this.javaClass.name,"add - start")
        CreditCardSettingParams.newInstance(codeCreditCard,findNavController())
    }

    private fun setField(view:View){
        view?.let {
            recycle = it.findViewById (R.id.rvCreditCardSettingCCS)
            btnAdd = it.findViewById(R.id.btnAddNewCCS)
            btnAdd.setOnClickListener(this)
            btnCancel = it.findViewById(R.id.btnCancelLCCS)
            btnCancel.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnAddNewCCS ->{add()}
            R.id.btnCancelLCCS -> {
                ListCreditCardSettingParams.toBack(codeCreditCard.toString(),findNavController())
            }
            else->{
                view.let{
                Toast.makeText(it!!.context,getString(R.string.toast_invalid_option),Toast.LENGTH_LONG).show()}}
        }
    }


}