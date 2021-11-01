package interaktions.common.command.user

import interaktions.api.entity.Member
import interaktions.api.entity.User
import interaktions.common.command.interaction.InteractionCommandCondition
import interaktions.common.context.commands.ApplicationCommandContext

public fun interface UserCommandCondition : InteractionCommandCondition {
    public suspend fun execute(context: ApplicationCommandContext, targetUser: User, targetMember: Member?): Boolean
}
