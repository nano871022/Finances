package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.SimulatorCreditDTO

interface ISimulatorCreditPort {

    fun save(dto: SimulatorCreditDTO,cache:Boolean): Long

    fun getAll(isVariable:Boolean):List<SimulatorCreditDTO>

    fun delete(code:Int):Boolean

    fun findByCode(code:Int,cache:Boolean):SimulatorCreditDTO?
}