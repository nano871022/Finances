package co.japl.android.myapplication.controller

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.impl.*
import co.japl.android.myapplication.bussiness.impl.QuoteCreditVariable
import co.japl.android.myapplication.bussiness.interfaces.*
import co.japl.android.myapplication.finanzas.holders.QuoteBoughtHolder
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class QuoteBought : Fragment(), View.OnClickListener{


    private var config:ConfigSvc = Config()

    private lateinit var taxSvc: ITaxSvc
    private lateinit var creditCardSvc:SaveSvc<CreditCardDTO>
    private lateinit var saveSvc: SaveSvc<CreditCardBoughtDTO>
    private lateinit var holder:IHolder<CreditCardBoughtDTO>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.buys_credit_card, container, false)
        val connect: SQLiteOpenHelper = ConnectDB(rootView.context)
        creditCardSvc = CreditCardImpl(connect)
        taxSvc = TaxImpl(connect)
        saveSvc = SaveCreditCardBoughtImpl(connect)
        holder = QuoteBoughtHolder(rootView)
        holder.setFields(this)
        holder.cleanField()
        return rootView

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parentFragmentManager.setFragmentResultListener("CreditCard", this) { _, bundle ->
            val split =
                bundle.getString("CreditCardName").toString().split("|")
            val creditCardCode = split[1].toString().toInt()
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
                ).show()
                toBack()
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
                if(saveSvc.save(dto)) {
                    Toast.makeText(this.context, "Registro Guardado", Toast.LENGTH_LONG).show().also {
                        toBack()
                    }
                }else{
                    Toast.makeText(this.context,"Registro no Guardado",Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this.context,"Hay campos que no estan llenos correctamente",Toast.LENGTH_SHORT).show()
            }
        }

    private fun toBack(){
        parentFragmentManager.beginTransaction() .replace(R.id.fragment_initial,ListCreditCardQuote()).setTransition(
            FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
    }



}
