package co.japl.android.finances.services.dao.interfaces

import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.japl.android.finances.services.entities.SmsCreditCard
import co.japl.android.finances.services.interfaces.SaveSvc

interface ISmsCreditCardDAO : SaveSvc<SmsCreditCard> {
    fun getByCreditCardAndKindInterest(codeCreditCard:Int,kind: KindInterestRateEnum):List<SmsCreditCard>
}