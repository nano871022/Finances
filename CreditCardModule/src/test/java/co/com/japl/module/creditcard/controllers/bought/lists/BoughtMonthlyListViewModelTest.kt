package co.com.japl.module.creditcard.controllers.bought.lists

import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.ui.utils.NumbersUtil
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime
import java.time.YearMonth

class BoughtMonthlyListViewModelTest {

    @Test
    fun when_monthly_totals_are_thousands_then_compact_values_use_k() {
        val list = listOf(item(pendingToPay = 10_000.0), item(pendingToPay = 5_000.0))

        val viewModel = viewModel(list)

        assertEquals(15_000.0, viewModel.totalBoughtValue, 0.0)
        assertEquals("$ 15K", viewModel.compactTotalBought())
        assertEquals(NumbersUtil.COPtoString(15_000.0), viewModel.totalBought)
    }

    @Test
    fun when_monthly_totals_are_millions_then_compact_values_use_m() {
        val list = listOf(item(quoteValue = 1_000_000.0), item(quoteValue = 1_500_000.0))

        val viewModel = viewModel(list)

        assertEquals(2_500_000.0, viewModel.totalQuoteValue, 0.0)
        assertEquals("$ 2.5M", viewModel.compactTotalQuote())
        assertEquals(NumbersUtil.COPtoString(2_500_000.0), viewModel.totalQuote)
    }

    private fun viewModel(list: List<CreditCardBoughtItemDTO>): BoughtMonthlyListViewModel =
        BoughtMonthlyListViewModel(
            YearMonth.of(2026, 11),
            list,
            NumbersUtil.COPtoString(list.sumOf { it.pendingToPay }),
            NumbersUtil.COPtoString(list.sumOf { it.quoteValue }),
            NumbersUtil.COPtoString(list.sumOf { it.interestValue })
        )

    private fun item(
        pendingToPay: Double = 0.0,
        quoteValue: Double = 0.0,
        interestValue: Double = 0.0
    ): CreditCardBoughtItemDTO = CreditCardBoughtItemDTO(
        codeCreditCard = 1,
        nameCreditCard = "Visa",
        nameItem = "Item",
        valueItem = 10_000.0,
        interest = 0.0,
        month = 1,
        boughtDate = LocalDateTime.of(2026, 11, 1, 0, 0),
        cutOutDate = LocalDateTime.of(2026, 11, 15, 0, 0),
        createDate = LocalDateTime.of(2026, 11, 1, 0, 0),
        endDate = LocalDateTime.MAX,
        id = 1,
        recurrent = false,
        kind = KindInterestRateEnum.CREDIT_CARD,
        kindOfTax = KindOfTaxEnum.MONTHLY_EFFECTIVE,
        monthPaid = 0,
        capitalValue = 0.0,
        interestValue = interestValue,
        settings = 0.0,
        settingCode = 0,
        pendingToPay = pendingToPay,
        quoteValue = quoteValue,
        tagName = ""
    )
}
