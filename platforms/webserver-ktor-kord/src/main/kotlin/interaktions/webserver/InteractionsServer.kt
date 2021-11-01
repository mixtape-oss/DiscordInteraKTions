package interaktions.webserver

import dev.kord.common.entity.Snowflake
import dev.kord.rest.service.RestClient
import interaktions.common.command.InteraKTions
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.serialization.json.Json
import mu.KotlinLogging

/**
 * Class represents an interactions Server, which connects
 * to the Discord API and wrap your requests.
 *
 * @param applicationId Your bot ID/Client ID (https://i.imgur.com/075OBWk.png)
 * @param publicKey The public key of your bot (https://i.imgur.com/xDZnJ5J.png)
 * @param token Your bot token (https://i.imgur.com/VXLOFte.png)
 * @param port HTTP server port to bind
 */
class InteractionsServer(
    val applicationId: Long,
    val publicKey: String,
    val token: String,
    val port: Int = 12212,
    val engineFactory: ApplicationEngineFactory<*, *> = Netty
) {
    companion object {
        val json = Json {
            // If there are any unknown keys, we'll ignore them instead of throwing an exception.
            ignoreUnknownKeys = true
        }

        private val logger = KotlinLogging.logger {}
    }

    val rest = RestClient(token)
    val interaKTions = InteraKTions()
    val interactionRequestHandler: InteractionRequestHandler = DefaultInteractionRequestHandler(
        Snowflake(applicationId),
        rest,
        interaKTions,
    )

    /**
     * You can use this method to start the interactions' server,
     * which will open a connection on the 12212 port with the **Netty** engine.
     */
    fun start() {
        val server = embeddedServer(engineFactory, port = port) {
            routing {
                get("/") {
                    call.respondText("Hello, Discord Interactions!")
                }

                installDiscordInteractions(
                    publicKey,
                    "/",
                    interactionRequestHandler
                )
            }
        }

        server.start(true)
    }
}
