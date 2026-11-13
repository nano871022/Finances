package co.com.japl.module.creditcard.controllers.bought.forms

import androidx.lifecycle.SavedStateHandle
import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.RecapMonthly
import co.com.japl.finances.iports.dtos.TagDTO
import co.com.japl.finances.iports.dtos.TaxDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtPort
import co.com.japl.ui.Prefs
import co.com.japl.ui.testutil.MainDispatcherRule
import co.com.japl.ui.testutil.waitUntil
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import android.content.ContextWrapper
import java.time.LocalDate
import java.time.LocalDateTime

class QuoteViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testPeriod: LocalDateTime = LocalDateTime.of(2026, 11, 12, 0, 0)

    @Test
    fun when_rate_is_loaded_then_missing_handler_is_not_invoked() {
        val viewModel = newViewModel(rate = testRate(KindInterestRateEnum.CREDIT_CARD))
        awaitInitialLoad(viewModel)

        viewModel.setTaxDtoForTest(testRate(KindInterestRateEnum.CREDIT_CARD))
        var invoked = false
        viewModel.rateMissingHandler = { invoked = true }

        viewModel.creditRateEmpty()

        assertFalse("The handler must not run when a rate is loaded", invoked)
    }

    @Test
    fun when_rate_is_absent_then_missing_handler_is_invoked() {
        val viewModel = newViewModel(rate = null)
        awaitInitialLoad(viewModel)

        viewModel.setTaxDtoForTest(null)
        var invoked = false
        viewModel.rateMissingHandler = { invoked = true }

        viewModel.creditRateEmpty()

        assertTrue("The handler must run when there is no rate", invoked)
    }

    @Test
    fun when_execute_finds_rate_then_missing_handler_is_not_invoked() {
        val viewModel = newViewModel(rate = testRate(KindInterestRateEnum.CREDIT_CARD))
        awaitInitialLoad(viewModel)
        var invoked = false
        viewModel.rateMissingHandler = { invoked = true }

        runBlocking { viewModel.execute() }

        assertFalse("The handler must not run when the query returns a rate", invoked)
    }

    @Test
    fun when_execute_finds_no_rate_then_missing_handler_is_invoked() {
        val viewModel = newViewModel(rate = null)
        awaitInitialLoad(viewModel)
        var invoked = false
        viewModel.rateMissingHandler = { invoked = true }

        runBlocking { viewModel.execute() }

        assertTrue("The handler must run after the query returns null", invoked)
    }

    @Test
    fun when_create_and_new_twice_then_values_are_kept_and_two_records_are_created() {
        val port = RecordingBoughtPort()
        val viewModel = newViewModel(testRate(KindInterestRateEnum.CREDIT_CARD), port)
        awaitInitialLoad(viewModel)

        viewModel.nameProduct.onValueChange("Laptop")
        viewModel.valueProduct.onValueChange("1200000")
        viewModel.monthProduct.onValueChange("12")
        viewModel.recurrent.onValueChange(true)
        viewModel.tagSelected.onValueChange(
            TagDTO(id = 7, create = LocalDate.of(2026, 11, 1), name = "Tech", active = true)
        )

        runBlocking { viewModel.createAndBack().join() }

        assertEquals("Laptop", viewModel.nameProduct.value.value)
        assertEquals("1200000", viewModel.valueProduct.value.value)
        assertEquals("12", viewModel.monthProduct.value.value)
        assertEquals("Test credit card", viewModel.creditCardName.value.value)
        assertTrue(viewModel.recurrent.value.value)
        assertEquals(7, viewModel.tagSelected.value.value?.id)
        assertEquals(0, viewModel.currentBoughtIdForTest())
        assertTrue(viewModel.isNew.value)

        runBlocking { viewModel.createAndBack().join() }

        assertEquals("Laptop", viewModel.nameProduct.value.value)
        assertEquals("1200000", viewModel.valueProduct.value.value)
        assertEquals("12", viewModel.monthProduct.value.value)
        assertTrue(viewModel.recurrent.value.value)
        assertEquals(2, port.created.size)
        assertTrue(port.created.all { it.id == 0 })
    }

    private fun newViewModel(
        rate: TaxDTO?,
        boughtSvc: IBoughtPort? = null
    ): QuoteViewModel = QuoteViewModel(
        codeCreditCard = 1,
        savedStateHandle = SavedStateHandle(),
        codeBought = 0,
        period = testPeriod,
        boughtSvc = boughtSvc,
        creditRateSvc = FakeTaxPort(rate),
        creditCardSvc = FakeCreditCardPort(testCreditCard()),
        tagSvc = null,
        creditCardSettingSvc = null,
        buyCreditCardSettingSvc = null,
        navController = null,
        prefs = fakePrefs()
    )

    private fun fakePrefs(): Prefs {
        val sharedPreferences = mock(android.content.SharedPreferences::class.java)
        org.mockito.Mockito.`when`(
            sharedPreferences.getString(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString()
            )
        ).thenReturn("false")

        return Prefs(
            object : ContextWrapper(null) {
                override fun getSharedPreferences(
                    name: String,
                    mode: Int
                ): android.content.SharedPreferences = sharedPreferences
            }
        )
    }

    private fun awaitInitialLoad(viewModel: QuoteViewModel) {
        waitUntil { viewModel.loading.value.not() }
    }
}

private class RecordingBoughtPort : IBoughtPort {
    val created = mutableListOf<CreditCardBoughtDTO>()

    override fun create(creditCardBoughtDTO: CreditCardBoughtDTO, cache: Boolean): Int {
        require(creditCardBoughtDTO.id == 0) { "create() requires a new purchase id" }
        created.add(creditCardBoughtDTO.copy())
        return created.size
    }

    override fun update(creditCardBoughtDTO: CreditCardBoughtDTO, cache: Boolean): Boolean = false

    override fun getById(codeBought: Int, cache: Boolean): CreditCardBoughtDTO? = null

    override fun getRecap(
        creditCard: CreditCardDTO,
        cutOffDate: LocalDateTime,
        cache: Boolean
    ): RecapMonthly? = null

    override fun getBoughtCurrentPeriodList(
        creditCardDTO: CreditCardDTO,
        cutOff: LocalDateTime,
        cache: Boolean
    ): List<Pair<String, Double>>? = null

    override fun quoteValue(
        codeCreditRate: Int,
        months: Short,
        value: Double,
        kindOfTax: KindOfTaxEnum,
        kindOfInterest: KindInterestRateEnum
    ): Double = 0.0

    override fun capitalValue(
        codeCreditRate: Int,
        months: Short,
        value: Double,
        kindOfTax: KindOfTaxEnum,
        kindOfInterest: KindInterestRateEnum
    ): Double = 0.0

    override fun interestValue(
        codeCreditRate: Int,
        months: Short,
        value: Double,
        kindOfTax: KindOfTaxEnum,
        kindOfInterest: KindInterestRateEnum
    ): Double = 0.0

    override fun endingRecurrentPayment(codeBought: Int, cutOff: LocalDateTime): Boolean = false

    override fun fixDataProcess() = Unit
}
