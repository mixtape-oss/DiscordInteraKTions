package interaktions.declaration.command.message

import interaktions.declaration.command.MessageCommandDeclaration
import interaktions.declaration.command.application.ApplicationCommandDeclarationBuilder
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
public fun messageCommand(name: String, builder: MessageCommandDeclarationBuilder.() -> Unit): MessageCommandDeclaration {
    contract {
        callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
    }

    return MessageCommandDeclarationBuilder(name)
        .apply(builder)
        .build()
}

public class MessageCommandDeclarationBuilder(public val name: String) : ApplicationCommandDeclarationBuilder {
    public var executor: MessageCommandExecutorDeclaration? = null

    override fun build(): MessageCommandDeclaration {
        return MessageCommandDeclaration(
            name,
            executor ?: throw IllegalArgumentException("Executor must not be null"),
        )
    }
}
