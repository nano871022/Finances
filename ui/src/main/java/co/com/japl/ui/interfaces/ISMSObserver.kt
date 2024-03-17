package co.com.japl.ui.interfaces

interface ISMSObserver {

    fun notify(message: String)

    /*
    * Number of the phone to check if it receive a sms
    * */
    fun getNumber():String
}