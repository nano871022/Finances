package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.CreditDTO

interface ICreditFixRecapPort{

    fun getAll():List<CreditDTO>

}