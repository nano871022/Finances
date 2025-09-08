package co.japl.android.myapplication.finanzas.modules

import androidx.lifecycle.ViewModel
import co.com.japl.module.credit.controllers.amortization.AmortizationViewModel
import co.com.japl.module.credit.controllers.creditamortization.CreditAmortizationViewModel
import co.com.japl.module.credit.controllers.extravalue.ExtraValueListViewModel
import co.com.japl.module.credit.controllers.forms.AdditionalFormViewModel
import co.com.japl.module.credit.controllers.forms.CreditFormViewModel
import co.com.japl.module.credit.controllers.simulator.SimulatorFixViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModuleModule {
    @Binds
    abstract fun bindCreditModuleViewModelAmortization(implement: AmortizationViewModel): ViewModel
    @Binds
    abstract fun bindCreditModuleViewModelCreditAmortization(implement: CreditAmortizationViewModel): ViewModel
    @Binds
    abstract fun bindCreditModuleViewModelExtraValueList(implement: ExtraValueListViewModel): ViewModel
    @Binds
    abstract fun bindCreditModuleAdditionalForm(implement: AdditionalFormViewModel): ViewModel
    @Binds
    abstract fun bindCreditModuleCreditForm(implement: CreditFormViewModel): ViewModel
    @Binds
    abstract fun bindCreditModuleSimulatorFix(implement: SimulatorFixViewModel):ViewModel
}