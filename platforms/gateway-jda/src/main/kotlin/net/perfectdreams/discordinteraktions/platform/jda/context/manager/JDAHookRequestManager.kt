package net.perfectdreams.discordinteraktions.platform.jda.context.manager

import net.dv8tion.jda.api.interactions.InteractionHook
import interaktions.common.context.RequestBridge
import interaktions.common.context.manager.RequestManager
import interaktions.common.entities.DummyMessage
import interaktions.common.entities.Message
import interaktions.common.utils.ActionRowComponent
import interaktions.common.utils.ButtonComponent
import interaktions.common.utils.InteractionMessage
import net.perfectdreams.discordinteraktions.platform.jda.utils.JDAConversionUtils
import net.perfectdreams.discordinteraktions.platform.jda.utils.await

class JDAHookRequestManager(bridge: RequestBridge, private val hook: InteractionHook) : RequestManager(bridge) {
    override suspend fun deferReply(isEphemeral: Boolean) = throw UnsupportedOperationException("Can't defer a interaction that was already deferred!")
    override suspend fun deferEdit(message: InteractionMessage?) = throw UnsupportedOperationException("Can't defer a interaction that was already deferred!")

    override suspend fun sendMessage(message: InteractionMessage): Message {
        hook.sendMessage(message.content)
            .setEphemeral(message.isEphemeral == true) // TODO: Fix this
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
        return DummyMessage()
    }
}
