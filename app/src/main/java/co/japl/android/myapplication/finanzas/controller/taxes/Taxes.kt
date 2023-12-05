package co.japl.android.myapplication.finanzas.controller.taxes

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.bussiness.interfaces.ITaxSvc
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICreditCardSvc
import co.japl.android.myapplication.finanzas.enums.KindOfTaxEnum
import co.japl.android.myapplication.finanzas.enums.TaxEnum
import co.japl.android.myapplication.holders.TaxesHolder
import co.japl.android.myapplication.putParams.TaxesParams
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class Taxes : Fragment() , View.OnClickListener{
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var holder: IHolder<TaxDTO>
    private lateinit var listCreditCard:List<CreditCardDTO>

    @Inject lateinit var service:ITaxSvc
    @Inject lateinit var creditCardSvc:ICreditCardSvc

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_taxes, container, false)
        getData(view)
        holder = TaxesHolder(view,listCreditCard)
        holder.setFields(this)
        holderSetUp()
        getParameters()
        return view
    }

    private fun getParameters(){
        arguments?.let {
            val params = TaxesParams.download(it)
            param1 =params.first
            param2 = params.second
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getData(view:View){
        listCreditCard  = creditCardSvc.getAll()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun holderSetUp(){
        holder.loadFields(getTaxDTO())
        holder.cleanField()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTaxDTO():TaxDTO{
        val today = LocalDate.now()
        return TaxDTO(0,
            today.month.value.toShort()
            ,today.year
            ,0
            ,0
            , LocalDateTime.now()
            ,2.0
            , TaxEnum.CREDIT_CARD.ordinal.toShort()
            ,0
            , KindOfTaxEnum.EM.name)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun save(){
        if(holder.validate()){
            val dto = holder.downLoadFields()
            val response = service.save(dto)
            if(response > 0 && response.toInt() != dto.id){
                dto.id = response.toInt()
                Toast.makeText(this.context,"Record Saved",Toast.LENGTH_LONG).show()
                TaxesParams.toBack(findNavController())
            }else if(response.toInt() == dto.id){
                Toast.makeText(this.context,"Record Updated",Toast.LENGTH_LONG).show()
                TaxesParams.toBack(findNavController())
            }else{
                Toast.makeText(this.context,"Record did not Save",Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(context,"Campos Invalidos",Toast.LENGTH_LONG).show()
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