package co.japl.android.myapplication.controller

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListCreditCardAdapter
import co.japl.android.myapplication.adapter.ListSaveAdapter
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.impl.CreditCardImpl
import co.japl.android.myapplication.utils.DateUtils

class ListCreditCard : Fragment() , View.OnClickListener{
    private lateinit var recycle:RecyclerView
    private lateinit var button:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_list_credit_card, container, false)
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
                val saveSvc = CreditCardImpl(connect)
                val data = saveSvc.getAll()
                recycler.adapter = ListCreditCardAdapter(data.toMutableList(),parentFragmentManager)
            }
        }
    }

    private fun add(){
        Log.d(this.javaClass.name,"add - start")
        parentFragmentManager.beginTransaction().replace(R.id.fragment_initial,CreateCreditCard()).setTransition(
            FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
    }

    private fun setField(view:View){
        view?.let {
            recycle = it.findViewById (R.id.rvCreditCardCC)
            button = it.findViewById(R.id.btnAddCC)
            button.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnAddCC ->{add()}
            else->{
                view.let{
                Toast.makeText(it!!.context,"Invalid Option",Toast.LENGTH_LONG).show()}}
        }
    }


}