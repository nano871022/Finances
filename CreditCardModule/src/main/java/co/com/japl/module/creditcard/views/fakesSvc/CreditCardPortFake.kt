package co.com.japl.module.creditcard.views.fakesSvc

import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import java.math.BigDecimal
import java.time.LocalDateTime

class CreditCardPortFake : ICreditCardPort {
    override fun getAll(): List<CreditCardDTO> {
        return listOf(
            CreditCardDTO(
                id = 1,
                name = "Credit Card 1",
                maxQuotes = 36,
                cutOffDay = 1,
                warningValue = BigDecimal.ZERO,
                create = LocalDateTime.now(),
                status = true,
                interest1Quote = false,
                interest1NotQuote = false
            )
        )
    }

    override fun get(id: Int): CreditCardDTO? {
        return CreditCardDTO(
            id = 1,
            name = "Credit Card 1",
            maxQuotes = 36,
            cutOffDay = 1,
            warningValue = BigDecimal.ZERO,
            create = LocalDateTime.now(),
            status = true,
            interest1Quote = false,
            interest1NotQuote = false
        )
    }

    override fun delete(codeCreditCard: Int): Boolean {
        return true
    }

    override fun create(dto: CreditCardDTO): Int {
        return 1
    }

    override fun update(dto: CreditCardDTO): Boolean {
        return true
    }
}
