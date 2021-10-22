package dev.chieppa.model.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import dev.chieppa.model.exception.ExceptionType
import dev.chieppa.model.recievables.MonitorState

@Serializable
enum class ResponseType {
    EMPTY,
    ARRAY,
    ERROR,
    SUCCESS,
    MESSAGE,

    MONITOR_STATE,
    SERVICE,
}

@Serializable
abstract class BasicResponse {
    abstract val type: ResponseType
}

@Serializable
data class ArrayResponse(
    override val type: ResponseType = ResponseType.ARRAY,
    val array: JsonArray
) : BasicResponse()

@Serializable
data class SuccessResponse(
    override val type: ResponseType = ResponseType.SUCCESS
) : BasicResponse()

@Serializable
data class InformationalResponse(
    override val type: ResponseType = ResponseType.MESSAGE,
    val message: String,
) : BasicResponse()

@Serializable
data class MonitorStateChanged(
    override val type: ResponseType = ResponseType.MONITOR_STATE,
    val state: MonitorState
) : BasicResponse()

@Serializable
data class ServiceComplete(
    override val type: ResponseType = ResponseType.SERVICE,
    val consoleOutput: String,
    val exitCode: Int
): BasicResponse()

@Serializable
data class ErrorResponse(
    override val type: ResponseType = ResponseType.ERROR,
    val errorType: ExceptionType,
    val error: JsonElement
) : BasicResponse()