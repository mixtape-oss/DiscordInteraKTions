package interaktions.platforms.kord.entity

import dev.kord.common.entity.DiscordRole
import dev.kord.common.entity.DiscordRoleTags
import dev.kord.common.entity.Permissions
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.Optional
import interaktions.api.entity.Role

public class KordRole(public val handle: DiscordRole) : Role {
    override val name: String by handle::name
    override val id: Snowflake by handle::id
    override val color: Int by handle::color
    override val hoist: Boolean by handle::hoist
    override val managed: Boolean by handle::managed
    override val mentionable: Boolean by handle::mentionable
    override val permissions: Permissions by handle::permissions
    override val position: Int by handle::position
    override val tags: Optional<DiscordRoleTags> by handle::tags
}
