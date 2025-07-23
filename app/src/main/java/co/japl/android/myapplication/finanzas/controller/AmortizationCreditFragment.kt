package co.japl.android.myapplication.finanzas.controller

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.AmortizationCreditFix
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.GracePeriodDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.AdditionalCreditImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditFixImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.GracePeriodImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IAdditionalCreditSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICreditFix
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGracePeriod
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ISaveSvc
import co.japl.android.myapplication.finanzas.holders.interfaces.ITableHolder
import co.japl.android.myapplication.finanzas.holders.AmortizationCreditTableHolder
import co.japl.android.myapplication.finanzas.putParams.CreditFixParams
import co.japl.android.myapplication.finanzas.enums.AmortizationCreditFixEnum
import co.japl.android.myapplication.finanzas.putParams.ExtraValueListParam
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Optional
import javax.inject.Inject

@AndroidEntryPoint
class AmortizationCreditFragment : Fragment() ,LoaderManager.LoaderCallbacks<Triple<Optional<CreditDTO>,List<AdditionalCreditDTO>,List<GracePeriodDTO>>>, OnClickListener{
    private lateinit var holder: ITableHolder<AmortizationCreditFix>
    private lateinit var creditDto: CreditDTO
    private lateinit var lastDate: LocalDate

    @Inject lateinit var gracePeriodSvc: IGracePeriod
    @Inject lateinit var credit: ICreditFix
    @Inject  lateinit var svc: IAdditionalCreditSvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onResume() {
        super.onResume()
        loaderManager.restartLoader(1, null, this)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_amortization_credit, container, false)
        holder = AmortizationCreditTableHolder(root)
        holder.setup(this)
        getData()
        loaderManager.initLoader(1,null,this)
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getData(){
        arguments?.let {
            val values = CreditFixParams.downloadAmortizationList(it)
            creditDto = values.first
            lastDate = values?.second ?: LocalDate.now()
            holder.add(AmortizationCreditFixEnum.DATE_BILL.name,creditDto.date)
        }
    }

    private fun getCalc(data:CreditDTO):CalcDTO{
        val name = data.name
        val valueCredit = data.value
        val interest = data.tax
        val period = data.periods
        val quoteCredit = data.quoteValue
        val type = data.kindOf
        val id = data.id
        val interestValue = BigDecimal.ZERO
        val capitalValue = BigDecimal.ZERO
        val kindOfTax = data.kindOfTax
        return CalcDTO(name,valueCredit,interest,period.toLong(),quoteCredit,type,id,interestValue,capitalValue,kindOfTax)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAdditional(data: CreditDTO):AdditionalCreditDTO{
        val id = 0
        val name = ""
        val value = BigDecimal.ZERO
        val creditCode = data.id
        val startDate = LocalDate.now()
        val endDate = LocalDate.MAX
        return AdditionalCreditDTO(id, name ,value, creditCode.toLong() ,startDate,endDate )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Triple<Optional<CreditDTO>,List<AdditionalCreditDTO>,List<GracePeriodDTO>>> {
        return object:AsyncTaskLoader<Triple<Optional<CreditDTO>,List<AdditionalCreditDTO>,List<GracePeriodDTO>>>(requireContext()){
            private var data:Triple<Optional<CreditDTO>,List<AdditionalCreditDTO>,List<GracePeriodDTO>>? = null
            override fun onStartLoading() {
                super.onStartLoading()
                if(data != null){
                    deliverResult(data)
                }else{
                    forceLoad()
                }
            }
            @RequiresApi(Build.VERSION_CODES.O)
            override fun loadInBackground(): Triple<Optional<CreditDTO>,List<AdditionalCreditDTO>,List<GracePeriodDTO>>? {
                val credit = if(creditDto.quoteValue == BigDecimal.ZERO) credit.get(creditDto.id) else Optional.of(creditDto)
                val additionalList = svc.get(getAdditional(creditDto))
                val gracePeriodList = gracePeriodSvc.get(creditDto.id.toLong())
                data = Triple(credit,additionalList,gracePeriodList)
                return data
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Triple<Optional<CreditDTO>,List<AdditionalCreditDTO>,List<GracePeriodDTO>>>) {
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onLoadFinished(
        loader: Loader<Triple<Optional<CreditDTO>,List<AdditionalCreditDTO>,List<GracePeriodDTO>>>,
        data: Triple<Optional<CreditDTO>,List<AdditionalCreditDTO>,List<GracePeriodDTO>>?
    ) {
        data?.let {
            val additional =
                data.second.map { it.value }.reduceOrNull { acc, bigDecimal -> acc + bigDecimal }
                    ?: BigDecimal.ZERO
            holder.add(AmortizationCreditFixEnum.ADDITIONAL.name, additional)
            val gracePeriods = data.third.filter { it.create > lastDate || it.end < lastDate }.takeIf { it.isNotEmpty() }?.map{it.periods.toInt()}?.reduceOrNull{a,b->a+b}?:0
            val months = ChronoUnit.MONTHS.between(data.first.orElse(creditDto).date, lastDate)
            holder.add(AmortizationCreditFixEnum.QUOTES_PAID.name,months)
            holder.setData(getCalc(data.first.orElse(creditDto)))
            holder.create()
            holder.load()
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_additional_acf->CreditFixParams.newInstanceAmortizationToAdditionalList(creditDto.id.toLong(),findNavController())
            R.id.btn_extra_values_list_acf->ExtraValueListParam.newInstance(creditDto.id,findNavController())
        }
    }

}