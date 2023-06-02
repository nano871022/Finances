package co.japl.android.myapplication.finanzas.holders

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListCreditCardAdapter
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.finanzas.holders.interfaces.IListHolder

class ListCreditCardHolder(val view:View,val parentFragmentManager:FragmentManager,val findNavController: NavController):IListHolder<ListCreditCardHolder,CreditCardDTO> {
    private lateinit var recycle: RecyclerView
    private lateinit var button: Button
    private lateinit var progressBar: ProgressBar

    override fun setFields(actions: View.OnClickListener?) {
        recycle = view.findViewById (R.id.rvCreditCardSettingCCS)
        button = view.findViewById(R.id.btnAddNewCCS)
        progressBar = view.findViewById(R.id.pb_load_ccs)
        button.setOnClickListener(actions)
        recycle.layoutManager = LinearLayoutManager(
            view.context,
            LinearLayoutManager.VERTICAL, false
        )
        progressBar.visibility = View.VISIBLE
    }

    override fun loadRecycler(data: MutableList<CreditCardDTO>) {
        recycle.adapter = ListCreditCardAdapter(data,parentFragmentManager,findNavController)
        progressBar.visibility = View.GONE
    }

    override fun loadFields(fn: ((ListCreditCardHolder) -> Unit)?) {
        fn?.invoke(this)
    }
}