package interaktions.webserver

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.InteractionType
import interaktions.verifier.InteractionRequestVerifier
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.decodeFromStream

/**
 * Installs the Discord Interactions in the [path]
 * post requests to our defined route.
 *
 * @param publicKey The public key of your bot (https://i.imgur.com/xDZnJ5J.png)
 * @param path The location that we'll listing the requests.
 * @param handler The class that we used to handle these requests.
 *
 * @see InteractionsServer
 */
@OptIn(ExperimentalSerializationApi::class)
fun Routing.installDiscordInteractions(
    publicKey: String,
    path: String,
    handler: InteractionRequestHandler
) {
    val keyVerifier = InteractionRequestVerifier(publicKey)

    post(path) {
        val signature = call.request.header("X-Signature-Ed25519")!!
        val timestamp = call.request.header("X-Signature-Timestamp")!!

        val parse = Json.decodeFromStream<JsonObject>(call.receiveStream())
        val verified = keyVerifier.verifyKey(parse.toString(), signature, timestamp)

        if (!verified) {
            call.respondText("", ContentType.Application.Json, HttpStatusCode.Unauthorized)
            return@post
        }

        when (Json.decodeFromJsonElement<InteractionType>(parse["type"]!!)) {
            InteractionType.Ping ->
                handler.onPing(call)

            InteractionType.ApplicationCommand -> {
                // Kord still has some fields missing (like "deaf") so we need to decode ignoring missing fields
                val interaction = InteractionsServer.json.decodeFromJsonElement<DiscordInteraction>(parse)
                handler.onCommand(call, interaction)
            }

            InteractionType.Component -> {
                // Kord still has some fields missing (like "deaf") so we need to decode ignoring missing fields
                val interaction = InteractionsServer.json.decodeFromJsonElement<DiscordInteraction>(parse)
                handler.onComponent(call, interaction)
            }

            else -> {
            }
        }
    }
}
