package co.japl.android.myapplication.finanzas.modules

import android.content.Context
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.common.IDifferQuotesPort
import co.com.japl.finances.iports.inbounds.common.ISMSRead
import co.com.japl.finances.iports.inbounds.credit.IAdditional
import co.com.japl.finances.iports.inbounds.credit.IAdditionalFormPort
import co.com.japl.finances.iports.inbounds.credit.ICreditFormPort
import co.com.japl.finances.iports.inbounds.credit.IPeriodCreditPort
import co.com.japl.finances.iports.inbounds.credit.IPeriodGracePort
import co.com.japl.finances.iports.inbounds.creditcard.IAmortizationTablePort
import co.com.japl.finances.iports.inbounds.creditcard.IBuyCreditCardSettingPort
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardSettingPort
import co.com.japl.finances.iports.inbounds.creditcard.ISMSCreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ISimulatorCreditVariablePort
import co.com.japl.finances.iports.inbounds.creditcard.ITagPort
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtSmsPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import co.com.japl.finances.iports.inbounds.paid.ICheckPaymentPort
import co.com.japl.finances.iports.inbounds.paid.IPaidPort
import co.com.japl.finances.iports.inbounds.paid.IPeriodPaidPort
import co.com.japl.finances.iports.inbounds.paid.IProjectionFormPort
import co.com.japl.finances.iports.inbounds.paid.IProjectionListPort
import co.com.japl.finances.iports.inbounds.paid.IProjectionsPort
import co.com.japl.finances.iports.inbounds.paid.ISMSPaidPort
import co.com.japl.finances.iports.inbounds.paid.ISmsPort
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
import co.com.japl.finances.iports.outbounds.IAdditionalPort
import co.com.japl.finances.iports.outbounds.ICreditPort
import co.com.japl.finances.iports.outbounds.ISimulatorCreditPort
import co.com.japl.ui.impls.SMSObservable
import co.com.japl.ui.interfaces.ISMSObservablePublicher
import co.com.japl.ui.interfaces.ISMSObservableSubscriber
import co.japl.android.finances.services.dao.interfaces.IAdditionalCreditDAO
import co.japl.android.finances.services.dao.interfaces.ICheckCreditDAO
import co.japl.android.finances.services.dao.interfaces.ICheckQuoteDAO
import co.japl.android.finances.services.dao.interfaces.IGracePeriodDAO
import co.japl.android.finances.services.dao.interfaces.ISimulatorCreditDAO
import co.japl.android.myapplication.finanzas.controller.SMS
import co.japl.finances.core.adapters.inbound.implement.common.CheckPaymentsImpl
import co.japl.finances.core.adapters.inbound.implement.credit.AdditionalPort
import co.japl.finances.core.adapters.inbound.implement.credit.CreditImpl
import co.japl.finances.core.adapters.inbound.implement.credit.PeriodCreditImpl
import co.japl.finances.core.adapters.inbound.implement.credit.PeriodGraceImpl
import co.japl.finances.core.adapters.inbound.implement.credit.SimulatorCreditFixImpl
import co.japl.finances.core.adapters.inbound.implement.creditCard.AmortizationTableImpl
import co.japl.finances.core.adapters.inbound.implement.creditCard.SMSCreditCardImpl
import co.japl.finances.core.adapters.inbound.implement.creditCard.SimulatorImpl
import co.japl.finances.core.adapters.inbound.implement.creditcard.bought.BoughtImpl
import co.japl.finances.core.adapters.inbound.implement.creditcard.bought.lists.ListImpl
import co.japl.finances.core.adapters.inbound.implement.paid.PeriodImpl
import co.japl.finances.core.adapters.inbound.implement.paid.ProjectionImpl
import co.japl.finances.core.adapters.inbound.implement.paid.SMSImpl
import co.japl.finances.core.adapters.inbound.implement.recap.RecapImp
import co.japl.finances.core.usercases.implement.common.DifferQuoteImpl
import co.japl.finances.core.usercases.implement.common.PaidImp
import co.japl.finances.core.usercases.implement.common.QuoteCreditCardImpl
import co.japl.finances.core.usercases.implement.common.SimulatorCreditImpl
import co.japl.finances.core.usercases.implement.credit.AdditionalImpl
import co.japl.finances.core.usercases.implement.credit.PeriodCredit
import co.japl.finances.core.usercases.implement.creditcard.bought.BoughtSmsImpl
import co.japl.finances.core.usercases.implement.creditcard.bought.lists.BoughtList
import co.japl.finances.core.usercases.implement.creditcard.paid.lists.PaidListImpl
import co.japl.finances.core.usercases.implement.inputs.InputsImpl
import co.japl.finances.core.usercases.implement.recap.RecapImpl
import co.japl.finances.core.usercases.interfaces.IAccount
import co.japl.finances.core.usercases.interfaces.common.ICreditCard
import co.japl.finances.core.usercases.interfaces.common.IDifferQuotes
import co.japl.finances.core.usercases.interfaces.common.IInput
import co.japl.finances.core.usercases.interfaces.common.IPaid
import co.japl.finances.core.usercases.interfaces.common.IProjections
import co.japl.finances.core.usercases.interfaces.common.IQuoteCreditCard
import co.japl.finances.core.usercases.interfaces.common.ISimulatorCredit
import co.japl.finances.core.usercases.interfaces.credit.ICredit
import co.japl.finances.core.usercases.interfaces.credit.IPeriodCredit
import co.japl.finances.core.usercases.interfaces.credit.IPeriodGrace
import co.japl.finances.core.usercases.interfaces.creditcard.IAmortizationTable
import co.japl.finances.core.usercases.interfaces.creditcard.IBuyCreditCardSetting
import co.japl.finances.core.usercases.interfaces.creditcard.ICreditCardSetting
import co.japl.finances.core.usercases.interfaces.creditcard.ISMSCreditCard
import co.japl.finances.core.usercases.interfaces.creditcard.ITag
import co.japl.finances.core.usercases.interfaces.creditcard.ITax
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBought
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBoughtList
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBoughtSms
import co.japl.finances.core.usercases.interfaces.creditcard.paid.lists.IPaidList
import co.japl.finances.core.usercases.interfaces.paid.IProjection
import co.japl.finances.core.usercases.interfaces.paid.ISMS
import co.japl.finances.core.usercases.interfaces.paid.ISms
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
    abstract fun bindServiceCreditFix(implement: co.japl.android.finances.services.dao.implement.CreditFixImpl): co.japl.android.finances.services.dao.interfaces.ICreditDAO

    @Binds
    abstract fun bindServiceAdditionalCredit(implement: co.japl.android.finances.services.dao.implement.AdditionalCreditImpl): IAdditionalCreditDAO


    @Binds
    abstract fun bindServiceProjections(implement: co.japl.android.finances.services.dao.implement.ProjectionsImpl): co.japl.android.finances.services.dao.interfaces.IProjectionsSvc

    @Binds
    abstract fun bindOutboundProjections(implement: co.japl.android.finances.services.core.ProjectionsImpl): co.com.japl.finances.iports.outbounds.IProjectionsRecapPort

    @Binds
    abstract fun bindUserCaseProjections(implement: co.japl.finances.core.usercases.implement.common.ProjectionsImpl): IProjections


    @Binds
    abstract fun bindUserCasePaid(implement: PaidImp): IPaid

    @Binds
    abstract fun bindOutboundPaid(implement: co.japl.android.finances.services.core.PaidImpl): co.com.japl.finances.iports.outbounds.IPaidRecapPort

    @Binds
    abstract fun bindServicePaid(implement: co.japl.android.finances.services.dao.implement.PaidImpl): co.japl.android.finances.services.dao.interfaces.IPaidDAO


    @Binds
    abstract fun bindUserCaseQuoteCreditCard(implement: QuoteCreditCardImpl): IQuoteCreditCard

    @Binds
    abstract fun bindOutboundCreditCard(implement: co.japl.android.finances.services.core.CreditCardImpl): co.com.japl.finances.iports.outbounds.ICreditCardPort

    @Binds
    abstract fun bindServiceCreditCard(implement: co.japl.android.finances.services.implement.CreditCardImpl): co.japl.android.finances.services.interfaces.ICreditCardSvc

    @Binds
    abstract fun bindOutboundQuoteCreditCard(implement: co.japl.android.finances.services.core.QuoteCreditCardImpl): co.com.japl.finances.iports.outbounds.IQuoteCreditCardPort

    @Binds
    abstract fun bindServiceQuoteCreditCard(implement: co.japl.android.finances.services.dao.implement.SaveCreditCardBoughtImpl): co.japl.android.finances.services.dao.interfaces.IQuoteCreditCardDAO

    @Binds
    abstract fun bindOutboundDifferInstallment(implement: co.japl.android.finances.services.core.DifferInstallmentImpl): co.com.japl.finances.iports.outbounds.IDifferInstallmentRecapPort

    @Binds
    abstract fun bindServiceDifferInstallment(implement: co.japl.android.finances.services.implement.DifferInstallmentImpl): co.japl.android.finances.services.interfaces.IDifferInstallment

    @Binds
    abstract fun bindOutboundTax(implement: co.japl.android.finances.services.core.TaxImpl): co.com.japl.finances.iports.outbounds.ITaxPort

    @Binds
    abstract fun bindServiceRate(implement: co.japl.finances.core.adapters.inbound.implement.creditCard.TaxImpl): ITaxPort


    @Binds
    abstract fun bindOutboundRate(implement: co.japl.finances.core.usercases.implement.creditcard.TaxImpl): ITax

    @Binds
    abstract fun bindServiceTax(implement: co.japl.android.finances.services.dao.implement.TaxImpl): co.japl.android.finances.services.dao.interfaces.ITaxDAO

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
    abstract fun bindServiceInput(implement: co.japl.android.finances.services.dao.implement.InputImpl): co.japl.android.finances.services.dao.interfaces.IInputDAO

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
    abstract fun bindCreditCardPort(implement:co.japl.finances.core.adapters.inbound.implement.creditCard.CreditCardImpl):ICreditCardPort

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

    @Binds
    abstract fun bindInputCreditCardSetting(implement:co.japl.finances.core.adapters.inbound.implement.creditCard.CreditCardSettingImpl):ICreditCardSettingPort

    @Binds
    abstract fun bindUserCaseCreditCardSetting(implement:co.japl.finances.core.usercases.implement.creditcard.CreditCardSettingImpl):ICreditCardSetting

    @Binds
    abstract fun bindInputAccount(implement:co.japl.finances.core.adapters.inbound.implement.account.AccountPort):IAccountPort

    @Binds
    abstract fun bindUserCaseAccount(implement:co.japl.finances.core.usercases.implement.account.Account):IAccount

    @Binds
    abstract fun bindOutputCaseAccount(implement:co.japl.android.finances.services.core.AccountImpl):co.com.japl.finances.iports.outbounds.IAccountPort

    @Binds
    abstract fun bindDAOAccount(implement:co.japl.android.finances.services.dao.implement.AccountImpl):co.japl.android.finances.services.dao.interfaces.IAccountDAO

    @Binds
    abstract fun bindInputBought(implement:co.japl.finances.core.adapters.inbound.implement.creditcard.bought.BoughtImpl):IBoughtPort

    @Binds
    abstract fun bindUserCaseBought(implement:co.japl.finances.core.usercases.implement.creditcard.bought.Bought):IBought

    @Binds
    abstract fun bindUserCaseTag(implement:co.japl.finances.core.usercases.implement.creditcard.Tag):ITag

    @Binds
    abstract fun bindOutputTag(implement:co.japl.android.finances.services.core.TagImpl):co.com.japl.finances.iports.outbounds.ITagPort

    @Binds
    abstract fun bindInputTag(implement:co.japl.finances.core.adapters.inbound.implement.creditCard.TagImpl):ITagPort

    @Binds
    abstract fun bindTagSvc(implement:co.japl.android.finances.services.implement.TagsImpl):co.japl.android.finances.services.interfaces.ITagSvc

    @Binds
    abstract fun bindInboundBuyCreditCadSetting(implement:co.japl.finances.core.adapters.inbound.implement.creditCard.BuyCreditCardSettingImpl):IBuyCreditCardSettingPort
    @Binds
    abstract fun bindUserCaseBuyCreditCardSetting(implement:co.japl.finances.core.usercases.implement.creditcard.BuyCreditCardSettingImpl):IBuyCreditCardSetting

    @Binds
    abstract fun getSMSObservablePublisher(sms: SMSObservable): ISMSObservablePublicher

    @Binds
    abstract fun getSMSObservableSubscriber(sms: SMSObservable): ISMSObservableSubscriber

    @Binds
    abstract fun getInboundBoughtSmsPort(svc:BoughtImpl):IBoughtSmsPort

    @Binds
    abstract fun getUserCaseBoughtSmsSvc(svc:BoughtSmsImpl):IBoughtSms

    @Binds
    abstract fun getInboundSmsCreditCardPort(svc:SMSCreditCardImpl):ISMSCreditCardPort

    @Binds
    abstract fun SmsCreditCardQuery(svc:co.japl.finances.core.usercases.implement.creditcard.SMSCreditCardImpl):ISMSCreditCard

    @Binds
    abstract fun getOutboundSmsCreditCardPort(svc:co.japl.android.finances.services.core.SMSCreditCardImpl):co.com.japl.finances.iports.outbounds.ISMSCreditCardPort

    @Binds
    abstract fun getDAOSmsCreditCard(svc:co.japl.android.finances.services.dao.implement.SmsCreditCardImpl):co.japl.android.finances.services.dao.interfaces.ISmsCreditCardDAO

    @Binds
    abstract fun getSmsRead(svc:SMS):ISMSRead

    @Binds
    abstract fun bindInboundPaidPort(svc:co.japl.finances.core.adapters.inbound.implement.paid.PaidImpl):IPaidPort

    @Binds
    abstract fun bindUserCasePaid2(svc:co.japl.finances.core.usercases.implement.paid.PaidImpl):co.japl.finances.core.usercases.interfaces.paid.IPaid

    @Binds
    abstract fun bindInputPeriodPaid(svc:PeriodImpl):IPeriodPaidPort

    @Binds
    abstract fun bindOutputPeriodPaid(svc:co.japl.android.finances.services.core.PaidImpl):co.com.japl.finances.iports.outbounds.IPeriodPaidPort

    @Binds
    abstract fun bindOutputPaid(svc:co.japl.android.finances.services.core.PaidImpl):co.com.japl.finances.iports.outbounds.IPaidPort

    @Binds
    abstract fun bindUserCasePeriodPaid(svc:co.japl.finances.core.usercases.implement.paid.PeriodPaid):co.japl.finances.core.usercases.interfaces.paid.IPeriodPaid

    @Binds
    abstract fun getInboundSmsPaidPort(svc:SMSImpl):ISMSPaidPort
    @Binds
    abstract fun SmsPaidQuery(svc:co.japl.finances.core.usercases.implement.paid.SMSImpl):ISMS

    @Binds
    abstract fun getOutboundSmsPaidPort(svc:co.japl.android.finances.services.core.SMSPaidImpl):co.com.japl.finances.iports.outbounds.ISMSPaidPort

    @Binds
    abstract fun getDAOSmsPaid(svc:co.japl.android.finances.services.dao.implement.SmsPaidImpl):co.japl.android.finances.services.dao.interfaces.ISmsPaidDAO

    @Binds
    abstract fun bindInboundSmsPaidPort(svc:SMSImpl):ISmsPort

    @Binds
    abstract fun bindUserCaseSmsPaid(svc:co.japl.finances.core.usercases.implement.paid.PaidImpl):ISms

    @Binds
    abstract fun bindInboundPaidCheckPaymentPort(svc:co.japl.finances.core.adapters.inbound.implement.paid.CheckPaymentImpl):ICheckPaymentPort

    @Binds
    abstract fun bindUserCaseCheckPayment(svc:co.japl.finances.core.usercases.implement.paid.CheckPaymentImpl):co.japl.finances.core.usercases.interfaces.paid.ICheckPayment

    @Binds
    abstract fun bindOutputCheckPayment(svc:co.japl.android.finances.services.core.PaidCheckPaymentImpl):co.com.japl.finances.iports.outbounds.IPaidCheckPaymentPort

    @Binds
    abstract fun bindInboundCreditCheckPayment(svc:co.japl.finances.core.adapters.inbound.implement.credit.CheckPaymentImp):co.com.japl.finances.iports.inbounds.credit.ICheckPaymentPort

    @Binds
    abstract fun bindUserCaseCreditCheckPayment(svc:co.japl.finances.core.usercases.implement.credit.CheckPaymentImpl):co.japl.finances.core.usercases.interfaces.credit.ICheckPayment

    @Binds
    abstract fun bindOutputCreditCheckPayment(svc:co.japl.android.finances.services.core.CreditCheckPaymentImpl):co.com.japl.finances.iports.outbounds.ICreditCheckPaymentPort

    @Binds
    abstract fun  bindInboundCreditCardCheckPayment(svc:co.japl.finances.core.adapters.inbound.implement.creditcard.bought.CheckPaymentImpl):co.com.japl.finances.iports.inbounds.creditcard.bought.ICheckPaymentPort

    @Binds
    abstract fun  bindUserCaseCreditCardCheckPayment2(svc:co.japl.finances.core.usercases.implement.creditcard.bought.CheckPaymentImpl):co.japl.finances.core.usercases.interfaces.creditcard.ICheckPayment

    @Binds
    abstract fun  bindOutputCreditCardCheckPayment(svc:co.japl.android.finances.services.core.CreditCardCheckPaymentImpl):co.com.japl.finances.iports.outbounds.ICreditCardCheckPaymentPort

    @Binds
    abstract fun bindDAOPaidCheckPayment(svc:co.japl.android.finances.services.dao.implement.CheckPaymentImpl):co.japl.android.finances.services.dao.interfaces.ICheckPaymentDAO

    @Binds
    abstract fun bindDAOCreditCheckPayment(svc:co.japl.android.finances.services.dao.implement.CheckCreditImpl):ICheckCreditDAO

    @Binds
    abstract fun bindDAOCreditCardCheckPayment(svc:co.japl.android.finances.services.dao.implement.CheckQuoteImpl):ICheckQuoteDAO

    @Binds
    abstract fun bindInputCheckPaymentPort(svc:CheckPaymentsImpl):co.com.japl.finances.iports.inbounds.common.ICheckPaymentPort

    @Binds
    abstract fun bindOutboundCreditPort(svc:co.japl.android.finances.services.core.CreditFixImpl):ICreditPort

    @Binds
    abstract fun bindInboundCreditPort(impl:CreditImpl):co.com.japl.finances.iports.inbounds.credit.ICreditPort
    @Binds
    abstract fun bindUsercaseCreditPort(impl:co.japl.finances.core.usercases.implement.credit.CreditImpl):ICredit
    @Binds
    abstract fun bindInboundPeriodGracePort(impl:PeriodGraceImpl):IPeriodGracePort
    @Binds
    abstract fun bindUsercasePeriodGrace(impl:co.japl.finances.core.usercases.implement.credit.PeriodGraceImpl):IPeriodGrace
    @Binds
    abstract fun bindOutboundPeriodGracePort(impl:co.japl.android.finances.services.core.PeriodGraceImpl):co.com.japl.finances.iports.outbounds.IPeriodGracePort
    @Binds
    abstract fun bindDAOPeriodGrace(impl:co.japl.android.finances.services.dao.implement.GracePeriodImpl):IGracePeriodDAO
    @Binds
    abstract fun bindInboundCreditFormPort(impl:co.japl.finances.core.usercases.implement.credit.CreditFormImpl):ICreditFormPort
    @Binds
    abstract fun bindInboundPeriodCreditPort(impl: PeriodCreditImpl): IPeriodCreditPort
    @Binds
    abstract fun bindUserCasePeriodCredit(impl: PeriodCredit): IPeriodCredit
    @Binds
    abstract fun bindInboundAdditionalCredit(impl: AdditionalPort): IAdditional
    @Binds
    abstract fun bindUsercaseAdditionalCredit(impl: AdditionalImpl):co.japl.finances.core.usercases.interfaces.credit.IAdditional
    @Binds
    abstract fun binOutboundAdditionalCredit(impl:co.japl.android.finances.services.core.AdditionalCreditImpl): IAdditionalPort
    @Binds
    abstract fun bindInboundAdditionalFormCredit(impl:AdditionalPort): IAdditionalFormPort
    @Binds
    abstract fun bindInboundProjectionRecap(impl: ProjectionImpl): IProjectionsPort
    @Binds
    abstract fun bindUsercaseProjectionRecap(impl:co.japl.finances.core.usercases.implement.paid.ProjectionImpl): IProjection
    @Binds
    abstract fun bindInpboundProjectionList(impl: ProjectionImpl): IProjectionListPort
    @Binds
    abstract fun bindInboundProjectionForm(impl:ProjectionImpl): IProjectionFormPort

    @Binds
    abstract fun bindInboundSimulatorCreditVariablePort(impl: SimulatorImpl): ISimulatorCreditVariablePort
    @Binds
    abstract fun bindUserCaseSimulator(impl: SimulatorCreditImpl): ISimulatorCredit
    @Binds
    abstract fun binOutboundSimulator(impl: co.japl.android.finances.services.core.SimulatorCreditImpl): ISimulatorCreditPort
    @Binds
    abstract fun binDAOSimulator(impl: co.japl.android.finances.services.dao.implement.SimulatorCreditImpl): ISimulatorCreditDAO
    @Binds
    abstract fun bindInboundAmortizationVariablePort(impl: AmortizationTableImpl): IAmortizationTablePort
    @Binds
    abstract fun bindUserCaseAmortization(impl: co.japl.finances.core.usercases.implement.creditcard.AmortizationTableImpl): IAmortizationTable

    @Binds
    abstract fun bindInboundSimulatorCreditFixPort(impl: SimulatorCreditFixImpl): co.com.japl.finances.iports.inbounds.credit.ISimulatorCreditFixPort
    @Binds
    abstract fun bindInboundAmortizationFixPort(impl: co.japl.finances.core.adapters.inbound.implement.credit.AmortizationTableImpl): co.com.japl.finances.iports.inbounds.credit.IAmortizationTablePort
    @Binds
    abstract fun bindUserCaseAmortizationCredit(impl: co.japl.finances.core.usercases.implement.credit.AmortizationTableImpl): co.japl.finances.core.usercases.interfaces.credit.IAmortizationTable
}