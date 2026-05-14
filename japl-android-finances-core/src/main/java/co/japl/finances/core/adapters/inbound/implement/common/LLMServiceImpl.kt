package co.japl.finances.core.adapters.inbound.implement.common

import android.util.Log
import co.com.japl.finances.iports.inbounds.common.ILLMService
import co.com.japl.finances.iports.enums.LLMType
import co.com.japl.ui.Prefs
import co.japl.finances.core.usercases.implement.common.GeminiService
import co.japl.finances.core.usercases.implement.common.DeepSeekService
import javax.inject.Inject

class LLMServiceImpl @Inject constructor(
    private val prefs: Prefs,
    private val geminiService: GeminiService,
    private val deepSeekService: DeepSeekService
) : ILLMService {

    override suspend fun getAiResponse(prompt: String): Result<String> {
        Log.d(this::javaClass.name, "getAiResponse: $prompt")
        if (!prefs.llmEnabled) {
            return Result.failure(Exception("LLM is disabled"))
        }
        val type = try { LLMType.valueOf(prefs.llmType) } catch (e: Exception) { LLMType.GEMINI }
        return when (type) {
            LLMType.GEMINI -> geminiService.getAiResponse(prompt)
            LLMType.DEEPSEEK -> deepSeekService.getAiResponse(prompt)
        }
    }

    override suspend fun getModels(): Result<List<String>> {
        val type = try { LLMType.valueOf(prefs.llmType) } catch (e: Exception) { LLMType.GEMINI }
        return when (type) {
            LLMType.GEMINI -> geminiService.getModels()
            LLMType.DEEPSEEK -> deepSeekService.getModels()
        }
    }
}
