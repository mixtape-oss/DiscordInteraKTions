package interaktions.api.entity

import dev.kord.common.entity.DiscordRoleTags
import dev.kord.common.entity.Permissions
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.Optional

public interface Role {
	// id=Snowflake(value=297051132793061378), name=new role, color=15277667, hoist=false, position=60, permissions=Permissions(values=DiscordBitSet(100100100111101111101100011000011)), managed=false, mentionable=true, tags=Optional.Missing)}
	public val id: Snowflake
	public val name: String
	public val color: Int
	public val hoist: Boolean
	public val position: Int
	public val permissions: Permissions
	public val managed: Boolean
	public val mentionable: Boolean
	public val tags: Optional<DiscordRoleTags>
}
