package co.japl.finances.core.usercases.implement.common

import co.com.japl.finances.iports.dtos.LLMConfigDTO
import co.com.japl.finances.iports.enums.LLMType
import co.com.japl.finances.iports.inbounds.common.ILLMService
import co.com.japl.finances.iports.outbounds.ILLMOutboundPort
import co.com.japl.ui.Prefs
import javax.inject.Inject

class GeminiService @Inject constructor(
    private val prefs: Prefs,
    private val llmOutboundPort: ILLMOutboundPort
) : ILLMService {

    override suspend fun getAiResponse(prompt: String): Result<String> {
        val config = LLMConfigDTO(
            type = LLMType.GEMINI,
            apiKey = prefs.llmApiKey,
            model = prefs.llmModel.ifEmpty { "gemini-1.5-flash" },
            url = prefs.llmGeminiUrl
        )
        return llmOutboundPort.getAiResponse(config, prompt)
    }

    override suspend fun getModels(): Result<List<String>> {
        val config = LLMConfigDTO(
            type = LLMType.GEMINI,
            apiKey = prefs.llmApiKey,
            url = prefs.llmGeminiUrl
        )
        return llmOutboundPort.getModels(config)
    }
}
