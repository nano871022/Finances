package co.japl.finances.core.usercases.interfaces.paid

import co.com.japl.finances.iports.dtos.PaidDTO

interface ISms {
    fun createBySms(dto: PaidDTO): Boolean
}