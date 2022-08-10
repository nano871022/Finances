package co.japl.android.myapplication.finanzas.controller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import co.japl.android.myapplication.BuildConfig
import co.japl.android.myapplication.R

class AboutIt : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_about_it, container, false)
        root.findViewById<TextView>(R.id.tvVersionName).text = BuildConfig.VERSION_NAME
        root.findViewById<TextView>(R.id.tvCodeVersion).text =BuildConfig.VERSION_CODE.toString()
        return root
    }
}