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
import co.japl.android.myapplication.adapter.ListCreditCardSettingAdapter
import co.japl.android.myapplication.bussiness.DTO.CreditCardSettingDTO
import co.japl.android.myapplication.finanzas.holders.interfaces.IListHolder

class ListCreditCardSettingHolder(val view:View,val parentFragmentManager: FragmentManager,val findNavController: NavController):IListHolder<ListCreditCardSettingHolder,CreditCardSettingDTO> {
    private lateinit var recycle: RecyclerView
    private lateinit var btnAdd: Button
    private lateinit var btnCancel: Button
    private lateinit var progressBar: ProgressBar

    override fun setFields(actions: View.OnClickListener?) {
        recycle = view.findViewById (R.id.rvCreditCardSettingCCS)
        btnAdd = view.findViewById(R.id.btnAddNewCCS)
        btnAdd.setOnClickListener(actions)
        btnCancel = view.findViewById(R.id.btnCancelLCCS)
        progressBar = view.findViewById(R.id.pb_load_lccs)
        btnCancel.setOnClickListener(actions)

        recycle.layoutManager = LinearLayoutManager(
            view.context,
            LinearLayoutManager.VERTICAL, false
        )
        progressBar.visibility = View.VISIBLE
    }

    override fun loadRecycler(data: MutableList<CreditCardSettingDTO>) {
        recycle.adapter = ListCreditCardSettingAdapter(data.toMutableList(),parentFragmentManager,findNavController)
        progressBar.visibility = View.GONE
    }

    override fun loadFields(fn: ((ListCreditCardSettingHolder) -> Unit)?) {
        fn?.invoke(this)
    }
}