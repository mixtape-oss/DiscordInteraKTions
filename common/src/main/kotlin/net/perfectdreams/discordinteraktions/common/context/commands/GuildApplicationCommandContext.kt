package net.perfectdreams.discordinteraktions.common.context.commands

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.Member
import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData

public open class GuildApplicationCommandContext(
    bridge: RequestBridge,
    sender: User,
    channelId: Snowflake,
    data: InteractionData,
    public val guildId: Snowflake,
    public val member: Member,
    interaction: DiscordInteraction
) : ApplicationCommandContext(bridge, sender, channelId, data, interaction)
