package co.japl.android.myapplication.finanzas.controller

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidsPOJO
import co.japl.android.myapplication.finanzas.bussiness.impl.PaidImpl
import co.japl.android.myapplication.finanzas.holders.PaidsHolder
import co.japl.android.myapplication.finanzas.putParams.PaidsParams
import java.time.LocalDate

class PaidsFragment : Fragment(), OnClickListener {
    private lateinit var holder: IHolder<PaidsPOJO>
    private lateinit var service:SaveSvc<PaidDTO>
    @RequiresApi(Build.VERSION_CODES.O)
    private val date = LocalDate.now().withDayOfMonth(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_paids, container, false)
        service = PaidImpl(ConnectDB(root.context))
        holder = PaidsHolder(root)
        holder.setFields(this)
        holder.loadFields(getPaids())
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPaids():PaidsPOJO{
        return (service as PaidImpl).getPaids(date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_add_ps->PaidsParams.newInstance(findNavController())
            R.id.btn_detail_ps->PaidsParams.newInstanceList(date,findNavController())
            R.id.btn_periods_ps->PaidsParams.newInstancePeriods(findNavController())
        }
    }

}