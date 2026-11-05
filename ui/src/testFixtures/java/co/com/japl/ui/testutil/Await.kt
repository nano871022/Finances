package co.com.japl.ui.testutil

fun waitUntil(timeoutMs: Long = 5_000, condition: () -> Boolean) {
    val deadline = System.currentTimeMillis() + timeoutMs
    while (System.currentTimeMillis() < deadline) {
        if (condition()) return
        Thread.sleep(10)
    }
    throw AssertionError("Condition not met within ${timeoutMs}ms")
}
