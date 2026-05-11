package co.japl.finances.core.adapters.inbound.implement.common

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
        if (!prefs.llmEnabled) {
            return Result.failure(Exception("LLM is disabled"))
        }
        val type = LLMType.valueOf(prefs.llmType)
        return when (type) {
            LLMType.GEMINI -> geminiService.getAiResponse(prompt)
            LLMType.DEEPSEEK -> deepSeekService.getAiResponse(prompt)
        }
    }
}
