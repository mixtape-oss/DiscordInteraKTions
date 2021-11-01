package interaktions.api.entity

import dev.kord.common.entity.Snowflake

public interface Member {
    public val user: User
    public val roles: List<Snowflake>
}
