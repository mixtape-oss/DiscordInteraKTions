package net.perfectdreams.discordinteraktions.common.commands.slash

import net.perfectdreams.discordinteraktions.common.context.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.commands.interaction.InteractionCommandCondition

fun interface SlashCommandCondition : InteractionCommandCondition {
    suspend fun execute(context: ApplicationCommandContext): Boolean
}
