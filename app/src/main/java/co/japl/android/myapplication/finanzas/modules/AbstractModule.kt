package co.japl.android.myapplication.finanzas.modules

import android.content.Context
import co.com.japl.finances.iports.inbounds.common.ICreditCardPort
import co.com.japl.finances.iports.inbounds.common.IDifferQuotesPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
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
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGracePeriod
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IInputSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IKindOfTaxSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IPaidSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IProjectionsSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IQuoteCreditCardSvc
import co.com.japl.finances.iports.inbounds.recap.IRecapPort
import co.japl.finances.core.adapters.inbound.implement.creditcard.bought.lists.ListImpl
import co.japl.finances.core.adapters.inbound.implement.recap.RecapImp
import co.japl.finances.core.usercases.implement.common.DifferQuoteImpl
import co.japl.finances.core.usercases.implement.common.PaidImp
import co.japl.finances.core.usercases.implement.common.QuoteCreditCardImpl
import co.japl.finances.core.usercases.implement.creditcard.bought.lists.BoughtList
import co.japl.finances.core.usercases.implement.creditcard.paid.lists.PaidListImpl
import co.japl.finances.core.usercases.implement.inputs.InputsImpl
import co.japl.finances.core.usercases.implement.recap.RecapImpl
import co.japl.finances.core.usercases.interfaces.common.ICreditCard
import co.japl.finances.core.usercases.interfaces.common.IDifferQuotes
import co.japl.finances.core.usercases.interfaces.common.IInput
import co.japl.finances.core.usercases.interfaces.common.IPaid
import co.japl.finances.core.usercases.interfaces.common.IProjections
import co.japl.finances.core.usercases.interfaces.common.IQuoteCreditCard
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBoughtList
import co.japl.finances.core.usercases.interfaces.creditcard.paid.lists.IPaidList
import co.japl.finances.core.usercases.interfaces.recap.IRecap
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
    abstract fun bindUserCaseCreditFix(implement: co.japl.finances.core.usercases.implement.common.CreditFixImpl): co.japl.finances.core.usercases.interfaces.common.ICreditFix

    @Binds
    abstract fun bindOutboundCreditFixPort(implement: co.japl.android.finances.services.core.CreditFixImpl): co.com.japl.finances.iports.outbounds.ICreditFixRecapPort

    @Binds
    abstract fun bindOutboundAdditionalCreditPort(implement: co.japl.android.finances.services.core.AdditionalCreditImpl): co.com.japl.finances.iports.outbounds.IAdditionalRecapPort

    @Binds
    abstract fun bindServiceCreditFix(implement: co.japl.android.finances.services.implement.CreditFixImpl): co.japl.android.finances.services.interfaces.ICreditFix

    @Binds
    abstract fun bindServiceAdditionalCredit(implement: co.japl.android.finances.services.implement.AdditionalCreditImpl): co.japl.android.finances.services.interfaces.IAdditionalCreditSvc


    @Binds
    abstract fun bindServiceProjections(implement: co.japl.android.finances.services.implement.ProjectionsImpl): co.japl.android.finances.services.interfaces.IProjectionsSvc

    @Binds
    abstract fun bindOutboundProjections(implement: co.japl.android.finances.services.core.ProjectionsImpl): co.com.japl.finances.iports.outbounds.IProjectionsRecapPort

    @Binds
    abstract fun bindUserCaseProjections(implement: co.japl.finances.core.usercases.implement.common.ProjectionsImpl): IProjections


    @Binds
    abstract fun bindUserCasePaid(implement: PaidImp): IPaid

    @Binds
    abstract fun bindOutboundPaid(implement: co.japl.android.finances.services.core.PaidImpl): co.com.japl.finances.iports.outbounds.IPaidRecapPort

    @Binds
    abstract fun bindServicePaid(implement: co.japl.android.finances.services.implement.PaidImpl): co.japl.android.finances.services.interfaces.IPaidSvc


    @Binds
    abstract fun bindUserCaseQuoteCreditCard(implement: QuoteCreditCardImpl): IQuoteCreditCard

    @Binds
    abstract fun bindOutboundCreditCard(implement: co.japl.android.finances.services.core.CreditCardImpl): co.com.japl.finances.iports.outbounds.ICreditCardPort

    @Binds
    abstract fun bindServiceCreditCard(implement: co.japl.android.finances.services.implement.CreditCardImpl): co.japl.android.finances.services.interfaces.ICreditCardSvc

    @Binds
    abstract fun bindOutboundQuoteCreditCard(implement: co.japl.android.finances.services.core.QuoteCreditCardImpl): co.com.japl.finances.iports.outbounds.IQuoteCreditCardPort

    @Binds
    abstract fun bindServiceQuoteCreditCard(implement: co.japl.android.finances.services.implement.SaveCreditCardBoughtImpl): co.japl.android.finances.services.interfaces.IQuoteCreditCardSvc

    @Binds
    abstract fun bindOutboundDifferInstallment(implement: co.japl.android.finances.services.core.DifferInstallmentImpl): co.com.japl.finances.iports.outbounds.IDifferInstallmentRecapPort

    @Binds
    abstract fun bindServiceDifferInstallment(implement: co.japl.android.finances.services.implement.DifferInstallmentImpl): co.japl.android.finances.services.interfaces.IDifferInstallment

    @Binds
    abstract fun bindOutboundTax(implement: co.japl.android.finances.services.core.TaxImpl): co.com.japl.finances.iports.outbounds.ITaxPort

    @Binds
    abstract fun bindServiceTax(implement: co.japl.android.finances.services.implement.TaxImpl): co.japl.android.finances.services.interfaces.ITaxSvc

    @Binds
    abstract fun bindOutboundBuyCreditCardSetting(implement: co.japl.android.finances.services.core.BuyCreditCardSettingImpl): co.com.japl.finances.iports.outbounds.IBuyCreditCardSettingPort

    @Binds
    abstract fun bindServiceBuyCreditCardSetting(implement: co.japl.android.finances.services.implement.BuyCreditCardSettingImpl): co.japl.android.finances.services.interfaces.IBuyCreditCardSettingSvc

    @Binds
    abstract fun bindOutboundCreditCardSetting(implement: co.japl.android.finances.services.core.CreditCardSettingImpl): co.com.japl.finances.iports.outbounds.ICreditCardSettingPort

    @Binds
    abstract fun bindServiceCreditCardSetting(implement: co.japl.android.finances.services.implement.CreditCardSettingImpl): co.japl.android.finances.services.interfaces.ICreditCardSettingSvc


    @Binds
    abstract fun bindUserCaseInput(implement: co.japl.finances.core.usercases.implement.common.InputImpl): IInput

    @Binds
    abstract fun bindServiceInput(implement: co.japl.android.finances.services.dao.implement.InputImpl): co.japl.android.finances.services.dao.interfaces.IInputSvc

    @Binds
    abstract fun bindOutboundInput(implement: co.japl.android.finances.services.core.InputImpl): co.com.japl.finances.iports.outbounds.IInputPort


    @Binds
    abstract fun binUserCaseCreditCard(implement: co.japl.finances.core.usercases.implement.common.CreditCardImpl): ICreditCard

    @Binds
    abstract fun binOutboundTags(implement: co.japl.android.finances.services.core.TagQuoteCreditCardImpl): co.com.japl.finances.iports.outbounds.ITagQuoteCreditCardPort

    @Binds
    abstract fun bindServiceTags(implement: co.japl.android.finances.services.implement.TagQuoteCreditCardImpl): co.japl.android.finances.services.interfaces.ITagQuoteCreditCardSvc

    @Binds
    abstract fun bindRecapPort(implement:RecapImp):IRecapPort

    @Binds
    abstract fun bindUSerCaseRecap(implement:RecapImpl):IRecap

    @Binds
    abstract fun bindBoughtListPort(implement:ListImpl):IBoughtListPort

    @Binds
    abstract fun bindUserCaseBoughtList(implement:BoughtList):IBoughtList

    @Binds
    abstract fun bindCreditCardPort(implement:co.japl.finances.core.adapters.inbound.implement.common.CreditCardImpl):ICreditCardPort

    @Binds
    abstract fun bindDifferInstallmentPort(implement:co.japl.finances.core.adapters.inbound.implement.common.DifferQuotesImpl):IDifferQuotesPort

    @Binds
    abstract fun bindUserCaseDifferInstallment(implement:DifferQuoteImpl):IDifferQuotes

    @Binds
    abstract fun bindInputListPort(implement:co.japl.finances.core.adapters.inbound.implement.inputs.InputImpl):IInputPort

    @Binds
    abstract fun bindUserCaseInputList(implement:InputsImpl):co.japl.finances.core.usercases.interfaces.inputs.IInput

    @Binds
    abstract fun bindUserCasePaidList(implement:PaidListImpl):IPaidList

}