package co.com.japl.module.creditcard.controllers.setting

import androidx.lifecycle.SavedStateHandle
import co.com.japl.finances.iports.dtos.CreditCardSettingDTO
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardSettingPort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * 25.1 — execute() must turn showProgress on, so ON_RESUME reloads and
 * post-delete reloads paint the progress bar.
 */
class CreditCardSettingListViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `execute turns the progress on while the core is consulted`() {
        val coreStarted = CountDownLatch(1)
        val releaseCore = CountDownLatch(1)

        val service = object : ICreditCardSettingPort {
            override fun getAll(codeCreditCard: Int): List<CreditCardSettingDTO> {
                coreStarted.countDown()
                releaseCore.await(5, TimeUnit.SECONDS)
                return emptyList()
            }

            override fun get(codeCreditCard: Int, codeCreditCardSetting: Int): CreditCardSettingDTO? = null
            override fun delete(codeCreditCard: Int, codeCreditCardSetting: Int): Boolean = false
            override fun update(dto: CreditCardSettingDTO): Boolean = false
            override fun create(dto: CreditCardSettingDTO): Int = 0
        }

        val viewModel = CreditCardSettingListViewModel(SavedStateHandle(), service)
        viewModel.showProgress.value = false

        viewModel.execute()

        assertTrue("execute must reach the core", coreStarted.await(5, TimeUnit.SECONDS))
        assertTrue(
            "showProgress must be on while the core returns data",
            viewModel.showProgress.value
        )
        releaseCore.countDown()
    }
}
