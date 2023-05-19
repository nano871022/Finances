package co.japl.android.myapplication.finanzas.controller

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.impl.CreditCardImpl
import co.japl.android.myapplication.bussiness.impl.SaveCreditCardBoughtImpl
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditFixImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.InputImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.PaidImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.ProjectionsImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.*
import co.japl.android.myapplication.finanzas.holders.RecapHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.IRecapHolder
import co.japl.android.myapplication.utils.NumbersUtil
import org.apache.commons.codec.language.Nysiis
import java.math.BigDecimal

class RecapFragment : Fragment() {
    private lateinit var holder:IRecapHolder<RecapHolder>
    private lateinit var projectionSvc:IProjectionsSvc
    private lateinit var creditSvc:ICreditFix
    private lateinit var paidSvc:IPaidSvc
    private lateinit var quoteCreditCardSvc:IQuoteCreditCardSvc
    private lateinit var inputSvc:IInputSvc
    private lateinit var creditCardSvc: SaveSvc<CreditCardDTO>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_recap, container, false)
        val connectDB =  ConnectDB(root.context)
        projectionSvc = ProjectionsImpl(connectDB,root)
        creditSvc = CreditFixImpl(connectDB)
        paidSvc = PaidImpl(connectDB)
        quoteCreditCardSvc = SaveCreditCardBoughtImpl(connectDB)
        inputSvc = InputImpl(root,connectDB)
        creditCardSvc = CreditCardImpl(connectDB)

        holder = RecapHolder(root)
        holder.setFields(null)
        loadData()
        return root
    }

    private fun loadData(){
        val projection = projectionSvc.getTotalSavedAndQuote()
        val totalQuoteCredit = creditSvc.getTotalQuote()
        val totalPaid = paidSvc.getTotalPaid()
        val totalQuoteTC = quoteCreditCardSvc.getTotalQuoteTC()
        val totalInputs = inputSvc.getTotalInputs()
        val warning = creditCardSvc.getAll().sumOf { it.warningValue }

        holder.loadFields{
            it.inputs.text = NumbersUtil.COPtoString(totalInputs)
            it.quoteTC.text = NumbersUtil.COPtoString(totalQuoteTC)
            it.totalCredits.text = NumbersUtil.COPtoString(totalQuoteCredit)
            it.saved.text = NumbersUtil.COPtoString(projection.first)
            it.quoteSaved.text = NumbersUtil.COPtoString(projection.second)
            it.totalPaid.text = NumbersUtil.COPtoString(totalPaid)

            it.totalFix.text = NumbersUtil.COPtoString(totalPaid + totalQuoteCredit)
            val inputFix = totalInputs - (totalPaid + totalQuoteCredit)
            it.totalInputFix.text = NumbersUtil.COPtoString(inputFix)
            if(inputFix < BigDecimal.ZERO){
                it.totalInputFix.setTextColor(Color.RED)
            }
            it.totalPaids.text = NumbersUtil.COPtoString(totalPaid + totalQuoteCredit + totalQuoteTC)
            val totalPaids= totalInputs - (totalPaid + totalQuoteCredit + totalQuoteTC)
            it.totalInputsPaids.text = NumbersUtil.COPtoString( totalPaids)
            if(totalPaids < BigDecimal.ZERO){
                it.totalInputsPaids.setTextColor(Color.RED)
            }
            it.warning.text = NumbersUtil.COPtoString(warning)
            val limit = warning - totalQuoteTC
            it.limit.text = NumbersUtil.COPtoString(limit)
            if(limit < BigDecimal.ZERO){
                it.limit.setTextColor(Color.RED)
            }
        }
    }
}