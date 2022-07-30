package co.japl.android.myapplication.controller

import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DB.connections.CreditCardBoughtConnectDB
import co.japl.android.myapplication.bussiness.interfaces.Calc
import co.japl.android.myapplication.bussiness.interfaces.ConfigSvc
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.bussiness.impl.*
import co.japl.android.myapplication.bussiness.impl.QuoteCreditVariable
import co.japl.android.myapplication.bussiness.interfaces.ITaxSvc
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import android.content.Context as Context

class QuoteBought : Fragment(), View.OnClickListener {
    private lateinit var etProductName: EditText
    private lateinit var etProductValue: EditText
    private lateinit var etTax: TextView
    private lateinit var etMonths: EditText
    private lateinit var etQuotesValue: TextView
    private lateinit var tvCardAssing: TextView
    private lateinit var dtBought: EditText
    private lateinit var llTax : LinearLayout

    private lateinit var contexto: Context
    private lateinit var btnSave: Button
    private var calcTax: Calc = QuoteCreditVariableInterest()
    private var calc: Calc = QuoteCreditVariable()
    private var config:ConfigSvc = Config()
    private var taxMonthly: Double = 0.0
    private lateinit var taxSvc: ITaxSvc
    private lateinit var creditCardSvc:SaveSvc<CreditCardDTO>
    private lateinit var creditCardName: String
    private var creditCardCode: Int = 0
    private lateinit var cutOffDate:LocalDateTime

    private lateinit var saveSvc: SaveSvc<CreditCardBoughtDTO>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.buys_credit_card, container, false)
        contexto = rootView.context
        val connect: SQLiteOpenHelper = ConnectDB(rootView.context)
        creditCardSvc = CreditCardImpl(connect)
        taxSvc = TaxImpl(connect)
        saveSvc = SaveCreditCardBoughtImpl(connect)
        setFields(rootView)
        clear()

        return rootView

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parentFragmentManager.setFragmentResultListener("CreditCard", this) { _, bundle ->
            val split =
                bundle.getString("CreditCardName").toString().split("|")
            creditCardCode = split[1].toString().toInt()
            creditCardName = split[0]
            val now = LocalDateTime.now()
            val creditCard = creditCardSvc.get(creditCardCode)
            val tax = taxSvc.get(now.month.value,now.year)
            if(tax.isPresent){
                taxMonthly = tax.get().value
                cutOffDate = config.nextCutOff(creditCard.get().cutOffDay.toInt())
                tvCardAssing.text = creditCard.get().name
                loadField()
            }else{
                Toast.makeText(context,"Invalid Tax, Create One to ${now.month} ${now.year}",Toast.LENGTH_LONG ).show()
                toBack()
            }
        }


        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onClick(view: View) {
            when (view.id) {
                R.id.btnClearBought -> clear()
                R.id.btnSaveBought -> save()
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun save() {
            if(validate()){
                var dto = map()
                if(saveSvc.save(dto)) {
                    println("recorder")
                    Toast.makeText(contexto, "Registro Guardado", Toast.LENGTH_LONG).show()
                    toBack()
                }else{
                    println("no recorder")
                    Toast.makeText(contexto,"Registro no Guardado",Toast.LENGTH_LONG).show()
                }
            }else{
                println("field does not filled")
                Toast.makeText(contexto,"Hay campos que no estan llenos correctamente",Toast.LENGTH_SHORT).show()
            }
        }

    private fun toBack(){
        parentFragmentManager.beginTransaction() .replace(R.id.fragment_initial,ListCreditCardQuote()).setTransition(
            FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
    }

        private fun calc(): Boolean {
            val value = etProductValue.text.toString().toBigDecimal()
            val period = etMonths.text.toString().toLong()
            val tax = etTax.text.toString().toDouble()
            if(period == 1L){
                println("Invisible")
                llTax.visibility = View.INVISIBLE
            }else{
                llTax.visibility = View.VISIBLE
                println("Visible")
            }

            val responseQuote = calc.calc(value, period, tax)
            val responseiNteres = calcTax.calc(value, period, tax)

            responseQuote.let { quote ->
                responseiNteres.let { interes ->
                    val format = DecimalFormat("#,###.00")
                    var total = quote.add(interes)
                    if(period == 1L){
                        total = quote;
                    }
                    etQuotesValue.text = format.format(total.setScale(2, RoundingMode.HALF_EVEN))
                    etQuotesValue.visibility = View.VISIBLE
                    btnSave.visibility = View.VISIBLE
                }
            }
            return true
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun validate(): Boolean {
            var valid: Boolean = true
            if (etProductName.text.isBlank()) {
                etProductName.setBackgroundColor(Color.RED)
                valid = valid && false
                etProductName.setError("El nombre del producto o tienda esta vacia")
            }else{etProductName.setBackgroundColor(Color.WHITE)}
            if (etProductValue.text.isBlank()) {
                etProductValue.setBackgroundColor(Color.RED)
                valid = valid && false
                etProductValue.setError("El  valor esta vacia")
            }else{etProductValue.setBackgroundColor(Color.WHITE)}
            if (etTax.text.isBlank()) {
                etTax.setBackgroundColor(Color.RED)
                valid = valid && false
                etTax.setError("El tasa es vacia")
            }else{etTax.setBackgroundColor(Color.WHITE)}
            if (etMonths.text.isBlank()) {
                etMonths.setBackgroundColor(Color.RED)
                valid = valid && false
                etMonths.setError("El mes es vacio")
            }else{etMonths.setBackgroundColor(Color.WHITE)}

            if (etMonths.text.isNotBlank() && (etMonths.text.toString().toLong() <= 0L || etMonths.text.toString().toLong() > 72L)) {
                etMonths.setBackgroundColor(Color.RED)
                valid = valid && false
                etMonths.setError("La cantidad de meses es invalido, debe ser entre 1 y 72")
            }else if(etMonths.text.isNotBlank()){etMonths.setBackgroundColor(Color.WHITE)}
            if(dtBought.text.isBlank()){
                dtBought.setBackgroundColor(Color.RED)
                valid = valid && false
                dtBought.setError("La fecha se encuentra vacia")
            }else{dtBought.setBackgroundColor(Color.WHITE)}
            val bought = dtBought.text.toString()
            val date = bought.split("/")
            if(date[2].toInt() < 2010 || date[2].toInt() > LocalDateTime.now().year){
                dtBought.setBackgroundColor(Color.RED)
                valid = valid && false
                dtBought.setError("El año es invalido entre 2010 hasta el actual año en curso")
            }else{dtBought.setBackgroundColor(Color.WHITE)}
            if(date[1].toInt()  < 1 || date[1].toInt() > 12){
                dtBought.setBackgroundColor(Color.RED)
                valid = valid && false
                dtBought.setError("Mes ingresado es invalido entre 01-12")
            }else{dtBought.setBackgroundColor(Color.WHITE)}

            if(date[0].toInt() < 1 || date[0].toInt() > 31){
                dtBought.setBackgroundColor(Color.RED)
                valid = valid && false
                dtBought.setError("El dia ingresado no es valido entre 01-31")
            }else{dtBought.setBackgroundColor(Color.WHITE)}
            return valid
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun getLocalDateTimeByString():LocalDateTime{
            val bought = dtBought.text.toString()
            val date = bought.split("/")

            return LocalDateTime.of(date[2].toInt(),date[1].toInt(),date[0].toInt(),0,0,0)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun clear() {
            etQuotesValue.editableText.clear();
            etProductName.editableText.clear();
            etProductValue.editableText.clear();
            etMonths.editableText.clear()
            etTax.setBackgroundColor(Color.WHITE)
            etMonths.setBackgroundColor(Color.WHITE)
            btnSave.visibility = View.INVISIBLE
            etQuotesValue.setBackgroundColor(Color.WHITE)
            etProductName.setBackgroundColor(Color.WHITE)
            dtBought.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            dtBought.setBackgroundColor(Color.WHITE)
            etTax.text = taxMonthly.toString()
        }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun map():CreditCardBoughtDTO{
        return CreditCardBoughtDTO(
            creditCardCode,
            creditCardName,
            etProductName.text.toString(),
            etProductValue.text.toString().toBigDecimal(),
            etTax.text.toString().toDouble(),
            etMonths.text.toString().toInt(),
            getLocalDateTimeByString(),
            cutOffDate,
            LocalDateTime.now(),
            0,
             0)

    }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun setFields(container: View) {
            container.let{
                etProductName = container.findViewById(R.id.etNameItem)
                etTax = container?.findViewById(R.id.etTaxBought)!!
                etMonths = container?.findViewById(R.id.etMonths)!!
                etQuotesValue = container?.findViewById(R.id.etQuoteValue)!!
                etProductValue = container?.findViewById(R.id.etProductValue)!!
                tvCardAssing = container?.findViewById(R.id.etCreditCardAssign)!!
                dtBought = container?.findViewById(R.id.dtBought)!!
                llTax = container?.findViewById(R.id.linearLayoutTax)!!

                etProductName.setOnFocusChangeListener { _, b -> !b and validate() && calc() }
                etProductValue.setOnFocusChangeListener { _, b -> !b and validate() && calc() }
                etMonths.setOnFocusChangeListener { _, b -> !b and validate() && calc() }

                val btnClear: Button = container.findViewById(R.id.btnClearBought)
                btnClear.setOnClickListener(this)
                btnSave = container.findViewById(R.id.btnSaveBought)
                btnSave.setOnClickListener(this)

            }

        }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadField(){
        dtBought.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        etTax.text = taxMonthly.toString()
    }
    }
