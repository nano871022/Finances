package co.japl.android.myapplication.finanzas.putParams

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.com.japl.ui.utils.DateUtils
import java.time.LocalDate

class ProjectionsParams {
    object Params{
        val PARAM_DATE_PERIOD = "date_period"
        val PARAM_PROJECTION_ID = "id_projection"
    }

    companion object{

        @RequiresApi(Build.VERSION_CODES.O)
        fun newInstanceList(date:LocalDate, navController: NavController){
            val parameter = bundleOf(Params.PARAM_DATE_PERIOD to DateUtils.localDateToString(date))
            navController.navigate(R.id.action_menu_projections_to_listProjectFragment,parameter)
        }

        fun newInstance(navController: NavController){
            navController.navigate(R.id.action_menu_projections_to_projectionFragment)
        }

        fun newInstance(id:Long,navController: NavController){
            val parameter = bundleOf(Params.PARAM_PROJECTION_ID to id)
            navController.navigate(R.id.action_menu_projections_to_projectionFragment,parameter)
        }

        fun newInstanceFromList(id:Long,navController: NavController){
            val parameter = bundleOf(Params.PARAM_PROJECTION_ID to id)
            navController.navigate(R.id.action_listProjectFragment_to_projectionFragment,parameter)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun downloadList(parameters:Bundle):LocalDate?{
            return parameters.getString(Params.PARAM_DATE_PERIOD)?.let{
                DateUtils.toLocalDate(it)
            }

        }

        fun download(parameters:Bundle):Long = parameters.getLong(Params.PARAM_PROJECTION_ID,0)

        fun toBack(navController: NavController){
            navController.popBackStack()
        }

    }
}