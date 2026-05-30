package co.japl.finances.core.adapters.inbound.implement.di

import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.common.IDifferQuotesPort
import co.com.japl.finances.iports.inbounds.common.ILLMService
import co.com.japl.finances.iports.inbounds.credit.IAdditional
import co.com.japl.finances.iports.inbounds.credit.IAdditionalFormPort
import co.com.japl.finances.iports.inbounds.credit.ICreditFormPort
import co.com.japl.finances.iports.inbounds.credit.IExtraValueAmortizationCreditPort
import co.com.japl.finances.iports.inbounds.credit.IPeriodCreditPort
import co.com.japl.finances.iports.inbounds.credit.IPeriodGracePort
import co.com.japl.finances.iports.inbounds.creditcard.IAmortizationTablePort
import co.com.japl.finances.iports.inbounds.creditcard.IBuyCreditCardSettingPort
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardSettingPort
import co.com.japl.finances.iports.inbounds.creditcard.ISMSCreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.IEmailCreditCardPort
import co.com.japl.finances.iports.inbounds.paid.IEmailPaidPort
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
import co.com.japl.finances.iports.inbounds.recap.IRecapPort
import co.com.japl.ui.impls.SMSObservable
import co.com.japl.ui.interfaces.ISMSObservablePublicher
import co.com.japl.ui.interfaces.ISMSObservableSubscriber
import co.japl.finances.core.adapters.inbound.implement.common.CheckPaymentsImpl
import co.japl.finances.core.adapters.inbound.implement.common.LLMServiceImpl
import co.japl.finances.core.adapters.inbound.implement.credit.AdditionalPort
import co.japl.finances.core.adapters.inbound.implement.credit.CreditImpl
import co.japl.finances.core.adapters.inbound.implement.credit.PeriodCreditImpl
import co.japl.finances.core.adapters.inbound.implement.credit.PeriodGraceImpl
import co.japl.finances.core.adapters.inbound.implement.credit.SimulatorCreditFixImpl
import co.japl.finances.core.adapters.inbound.implement.creditcard.AmortizationTableImpl
import co.japl.finances.core.adapters.inbound.implement.creditcard.SMSCreditCardImpl
import co.japl.finances.core.adapters.inbound.implement.creditcard.SimulatorImpl
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
import co.japl.finances.core.usercases.implement.credit.ExtraValueAmortizationImpl
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
import co.japl.finances.core.usercases.interfaces.credit.IExtraValueAmortization
import co.japl.finances.core.usercases.interfaces.credit.ICredit
import co.japl.finances.core.usercases.interfaces.credit.IPeriodCredit
import co.japl.finances.core.usercases.interfaces.credit.IPeriodGrace
import co.japl.finances.core.usercases.interfaces.creditcard.IAmortizationTable
import co.japl.finances.core.usercases.interfaces.creditcard.IBuyCreditCardSetting
import co.japl.finances.core.usercases.interfaces.creditcard.ICreditCardSetting
import co.japl.finances.core.usercases.interfaces.creditcard.ISMSCreditCard
import co.japl.finances.core.usercases.interfaces.creditcard.ITag
import co.japl.finances.core.usercases.interfaces.creditcard.ITax
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBought
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBoughtList
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBoughtSms
import co.japl.finances.core.usercases.interfaces.creditcard.paid.lists.IPaidList
import co.japl.finances.core.usercases.interfaces.paid.IProjection
import co.japl.finances.core.usercases.interfaces.paid.ISMSOld
import co.japl.finances.core.usercases.interfaces.paid.ISms2
import co.japl.finances.core.usercases.interfaces.recap.IRecap
import co.japl.finances.core.adapters.inbound.implement.creditcard.EmailCreditCard
import co.japl.finances.core.usercases.implement.creditcard.EmailCreditCardImpl
import co.japl.finances.core.usercases.interfaces.creditcard.IEmailCreditCard
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreModule {

    @Binds
    abstract fun bindUserCaseCreditFix(implement: co.japl.finances.core.usercases.implement.common.CreditFixImpl): co.japl.finances.core.usercases.interfaces.common.ICreditFix

    @Binds
    abstract fun bindUserCaseProjections(implement: co.japl.finances.core.usercases.implement.common.ProjectionsImpl): IProjections

    @Binds
    abstract fun bindUserCasePaid(implement: PaidImp): IPaid

    @Binds
    abstract fun bindUserCaseQuoteCreditCard(implement: QuoteCreditCardImpl): IQuoteCreditCard

    @Binds
    abstract fun bindServiceRate(implement: co.japl.finances.core.adapters.inbound.implement.creditcard.TaxImpl): ITaxPort

    @Binds
    abstract fun bindOutboundRate(implement: co.japl.finances.core.usercases.implement.creditcard.TaxImpl): ITax

    @Binds
    abstract fun bindUserCaseInput(implement: co.japl.finances.core.usercases.implement.common.InputImpl): IInput

    @Binds
    abstract fun binUserCaseCreditCard(implement: co.japl.finances.core.usercases.implement.common.CreditCardImpl): ICreditCard

    @Binds
    abstract fun bindRecapPort(implement:RecapImp):IRecapPort

    @Binds
    abstract fun bindUSerCaseRecap(implement:RecapImpl):IRecap

    @Binds
    abstract fun bindBoughtListPort(implement:ListImpl): IBoughtListPort

    @Binds
    abstract fun bindUserCaseBoughtList(implement:BoughtList):IBoughtList

    @Binds
    abstract fun bindCreditCardPort(implement:co.japl.finances.core.adapters.inbound.implement.creditcard.CreditCardImpl):ICreditCardPort

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
    abstract fun bindInputCreditCardSetting(implement:co.japl.finances.core.adapters.inbound.implement.creditcard.CreditCardSettingImpl):ICreditCardSettingPort

    @Binds
    abstract fun bindUserCaseCreditCardSetting(implement:co.japl.finances.core.usercases.implement.creditcard.CreditCardSettingImpl):ICreditCardSetting

    @Binds
    abstract fun bindInputAccount(implement:co.japl.finances.core.adapters.inbound.implement.account.AccountPort):IAccountPort

    @Binds
    abstract fun bindUserCaseAccount(implement:co.japl.finances.core.usercases.implement.account.Account):IAccount

    @Binds
    abstract fun bindInputBought(implement: BoughtImpl):IBoughtPort

    @Binds
    abstract fun bindUserCaseBought(implement:co.japl.finances.core.usercases.implement.creditcard.bought.Bought):co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBought

    @Binds
    abstract fun bindUserCaseTag(implement:co.japl.finances.core.usercases.implement.creditcard.Tag):ITag

    @Binds
    abstract fun bindInputTag(implement:co.japl.finances.core.adapters.inbound.implement.creditcard.TagImpl):ITagPort

    @Binds
    abstract fun bindInboundBuyCreditCadSetting(implement:co.japl.finances.core.adapters.inbound.implement.creditcard.BuyCreditCardSettingImpl):IBuyCreditCardSettingPort

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
    abstract fun bindInboundPaidPort(svc:co.japl.finances.core.adapters.inbound.implement.paid.PaidImpl):IPaidPort

    @Binds
    abstract fun bindUserCasePaid2(svc:co.japl.finances.core.usercases.implement.paid.PaidImpl):co.japl.finances.core.usercases.interfaces.paid.IPaid

    @Binds
    abstract fun bindInputPeriodPaid(svc:PeriodImpl):IPeriodPaidPort

    @Binds
    abstract fun bindUserCasePeriodPaid(svc:co.japl.finances.core.usercases.implement.paid.PeriodPaid):co.japl.finances.core.usercases.interfaces.paid.IPeriodPaid

    @Binds
    abstract fun getInboundSmsPaidPort(svc:SMSImpl):ISMSPaidPort

    @Binds
    abstract fun SmsPaidQuery(svc:co.japl.finances.core.usercases.implement.paid.SMSImpl):ISMSOld

    @Binds
    abstract fun bindInboundSmsPaidPort(svc:SMSImpl):ISmsPort

    @Binds
    abstract fun bindUserCaseSmsPaid(svc:co.japl.finances.core.usercases.implement.paid.PaidImpl):ISms2

    @Binds
    abstract fun bindInboundPaidCheckPaymentPort(svc:co.japl.finances.core.adapters.inbound.implement.paid.CheckPaymentImpl):ICheckPaymentPort

    @Binds
    abstract fun bindUserCaseCheckPayment(svc:co.japl.finances.core.usercases.implement.paid.CheckPaymentImpl):co.japl.finances.core.usercases.interfaces.paid.ICheckPayment

    @Binds
    abstract fun bindInboundCreditCheckPayment(svc:co.japl.finances.core.adapters.inbound.implement.credit.CheckPaymentImp):co.com.japl.finances.iports.inbounds.credit.ICheckPaymentPort

    @Binds
    abstract fun bindUserCaseCreditCheckPayment(svc:co.japl.finances.core.usercases.implement.credit.CheckPaymentImpl):co.japl.finances.core.usercases.interfaces.credit.ICheckPayment

    @Binds
    abstract fun  bindInboundCreditCardCheckPayment(svc:co.japl.finances.core.adapters.inbound.implement.creditcard.bought.CheckPaymentImpl):co.com.japl.finances.iports.inbounds.creditcard.bought.ICheckPaymentPort

    @Binds
    abstract fun  bindUserCaseCreditCardCheckPayment2(svc:co.japl.finances.core.usercases.implement.creditcard.bought.CheckPaymentImpl):co.japl.finances.core.usercases.interfaces.creditcard.ICheckPayment

    @Binds
    abstract fun bindInputCheckPaymentPort(svc:CheckPaymentsImpl):co.com.japl.finances.iports.inbounds.common.ICheckPaymentPort

    @Binds
    abstract fun bindInboundCreditPort(impl:CreditImpl):co.com.japl.finances.iports.inbounds.credit.ICreditPort

    @Binds
    abstract fun bindUsercaseCreditPort(impl:co.japl.finances.core.usercases.implement.credit.CreditImpl):ICredit

    @Binds
    abstract fun bindInboundPeriodGracePort(impl:PeriodGraceImpl):IPeriodGracePort

    @Binds
    abstract fun bindUsercasePeriodGrace(impl:co.japl.finances.core.usercases.implement.credit.PeriodGraceImpl):IPeriodGrace

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
    abstract fun bindInboundAmortizationVariablePort(impl: AmortizationTableImpl): IAmortizationTablePort

    @Binds
    abstract fun bindUserCaseAmortization(impl: co.japl.finances.core.usercases.implement.creditcard.AmortizationTableImpl): IAmortizationTable

    @Binds
    abstract fun bindInboundSimulatorCreditFixPort(impl: SimulatorCreditFixImpl): co.com.japl.finances.iports.inbounds.credit.ISimulatorCreditFixPort

    @Binds
    abstract fun bindInboundAmortizationFixPort(impl: co.japl.finances.core.adapters.inbound.implement.credit.AmortizationTableImpl): co.com.japl.finances.iports.inbounds.credit.IAmortizationTablePort

    @Binds
    abstract fun bindUserCaseAmortizationCredit(impl: co.japl.finances.core.usercases.implement.credit.AmortizationTableImpl): co.japl.finances.core.usercases.interfaces.credit.IAmortizationTable

    @Binds
    abstract fun bindInboundExtraValueAmortizationCredit(impl: co.japl.finances.core.adapters.inbound.implement.credit.ExtraValueAmortizationCreditImpl): IExtraValueAmortizationCreditPort

    @Binds
    abstract fun bindUserCaseAddAdditionalValue(impl: ExtraValueAmortizationImpl ): IExtraValueAmortization

    @Binds
    abstract fun bindInboundLLMService(impl: LLMServiceImpl): ILLMService

    @Binds
    abstract fun bindInbountEmailCreditCard(impl: EmailCreditCard): IEmailCreditCardPort

    @Binds
    abstract fun bindInboundEmailPaid(impl: co.japl.finances.core.adapters.inbound.implement.paid.EmailPaid): IEmailPaidPort

    @Binds
    abstract fun bindUserCaseEmailCreditCard(impl: EmailCreditCardImpl): IEmailCreditCard

    @Binds
    abstract fun bindUserCaseEmailPaid(impl: co.japl.finances.core.usercases.implement.paid.EmailPaidImpl): co.japl.finances.core.usercases.interfaces.paid.IEmailPaid

}
