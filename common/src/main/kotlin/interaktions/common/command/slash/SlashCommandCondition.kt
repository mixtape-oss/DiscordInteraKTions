package interaktions.common.command.slash

import interaktions.common.command.interaction.InteractionCommandCondition
import interaktions.common.context.commands.ApplicationCommandContext

public fun interface SlashCommandCondition : InteractionCommandCondition {
    public suspend fun execute(context: ApplicationCommandContext): Boolean
}
