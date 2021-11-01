package interaktions.common.command.user

import interaktions.api.entity.Member
import interaktions.api.entity.User
import interaktions.common.command.interaction.InteractionCommandExecutor
import interaktions.common.context.commands.ApplicationCommandContext

/**
 * This is the class that should be inherited if you
 * want to create an User Command.
 */
public abstract class UserCommandExecutor : InteractionCommandExecutor() {
    public val conditions: MutableList<UserCommandCondition> = mutableListOf()

    public abstract suspend fun execute(context: ApplicationCommandContext, targetUser: User, targetMember: Member?)
}
