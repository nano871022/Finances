package co.japl.android.myapplication.controller

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.CreditCardConnectDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.impl.CreditCardImpl
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.holders.CreditCardHolder
import co.japl.android.myapplication.putParams.CreditCardParams.Params.ARG_PARAM1
import co.japl.android.myapplication.putParams.CreditCardParams.Params.ARG_PARAM2


class CreateCreditCard : Fragment(),View.OnClickListener {
    private lateinit var holder: IHolder<CreditCardDTO>
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var service:SaveSvc<CreditCardDTO>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_credit_card, container, false)
        service = CreditCardImpl(CreditCardConnectDB(view.context))
        holder = CreditCardHolder(view)
        holder.setFields(this)

        return view
    }

    fun save(){
        if(holder.validate()){
            val dto = holder.downLoadFields()
            if(service.save(dto)){
                Toast.makeText(this.context,"Record saved",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this.context,"Record did not save",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnCleanCCC->{holder.cleanField()}
            R.id.btnSaveCCC->{save()}
            else->{
                Toast.makeText(this.context,"Invalid Option",Toast.LENGTH_LONG).show()}
        }
    }


}