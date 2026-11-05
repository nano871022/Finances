package co.com.japl.module.creditcard.controllers.setting

import androidx.lifecycle.SavedStateHandle
import co.com.japl.finances.iports.dtos.CreditCardSettingDTO
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardSettingPort
import co.com.japl.ui.testutil.MainDispatcherRule
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class CreditCardSettingListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun execute_whenCoreIsConsulted_thenProgressIsOn() {
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
