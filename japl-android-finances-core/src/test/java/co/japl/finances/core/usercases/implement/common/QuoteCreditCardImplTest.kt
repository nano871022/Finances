package co.japl.finances.core.usercases.implement.common

import co.com.japl.finances.iports.dtos.CreditCardBoughtListDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.RecapCreditCardBoughtListDTO
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBoughtList
import co.com.japl.finances.iports.outbounds.ICreditCardPort
import co.com.japl.finances.iports.outbounds.IDifferInstallmentRecapPort
import co.com.japl.finances.iports.outbounds.IQuoteCreditCardPort
import co.com.japl.finances.iports.outbounds.ITagQuoteCreditCardPort
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

class QuoteCreditCardImplTest {

    @Test
    fun getTotalQuote_usesPreviousCutOff() {
        val creditCardSvc = mock(ICreditCardPort::class.java)
        val quoteCCImpl = mock(IQuoteCreditCardPort::class.java)
        val differSvc = mock(IDifferInstallmentRecapPort::class.java)
        val tagSvc = mock(ITagQuoteCreditCardPort::class.java)
        val context = android.app.Application()
        val buySetting = mock(co.com.japl.finances.iports.outbounds.IBuyCreditCardSettingPort::class.java)
        val taxPort = mock(co.com.japl.finances.iports.outbounds.ITaxPort::class.java)
        val ccSetting = mock(co.com.japl.finances.iports.outbounds.ICreditCardSettingPort::class.java)
        val interestCalc = co.japl.finances.core.usercases.calculations.InterestCalculations(buySetting, taxPort, ccSetting)
        val valuesCalc = co.japl.finances.core.usercases.calculations.ValuesCalculation()
        val listBoughts = co.japl.finances.core.usercases.implement.common.ListBoughts(quoteCCImpl, valuesCalc, interestCalc)

        val captured = mutableListOf<LocalDateTime>()
        val emptyList = CreditCardBoughtListDTO(emptyList(), RecapCreditCardBoughtListDTO())
        val boughtSvc = object : IBoughtList {
            override fun getBoughtList(creditCard: co.japl.finances.core.model.CreditCard, cutoff: LocalDateTime, cache: Boolean): CreditCardBoughtListDTO {
                captured.add(cutoff)
                return emptyList
            }
            override fun delete(codeBought: Int, cache: Boolean): Boolean = true
            override fun restore(codeBought: Int, cache: Boolean): Boolean = true
            override fun restoreEndDate(codeBought: Int, cache: Boolean): Boolean = true
            override fun endingRecurrentPayment(codeBought: Int, cutoff: LocalDateTime): Boolean = true
            override fun endingPayment(codeBought: Int, message: String, cutoff: LocalDateTime): Boolean = true
            override fun updateRecurrentValue(codeBought: Int, value: Double, cutoff: LocalDateTime, cache: Boolean): Boolean = true
            override fun differntInstallment(codeBought: Int, value: Long, cutOff: LocalDateTime, cache: Boolean): Boolean = true
            override fun clone(codeBought: Int, cache: Boolean): Boolean = true
            override fun getAllRecurrent(idCreditCard: Int): List<co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO> = emptyList()
            override fun reactivateRecurrent(codeBought: Int): Boolean = true
            override fun changeQuotas(codeBought: Int, months: Int, cache: Boolean): Boolean = true
            override fun getAllBoughts(): List<co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO> = emptyList()
        }

        val card = CreditCardDTO(
            id = 1,
            name = "Card",
            maxQuotes = 12.toShort(),
            cutOffDay = 15.toShort(),
            warningValue = BigDecimal.ZERO,
            create = LocalDateTime.now(),
            status = true,
            interest1Quote = false,
            interest1NotQuote = false
        )
        `when`(creditCardSvc.getAll()).thenReturn(listOf(card))

        val impl = QuoteCreditCardImpl(
            creditCardSvc, quoteCCImpl, differSvc, tagSvc, context,
            valuesCalc, interestCalc, listBoughts, boughtSvc
        )

        impl.getTotalQuote(false)

        val expected = co.japl.finances.core.utils.DateUtils.cutOffLastMonth(
            15.toShort(),
            co.japl.finances.core.utils.DateUtils.cutOff(15.toShort(), LocalDate.now())
        )
        assertEquals(expected, captured.first())
    }
}