package interaktions.platforms.kord.entity

import dev.kord.common.entity.DiscordGuildMember
import dev.kord.common.entity.Snowflake
import interaktions.api.entity.Member
import interaktions.api.entity.User

public class KordMember(
    public val handle: DiscordGuildMember,
    override val user: User // The user object is here too because sometimes the handle user value may be null!
) : Member {
    override val roles: List<Snowflake> by handle::roles
}
