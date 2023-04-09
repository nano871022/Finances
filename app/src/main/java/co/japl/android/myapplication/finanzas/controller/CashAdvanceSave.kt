package co.japl.android.myapplication.finanzas.controller

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardBought
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.bussiness.impl.Config
import co.japl.android.myapplication.bussiness.impl.CreditCardImpl
import co.japl.android.myapplication.bussiness.impl.SaveCreditCardBoughtImpl
import co.japl.android.myapplication.bussiness.impl.TaxImpl
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.ITaxSvc
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.bussiness.mapping.CreditCardBoughtMap
import co.japl.android.myapplication.finanzas.bussiness.impl.KindOfTaxImpl
import co.japl.android.myapplication.finanzas.holders.CashAdvanceHolder
import co.japl.android.myapplication.finanzas.putParams.CashAdvanceParams
import co.japl.android.myapplication.finanzas.utils.KindBoughtEnum
import co.japl.android.myapplication.finanzas.utils.KindOfTaxEnum
import co.japl.android.myapplication.finanzas.utils.TaxEnum
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.util.*

class CashAdvanceSave: Fragment() , View.OnClickListener{
    private val kindOfTaxSvc = KindOfTaxImpl()
    lateinit var holder:IHolder<CreditCardBought>
    lateinit var saveSvc: SaveSvc<CreditCardBoughtDTO>
    lateinit var creditCardSvc: SaveSvc<CreditCardDTO>
    lateinit var codeCreditCard:Optional<Int>
    lateinit var creditCard:Optional<CreditCardDTO>
    val config = Config()
    lateinit var tax:TaxDTO
    lateinit var taxSvc:ITaxSvc
    lateinit var navController: NavController

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.cash_advance_credit_card,container,false)
        codeCreditCard = Optional.ofNullable(CashAdvanceParams.download(requireArguments()).toInt())
        val connect = ConnectDB(view.context)
        navController = findNavController()
        saveSvc = SaveCreditCardBoughtImpl(connect)
        creditCardSvc = CreditCardImpl(connect)
        taxSvc = TaxImpl(connect)
        creditCard = creditCardSvc.get(codeCreditCard.get())
        holder = CashAdvanceHolder(view,activity?.supportFragmentManager!!) { bought,date -> calc(creditCard.get().id.toLong(),bought,date)}
        holder.setFields(this)
        val quote = CreditCardBought()
        quote.nameCreditCard = creditCard.get().name
        quote.nameItem = resources.getString(R.string.sucursal_advance)
        holder.loadFields(quote)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun save(){
        if(holder.validate()){
            val quote = holder.downLoadFields()
            quote.codeCreditCard = creditCard.get().id
            quote.kind = KindBoughtEnum.CASH_ADVANCE.kind
            quote.cutOutDate = config.nextCutOff(creditCard.get().cutOffDay.toInt())
            quote.month = tax.period.toInt()
            quote.kindOfTax = tax.kindOfTax
            val dto = CreditCardBoughtMap().mapping(quote)
            if(saveSvc.save(dto)>0){
                Toast.makeText(context,"Se ha agregado el avance de dinero correctamente",Toast.LENGTH_LONG).show().also {
                    CashAdvanceParams.toBack(navController)
                }
            }else{
                Toast.makeText(context,"No se logro agregar avancce",Toast.LENGTH_LONG).show()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(p0: View?) {
        save()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calc(codCreditCard:Long,value:BigDecimal, date:LocalDateTime):CreditCardBought{
        val lTax = taxSvc.get(codCreditCard,date.monthValue,date.year,TaxEnum.CASH_ADVANCE)
        if(lTax.isPresent){
            this.tax = lTax.get()
        }
        if(!this::tax.isInitialized || !lTax.isPresent){
            Toast.makeText(context,"No se encontró tasa para avance ${date.year} ${date.month}",Toast.LENGTH_LONG).show()
            CashAdvanceParams.toBack(findNavController())
            return CreditCardBought()
        }
        val bought = CreditCardBought()
        bought.valueItem = value
        bought.interest =  tax.value
        bought.kindOfTax = tax.kindOfTax
        bought.month = tax.period.toInt()
        val month = bought.month?.toBigDecimal()
        val capital = bought.valueItem!!.divide(month,2,RoundingMode.CEILING)
        Log.d(this.javaClass.name,"$capital = ${bought.valueItem} / $month")
        val percent = kindOfTaxSvc.getNM(tax.value, KindOfTaxEnum.valueOf(tax.kindOfTax!!)).toBigDecimal().divide(BigDecimal.valueOf(100))
        val interest = bought.valueItem!!.multiply(percent)
        Log.d(this.javaClass.name,"$interest = ${bought.valueItem} * ${percent}")
        val quote = capital.add(interest)
        Log.d(this.javaClass.name,"$quote = $capital + $interest")
        bought.quoteValue = quote
        return bought
    }
}