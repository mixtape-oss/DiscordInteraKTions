package net.perfectdreams.discordinteraktions.platforms.kord.entities

import dev.kord.common.entity.DiscordRole
import dev.kord.common.entity.DiscordRoleTags
import dev.kord.common.entity.Permissions
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.Optional
import net.perfectdreams.discordinteraktions.api.entities.Role

class KordRole(val handle: DiscordRole) : Role {
    override val name by handle::name
    override val id by handle::id
    override val color by handle::color
    override val hoist by handle::hoist
    override val managed by handle::managed
    override val mentionable by handle::mentionable
    override val permissions by handle::permissions
    override val position by handle::position
    override val tags by handle::tags
}
