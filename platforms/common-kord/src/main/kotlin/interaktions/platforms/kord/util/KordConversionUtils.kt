package interaktions.platforms.kord.util

import dev.kord.common.entity.ResolvedObjects
import dev.kord.common.entity.optional.Optional
import interaktions.platforms.kord.entity.KordChannel
import interaktions.platforms.kord.entity.KordMember
import interaktions.platforms.kord.entity.KordRole
import interaktions.platforms.kord.entity.KordUser
import interaktions.platforms.kord.entity.messages.KordPublicMessage

/**
 * Converts Kord's Resolved Objects to DiscordInteraKTions's Resolved Objects
 */
public fun ResolvedObjects.toDiscordInteraKTionsResolvedObjects(): interaktions.common.interaction.ResolvedObjects {
    /* In this case, the user map contains the user object, so we need to get it from there */
    val users = this.users.value?.mapValues { KordUser(it.value) }
    val members = this.members.value?.mapValues { KordMember(it.value, users?.get(it.key)!!) }
    val messages = this.messages.value?.mapValues { KordPublicMessage(it.value) }
    val channels = this.channels.value?.mapValues { KordChannel(it.value) }
    val roles = this.roles.value?.mapValues { KordRole(it.value) }

    return interaktions.common.interaction.ResolvedObjects(
        users = users,
        members = members,
        messages = messages,
        channels = channels,
        roles = roles
    )
}

public fun <T> runIfNotMissing(optional: Optional<T>, callback: (T?) -> (Unit)) {
    if (optional !is Optional.Missing)
        callback.invoke(optional.value)
}
