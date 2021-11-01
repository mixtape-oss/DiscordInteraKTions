package interaktions.platforms.kord.entity

import dev.kord.common.entity.DiscordGuild
import dev.kord.common.entity.Snowflake
import interaktions.api.entity.Guild

public class KordGuild(public val handle: DiscordGuild) : Guild {
    override val id: Snowflake by handle::id
}
