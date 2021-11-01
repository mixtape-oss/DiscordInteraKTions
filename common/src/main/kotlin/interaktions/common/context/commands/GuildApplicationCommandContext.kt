package interaktions.common.context.commands

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import interaktions.api.entity.Member
import interaktions.api.entity.User
import interaktions.common.context.RequestBridge
import interaktions.common.interaction.InteractionData

public open class GuildApplicationCommandContext(
    bridge: RequestBridge,
    sender: User,
    channelId: Snowflake,
    data: InteractionData,
    public val guildId: Snowflake,
    public val member: Member,
    handle: DiscordInteraction
) : ApplicationCommandContext(bridge, sender, channelId, data, handle)
