package co.japl.android.finances.services.dao.interfaces

import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.japl.android.finances.services.entities.EmailCreditCard
import co.japl.android.finances.services.interfaces.ISaveSvc
import co.japl.android.finances.services.interfaces.SaveSvc

interface IEmailCreditCardDAO : SaveSvc<EmailCreditCard>, ISaveSvc<EmailCreditCard> {
    fun getByCreditCardAndKindInterest(codeCreditCard: Int, kind: KindInterestRateEnum): List<EmailCreditCard>
}
