package co.japl.android.myapplication.finanzas.controller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.impl.SaveCreditCardBoughtImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditFixImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.InputImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.PaidImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.ProjectionsImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.*
import co.japl.android.myapplication.finanzas.holders.RecapHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.IRecapHolder
import co.japl.android.myapplication.utils.NumbersUtil
import org.apache.commons.codec.language.Nysiis

class RecapFragment : Fragment() {
    private lateinit var holder:IRecapHolder<RecapHolder>
    private lateinit var projectionSvc:IProjectionsSvc
    private lateinit var creditSvc:ICreditFix
    private lateinit var paidSvc:IPaidSvc
    private lateinit var quoteCreditCardSvc:IQuoteCreditCardSvc
    private lateinit var inputSvc:IInputSvc

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

        holder.loadFields{
            it.inputs.text = NumbersUtil.COPtoString(totalInputs)
            it.quoteTC.text = NumbersUtil.COPtoString(totalQuoteTC)
            it.totalCredits.text = NumbersUtil.COPtoString(totalQuoteCredit)
            it.saved.text = NumbersUtil.COPtoString(projection.first)
            it.quoteSaved.text = NumbersUtil.COPtoString(projection.second)
            it.totalPaid.text = NumbersUtil.COPtoString(totalPaid)

            it.totalFix.text = NumbersUtil.COPtoString(totalPaid + totalQuoteCredit)
            it.totalInputFix.text = NumbersUtil.COPtoString(totalInputs - (totalPaid + totalQuoteCredit))
            it.totalPaids.text = NumbersUtil.COPtoString(totalPaid + totalQuoteCredit + totalQuoteTC)
            it.totalInputsPaids.text = NumbersUtil.COPtoString( totalInputs - (totalPaid + totalQuoteCredit + totalQuoteTC))
        }
    }
}