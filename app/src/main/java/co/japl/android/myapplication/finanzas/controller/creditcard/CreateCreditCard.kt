package co.japl.android.myapplication.finanzas.controller.creditcard

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
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICreditCardSvc
import co.japl.android.myapplication.holders.CreditCardHolder
import co.japl.android.myapplication.putParams.CreditCardParams
import co.japl.android.myapplication.putParams.ListCreditCardSettingParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CreateCreditCard : Fragment(),View.OnClickListener {
    private lateinit var holder: IHolder<CreditCardDTO>
    private var param1: String? = null
    @Inject lateinit var service: ICreditCardSvc
    private var found = false

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_credit_card, container, false)
        holder = CreditCardHolder(view)
        holder.setFields(this)
        arguments?.let {
            param1 = CreditCardParams.download(it).orElse("0")
            search()
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun search(){
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
                if (service.save(dto)>0) {
                    Toast.makeText(this.context, "Record saved", Toast.LENGTH_LONG).show().also {
                        CreditCardParams.toBack(findNavController())
                    }
                } else {
                    Toast.makeText(this.context, "Record did not save", Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnCleanCCC->{holder.cleanField()}
            R.id.btnSettingsCCC->{
                val dto = holder.downLoadFields()
                ListCreditCardSettingParams.newInstance(dto.id,findNavController())
            }
            R.id.btnSaveCCC->{save()}
            else->{
                Toast.makeText(this.context,"Invalid Option",Toast.LENGTH_LONG).show()}
        }
    }


}