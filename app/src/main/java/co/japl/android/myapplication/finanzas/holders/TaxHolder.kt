package co.japl.android.myapplication.holders

import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListTaxAdapter
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.ISpinnerHolder
import co.japl.android.myapplication.finanzas.enums.KindOfTaxEnum
import co.japl.android.myapplication.finanzas.enums.TaxEnum
import co.japl.android.myapplication.finanzas.holders.interfaces.IRecyclerView
import co.japl.android.myapplication.putParams.TaxesParams
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import java.time.LocalDateTime

class TaxHolder (var view:View,var parentFragmentManager:FragmentManager,var navController: NavController): IHolder<TaxDTO>,
    ISpinnerHolder<TaxHolder>, View.OnClickListener,IRecyclerView<TaxDTO> {
    lateinit var creditCard:MaterialAutoCompleteTextView
    lateinit var recyclerView: RecyclerView
    lateinit var add:Button
    lateinit var lbNameTCC: TextInputLayout
    lateinit var progressBar: ProgressBar
    lateinit var linerlayout:LinearLayout


    override fun setFields(actions: View.OnClickListener?) {
        creditCard = view.findViewById(R.id.spNameCCTCC)
        recyclerView = view.findViewById(R.id.rvTaxCC)
        progressBar = view.findViewById(R.id.pb_load_tcc)
        add = view.findViewById(R.id.btnAddTCC)
        add.setOnClickListener(this)
        creditCard.setOnClickListener(actions)
        lbNameTCC = view.findViewById(R.id.lbNameTCC)
        linerlayout = view.findViewById<LinearLayout>(R.id.llMainTTCC)
        creditCard.isFocusable = false
        progressBar.visibility = View.VISIBLE
        linerlayout.visibility = View.VISIBLE
        recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL,false)
    }

    override fun loadFields(values: TaxDTO) {
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun downLoadFields(): TaxDTO {
        val id = 0
        val month = "0".toShort()
        val year = 0
        val status = "0".toShort()
        val codeCreditCard = creditCard.listSelection
        val create = LocalDateTime.now()
        val value = 0.0
        val kind:Short = TaxEnum.CREDIT_CARD.ordinal.toShort()
        val period:Short = 0
        val kindOfTax = KindOfTaxEnum.EM.toString()
        return TaxDTO(id,month,year,status,codeCreditCard,create,value,kind,period,kindOfTax)
    }

    override fun cleanField() {
    }

    override fun validate(): Boolean {
        return true
    }

    override fun lists(fn: ((TaxHolder) -> Unit)?) {
        fn?.invoke(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
          R.id.btnAddTCC ->  TaxesParams.newInstance(navController)
            R.id.lbNameTCC -> Log.d(this.javaClass.name,"Lb Nmave tcc")
        }
    }

    override fun loadRecycler(data: MutableList<TaxDTO>) {
        recyclerView.adapter = ListTaxAdapter(data)
        progressBar.visibility = View.GONE
    }

}