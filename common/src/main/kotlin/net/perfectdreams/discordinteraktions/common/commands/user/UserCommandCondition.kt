package net.perfectdreams.discordinteraktions.common.commands.user

import net.perfectdreams.discordinteraktions.api.entities.Member
import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.context.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.commands.interaction.InteractionCommandCondition

fun interface UserCommandCondition : InteractionCommandCondition {
    suspend fun execute(context: ApplicationCommandContext, targetUser: User, targetMember: Member?): Boolean
}
