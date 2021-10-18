package net.perfectdreams.discordinteraktions.common.commands.message

import net.perfectdreams.discordinteraktions.common.context.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.entities.messages.Message
import net.perfectdreams.discordinteraktions.common.commands.interaction.InteractionCommandCondition

public fun interface MessageCommandCondition : InteractionCommandCondition {
    public suspend fun execute(context: ApplicationCommandContext, targetMessage: Message): Boolean
}
