package co.com.japl.ui.interfaces

interface ISMSObservablePublicher {

    fun notify(number:String,message: String)

    fun getNumbers():List<String>
}