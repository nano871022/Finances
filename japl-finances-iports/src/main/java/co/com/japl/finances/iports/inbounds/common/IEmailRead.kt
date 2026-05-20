package co.com.japl.finances.iports.inbounds.common

interface IEmailRead {
    fun getEmails(sender: String, subject: String): List<String>
}