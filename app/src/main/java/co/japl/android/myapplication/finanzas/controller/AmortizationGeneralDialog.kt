package co.japl.android.myapplication.finanzas.controller

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.finanzas.holders.AmortizationGeneralDialogHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder

class AmortizationGeneralDialog(contexted:Context, val inflater:LayoutInflater,val calc:CalcDTO): Dialog(contexted) {
    private lateinit var holder:IHolder<CalcDTO>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.dialog_amortization_general, null)
        holder = AmortizationGeneralDialogHolder(view,this)
        holder.setFields(null)
        holder.loadFields(calc)
        setContentView(view)
    }
}