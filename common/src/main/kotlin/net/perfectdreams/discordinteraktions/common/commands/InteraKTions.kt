package net.perfectdreams.discordinteraktions.common.commands

import kotlinx.coroutines.*
import net.perfectdreams.discordinteraktions.common.commands.interaction.InteractionCommandExecutor
import net.perfectdreams.discordinteraktions.common.components.buttons.ButtonClickExecutor
import net.perfectdreams.discordinteraktions.common.components.buttons.ButtonClickExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.components.selects.SelectMenuExecutor
import net.perfectdreams.discordinteraktions.common.components.selects.SelectMenuExecutorDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.InteractionCommandDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.wrappers.InteractionCommandDeclarationWrapper
import kotlin.coroutines.CoroutineContext

public open class InteraKTions(private val dispatcher: CoroutineDispatcher = Dispatchers.Default) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = dispatcher + SupervisorJob() + CoroutineName("Discord InteraKTions Command Manager")

    /* commands */
    public val commandDeclarations: MutableList<InteractionCommandDeclaration> = mutableListOf()
    public val commandExecutors: MutableList<InteractionCommandExecutor> = mutableListOf()

    /* components */
    public val buttonDeclarations: MutableList<ButtonClickExecutorDeclaration> = mutableListOf()
    public val buttonExecutors: MutableList<ButtonClickExecutor> = mutableListOf()

    public val selectMenusDeclarations: MutableList<SelectMenuExecutorDeclaration> = mutableListOf()
    public val selectMenusExecutors: MutableList<SelectMenuExecutor> = mutableListOf()

    /* registering tings */
    public fun register(
        declarationWrapper: InteractionCommandDeclarationWrapper,
        vararg executors: InteractionCommandExecutor
    ) {
        val declaration = declarationWrapper.declaration()

        if (commandDeclarations.any { it.name == declaration.name })
            error("There's already a root command registered with the label ${declaration.name}!")

        commandDeclarations.add(declaration)
        this.commandExecutors.addAll(executors)
    }

    public fun register(declaration: ButtonClickExecutorDeclaration, executor: ButtonClickExecutor) {
        buttonDeclarations.add(declaration)
        buttonExecutors.add(executor)
    }

    public fun register(declaration: SelectMenuExecutorDeclaration, executor: SelectMenuExecutor) {
        selectMenusDeclarations.add(declaration)
        selectMenusExecutors.add(executor)
    }
}
