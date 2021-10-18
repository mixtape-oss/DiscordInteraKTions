package net.perfectdreams.discordinteraktions.declarations.commands.message

import net.perfectdreams.discordinteraktions.declarations.commands.MessageCommandDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.application.ApplicationCommandDeclarationBuilder
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun messageCommand(name: String, builder: MessageCommandDeclarationBuilder.() -> Unit): MessageCommandDeclaration {
    contract {
        callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
    }

    return MessageCommandDeclarationBuilder(name)
        .apply(builder)
        .build()
}

class MessageCommandDeclarationBuilder(val name: String) : ApplicationCommandDeclarationBuilder {
    var executor: MessageCommandExecutorDeclaration? = null

    override fun build(): MessageCommandDeclaration {
        return MessageCommandDeclaration(
            name,
            executor ?: throw IllegalArgumentException("Executor must not be null"),
        )
    }
}
