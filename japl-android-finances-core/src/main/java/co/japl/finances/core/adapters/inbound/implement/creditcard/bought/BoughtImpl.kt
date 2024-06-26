package co.japl.finances.core.adapters.inbound.implement.creditcard.bought

import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.RecapMonthly
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtSmsPort
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBought
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBoughtSms
import co.japl.finances.core.utils.DateUtils
import co.japl.finances.core.utils.NumbersUtil
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class BoughtImpl @Inject constructor(private val service:IBought, private  val smsImpl:IBoughtSms) : IBoughtPort, IBoughtSmsPort{
    override fun getRecap(creditCard: CreditCardDTO, cutOffDate: LocalDateTime, cache: Boolean): RecapMonthly? {
        return service.getRecap(creditCard,cutOffDate,cache)
    }

    override fun getBoughtCurrentPeriodList(
        creditCardDTO: CreditCardDTO,
        cutOff: LocalDateTime,cache: Boolean
    ): List<Pair<String, Double>>? {
        require(creditCardDTO.id != 0) { "CreditCard Id cannot be 0" }
        require(cutOff.compareTo(LocalDateTime.now())>0) { "Cutoff cannot be in the future" }
        return service.getBoughtCurrentPeriodList(creditCardDTO,cutOff,cache)
    }

    override fun create(creditCardBoughtDTO: CreditCardBoughtDTO,cache:Boolean): Int {
        require(creditCardBoughtDTO.id == 0) { "CreditCardBought Id should be 0" }
        return service.create(creditCardBoughtDTO,cache)
    }

    override fun update(creditCardBoughtDTO: CreditCardBoughtDTO,cache:Boolean): Boolean {
        require(creditCardBoughtDTO.id != 0) { "CreditCardBought Id cannot be 0" }
        return service.update(creditCardBoughtDTO,cache)
    }

    override fun quoteValue(codeCreditRate: Int, months: Short, value: Double,kindOfTax:KindOfTaxEnum,kindOfInterest:KindInterestRateEnum): Double {
        require(codeCreditRate > 0) { "CodeCreditRate cannot be 0" }
        require(months > 0.toShort()) { "Months cannot be 0" }
        require(value > 0.0) { "Value cannot be 0" }
        return service.quoteValue(codeCreditRate,months,value,kindOfTax,kindOfInterest)
    }

    override fun capitalValue(codeCreditRate: Int, months: Short, value: Double,kindOfTax:KindOfTaxEnum,kindOfInterest:KindInterestRateEnum): Double {
        require(codeCreditRate > 0) { "CodeCreditRate cannot be 0" }
        require(months > 0.toShort()) { "Months cannot be 0" }
        require(value > 0.0) { "Value cannot be 0" }
        return service.capitalValue(codeCreditRate,months,value,kindOfTax,kindOfInterest)
    }

    override fun interestValue(codeCreditRate: Int, months: Short, value: Double, kindOfTax: KindOfTaxEnum, kindOfInterest: KindInterestRateEnum): Double {
        require(codeCreditRate > 0) { "CodeCreditRate cannot be 0" }
        require(months > 0.toShort()) { "Months cannot be 0" }
        require(value > 0.0) { "Value cannot be 0" }
        return service.interestValue(codeCreditRate,months,value,kindOfTax,kindOfInterest)
    }

    override fun getById(codeBought: Int, cache: Boolean): CreditCardBoughtDTO? {
        require(codeBought > 0) { "CodeBought cannot be 0" }
        return service.getById(codeBought,cache)
    }

    override fun createBySms(name: String, value: Double, date: LocalDateTime,codeCreditCard:Int,kind:KindInterestRateEnum) {
        require(name.isNotEmpty()){"Name must not be empty"}
        require(value > 0.0){"Value must be greater than 0"}
        require(date != null){"Date must be valid"}
            val dto = CreditCardBoughtDTO(
                id = 0,
                nameItem = name,
                valueItem = value.toBigDecimal(),
                boughtDate = date,
                codeCreditCard = codeCreditCard,
                endDate = LocalDateTime.MAX,
                cutOutDate = LocalDateTime.now(),
                createDate = LocalDateTime.now(),
                interest = 0.0,
                kind = kind,
                kindOfTax = KindOfTaxEnum.MONTHLY_EFFECTIVE,
                month = 1,
                nameCreditCard = "",
                recurrent = 0
            )
            smsImpl.createBySms(dto)
    }

}