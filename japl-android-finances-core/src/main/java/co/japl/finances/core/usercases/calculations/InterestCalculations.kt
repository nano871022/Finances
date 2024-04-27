package co.japl.finances.core.usercases.calculations

import android.util.Log
import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.outbounds.IBuyCreditCardSettingPort
import co.com.japl.finances.iports.outbounds.ICreditCardSettingPort
import co.com.japl.finances.iports.outbounds.ITaxPort
import co.japl.finances.core.model.CreditCard
import co.japl.finances.core.model.Tax
import java.time.YearMonth
import javax.inject.Inject

class InterestCalculations @Inject constructor(private val buyCreditCardSettingSvc: IBuyCreditCardSettingPort
                                               ,private val taxScv: ITaxPort
                                                , private val creditCardSettingSvc: ICreditCardSettingPort){

    internal fun getTax(codeCreditCard:Int, kindTax:KindInterestRateEnum, period: YearMonth):Tax?{
        return taxScv.get(codeCreditCard,period.monthValue,period.year,kindTax)?.let {
            Tax(it.value,it.kindOfTax?:KindOfTaxEnum.MONTHLY_EFFECTIVE)
        }?:taxScv.get(codeCreditCard,YearMonth.now().monthValue,YearMonth.now().year,kindTax)?.let {
            Tax(it.value,it.kindOfTax?:KindOfTaxEnum.MONTHLY_EFFECTIVE).also{
                Log.w(javaClass.name,"=== getTax: CodeCreditCard $codeCreditCard KindTax: $kindTax Tax: $it EM")
            }
        }
    }

    internal fun lastInterestCalc(dto:CreditCardBoughtItemDTO,creditCard: CreditCard,differ:Boolean = false):Tax?{
        return creditCard.takeIf { (
                ( it.interest1Quote    && dto.monthPaid == 0L && dto.month == 1 && !differ) ||
                        ( it.interest1NotQuote && dto.monthPaid == 0L && dto.month > 1 && !differ)
                )  && dto.kind == KindInterestRateEnum.CREDIT_CARD}?.let{
            Tax(0.0,KindOfTaxEnum.MONTHLY_EFFECTIVE)
        }
    }
    internal fun calculateInterestMonthlyEffective(kind: KindInterestRateEnum, defaultTax:Tax, taxCCValue: Tax?, taxADVValue: Tax?, setting: Pair<Int, Double>?): Tax {
        return setting?.let{setting->
            setting.takeIf { it.second > 0}?.let {
                Tax(InterestRateCalculation.getNM(setting.second, KindOfTaxEnum.MONTHLY_EFFECTIVE),KindOfTaxEnum.MONTHLY_EFFECTIVE)
            }?: Tax(setting.second, KindOfTaxEnum.MONTHLY_EFFECTIVE)
        }?: when (kind) {
            KindInterestRateEnum.CASH_ADVANCE -> {
                taxADVValue?.let {
                    Tax(InterestRateCalculation.getNM(taxADVValue.value, taxADVValue.kind), KindOfTaxEnum.MONTHLY_EFFECTIVE)
                }
            }
            KindInterestRateEnum.CREDIT_CARD -> {
                taxCCValue?.let{
                    Tax(InterestRateCalculation.getNM(taxCCValue.value, taxCCValue.kind), KindOfTaxEnum.MONTHLY_EFFECTIVE)
                }
            }
            else-> null
        }?: Tax(InterestRateCalculation.getNM(defaultTax.value, defaultTax.kind), KindOfTaxEnum.MONTHLY_EFFECTIVE)
    }

    internal fun getSettings(codBought:Int,taxSetUp:Double):Pair<Int,Double>?{
        return buyCreditCardSettingSvc.get(codBought).also { Log.v(this.javaClass.name,"=== getSettings response buy setting BoughtId: $codBought Response: $it") }?.let{ buyCCSettingDto ->
            creditCardSettingSvc.get(buyCCSettingDto.codeCreditCardSetting)?.let{creditCardSettingDto->
                Pair(creditCardSettingDto.id,creditCardSettingDto.value.toDouble()).also { Log.v(this.javaClass.name,"=== getSettings credit setting BoughtId $codBought Response $it") }
            }?:taxSetUp.takeIf { it == 0.0}?.let{
                creditCardSettingSvc.get(0).also { Log.v(this.javaClass.name,"=== getSettings creditSetting response setting BoughtId: $codBought Response: $it") }?.let{ creditCardSettingDto ->
                    Pair(creditCardSettingDto.id,creditCardSettingDto.value.toDouble()).also { Log.v(this.javaClass.name,"=== getSettings credit setting BoughtId $codBought Response $it") }
                }
            }
        }
    }

    internal fun getInterestValue(month:Short, monthPaid:Short, kind:KindInterestRateEnum,pendingToPay:Double
                                  ,valueItem:Double, interest:Double,kindOfRate:KindOfTaxEnum
                                  , interest1Quote: Boolean=false,interest1NotQuote: Boolean=false,rediffer:Boolean=false):Double{
        require(KindOfTaxEnum.MONTHLY_EFFECTIVE == kindOfRate || KindOfTaxEnum.MONTLY_NOMINAL == kindOfRate)
        return (month.takeIf { it == 1.toShort() && (!interest1Quote || rediffer) }?.let {
                (pendingToPay * interest)
        }?:month.takeIf { it > 1}?.let{
            it.takeIf {
                interest1NotQuote
                && monthPaid == 1.toShort() && !rediffer
                && kind == KindInterestRateEnum.CREDIT_CARD}?.let{
                0.0
            }?: it.takeIf {
                interest1NotQuote
                        && monthPaid == 2.toShort()
                        && month > 1.toShort()
                        && kind == KindInterestRateEnum.CREDIT_CARD}?.let{
                (pendingToPay * interest) + (valueItem * interest)
            }?:pendingToPay.takeIf { it > 0.0}?.let {
                (pendingToPay * interest)
            }
        } ?: 0.0).also {
            Log.v(this.javaClass.name,"<<<=== FINISH::GetInterestValue month: $month monthPaid: $monthPaid kind: $kind pendingToPay: $pendingToPay valueItem: $valueItem interest: $interest kindOfRate: $kindOfRate interest1Quote: $interest1Quote interest1NotQuote: $interest1NotQuote Response $it")
        }


    }

}