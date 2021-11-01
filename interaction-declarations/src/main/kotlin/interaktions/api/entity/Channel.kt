package interaktions.api.entity

import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.Snowflake

public interface Channel {
    public val id: Snowflake

    public val type: ChannelType
}
