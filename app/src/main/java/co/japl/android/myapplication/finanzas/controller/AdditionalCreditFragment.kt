package co.japl.android.myapplication.finanzas.controller

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
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.AdditionalCreditImpl
import co.japl.android.myapplication.finanzas.holders.AdditionalCreditHolder
import co.japl.android.myapplication.finanzas.putParams.AdditionalCreditParams
import java.math.BigDecimal
import java.time.LocalDate

class AdditionalCreditFragment : Fragment(), OnClickListener {
    private lateinit var holder: IHolder<AdditionalCreditDTO>
    private lateinit var additionalSvc:SaveSvc<AdditionalCreditDTO>
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
        additionalSvc = AdditionalCreditImpl(ConnectDB(root.context))
        activity?.supportFragmentManager?.let {holder = AdditionalCreditHolder(root,it)}
        holder.setFields(this)
        holder.loadFields(getAdditionalCredit())
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAdditionalCredit():AdditionalCreditDTO{
        val additional = arguments?.let {  AdditionalCreditParams.download(it) }
        return if(additional != null){ additional }
        else {
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