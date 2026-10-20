package co.japl.android.finances.services.cache.impl

import android.database.sqlite.SQLiteOpenHelper
import co.japl.android.finances.services.dao.interfaces.IQuoteCreditCardDAO
import co.japl.android.finances.services.dto.CreditCardBoughtDTO
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class QuoteCreditCardCacheTest {

    private lateinit var quoteCreditCardDAO: FakeQuoteCreditCardDAO
    private lateinit var cache: QuoteCreditCardCache

    @Before
    fun setUp() {
        quoteCreditCardDAO = FakeQuoteCreditCardDAO()
        cache = QuoteCreditCardCache(quoteCreditCardDAO)
    }

    @Test
    fun update_shouldSaveToDAOAndReturnTrue() {
        val dto = CreditCardBoughtDTO(
            id = 1,
            codeCreditCard = 10,
            nameItem = "Test Bought",
            valueItem = BigDecimal("100.00"),
            interest = 0.0,
            month = 12,
            boughtDate = LocalDateTime.now(),
            cutOutDate = LocalDateTime.now(),
            createDate = LocalDateTime.now(),
            recurrent = 0,
            kind = 1,
            kindOfTax = "1",
            endDate = LocalDateTime.now(),
            nameCreditCard = "Visa"
        )

        val result = cache.update(dto)

        assertTrue(result)
        assertEquals(dto, quoteCreditCardDAO.lastSavedDto)
    }

    @Test
    fun create_shouldSaveToDAOAndReturnId() {
        val dto = CreditCardBoughtDTO(
            id = 0,
            codeCreditCard = 10,
            nameItem = "Test Bought",
            valueItem = BigDecimal("100.00"),
            interest = 0.0,
            month = 12,
            boughtDate = LocalDateTime.now(),
            cutOutDate = LocalDateTime.now(),
            createDate = LocalDateTime.now(),
            recurrent = 0,
            kind = 1,
            kindOfTax = "1",
            endDate = LocalDateTime.now(),
            nameCreditCard = "Visa"
        )

        val result = cache.create(dto)

        assertEquals(5, result)
        assertEquals(5, dto.id)
        assertEquals(dto, quoteCreditCardDAO.lastSavedDto)
    }

    private class FakeQuoteCreditCardDAO : IQuoteCreditCardDAO {
        override var dbConnect: SQLiteOpenHelper
            get() = TODO("Not yet implemented")
            set(value) {}

        var lastSavedDto: CreditCardBoughtDTO? = null

        override fun save(dto: CreditCardBoughtDTO): Long {
            lastSavedDto = dto
            return if (dto.id == 0) 5L else 1L
        }

        override fun getPeriods(creditCardId: Int) = TODO()
        override fun getToDate(key: Int, startDate: LocalDateTime, cutOffDate: LocalDateTime) = emptyList<CreditCardBoughtDTO>()
        override fun getPendingQuotes(key: Int, startDate: LocalDateTime, cutoffCurrent: LocalDateTime) = emptyList<CreditCardBoughtDTO>()
        override fun getCapital(key: Int, startDate: LocalDateTime, cutOff: LocalDateTime) = TODO()
        override fun getInterest(key: Int, startDate: LocalDateTime, cutOff: LocalDateTime) = TODO()
        override fun getCapitalPendingQuotes(key: Int, startDate: LocalDateTime, cutOff: LocalDateTime) = TODO()
        override fun getInterestPendingQuotes(key: Int, startDate: LocalDateTime, cutOff: LocalDateTime) = TODO()
        override fun getBought(key: Int, startDate: LocalDateTime, cutOff: LocalDateTime) = TODO()
        override fun getBoughtPendingQuotes(key: Int, startDate: LocalDateTime, cutOff: LocalDateTime) = TODO()
        override fun getBoughtQuotes(key: Int, startDate: LocalDateTime, cutOff: LocalDateTime) = TODO()
        override fun getPendingToPay(key: Int, startDate: LocalDateTime, cutOff: LocalDateTime) = TODO()
        override fun getPendingToPayQuotes(key: Int, startDate: LocalDateTime, cutOff: LocalDateTime) = TODO()
        override fun getRecurrentBuys(key: Int, cutOff: LocalDateTime) = emptyList<CreditCardBoughtDTO>()
        override fun getAllRecurrent(idCreditCard: Int) = emptyList<CreditCardBoughtDTO>()
        override fun getRecurrentPendingQuotes(key: Int, cutOff: LocalDateTime) = emptyList<CreditCardBoughtDTO>()
        override fun endingRecurrentPayment(idBought: Int, cutOff: LocalDateTime) = TODO()
        override fun endingPayment(idBought: Int, message: String, cutOff: LocalDateTime) = TODO()
        override fun backup(pathFile: String) = TODO()
        override fun restoreBackup(pathFile: String) = TODO()
        override fun getLastAvailableQuotesTC() = TODO()
        override fun getPeriod(creditCardId: Int) = TODO()
        override fun findByNameAndBoughtDateAndValue(name: String, boughtDate: LocalDateTime, amount: BigDecimal) = TODO()
        override fun fixDataProcess() = TODO()
        override fun getAll() = emptyList<CreditCardBoughtDTO>()
        override fun get(id: Int) = java.util.Optional.empty<CreditCardBoughtDTO>()
        override fun delete(id: Int) = true
    }
}
