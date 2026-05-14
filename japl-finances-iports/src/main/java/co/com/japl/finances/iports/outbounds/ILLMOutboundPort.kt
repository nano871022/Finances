package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.LLMConfigDTO

interface ILLMOutboundPort {
    suspend fun getAiResponse(config: LLMConfigDTO, prompt: String): Result<String>
    suspend fun getModels(config: LLMConfigDTO): Result<List<String>>
}
