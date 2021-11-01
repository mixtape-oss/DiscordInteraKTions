package interaktions.api.entity

import dev.kord.common.entity.Snowflake

public interface User {
    public val id: Snowflake
    public val name: String
    public val discriminator: String
    public val avatar: UserAvatar
    public val bot: Boolean
}
