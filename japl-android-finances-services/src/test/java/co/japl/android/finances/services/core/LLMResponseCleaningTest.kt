package co.japl.android.finances.services.core

import org.junit.Assert.assertEquals
import org.junit.Test

class LLMResponseCleaningTest {

    private val adapter = LLMOutboundAdapter()

    @Test
    fun `test cleanResponse with simple text`() {
        val input = "This is a simple response"
        val expected = "This is a simple response"
        assertEquals(expected, adapter.cleanResponse(input))
    }

    @Test
    fun `test cleanResponse with think tags`() {
        val input = "<think>I should calculate the sum of 2 and 2</think>The answer is 4"
        val expected = "The answer is 4"
        assertEquals(expected, adapter.cleanResponse(input))
    }

    @Test
    fun `test cleanResponse with multiline think tags`() {
        val input = """
            <think>
            Analyzing the request...
            Done.
            </think>
            Final result.
        """.trimIndent()
        val expected = "Final result."
        assertEquals(expected, adapter.cleanResponse(input).trim())
    }

    @Test
    fun `test cleanResponse with thinking json - response key`() {
        val input = """{"thinking": "calculating...", "response": "The result is 42"}"""
        val expected = "The result is 42"
        assertEquals(expected, adapter.cleanResponse(input))
    }

    @Test
    fun `test cleanResponse with thinking json - content key`() {
        val input = """{"thinking": "calculating...", "content": "The result is 42"}"""
        val expected = "The result is 42"
        assertEquals(expected, adapter.cleanResponse(input))
    }

    @Test
    fun `test cleanResponse with thinking json - answer key`() {
        val input = """{"thought": "thinking...", "answer": "This is the answer"}"""
        val expected = "This is the answer"
        assertEquals(expected, adapter.cleanResponse(input))
    }

    @Test
    fun `test cleanResponse with nested json-like string in think tags`() {
        val input = "<think>{\"reasoning\": \"...\"}</think>Actual content"
        val expected = "Actual content"
        assertEquals(expected, adapter.cleanResponse(input))
    }
}
