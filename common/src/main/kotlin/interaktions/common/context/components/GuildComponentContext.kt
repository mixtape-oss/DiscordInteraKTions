package interaktions.common.context.components

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import interaktions.api.entity.Member
import interaktions.api.entity.User
import interaktions.common.context.RequestBridge
import interaktions.common.entity.message.Message
import interaktions.common.interaction.InteractionData

public open class GuildComponentContext(
    bridge: RequestBridge,
    sender: User,
    channelId: Snowflake,
    message: Message,
    data: InteractionData,
    public val guildId: Snowflake,
    public val member: Member,
    interaction: DiscordInteraction
) : ComponentContext(bridge, sender, channelId, message, data, interaction)
