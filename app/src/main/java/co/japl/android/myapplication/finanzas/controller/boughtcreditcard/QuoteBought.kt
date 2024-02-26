package co.japl.android.myapplication.finanzas.controller.boughtcreditcard

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.myapplication.bussiness.impl.*
import co.japl.android.myapplication.bussiness.interfaces.*
import co.japl.android.myapplication.finanzas.bussiness.DTO.TagsQuoteCreditCardDTO
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IBuyCreditCardSettingSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICreditCardSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IQuoteCreditCardSvc
import co.japl.android.myapplication.finanzas.holders.QuoteBoughtHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.putParams.CreditCardQuotesParams
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class QuoteBought : Fragment(), View.OnClickListener{

    private lateinit var holder: IHolder<CreditCardBoughtDTO>

    @Inject lateinit var config:ConfigSvc
    @Inject lateinit var taxSvc: ITaxSvc
    @Inject lateinit var creditCardSvc:ICreditCardSvc
    @Inject lateinit var saveSvc: IQuoteCreditCardSvc
    @Inject lateinit var buyCCSSvc: IBuyCreditCardSettingSvc
    @Inject lateinit var tagQuoteSvc:ITagQuoteCreditCardSvc

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.buys_credit_card, container, false)
        holder = activity?.supportFragmentManager?.let { QuoteBoughtHolder(rootView, it) }!!
        holder.setFields(this)
        loadArguments()
        return rootView

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadArguments() {
        arguments?.let {
            val params = CreditCardQuotesParams.Companion.CreateQuote.download(it)
            val creditCardCode = params.first
            val now = LocalDateTime.now()
            val creditCard = creditCardSvc.get(creditCardCode)
            val tax = taxSvc.get(creditCardCode.toLong(),now.month.value, now.year)
            if (tax.isPresent) {
                val cutOffDate = config.nextCutOff(creditCard.get().cutOffDay.toInt())
                val dto = CreditCardBoughtDTO(
                    creditCardCode, creditCard.get().name, "", BigDecimal.ZERO, tax.get()
                        .value, 0, LocalDateTime.now(), cutOffDate, LocalDateTime.now(), LocalDateTime.now(),0, 0, 0
                ,tax.get().kindOfTax!! )
                holder.loadFields(dto)
            } else {
                Toast.makeText(context,"Invalid Tax, Create One to ${now.month} ${now.year}",Toast.LENGTH_LONG).show().also {
                    CreditCardQuotesParams.Companion.CreateQuote.toBack(findNavController())
                }
            }

            if(params.third > 0){
                val dto = saveSvc.get(params.third)
                Log.d(javaClass.name,"$dto ")
                dto.ifPresent {
                    holder.loadFields(it)
                }
            }
        }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onClick(view: View) {
            when (view.id) {
                R.id.btnClearBought -> holder.cleanField()
                R.id.btnSaveBought -> save()
                R.id.btn_save_new_one -> save(true)
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun save(back:Boolean = false) {
            if(holder.validate()){
                val dto = holder.downLoadFields()
                val id = saveSvc.save(dto)
                if( id > 0) {
                    if(dto.id == 0){dto.id = id.toInt()}
                    val buyCCS = (holder as QuoteBoughtHolder).downLoadBuyCreditCardSetting()
                    if(buyCCS.codeCreditCardSetting > 0) {
                        buyCCS.codeBuyCreditCard = id.toInt()
                        buyCCSSvc.save(buyCCS)
                    }
                    tagProcess(dto)
                    Toast.makeText(this.context, R.string.toast_successful_insert, Toast.LENGTH_LONG).show().also {
                        if(!back) {
                            CreditCardQuotesParams.Companion.CreateQuote.toBack(findNavController())
                        }else{
                            holder.cleanField()
                        }
                    }
                }else{
                    Toast.makeText(this.context,R.string.toast_record_didnt_save,Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this.context,R.string.toast_there_are_fields_isnt_fill_out,Toast.LENGTH_SHORT).show()
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun tagProcess(dto: CreditCardBoughtDTO){
        (holder as QuoteBoughtHolder).tagDto?.takeIf { it.id > 0 }?.let{tagDto->
            val tagQuote = tagQuoteSvc.get(dto.id)
            if(tagQuote.isPresent) {
                tagQuote.get().copy(codTag = tagDto.id)?.let {dto->
                    tagQuoteSvc.save(dto).takeIf { it <= 0 }
                        ?.let {Toast.makeText(this.context,R.string.toast_tags_didnt_save,Toast.LENGTH_LONG).show()}
                }
            }else{
                TagsQuoteCreditCardDTO(0, LocalDate.now(),dto.id,tagDto.id,false).let { dto ->
                tagQuoteSvc.save(dto).takeIf { it <= 0 }
                    ?.let {
                        Toast.makeText(
                            this.context,
                            R.string.toast_tags_didnt_save,
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }
            }
        }
    }

}
