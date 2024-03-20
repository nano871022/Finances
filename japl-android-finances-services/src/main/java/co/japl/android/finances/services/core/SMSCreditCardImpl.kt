package co.japl.android.finances.services.core

import co.com.japl.finances.iports.dtos.SMSCreditCard
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.outbounds.ISMSCreditCardPort
import co.japl.android.finances.services.core.mapper.SmsCreditCardMapper
import co.japl.android.finances.services.dao.interfaces.ISmsCreditCardDAO
import co.japl.android.finances.services.entities.SmsCreditCard
import java.util.Optional
import javax.inject.Inject

class SMSCreditCardImpl @Inject constructor(private val svc:ISmsCreditCardDAO): ISMSCreditCardPort {
    override fun create(dto: SMSCreditCard): Int {
        return svc.save(SmsCreditCardMapper.mapping(dto)).toInt()
    }

    override fun update(dto: SMSCreditCard): Boolean {
        return svc.save(SmsCreditCardMapper.mapping(dto))>0
    }

    override fun delete(codeSMSCreditCard: Int): Boolean {
        return svc.delete(codeSMSCreditCard)
    }

    override fun getById(codeSMSCreditCard: Int): SMSCreditCard? {
        return svc.get(codeSMSCreditCard).takeIf { it.isPresent }?.let {SmsCreditCardMapper.mapping(it.get())}
    }

    override fun getByCodeCreditCard(codeCreditCard: Int): List<SMSCreditCard> {
        require(codeCreditCard > 0)
        val entity = SmsCreditCard(
            codeCreditCard = codeCreditCard,
        )
        return svc.get(values=entity).map { SmsCreditCardMapper.mapping(it) }
    }

    override fun getByCreditCardAndKindInterest(
        codeCreditCard: Int,
        kind: KindInterestRateEnum
    ): List<SMSCreditCard> {
        return svc.getByCreditCardAndKindInterest(codeCreditCard,kind).map { SmsCreditCardMapper.mapping(it) }
    }
}