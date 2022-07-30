package co.japl.android.myapplication.controller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListSaveAdapter

class ListCreditCard : Fragment() , View.OnClickListener{
    private lateinit var recycle:RecyclerView
    private lateinit var button:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_list_credit_card, container, false)

        return view
    }

    private fun loadRecyclerView(){
        view.let {
            recycle.layoutManager = LinearLayoutManager(
                it!!.context,
                LinearLayoutManager.VERTICAL, false
            )
            recycle.adapter = ListSaveAdapter(list)
        }
    }

    private fun add(){

    }

    private fun setField(){
        view.let {
            recycle = it!!.findViewById (R.id.rvCreditCardCC)
            button = it!!.findViewById(R.id.btnAddCC)
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