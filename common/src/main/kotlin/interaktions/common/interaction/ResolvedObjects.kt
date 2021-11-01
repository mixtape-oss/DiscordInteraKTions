package interaktions.common.interaction

import dev.kord.common.entity.Snowflake
import interaktions.api.entity.Channel
import interaktions.api.entity.Member
import interaktions.api.entity.Role
import interaktions.api.entity.User
import interaktions.common.entity.message.Message

public data class ResolvedObjects(
    public val users: Map<Snowflake, User>?,
    public val members: Map<Snowflake, Member>?,
    public val messages: Map<Snowflake, Message>?,
    public val channels: Map<Snowflake, Channel>?,
    public val roles: Map<Snowflake, Role>?,
)
