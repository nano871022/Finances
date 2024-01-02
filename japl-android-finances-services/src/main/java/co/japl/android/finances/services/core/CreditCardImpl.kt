package co.japl.android.finances.services.core

import android.database.CursorWindowAllocationException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.core.mapper.CreditCardMap
import co.japl.android.finances.services.interfaces.ICreditCardSvc
import co.com.japl.finances.iports.outbounds.ICreditCardPort
import co.com.japl.finances.iports.dtos.CreditCardDTO
import javax.inject.Inject

class CreditCardImpl @Inject constructor(private val credirCardSvc:ICreditCardSvc): ICreditCardPort {
    override fun getAll(): List<CreditCardDTO> {
        return credirCardSvc.getAll().map(CreditCardMap::mapper)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun get(id: Int): CreditCardDTO? {
        try {
            val creditCard = credirCardSvc.get(id)
            if (creditCard.isPresent) {
                return CreditCardMap.mapper(creditCard.get())
            }
        }catch (e: CursorWindowAllocationException){
            Log.e(javaClass.name,e.message,e)
        }
        return null

    }

    override fun delete(codeCreditCard: Int): Boolean {
        require(codeCreditCard > 0 , {"the code credit card must be more than 0"})
        return credirCardSvc.delete(codeCreditCard)
    }

    override fun create(dto: CreditCardDTO): Int {
        require(dto.id <= 0, { "The id must be 0" })
        return credirCardSvc.save(CreditCardMap.mapper(dto)).toInt()
    }

    override fun update(dto: CreditCardDTO): Boolean {
        require(dto.id > 0, { "The id must be more than 0" })
        return credirCardSvc.save(CreditCardMap.mapper(dto)) > 0
    }
}