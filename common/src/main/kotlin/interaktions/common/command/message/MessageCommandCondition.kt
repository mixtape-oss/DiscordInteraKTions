package interaktions.common.command.message

import interaktions.common.command.interaction.InteractionCommandCondition
import interaktions.common.context.commands.ApplicationCommandContext
import interaktions.common.entity.message.Message

public fun interface MessageCommandCondition : InteractionCommandCondition {
    public suspend fun execute(context: ApplicationCommandContext, targetMessage: Message): Boolean
}
