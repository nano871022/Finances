package co.japl.android.finances.services.core.di

import co.com.japl.finances.iports.inbounds.common.ISMSRead
import co.com.japl.finances.iports.inbounds.common.IEmailRead
import co.com.japl.finances.iports.inbounds.common.IGoogleDriveService
import co.com.japl.finances.iports.outbounds.ExternalFinancialDataPort
import co.com.japl.finances.iports.outbounds.IExtraValueAmortizationPort
import co.com.japl.finances.iports.outbounds.IAdditionalPort
import co.com.japl.finances.iports.outbounds.ICreditPort
import co.com.japl.finances.iports.outbounds.IEmailCreditCardPattern
import co.com.japl.finances.iports.outbounds.ISimulatorCreditPort
import co.com.japl.finances.iports.outbounds.PatrimonyPersistencePort
import co.com.japl.finances.iports.outbounds.TaxConfigurationPort
import co.com.japl.finances.iports.outbounds.TaxHistoryPersistencePort
import co.japl.android.finances.services.dao.interfaces.IAddAmortizationDAO
import co.japl.android.finances.services.dao.interfaces.IAdditionalCreditDAO
import co.japl.android.finances.services.dao.interfaces.ICheckCreditDAO
import co.japl.android.finances.services.dao.interfaces.ICheckQuoteDAO
import co.japl.android.finances.services.dao.interfaces.IEmailCreditCardDAO
import co.japl.android.finances.services.dao.interfaces.IGracePeriodDAO
import co.japl.android.finances.services.dao.interfaces.ISimulatorCreditDAO
import co.japl.android.finances.services.implement.EmailCreditCardPatternImpl
import co.japl.android.finances.services.implement.GmailReadImpl
import co.japl.android.finances.services.core.SMS
import co.com.japl.finances.iports.outbounds.ICreditFixRecapPort
import co.com.japl.finances.iports.outbounds.IAdditionalRecapPort
import co.com.japl.finances.iports.outbounds.IProjectionsRecapPort
import co.com.japl.finances.iports.outbounds.IPaidRecapPort
import co.com.japl.finances.iports.outbounds.ICreditCardPort
import co.com.japl.finances.iports.outbounds.IQuoteCreditCardPort
import co.com.japl.finances.iports.outbounds.IDifferInstallmentRecapPort
import co.com.japl.finances.iports.outbounds.ITaxPort
import co.com.japl.finances.iports.outbounds.IBuyCreditCardSettingPort
import co.com.japl.finances.iports.outbounds.ICreditCardSettingPort
import co.com.japl.finances.iports.outbounds.IInputPort
import co.com.japl.finances.iports.outbounds.ITagQuoteCreditCardPort
import co.com.japl.finances.iports.outbounds.IAccountPort
import co.com.japl.finances.iports.outbounds.ITagPort
import co.com.japl.finances.iports.outbounds.ISMSCreditCardPort
import co.com.japl.finances.iports.outbounds.IPeriodPaidPort
import co.com.japl.finances.iports.outbounds.IPaidPort
import co.com.japl.finances.iports.outbounds.ISMSPaidPort
import co.com.japl.finances.iports.outbounds.IPaidCheckPaymentPort
import co.com.japl.finances.iports.outbounds.ICreditCheckPaymentPort
import co.com.japl.finances.iports.outbounds.ICreditCardCheckPaymentPort
import co.com.japl.finances.iports.outbounds.IPeriodGracePort
import co.com.japl.finances.iports.outbounds.ILLMOutboundPort
import co.com.japl.finances.iports.outbounds.IEmailCreditCardPort
import co.com.japl.finances.iports.outbounds.IEmailPaidPort
import co.com.japl.finances.iports.outbounds.IEmailPaidPattern
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    abstract fun bindOutboundCreditFixPort(implement: co.japl.android.finances.services.core.CreditFixImpl): ICreditFixRecapPort

    @Binds
    abstract fun bindOutboundAdditionalCreditPort(implement: co.japl.android.finances.services.core.AdditionalCreditImpl): IAdditionalRecapPort

    @Binds
    abstract fun bindServiceCreditFix(implement: co.japl.android.finances.services.dao.implement.CreditFixImpl): co.japl.android.finances.services.dao.interfaces.ICreditDAO

    @Binds
    abstract fun bindServiceAdditionalCredit(implement: co.japl.android.finances.services.dao.implement.AdditionalCreditImpl): IAdditionalCreditDAO

    @Binds
    abstract fun bindServiceProjections(implement: co.japl.android.finances.services.dao.implement.ProjectionsImpl): co.japl.android.finances.services.dao.interfaces.IProjectionsSvc

    @Binds
    abstract fun bindOutboundProjections(implement: co.japl.android.finances.services.core.ProjectionsImpl): IProjectionsRecapPort

    @Binds
    abstract fun bindOutboundPaid(implement: co.japl.android.finances.services.core.PaidImpl): IPaidRecapPort

    @Binds
    abstract fun bindServicePaid(implement: co.japl.android.finances.services.dao.implement.PaidImpl): co.japl.android.finances.services.dao.interfaces.IPaidDAO

    @Binds
    abstract fun bindOutboundCreditCard(implement: co.japl.android.finances.services.core.CreditCardImpl): ICreditCardPort

    @Binds
    abstract fun bindServiceCreditCard(implement: co.japl.android.finances.services.implement.CreditCardImpl): co.japl.android.finances.services.interfaces.ICreditCardSvc

    @Binds
    abstract fun bindOutboundQuoteCreditCard(implement: co.japl.android.finances.services.core.QuoteCreditCardImpl): IQuoteCreditCardPort

    @Binds
    abstract fun bindServiceQuoteCreditCard(implement: co.japl.android.finances.services.dao.implement.SaveCreditCardBoughtImpl): co.japl.android.finances.services.dao.interfaces.IQuoteCreditCardDAO

    @Binds
    abstract fun bindOutboundDifferInstallment(implement: co.japl.android.finances.services.core.DifferInstallmentImpl): IDifferInstallmentRecapPort

    @Binds
    abstract fun bindServiceDifferInstallment(implement: co.japl.android.finances.services.implement.DifferInstallmentImpl): co.japl.android.finances.services.interfaces.IDifferInstallment

    @Binds
    abstract fun bindOutboundTax(implement: co.japl.android.finances.services.core.TaxImpl): ITaxPort

    @Binds
    abstract fun bindServiceTax(implement: co.japl.android.finances.services.dao.implement.TaxImpl): co.japl.android.finances.services.dao.interfaces.ITaxDAO

    @Binds
    abstract fun bindOutboundBuyCreditCardSetting(implement: co.japl.android.finances.services.core.BuyCreditCardSettingImpl): IBuyCreditCardSettingPort

    @Binds
    abstract fun bindServiceBuyCreditCardSetting(implement: co.japl.android.finances.services.implement.BuyCreditCardSettingImpl): co.japl.android.finances.services.interfaces.IBuyCreditCardSettingSvc

    @Binds
    abstract fun bindOutboundCreditCardSetting(implement: co.japl.android.finances.services.core.CreditCardSettingImpl): ICreditCardSettingPort

    @Binds
    abstract fun bindServiceCreditCardSetting(implement: co.japl.android.finances.services.implement.CreditCardSettingImpl): co.japl.android.finances.services.interfaces.ICreditCardSettingSvc

    @Binds
    abstract fun bindServiceInput(implement: co.japl.android.finances.services.dao.implement.InputImpl): co.japl.android.finances.services.dao.interfaces.IInputDAO

    @Binds
    abstract fun bindOutboundInput(implement: co.japl.android.finances.services.core.InputImpl): IInputPort

    @Binds
    abstract fun binOutboundTags(implement: co.japl.android.finances.services.core.TagQuoteCreditCardImpl): ITagQuoteCreditCardPort

    @Binds
    abstract fun bindServiceTags(implement: co.japl.android.finances.services.implement.TagQuoteCreditCardImpl): co.japl.android.finances.services.interfaces.ITagQuoteCreditCardSvc

    @Binds
    abstract fun bindOutputCaseAccount(implement: co.japl.android.finances.services.core.AccountImpl): IAccountPort

    @Binds
    abstract fun bindDAOAccount(implement: co.japl.android.finances.services.dao.implement.AccountImpl): co.japl.android.finances.services.dao.interfaces.IAccountDAO

    @Binds
    abstract fun bindOutputTag(implement: co.japl.android.finances.services.core.TagImpl): ITagPort

    @Binds
    abstract fun bindTagSvc(implement: co.japl.android.finances.services.implement.TagsImpl): co.japl.android.finances.services.interfaces.ITagSvc

    @Binds
    abstract fun getOutboundSmsCreditCardPort(svc:co.japl.android.finances.services.core.SMSCreditCardImpl): ISMSCreditCardPort

    @Binds
    abstract fun getDAOSmsCreditCard(svc:co.japl.android.finances.services.dao.implement.SmsCreditCardImpl): co.japl.android.finances.services.dao.interfaces.ISmsCreditCardDAO

    @Binds
    abstract fun getSmsRead(svc:SMS):ISMSRead

    @Binds
    abstract fun bindOutputPeriodPaid(svc:co.japl.android.finances.services.core.PaidImpl): IPeriodPaidPort

    @Binds
    abstract fun bindOutputPaid(svc:co.japl.android.finances.services.core.PaidImpl): IPaidPort

    @Binds
    abstract fun getOutboundSmsPaidPort(svc:co.japl.android.finances.services.core.SMSPaidImpl): ISMSPaidPort

    @Binds
    abstract fun getDAOSmsPaid(svc:co.japl.android.finances.services.dao.implement.SmsPaidImpl): co.japl.android.finances.services.dao.interfaces.ISmsPaidDAO

    @Binds
    abstract fun bindOutputCheckPayment(svc:co.japl.android.finances.services.core.PaidCheckPaymentImpl): IPaidCheckPaymentPort

    @Binds
    abstract fun bindOutputCreditCheckPayment(svc:co.japl.android.finances.services.core.CreditCheckPaymentImpl): ICreditCheckPaymentPort

    @Binds
    abstract fun  bindOutputCreditCardCheckPayment(svc:co.japl.android.finances.services.core.CreditCardCheckPaymentImpl): ICreditCardCheckPaymentPort

    @Binds
    abstract fun bindDAOPaidCheckPayment(svc:co.japl.android.finances.services.dao.implement.CheckPaymentImpl): co.japl.android.finances.services.dao.interfaces.ICheckPaymentDAO

    @Binds
    abstract fun bindDAOCreditCheckPayment(svc:co.japl.android.finances.services.dao.implement.CheckCreditImpl): ICheckCreditDAO

    @Binds
    abstract fun bindDAOCreditCardCheckPayment(svc:co.japl.android.finances.services.dao.implement.CheckQuoteImpl): ICheckQuoteDAO

    @Binds
    abstract fun bindOutboundCreditPort(svc:co.japl.android.finances.services.core.CreditFixImpl): ICreditPort

    @Binds
    abstract fun bindOutboundPeriodGracePort(impl:co.japl.android.finances.services.core.PeriodGraceImpl): IPeriodGracePort

    @Binds
    abstract fun bindDAOPeriodGrace(impl:co.japl.android.finances.services.dao.implement.GracePeriodImpl): IGracePeriodDAO

    @Binds
    abstract fun binOutboundAdditionalCredit(impl:co.japl.android.finances.services.core.AdditionalCreditImpl): IAdditionalPort

    @Binds
    abstract fun binOutboundSimulator(impl: co.japl.android.finances.services.core.SimulatorCreditImpl): ISimulatorCreditPort

    @Binds
    abstract fun binDAOSimulator(impl: co.japl.android.finances.services.dao.implement.SimulatorCreditImpl): ISimulatorCreditDAO

    @Binds
    abstract fun bindOutputAddAdditionalValue(impl: co.japl.android.finances.services.core.ExtraValueAmortizationImpl): IExtraValueAmortizationPort

    @Binds
    abstract fun bindDAOAddAmortization(impl: co.japl.android.finances.services.dao.implement.AddAmortizationImpl): IAddAmortizationDAO

    @Binds
    abstract fun bindOutboundLLMService(impl: co.japl.android.finances.services.core.LLMOutboundAdapter): ILLMOutboundPort

    @Binds
    abstract fun bindOutboudEmailCreditCard(impl: co.japl.android.finances.services.core.EmailCreditCardImpl): IEmailCreditCardPort

    @Binds
    abstract fun bindOutboundEmailPaid(impl: co.japl.android.finances.services.core.EmailPaidImpl): IEmailPaidPort

    @Binds
    abstract fun binDAOEmailCreditCard(impl: co.japl.android.finances.services.dao.implement.EmailCreditCardImpl): IEmailCreditCardDAO

    @Binds
    abstract fun bindOutboudGmailRead(imp: GmailReadImpl): IEmailRead

    @Binds
    abstract fun bindOutbound(impl: EmailCreditCardPatternImpl ): IEmailCreditCardPattern

    @Binds
    abstract fun bindEmailPaidPattern(impl: co.japl.android.finances.services.implement.EmailPaidPatternImpl): IEmailPaidPattern

    @Binds
    abstract fun binDAOEmailPaid(impl: co.japl.android.finances.services.dao.implement.EmailPaidImpl): co.japl.android.finances.services.dao.interfaces.IEmailPaidDAO

    @Binds
    abstract fun bindTaxConfigurationPort(impl: co.japl.android.finances.services.core.TaxConfigurationPortImpl): TaxConfigurationPort

    @Binds
    abstract fun bindExternalFinancialDataPort(impl: co.japl.android.finances.services.core.FinancesModuleIntegrationAdapter): ExternalFinancialDataPort

    @Binds
    abstract fun bindTaxHistoryPersistencePort(impl: co.japl.android.finances.services.core.TaxHistoryPortImpl): TaxHistoryPersistencePort

    @Binds
    abstract fun bindPatrimonyPersistencePort(impl: co.japl.android.finances.services.core.PatrimonyPortImpl): PatrimonyPersistencePort

    @Binds
    abstract fun bindPatrimonyDAO(impl: co.japl.android.finances.services.dao.implement.PatrimonyImpl): co.japl.android.finances.services.dao.interfaces.IPatrimonyDAO

    @Binds
    abstract fun bindTaxHistoryDAO(impl: co.japl.android.finances.services.dao.implement.TaxHistoryImpl): co.japl.android.finances.services.dao.interfaces.ITaxHistoryDAO

    @Binds
    abstract fun bindGoogleDriveService(impl: co.japl.android.finances.services.implement.GoogleDriveServiceImpl): IGoogleDriveService
}
