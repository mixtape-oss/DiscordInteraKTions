package interaktions.platforms.kord

import dev.kord.common.entity.InteractionType
import dev.kord.common.entity.Snowflake
import dev.kord.gateway.Gateway
import dev.kord.gateway.InteractionCreate
import dev.kord.gateway.on
import dev.kord.rest.service.RestClient
import interaktions.common.command.InteraKTions
import interaktions.common.context.InteractionRequestState
import interaktions.common.context.RequestBridge
import interaktions.common.util.Observable
import interaktions.platforms.kord.context.manager.InitialHttpRequestManager
import interaktions.platforms.kord.util.KordCommandExecutor
import interaktions.platforms.kord.util.KordComponentExecutor

public fun Gateway.installDiscordInteraKTions(applicationId: Snowflake, rest: RestClient, interaKTions: InteraKTions) {
    val kordCommandExecutor = KordCommandExecutor(interaKTions)
    val kordComponentExecutor = KordComponentExecutor(interaKTions)

    on<InteractionCreate> {
        val request = this.interaction

        val observableState = Observable(InteractionRequestState.NOT_REPLIED_YET)
        val bridge = RequestBridge(observableState)
        val requestManager = InitialHttpRequestManager(
            bridge,
            rest,
            applicationId,
            request.token,
            request
        )

        bridge.manager = requestManager

        when (request.type) {
            InteractionType.ApplicationCommand ->
                kordCommandExecutor.handle(request, requestManager)

            InteractionType.Component ->
                kordComponentExecutor.handle(request, requestManager)

            else -> {}
        }
    }
}
