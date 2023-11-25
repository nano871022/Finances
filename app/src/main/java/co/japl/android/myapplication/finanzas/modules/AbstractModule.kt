package co.japl.android.myapplication.finanzas.modules

import android.content.Context
import co.japl.android.myapplication.bussiness.impl.Config
import co.japl.android.myapplication.bussiness.impl.CreditCardImpl
import co.japl.android.myapplication.bussiness.impl.SaveCreditCardBoughtImpl
import co.japl.android.myapplication.bussiness.impl.SaveImpl
import co.japl.android.myapplication.bussiness.impl.TaxImpl
import co.japl.android.myapplication.bussiness.interfaces.ConfigSvc
import co.japl.android.myapplication.bussiness.interfaces.ITagQuoteCreditCardSvc
import co.japl.android.myapplication.bussiness.interfaces.ITagSvc
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
import co.japl.android.myapplication.finanzas.bussiness.impl.TagQuoteCreditCardImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.TagsImpl
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
import co.japl.finances.core.adapters.inbound.interfaces.recap.ICreditCardPort
import co.japl.finances.core.adapters.inbound.interfaces.recap.ICreditFixPort
import co.japl.finances.core.adapters.inbound.interfaces.recap.IInputPort
import co.japl.finances.core.adapters.inbound.interfaces.recap.IPaidPort
import co.japl.finances.core.adapters.inbound.interfaces.recap.IProjectionsPort
import co.japl.finances.core.adapters.inbound.interfaces.recap.IQuoteCreditCardPort
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

    @Binds
    abstract fun bindITagQuoteCreditCardSvc(implement: TagQuoteCreditCardImpl): ITagQuoteCreditCardSvc

    @Binds
    abstract fun bindITagSvc(implement: TagsImpl):ITagSvc

    @Binds
    abstract fun bindInboudIndCreditFixPort(implement: co.japl.finances.core.adapters.inbound.implement.recap.CreditFixImpl): ICreditFixPort

    @Binds
    abstract fun bindUserCaseCreditFix(implement: co.japl.finances.core.usercases.implement.recap.CreditFixImpl): co.japl.finances.core.usercases.interfaces.recap.ICreditFix

    @Binds
    abstract fun bindOutboundCreditFixPort(implement: co.japl.android.finances.services.core.CreditFixImpl): co.japl.finances.core.adapters.outbound.interfaces.ICreditFixRecapPort

    @Binds
    abstract fun bindOutboundAdditionalCreditPort(implement: co.japl.android.finances.services.core.AdditionalCreditImpl): co.japl.finances.core.adapters.outbound.interfaces.IAdditionalRecapPort

    @Binds
    abstract fun bindServiceCreditFix(implement: co.japl.android.finances.services.implement.CreditFixImpl): co.japl.android.finances.services.interfaces.ICreditFix

    @Binds
    abstract fun bindServiceAdditionalCredit(implement: co.japl.android.finances.services.implement.AdditionalCreditImpl): co.japl.android.finances.services.interfaces.IAdditionalCreditSvc

    @Binds
    abstract fun bindInboundProjections(implement: co.japl.finances.core.adapters.inbound.implement.recap.ProjectionsImpl): IProjectionsPort

    @Binds
    abstract fun bindServiceProjections(implement: co.japl.android.finances.services.implement.ProjectionsImpl): co.japl.android.finances.services.interfaces.IProjectionsSvc

    @Binds
    abstract fun bindOutboundProjections(implement: co.japl.android.finances.services.core.ProjectionsImpl): co.japl.finances.core.adapters.outbound.interfaces.IProjectionsRecapPort

    @Binds
    abstract fun bindUserCaseProjections(implement: co.japl.finances.core.usercases.implement.recap.ProjectionsImpl): co.japl.finances.core.usercases.interfaces.recap.IProjections

    @Binds
    abstract fun bindInboundPaid(implement: co.japl.finances.core.adapters.inbound.implement.recap.PaidImpl): IPaidPort

    @Binds
    abstract fun bindUserCasePaid(implement: co.japl.finances.core.usercases.implement.recap.PaidImp): co.japl.finances.core.usercases.interfaces.recap.IPaid

    @Binds
    abstract fun bindOutboundPaid(implement: co.japl.android.finances.services.core.PaidImpl): co.japl.finances.core.adapters.outbound.interfaces.IPaidRecapPort

    @Binds
    abstract fun bindServicePaid(implement: co.japl.android.finances.services.implement.PaidImpl): co.japl.android.finances.services.interfaces.IPaidSvc

    @Binds
    abstract fun bindInboundQuoteCreditCard(implement: co.japl.finances.core.adapters.inbound.implement.recap.QuoteCreditCardImpl): IQuoteCreditCardPort

    @Binds
    abstract fun bindUserCaseQuoteCreditCard(implement: co.japl.finances.core.usercases.implement.recap.QuoteCreditCardImpl): co.japl.finances.core.usercases.interfaces.recap.IQuoteCreditCard

    @Binds
    abstract fun bindOutboundCreditCard(implement: co.japl.android.finances.services.core.CreditCardImpl): co.japl.finances.core.adapters.outbound.interfaces.ICreditCardPort

    @Binds
    abstract fun bindServiceCreditCard(implement: co.japl.android.finances.services.implement.CreditCardImpl): co.japl.android.finances.services.interfaces.ICreditCardSvc

    @Binds
    abstract fun bindOutboundQuoteCreditCard(implement: co.japl.android.finances.services.core.QuoteCreditCardImpl): co.japl.finances.core.adapters.outbound.interfaces.IQuoteCreditCardPort

    @Binds
    abstract fun bindServiceQuoteCreditCard(implement: co.japl.android.finances.services.implement.SaveCreditCardBoughtImpl): co.japl.android.finances.services.interfaces.IQuoteCreditCardSvc

    @Binds
    abstract fun bindOutboundDifferInstallment(implement: co.japl.android.finances.services.core.DifferInstallmentImpl): co.japl.finances.core.adapters.outbound.interfaces.IDifferInstallmentRecapPort

    @Binds
    abstract fun bindServiceDifferInstallment(implement: co.japl.android.finances.services.implement.DifferInstallmentImpl): co.japl.android.finances.services.interfaces.IDifferInstallment

    @Binds
    abstract fun bindOutboundTax(implement: co.japl.android.finances.services.core.TaxImpl): co.japl.finances.core.adapters.outbound.interfaces.ITaxPort

    @Binds
    abstract fun bindServiceTax(implement: co.japl.android.finances.services.implement.TaxImpl): co.japl.android.finances.services.interfaces.ITaxSvc

    @Binds
    abstract fun bindOutboundBuyCreditCardSetting(implement: co.japl.android.finances.services.core.BuyCreditCardSettingImpl): co.japl.finances.core.adapters.outbound.interfaces.IBuyCreditCardSettingPort

    @Binds
    abstract fun bindServiceBuyCreditCardSetting(implement: co.japl.android.finances.services.implement.BuyCreditCardSettingImpl): co.japl.android.finances.services.interfaces.IBuyCreditCardSettingSvc

    @Binds
    abstract fun bindOutboundCreditCardSetting(implement: co.japl.android.finances.services.core.CreditCardSettingImpl): co.japl.finances.core.adapters.outbound.interfaces.ICreditCardSettingPort

    @Binds
    abstract fun bindServiceCreditCardSetting(implement: co.japl.android.finances.services.implement.CreditCardSettingImpl): co.japl.android.finances.services.interfaces.ICreditCardSettingSvc

    @Binds
    abstract fun bindInboundInputs(implement: co.japl.finances.core.adapters.inbound.implement.recap.InputImp): IInputPort

    @Binds
    abstract fun bindUserCaseInput(implement: co.japl.finances.core.usercases.implement.recap.InputImpl): co.japl.finances.core.usercases.interfaces.recap.IInput

    @Binds
    abstract fun bindServiceInput(implement: co.japl.android.finances.services.implement.InputImpl): co.japl.android.finances.services.interfaces.IInputSvc

    @Binds
    abstract fun bindOutboundInput(implement: co.japl.android.finances.services.core.InputImpl): co.japl.finances.core.adapters.outbound.interfaces.IInputPort

    @Binds
    abstract fun bindInboundCreditCard(implement: co.japl.finances.core.adapters.inbound.implement.recap.CreditCardImpl): ICreditCardPort

    @Binds
    abstract fun binUserCaseCreditCard(implement: co.japl.finances.core.usercases.implement.recap.CreditCardImpl): co.japl.finances.core.usercases.interfaces.recap.ICreditCard

    @Binds
    abstract fun binOutboundTags(implement: co.japl.android.finances.services.core.TagQuoteCreditCardImpl): co.japl.finances.core.adapters.outbound.interfaces.ITagQuoteCreditCardPort

    @Binds
    abstract fun bindServiceTags(implement: co.japl.android.finances.services.implement.TagQuoteCreditCardImpl): co.japl.android.finances.services.interfaces.ITagQuoteCreditCardSvc


}