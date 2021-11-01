package net.perfectdreams.discordinteraktions.platform.jda.context.manager

import net.dv8tion.jda.api.interactions.components.ButtonInteraction
import interaktions.common.context.RequestBridge
import interaktions.common.utils.ActionRowComponent
import interaktions.common.utils.ButtonComponent
import interaktions.common.utils.InteractionMessage
import net.perfectdreams.discordinteraktions.platform.jda.utils.JDAConversionUtils
import net.perfectdreams.discordinteraktions.platform.jda.utils.await

class JDAButtonRequestManager(bridge: RequestBridge, private val interaction: ButtonInteraction) : JDARequestManager(bridge, interaction) {
    override suspend fun deferEdit(message: InteractionMessage?) {
        if (message != null) {
            interaction.deferEdit()
                .setContent(message.content)
                .await()
        }
    }
}
