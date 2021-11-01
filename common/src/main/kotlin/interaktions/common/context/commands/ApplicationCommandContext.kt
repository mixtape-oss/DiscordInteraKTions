package interaktions.common.context.commands

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import interaktions.api.entity.User
import interaktions.common.context.InteractionContext
import interaktions.common.context.RequestBridge
import interaktions.common.interaction.InteractionData

public open class ApplicationCommandContext(
    bridge: RequestBridge,
    sender: User,
    channelId: Snowflake,
    data: InteractionData,
    handle: DiscordInteraction
) : InteractionContext(bridge, sender, channelId, data, handle)
