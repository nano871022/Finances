package co.com.japl.module.paid.impl

import co.com.japl.finances.iports.dtos.SMSPaidDTO
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.finances.iports.inbounds.paid.ISMSPaidPort
import co.com.japl.finances.iports.inbounds.paid.ISmsPort
import co.com.japl.ui.interfaces.ISMSObservableSubscriber
import co.com.japl.ui.interfaces.ISMSObserver
import javax.inject.Inject

class SMSObserver @Inject constructor(
    private val smsSvc: ISMSPaidPort,
    private val subscriber: ISMSObservableSubscriber,
    private val accountSvc: IAccountPort,
    private val svc: ISmsPort
) : ISMSObserver {

    private val numbers = mutableListOf<String>()
    private val smsPaidList = mutableListOf<SMSPaidDTO>()

    init {
        subscriber.addObserver(this)
        accountSvc.getAll().takeIf { it.isNotEmpty() }?.forEach { account ->
            smsSvc.getAllByCodeAccount(account.id).takeIf { it.isNotEmpty() }?.forEach { sms ->
                numbers.add(sms.phoneNumber)
                smsPaidList.add(sms)
            }
        }
    }

    override fun notify(phoneNumber: String, message: String) {
        smsPaidList.takeIf { it.isNotEmpty() }?.filter { it.phoneNumber == phoneNumber && it.active }?.forEach { sms ->
            smsSvc.getSmsMessages(sms.pattern, message)?.let {
                svc.createBySms(it.first, it.second, it.third, sms.codeAccount)
            }
        }
    }

    override fun getNumber(): List<String> {
        return numbers
    }
}
