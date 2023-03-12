package co.japl.android.myapplication.finanzas.controller

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO
import co.japl.android.myapplication.bussiness.impl.CreditCardImpl
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.ISpinnerHolder
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditCardSettingImpl
import co.japl.android.myapplication.holders.CreditCardSettingHolder
import co.japl.android.myapplication.putParams.CreditCardSettingParams
import java.time.LocalDateTime
import java.util.Arrays
import java.util.Date
import java.util.Optional
import java.util.stream.Collectors

class CreditCardSettingFragment : Fragment() {
    private lateinit var holder: IHolder<CreditCardSettingDTO>
    private lateinit var creditCardSvc: SaveSvc<CreditCardDTO>
    private lateinit var listCreditCard:List<CreditCardDTO>
    private lateinit var listCreditCardNames:MutableList<String>
    private lateinit var listTypeNames: MutableList<String>
    private lateinit var creditCardSettingSvc:SaveSvc<CreditCardSettingDTO>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_credit_card_setting, container, false)
        loadCreditCard(root)
        loadHolder(root)
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadHolder(root: View){
        holder = CreditCardSettingHolder(root,listCreditCard, parentFragmentManager, findNavController())
        holder.setFields(null)
        val map = CreditCardSettingParams.download(arguments)

        var creditCardSettingDto:Optional<CreditCardSettingDTO> = Optional.empty()

        val id: Int = if(map.containsKey(CreditCardSettingParams.Params.ARG_ID.toString())){
            map[CreditCardSettingParams.Params.ARG_ID]?.or(0)!!
        }else{
            0
        }
        if(id > 0){
            creditCardSettingDto = creditCardSettingSvc.get(id)
        }

        val codeCreditCard : Int = if(map.containsKey(CreditCardSettingParams.Params.ARG_CODE_CREDIT_CARD)){
             map[CreditCardSettingParams.Params.ARG_CODE_CREDIT_CARD]!!
        }else{
            0
        }
        Log.v(this.javaClass.name,"LoadHolder code credit card: $codeCreditCard Id: $id")
        val dto = if(creditCardSettingDto.isPresent){ creditCardSettingDto.get()}else{ CreditCardSettingDTO(id, codeCreditCard, "", "", "", LocalDateTime.now(), 1)}


        (holder as ISpinnerHolder<CreditCardSettingHolder>).lists {
            ArrayAdapter(
                this.requireContext(),
                R.layout.spinner_simple,
                R.id.tvValueBigSp,
                listCreditCardNames.toTypedArray()
            ).let{ adapter->
                it.creditCard.setAdapter(adapter)
            }
            ArrayAdapter.createFromResource(
                this.requireContext(),
                R.array.CreditCardSettingType,
                R.layout.spinner1
            ).let { adapter ->
                adapter.setDropDownViewResource(R.layout.spinner1)
                it.type.setAdapter(adapter)
            }
            if (listCreditCardNames.isNotEmpty() && listCreditCardNames.size == 2) {
                it.creditCard.setText(listCreditCardNames[1])
            }
            if(it.creditCard.text.isBlank()){
                val args = CreditCardSettingParams.download(arguments)
                val codeCreditCard = args.get(CreditCardSettingParams.Params.ARG_CODE_CREDIT_CARD)
                val nameCreditCard = listCreditCard.firstOrNull { it.id == codeCreditCard }?.name ?: ""
                it.creditCard.setText(nameCreditCard)
            }
        }

        holder.loadFields(dto)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadCreditCard(root:View){
        creditCardSvc = CreditCardImpl( ConnectDB(root.context))
        listCreditCard  = creditCardSvc.getAll()
        listCreditCardNames = listCreditCard.stream().map { it.name }.collect(Collectors.toList())
        listCreditCardNames.add(0,"--- Seleccionar ---")

        creditCardSettingSvc =CreditCardSettingImpl(co.japl.android.myapplication.bussiness.DB.connections.ConnectDB(root.context))
    }



}