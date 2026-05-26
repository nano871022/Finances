package co.com.japl.finances.iports.inbounds.common

import java.time.LocalDateTime

interface IEmailRead {
    fun getEmails(sender: String, subject: String, numDaysRead: Int): List<Pair<String, LocalDateTime>>
}