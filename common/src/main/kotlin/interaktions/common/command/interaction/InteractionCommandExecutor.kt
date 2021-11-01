package interaktions.common.command.interaction

/**
 * This is the class that should be inherited if you
 * want to create an Interaction Command.
 */
public abstract class InteractionCommandExecutor {
    /**
     * Used by the [net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandExecutorDeclaration] to match declarations to executors.
     *
     * By default, the class of the executor is used, but this may cause issues when using anonymous classes!
     *
     * To avoid this issue, you can replace the signature with another unique identifier
     */
    public open fun signature(): Any = this::class
}
