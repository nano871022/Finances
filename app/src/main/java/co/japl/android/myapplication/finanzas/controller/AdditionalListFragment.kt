package co.japl.android.myapplication.finanzas.controller

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDTO
import co.japl.android.myapplication.finanzas.holders.AdditionalCreditListHolder
import co.japl.android.myapplication.finanzas.putParams.AdditionalCreditParams
import co.japl.android.myapplication.finanzas.putParams.CreditFixParams
import java.math.BigDecimal
import java.time.LocalDate

class AdditionalListFragment : Fragment() ,OnClickListener{
    private lateinit var holder: IHolder<AdditionalCreditDTO>
    private lateinit var additional: AdditionalCreditDTO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_additional_list, container, false)
        val creditCode = arguments?.let {
            CreditFixParams.downloadAdditionalList(it) ?: 0
        } ?: 0
        additional = AdditionalCreditDTO(0,"", BigDecimal.ZERO,creditCode, LocalDate.now(), LocalDate.MAX)
        holder = AdditionalCreditListHolder(root,findNavController())
        holder.setFields(this)
        holder.loadFields(additional)
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(view: View?) {
         when(view?.id){
            R.id.btn_add_al -> {
                AdditionalCreditParams.newInstance(additional,findNavController())
            }
        }
    }

}