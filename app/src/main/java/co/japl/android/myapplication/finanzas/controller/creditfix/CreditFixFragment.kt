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
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDTO
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICreditFix
import co.japl.android.myapplication.finanzas.holders.CreditFixHolder
import co.japl.android.myapplication.finanzas.putParams.CreditFixParams
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class CreditFixFragment : Fragment() , OnClickListener{
    private lateinit var holder: IHolder<CreditDTO>
    private  var creditCode = 0L

    @Inject lateinit var creditSvc:ICreditFix

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =  inflater.inflate(R.layout.fragment_credit_fix, container, false)
        activity?.supportFragmentManager?.let {holder = CreditFixHolder(root,it)}
        holder.setFields(this)
        holder.loadFields(getCredit())
        return root
    }

    override fun onResume() {
        super.onResume()
        creditCode?.let{
        if(creditCode > 0 && holder.validate()){
            with(holder as CreditFixHolder) {
                calc()
                additional.visibility = View.VISIBLE
            }
                val dto = holder.downLoadFields()
                dto.id = creditCode.toInt()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCredit():CreditDTO{
        return CreditDTO(0,"", LocalDate.MIN,0.0,0, BigDecimal.ZERO,BigDecimal.ZERO,"","")
    }

    private fun save(){
        if(holder.validate()){
            val dto = holder.downLoadFields()
            if(creditCode>0){
                dto.id = creditCode.toInt()
            }
            creditCode = creditSvc.save(dto)
            if(dto.id == 0){
                (holder as CreditFixHolder).additional.visibility = View.VISIBLE
                Toast.makeText(context,R.string.toast_successful_insert,Toast.LENGTH_LONG).show()
            }else if( dto.id > 0){
                (holder as CreditFixHolder).additional.visibility = View.VISIBLE
                Toast.makeText(context,R.string.toast_successful_update,Toast.LENGTH_LONG).show()
            }else{
                (holder as CreditFixHolder).additional.visibility = View.GONE
                Toast.makeText(context,R.string.dont_able_to_save,Toast.LENGTH_LONG).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btnAdditionalCFF->{CreditFixParams.newInstanceAdditionalList(creditCode,findNavController())}
            R.id.btnAmortizationCFF->{
                val dto = holder.downLoadFields()
                dto.id = creditCode.toInt()
                CreditFixParams.newInstanceAmortizationList(dto,LocalDate.now(),findNavController())
            }
            R.id.btnCancelCFF->{CreditFixParams.toBack(findNavController())}
            R.id.btn_save_cff->{save()}
        }
    }

}