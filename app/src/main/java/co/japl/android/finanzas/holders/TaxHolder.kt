package co.japl.android.finanzas.holders

import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.finanzas.R
import co.japl.android.finanzas.bussiness.DTO.TaxDTO
import co.japl.android.finanzas.bussiness.interfaces.IHolder
import co.japl.android.finanzas.bussiness.interfaces.ISpinnerHolder
import co.japl.android.finanzas.controller.Taxes
import java.time.LocalDateTime

class TaxHolder (var view:View,var parentFragmentManager:FragmentManager): IHolder<TaxDTO>,ISpinnerHolder<TaxHolder>, View.OnClickListener {
    lateinit var creditCard:Spinner
    lateinit var recyclerView: RecyclerView
    lateinit var add:Button


    override fun setFields(actions: View.OnClickListener?) {
        creditCard = view.findViewById(R.id.spNameCCTCC)
        recyclerView = view.findViewById(R.id.rvTaxCC)
        add = view.findViewById(R.id.btnAddTCC)
        add.setOnClickListener(this)
    }

    override fun loadFields(values: TaxDTO) {
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun downLoadFields(): TaxDTO {
        val id = 0
        val month = "0".toShort()
        val year = 0
        val status = "0".toShort()
        val codeCreditCard = creditCard.selectedItem.toString().toInt()
        val create = LocalDateTime.now()
        val value = 0.0
        return TaxDTO(id,month,year,status,codeCreditCard,create,value)
    }

    override fun cleanField() {
        (creditCard.selectedView.findViewById(R.id.tvValueBigSp) as TextView).text = creditCard.getItemAtPosition(0).toString()
    }

    override fun validate(): Boolean {
        return true
    }

    override fun lists(fn: ((TaxHolder) -> Unit)?) {
        fn?.invoke(this)
    }

    override fun onClick(v: View?) {
        parentFragmentManager.beginTransaction().replace(R.id.fragment_initial, Taxes()).setTransition(
            FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
    }
}