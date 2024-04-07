package co.com.japl.module.creditcard.impl

import co.com.japl.finances.iports.dtos.SMSCreditCard
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ISMSCreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtSmsPort
import co.com.japl.ui.interfaces.ISMSObservableSubscriber
import co.com.japl.ui.interfaces.ISMSObserver
import javax.inject.Inject

class SMSObserver @Inject constructor(private val smsSvc:ISMSCreditCardPort,private val subscriber: ISMSObservableSubscriber,private val ccSvc:ICreditCardPort,private val svc:IBoughtSmsPort,private val msmSvc:ISMSCreditCardPort): ISMSObserver{

    private val numbers = mutableListOf<String>()
    private val smsCreditCardList = mutableListOf<SMSCreditCard>()

    init{
        subscriber.addObserver(this)
        ccSvc.getAll().takeIf { it.isNotEmpty() }?.forEach{
            arrayListOf(
                msmSvc.getByCreditCardAndKindInterest(it.id,KindInterestRateEnum.CREDIT_CARD)
                ,msmSvc.getByCreditCardAndKindInterest(it.id,KindInterestRateEnum.CASH_ADVANCE)
                ,msmSvc.getByCreditCardAndKindInterest(it.id,KindInterestRateEnum.WALLET_BUY),
            )
                .takeIf { it.isNotEmpty() }?.flatten()?.forEach{
                numbers.add(it.phoneNumber)
                smsCreditCardList.add(it)
            }
        }

    }
    override fun notify(phoneNumber:String,message: String) {
        smsCreditCardList.takeIf { it.isNotEmpty() }?.filter { it.phoneNumber == phoneNumber  }?.forEach {sms->
            smsSvc.getSmsMessages(sms.pattern, message)?.let {
                svc.createBySms(it.first, it.second, it.third, sms.codeCreditCard, sms.kindInterestRateEnum)
            }
        }
    }

    override fun getNumber(): List<String> {
        return numbers
    }

}