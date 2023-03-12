package co.japl.android.myapplication.controller

import android.database.sqlite.SQLiteOpenHelper
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
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.BuyCreditCardSettingDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.impl.*
import co.japl.android.myapplication.bussiness.interfaces.*
import co.japl.android.myapplication.finanzas.bussiness.impl.BuyCreditCardSettingImpl
import co.japl.android.myapplication.finanzas.holders.QuoteBoughtHolder
import co.japl.android.myapplication.finanzas.putParams.CreditCardQuotesParams
import com.google.android.material.datepicker.MaterialDatePicker
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class QuoteBought : Fragment(), View.OnClickListener{


    private var config:ConfigSvc = Config()

    private lateinit var taxSvc: ITaxSvc
    private lateinit var creditCardSvc:SaveSvc<CreditCardDTO>
    private lateinit var saveSvc: SaveSvc<CreditCardBoughtDTO>
    private lateinit var buyCCSSvc: SaveSvc<BuyCreditCardSettingDTO>
    private lateinit var holder:IHolder<CreditCardBoughtDTO>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.buys_credit_card, container, false)
        val connect: SQLiteOpenHelper = ConnectDB(rootView.context)
        creditCardSvc = CreditCardImpl(connect)
        buyCCSSvc = BuyCreditCardSettingImpl(connect)
        taxSvc = TaxImpl(connect)
        saveSvc = SaveCreditCardBoughtImpl(connect)
        holder = activity?.supportFragmentManager?.let { QuoteBoughtHolder(rootView, it) }!!
        holder.setFields(this)
        loadArguments()
        holder.cleanField()
        return rootView

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadArguments() {
        arguments?.let {
            val params = CreditCardQuotesParams.Companion.CreateQuote.download(it)
            val creditCardCode = params.first
            val now = LocalDateTime.now()
            val creditCard = creditCardSvc.get(creditCardCode)
            val tax = taxSvc.get(now.month.value, now.year)
            if (tax.isPresent) {
                val cutOffDate = config.nextCutOff(creditCard.get().cutOffDay.toInt())
                val dto = CreditCardBoughtDTO(
                    creditCardCode, creditCard.get().name, "", BigDecimal.ZERO, tax.get()
                        .value, 0, LocalDateTime.now(), cutOffDate, LocalDateTime.now(), 0, 0, 0
                )
                holder.loadFields(dto)
            } else {
                Toast.makeText(
                    context,
                    "Invalid Tax, Create One to ${now.month} ${now.year}",
                    Toast.LENGTH_LONG
                ).show().also {
                    CreditCardQuotesParams.Companion.CreateQuote.toBack(findNavController())
                }
            }
        }


        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onClick(view: View) {
            when (view.id) {
                R.id.btnClearBought -> holder.cleanField()
                R.id.btnSaveBought -> save()
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun save() {
            if(holder.validate()){
                val dto = holder.downLoadFields()
                val id = saveSvc.save(dto)
                if( id > 0) {
                    val buyCCS = (holder as QuoteBoughtHolder).downLoadBuyCreditCardSetting()
                    if(buyCCS.codeCreditCardSetting > 0) {
                        buyCCS.codeBuyCreditCard = id.toInt()
                        buyCCSSvc.save(buyCCS)
                    }
                    Toast.makeText(this.context, "Registro Guardado", Toast.LENGTH_LONG).show().also {
                        CreditCardQuotesParams.Companion.CreateQuote.toBack(findNavController())
                    }
                }else{
                    Toast.makeText(this.context,"Registro no Guardado",Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this.context,"Hay campos que no estan llenos correctamente",Toast.LENGTH_SHORT).show()
            }
        }


}
