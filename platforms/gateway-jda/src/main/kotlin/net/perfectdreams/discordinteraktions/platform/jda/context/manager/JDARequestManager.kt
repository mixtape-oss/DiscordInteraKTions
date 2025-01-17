package net.perfectdreams.discordinteraktions.platform.jda.context.manager

import net.dv8tion.jda.api.interactions.Interaction
import interaktions.common.context.InteractionRequestState
import interaktions.common.context.RequestBridge
import interaktions.common.context.manager.RequestManager
import interaktions.common.entities.DummyMessage
import interaktions.common.entities.Message
import interaktions.common.utils.ActionRowComponent
import interaktions.common.utils.ButtonComponent
import interaktions.common.utils.InteractionMessage
import net.perfectdreams.discordinteraktions.platform.jda.utils.JDAConversionUtils
import net.perfectdreams.discordinteraktions.platform.jda.utils.await

open class JDARequestManager(bridge: RequestBridge, private val interaction: Interaction) : RequestManager(bridge) {
    override suspend fun deferReply(isEphemeral: Boolean) {
        val hook = interaction
            .deferReply(isEphemeral)
            .await()

        bridge.manager = JDAHookRequestManager(
            bridge,
            hook
        )

        bridge.state.value = InteractionRequestState.DEFERRED
    }

    override suspend fun deferEdit(message: InteractionMessage?) {
        throw UnsupportedOperationException("Can't defer a non button interaction!")
    }

    override suspend fun sendMessage(message: InteractionMessage): Message {
        val hook = interaction
            .reply(message.content)
            .setEphemeral(
                message.isEphemeral == true
            ) // TODO: Fix this, it works but it is kinda wonky
            .also {
                message.files?.forEach { (fileName, content) ->
                    it.addFile(content, fileName)
                }

                for (component in message.components) {
                    if (component is ButtonComponent)
                        it.addActionRow(JDAConversionUtils.convertButtonComponent(component))
                    else if (component is ActionRowComponent)
                        it.addActionRows(JDAConversionUtils.convertActionRowComponent(component))
                }
            }
            .await()

        bridge.manager = JDAHookRequestManager(
            bridge,
            hook
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        return DummyMessage()
    }
}
