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
import co.japl.android.myapplication.finanzas.holders.ProjectionsHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.IRecapHolder
import co.japl.android.myapplication.finanzas.putParams.ProjectionsParams
import java.time.LocalDate

class ProjectionsFragment : Fragment(),OnClickListener {
    private lateinit var holder:IRecapHolder<ProjectionsHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_projections, container, false)
        holder = ProjectionsHolder(root)
        holder.setFields(this)
        holder.loadFields()
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_detail_pjs->ProjectionsParams.newInstanceList(LocalDate.now(),findNavController())
            R.id.btn_add_pjs->ProjectionsParams.newInstance(findNavController())
        }
    }
}