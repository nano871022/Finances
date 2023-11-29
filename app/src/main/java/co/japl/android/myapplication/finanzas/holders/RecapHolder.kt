package co.japl.android.myapplication.finanzas.holders

import android.view.View
import android.widget.HorizontalScrollView
import android.widget.TextView
import co.japl.android.graphs.drawer.CustomDraw
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.holders.interfaces.IRecapHolder

class RecapHolder(val view:View):IRecapHolder<RecapHolder> {

    var graph: CustomDraw? = null

    override fun setFields(actions: View.OnClickListener?) {
        graph = view.findViewById(R.id.cv_canvas_rec)

    }

    override fun loadFields(fn: ((RecapHolder) -> Unit)?) {
        fn?.let {
            it.invoke(this)
        }
    }

}