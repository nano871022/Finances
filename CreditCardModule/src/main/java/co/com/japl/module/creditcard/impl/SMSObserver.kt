package co.com.japl.module.creditcard.impl

import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtSmsPort
import co.com.japl.ui.interfaces.ISMSObservableSubscriber
import co.com.japl.ui.interfaces.ISMSObserver
import javax.inject.Inject

class SMSObserver @Inject constructor(private val subscriber: ISMSObservableSubscriber,private val svc:IBoughtSmsPort): ISMSObserver{

    private val pattern = "\\W+\\$(\\d+\\.\\d+)[en ]+([\\w ]+)\\.[\\d :]+(\\d{2}/\\d{2}/\\d{4}) compra afiliada"
    private val number = "87400"
    private val codeCreditCard = 1
    private val kindInterest = KindInterestRateEnum.CREDIT_CARD
    init{
        subscriber.addObserver(this)
    }
    override fun notify(message: String) {
        if (pattern.toRegex().containsMatchIn(message)) {
            pattern.toRegex().find(message)?.let {
                val value = it.groupValues[1]
                val name = it.groupValues[2]
                val date = it.groupValues[3]
                svc.createBySms(name,value,date,codeCreditCard,kindInterest)
            }
        }
    }

    override fun getNumber(): String {
        return number
    }

}