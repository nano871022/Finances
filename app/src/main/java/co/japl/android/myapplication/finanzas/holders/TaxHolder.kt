package co.japl.android.myapplication.holders

import android.os.Build
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO
import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.ISpinnerHolder
import co.japl.android.myapplication.controller.Taxes
import co.japl.android.myapplication.finanzas.utils.TaxEnum
import co.japl.android.myapplication.putParams.TaxesParams
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import java.time.LocalDateTime

class TaxHolder (var view:View,var parentFragmentManager:FragmentManager,var navController: NavController,val list: List<CreditCardDTO>): IHolder<TaxDTO>,ISpinnerHolder<TaxHolder>, View.OnClickListener {
    lateinit var creditCard:MaterialAutoCompleteTextView
    lateinit var recyclerView: RecyclerView
    lateinit var add:Button
    private var positionCreditCard:Int = 0
    lateinit var lbNameTCC: TextInputLayout

    override fun setFields(actions: View.OnClickListener?) {
        creditCard = view.findViewById(R.id.spNameCCTCC)
        recyclerView = view.findViewById(R.id.rvTaxCC)
        add = view.findViewById(R.id.btnAddTCC)
        add.setOnClickListener(this)
        creditCard.setOnClickListener(this)
        lbNameTCC = view.findViewById(R.id.lbNameTCC)
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
        return TaxDTO(id,month,year,status,codeCreditCard,create,value,kind,period)
    }

    override fun cleanField() {
        //(creditCard.selectedView.findViewById(R.id.tvValueBigSp) as TextView).text = creditCard.getItemAtPosition(0).toString()
        creditCard.setSelection(1)
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
            R.id.spNameCCTCC -> creditCard.showDropDown()
            R.id.lbNameTCC -> Log.d(this.javaClass.name,"Lb Nmave tcc")
        }
    }

}