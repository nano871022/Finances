package co.japl.android.myapplication.finanzas.controller.creditfix

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDTO
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IAdditionalCreditSvc
import co.japl.android.myapplication.finanzas.holders.AdditionalCreditHolder
import co.japl.android.myapplication.finanzas.putParams.AdditionalCreditParams
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class AdditionalCreditFragment : Fragment(), OnClickListener {
    private lateinit var holder: IHolder<AdditionalCreditDTO>
    @Inject lateinit var additionalSvc: IAdditionalCreditSvc
    private var creditCode = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =  inflater.inflate(R.layout.fragment_additional_credit, container, false)
        val additional = arguments?.let {  AdditionalCreditParams.download(it) }
        activity?.supportFragmentManager?.let {holder = AdditionalCreditHolder(root,it,additional?.second?:false)}
        holder.setFields(this)
        holder.loadFields(getAdditionalCredit(additional?.first))
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAdditionalCredit(additional:AdditionalCreditDTO?):AdditionalCreditDTO{
        return if(additional != null) additional else {
            val id = 0
            val end = LocalDate.MAX
            val start = LocalDate.now()
            val name = ""
            val value = BigDecimal.ZERO
            AdditionalCreditDTO(id, name, value, creditCode, start, end)
        }
    }

    override fun onClick(action: View?) {
        when(action?.id){
            R.id.btn_save_ac->{
                if(holder.validate()){
                    val value = holder.downLoadFields()
                    if(additionalSvc.save(value) > 0){
                        Toast.makeText(context,R.string.toast_successful_insert,Toast.LENGTH_LONG).show()
                        AdditionalCreditParams.toBack(findNavController())
                    }else{
                        Toast.makeText(context,R.string.toast_unsuccessful_insert,Toast.LENGTH_LONG).show()
                    }
                }
            }
            R.id.btn_cancel_ac->{
                AdditionalCreditParams.toBack(findNavController())
            }
        }
    }

}