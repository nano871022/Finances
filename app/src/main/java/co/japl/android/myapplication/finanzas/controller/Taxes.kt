package co.japl.android.myapplication.controller

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.bussiness.impl.CreditCardImpl
import co.japl.android.myapplication.bussiness.impl.TaxImpl
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.ISpinnerHolder
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.holders.TaxesHolder
import co.japl.android.myapplication.putParams.TaxesParams.Params.ARG_PARAM1
import co.japl.android.myapplication.putParams.TaxesParams.Params.ARG_PARAM2
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.stream.Collectors

class Taxes : Fragment() , View.OnClickListener{
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var holder:IHolder<TaxDTO>
    private lateinit var service:SaveSvc<TaxDTO>
    private lateinit var creditCardSvc:SaveSvc<CreditCardDTO>
    private lateinit var listCreditCard:List<CreditCardDTO>
    private lateinit var listCreditCardNames:MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_taxes, container, false)
        holder = TaxesHolder(view)
        holder.setFields(this)
        service = TaxImpl(ConnectDB(view.context))
        creditCardSvc = CreditCardImpl( ConnectDB(view.context))
        listCreditCard  = creditCardSvc.getAll()
        listCreditCardNames = listCreditCard.stream().map { it.name }.collect(Collectors.toList())
        listCreditCardNames.add(0,"--- Seleccionar ---")
        val today = LocalDate.now()
        holder.loadFields(TaxDTO(0,today.month.value.toShort(),today.year,0,0, LocalDateTime.now(),2.0))

        (holder as ISpinnerHolder<TaxesHolder>).lists{
                it.creditCard.adapter = ArrayAdapter(this.requireContext(),R.layout.spinner_simple,R.id.tvValueBigSp,
                    listCreditCardNames.toTypedArray())
                ArrayAdapter.createFromResource(this.requireContext(),R.array.Months,R.layout.spinner1).also { adapter ->
                    adapter.setDropDownViewResource(R.layout.spinner1)
                    it.month.adapter = adapter
                }
        }
        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun save(){
        if(holder.validate()){
            val dto = holder.downLoadFields()
            val creditCard = listCreditCard.stream().filter{ it.name == listCreditCardNames[dto.codCreditCard] }.findAny()
            dto.codCreditCard = creditCard.get().id
            if(service.save(dto)){
                Toast.makeText(this.context,"Record Saved",Toast.LENGTH_LONG).show()
                parentFragmentManager.beginTransaction().replace(R.id.fragment_initial, ListTaxCreditCard()).setTransition(
                    FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
            }else{
                Toast.makeText(this.context,"Record did not Save",Toast.LENGTH_LONG).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnSaveTaxes->{ save() }
            R.id.btnClearTaxes-> {holder.cleanField()}
            else->{ Toast.makeText(this.context,"Invalid Option",Toast.LENGTH_LONG).show()}
        }
    }

}