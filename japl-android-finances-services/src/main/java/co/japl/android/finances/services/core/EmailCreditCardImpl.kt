package co.japl.android.finances.services.core

import co.com.japl.finances.iports.dtos.EmailCreditCardDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.outbounds.IEmailCreditCardPort
import co.japl.android.finances.services.core.mapper.EmailCreditCardMapper
import co.japl.android.finances.services.dao.interfaces.IEmailCreditCardDAO
import co.japl.android.finances.services.entities.EmailCreditCard
import java.util.Optional
import javax.inject.Inject

class EmailCreditCardImpl @Inject constructor(private val svc: IEmailCreditCardDAO) : IEmailCreditCardPort {

    override fun create(dto: EmailCreditCardDTO): Int {
        return svc.save(EmailCreditCardMapper.mapping(dto)).toInt()
    }

    override fun update(dto: EmailCreditCardDTO): Boolean {
        return svc.save(EmailCreditCardMapper.mapping(dto)) > 0
    }

    override fun delete(codeEmailCreditCard: Int): Boolean {
        return svc.delete(codeEmailCreditCard)
    }

    override fun getById(codeEmailCreditCard: Int): EmailCreditCardDTO? {
        return svc.get(codeEmailCreditCard).takeIf { it.isPresent }?.let { EmailCreditCardMapper.mapping(it.get()) }
    }

    override fun getByCodeCreditCard(codeCreditCard: Int): List<EmailCreditCardDTO> {
        val entity = EmailCreditCard(codeCreditCard = codeCreditCard)
        return svc.get(entity).map { EmailCreditCardMapper.mapping(it) }
    }

    override fun getByCreditCardAndKindInterest(
        codeCreditCard: Int,
        kind: KindInterestRateEnum
    ): List<EmailCreditCardDTO> {
        return svc.getByCreditCardAndKindInterest(codeCreditCard, kind).map { EmailCreditCardMapper.mapping(it) }
    }
}
