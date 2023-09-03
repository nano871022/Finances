package co.japl.android.myapplication.finanzas.modules

import android.content.Context
import co.japl.android.myapplication.bussiness.impl.Config
import co.japl.android.myapplication.bussiness.impl.CreditCardImpl
import co.japl.android.myapplication.bussiness.impl.SaveCreditCardBoughtImpl
import co.japl.android.myapplication.bussiness.impl.SaveImpl
import co.japl.android.myapplication.bussiness.impl.TaxImpl
import co.japl.android.myapplication.bussiness.interfaces.ConfigSvc
import co.japl.android.myapplication.bussiness.interfaces.ITaxSvc
import co.japl.android.myapplication.finanzas.bussiness.impl.AccountImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.AddAmortizationImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.AdditionalCreditImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.BuyCreditCardSettingImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.CheckCreditImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.CheckPaymentImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.CheckQuoteImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditCardSettingImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditFixImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.DifferInstallmentImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.ExtraValueAmortizationCreditImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.ExtraValueAmortizationQuoteCreditCardImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.GracePeriodImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.InputImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.KindOfTaxImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.PaidImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.ProjectionsImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IAccountSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IAddAmortizationSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IAdditionalCreditSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IBuyCreditCardSettingSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICalcSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICheckCreditSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICheckPaymentSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICheckQuoteSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICreditCardSettingSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICreditCardSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICreditFix
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IDifferInstallment
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IExtraValueAmortizationCreditSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IExtraValueAmortizationQuoteCreditCardSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGetPeriodsServices
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGracePeriod
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IInputSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IKindOfTaxSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IPaidSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IProjectionsSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IQuoteCreditCardSvc
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AbstractModule {
    @Binds
    abstract fun bindICreditCardService(implement: CreditCardImpl): ICreditCardSvc
    @Binds
    abstract fun bindIProjectionService(implement: ProjectionsImpl): IProjectionsSvc

    @Binds
    abstract fun bindICreditFixService(implement: CreditFixImpl): ICreditFix

    @Binds
    abstract fun bindIPaidService(implement: PaidImpl): IPaidSvc

    @Binds
    abstract fun bindIBoughtCrediCardService(implement: SaveCreditCardBoughtImpl): IQuoteCreditCardSvc
    @Binds
    abstract fun bindIInputService(implement: InputImpl): IInputSvc
    @Binds
    abstract fun bindContect(@ApplicationContext context:Context):Context

    @Binds
    abstract fun binITaxSvc(implement: TaxImpl):ITaxSvc

    @Binds
    abstract fun bindITaxSvc(implement: Config):ConfigSvc

    @Binds
    abstract fun bindIAccountSvc(implement: AccountImpl):IAccountSvc

    @Binds
    abstract fun bindIAdditionalCreditSvc(implement: AdditionalCreditImpl):IAdditionalCreditSvc

    @Binds
    abstract fun bindIAddAmortizationSvc(implement: AddAmortizationImpl):IAddAmortizationSvc

    @Binds
    abstract fun bindIGracePeriodSvc(implement: GracePeriodImpl):IGracePeriod

    @Binds
    abstract fun bindIDifferInstallmentSvc(implement: DifferInstallmentImpl):IDifferInstallment
    @Binds
    abstract fun bindIKindOfTaxSvc(implement: KindOfTaxImpl): IKindOfTaxSvc

    @Binds
    abstract fun bindICheckPaymentSvc(implement: CheckPaymentImpl): ICheckPaymentSvc
    @Binds
    abstract fun bindICheckQuoteSvc(implement: CheckQuoteImpl): ICheckQuoteSvc

    @Binds
    abstract fun bindICheckCreditSvc(implement: CheckCreditImpl): ICheckCreditSvc

    @Binds
    abstract fun bindICreditCardSettingSvc(implement: CreditCardSettingImpl): ICreditCardSettingSvc

    @Binds
    abstract fun bindIExtraValueAmortizationCreditSvc(implement: ExtraValueAmortizationCreditImpl): IExtraValueAmortizationCreditSvc

    @Binds
    abstract fun bindIExtraValueAmortizationQuoteCreditCardSvc(implement: ExtraValueAmortizationQuoteCreditCardImpl): IExtraValueAmortizationQuoteCreditCardSvc

    @Binds
    abstract fun bindICalcSvc(implement: SaveImpl): ICalcSvc
    @Binds
    abstract fun bindIBuyCreditCardSettingSvc(implement: BuyCreditCardSettingImpl): IBuyCreditCardSettingSvc
}