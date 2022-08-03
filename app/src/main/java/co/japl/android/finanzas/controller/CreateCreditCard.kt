package co.japl.android.finanzas.controller

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentTransaction
import co.japl.android.finanzas.R
import co.japl.android.finanzas.bussiness.DB.connections.ConnectDB
import co.japl.android.finanzas.bussiness.DB.connections.CreditCardConnectDB
import co.japl.android.finanzas.bussiness.DTO.CreditCardDB
import co.japl.android.finanzas.bussiness.DTO.CreditCardDTO
import co.japl.android.finanzas.bussiness.impl.CreditCardImpl
import co.japl.android.finanzas.bussiness.interfaces.IHolder
import co.japl.android.finanzas.bussiness.interfaces.SaveSvc
import co.japl.android.finanzas.holders.CreditCardHolder
import co.japl.android.finanzas.putParams.CreditCardParams.Params.ARG_PARAM_CODE


class CreateCreditCard : Fragment(),View.OnClickListener {
    private lateinit var holder: IHolder<CreditCardDTO>
    private var param1: String? = null
    private lateinit var service:SaveSvc<CreditCardDTO>
    private var found = false

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentFragmentManager.setFragmentResultListener(ARG_PARAM_CODE, this) { _, bundle ->
            param1 =
                bundle.getString(ARG_PARAM_CODE).toString()
            Log.d(this.javaClass.name,"$ARG_PARAM_CODE = $param1")
            search()
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_credit_card, container, false)
        service = CreditCardImpl(ConnectDB(view.context))
        holder = CreditCardHolder(view)
        holder.setFields(this)

        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun search(){
        Log.d(this.javaClass.name," $param1")
        if(param1?.isNotBlank() == true){
            param1?.toInt()?.let {
                service.get(it).ifPresent{ cc->
                    found = true
                    holder.loadFields(cc)
                }
            }
        }
    }

    fun save(){
        if(holder.validate()){
            val dto = holder.downLoadFields()
                if (service.save(dto)) {
                    Toast.makeText(this.context, "Record saved", Toast.LENGTH_LONG).show().also {
                        parentFragmentManager.beginTransaction().replace(R.id.fragment_initial,
                            ListCreditCard()
                        ).setTransition(
                            FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
                    }
                } else {
                    Toast.makeText(this.context, "Record did not save", Toast.LENGTH_LONG).show()
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