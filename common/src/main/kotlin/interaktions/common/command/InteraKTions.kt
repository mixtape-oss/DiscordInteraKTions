package interaktions.common.command

import interaktions.common.command.interaction.InteractionCommandExecutor
import interaktions.common.component.buttons.ButtonClickExecutor
import interaktions.common.component.buttons.ButtonClickExecutorDeclaration
import interaktions.common.component.selects.SelectMenuExecutor
import interaktions.common.component.selects.SelectMenuExecutorDeclaration
import interaktions.common.event.Event
import interaktions.declaration.command.InteractionCommandDeclaration
import interaktions.declaration.command.wrappers.InteractionCommandDeclarationWrapper
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.*
import mu.KLogger
import mu.KotlinLogging
import kotlin.coroutines.CoroutineContext

public open class InteraKTions(private val dispatcher: CoroutineDispatcher = Dispatchers.Default) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = dispatcher + SupervisorJob() + CoroutineName("Discord InteraKTions Command Manager")

    /* events */
    private val eventFlow = MutableSharedFlow<Event>(extraBufferCapacity = Int.MAX_VALUE)

    public val events: SharedFlow<Event>
        get() = eventFlow

    /* commands */
    public val commandDeclarations: MutableList<InteractionCommandDeclaration> = mutableListOf()
    public val commandExecutors: MutableList<InteractionCommandExecutor> = mutableListOf()

    /* components */
    public val buttonDeclarations: MutableList<ButtonClickExecutorDeclaration> = mutableListOf()
    public val buttonExecutors: MutableList<ButtonClickExecutor> = mutableListOf()

    public val selectMenusDeclarations: MutableList<SelectMenuExecutorDeclaration> = mutableListOf()
    public val selectMenusExecutors: MutableList<SelectMenuExecutor> = mutableListOf()

    public suspend fun publishEvent(event: Event) {
        eventFlow.emit(event)
    }

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

@PublishedApi
internal val interaKtionsOnLogger: KLogger = KotlinLogging.logger("InteraKTions.on")

public suspend inline fun <reified T: Event> InteraKTions.on(scope: CoroutineScope = this, noinline block: suspend T.() -> Unit): Job {
    return events
        .buffer(UNLIMITED)
        .filterIsInstance<T>()
        .onEach { event ->
            event
                .runCatching { block() }
                .onFailure { interaKtionsOnLogger.error(it) { "" } }
        }
        .launchIn(scope)
}
