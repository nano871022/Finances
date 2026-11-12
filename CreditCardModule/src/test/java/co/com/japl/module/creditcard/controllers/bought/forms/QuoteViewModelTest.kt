package co.com.japl.module.creditcard.controllers.bought.forms

import androidx.lifecycle.SavedStateHandle
import co.com.japl.finances.iports.dtos.TaxDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.ui.Prefs
import co.com.japl.ui.testutil.MainDispatcherRule
import co.com.japl.ui.testutil.waitUntil
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import android.content.ContextWrapper
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

    private fun newViewModel(rate: TaxDTO?): QuoteViewModel = QuoteViewModel(
        codeCreditCard = 1,
        savedStateHandle = SavedStateHandle(),
        codeBought = 0,
        period = testPeriod,
        boughtSvc = null,
        creditRateSvc = FakeTaxPort(rate),
        creditCardSvc = FakeCreditCardPort(testCreditCard()),
        tagSvc = null,
        creditCardSettingSvc = null,
        buyCreditCardSettingSvc = null,
        navController = null,
        prefs = Prefs(
            object : ContextWrapper(null) {
                override fun getSharedPreferences(
                    name: String,
                    mode: Int
                ): android.content.SharedPreferences = mock(android.content.SharedPreferences::class.java)
            }
        )
    )

    private fun awaitInitialLoad(viewModel: QuoteViewModel) {
        waitUntil { viewModel.loading.value.not() }
    }
}
