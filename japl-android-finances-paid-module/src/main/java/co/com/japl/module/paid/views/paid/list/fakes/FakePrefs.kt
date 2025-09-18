package co.com.japl.module.paid.views.paid.list.fakes

import co.com.japl.ui.Prefs

class FakePrefs : Prefs(null) {
    override fun maches(value: String, paterns: Array<String>): Boolean {
        return true
    }
}
