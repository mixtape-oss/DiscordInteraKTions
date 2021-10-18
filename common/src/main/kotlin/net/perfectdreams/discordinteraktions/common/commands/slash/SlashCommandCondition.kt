package net.perfectdreams.discordinteraktions.common.commands.slash

import net.perfectdreams.discordinteraktions.common.context.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.commands.interaction.InteractionCommandCondition

public fun interface SlashCommandCondition : InteractionCommandCondition {
    public suspend fun execute(context: ApplicationCommandContext): Boolean
}
