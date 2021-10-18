package net.perfectdreams.discordinteraktions.declarations.commands.user

import net.perfectdreams.discordinteraktions.declarations.commands.UserCommandDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.application.ApplicationCommandDeclarationBuilder
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun userCommand(name: String, builder: UserCommandDeclarationBuilder.() -> Unit): UserCommandDeclaration {
    contract {
        callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
    }

    return UserCommandDeclarationBuilder(name)
        .apply(builder)
        .build()
}

class UserCommandDeclarationBuilder(val name: String) : ApplicationCommandDeclarationBuilder {
    var executor: UserCommandExecutorDeclaration? = null

    override fun build(): UserCommandDeclaration {
        require (executor != null) { "An executor must be provided" }

        return UserCommandDeclaration(name, executor!!)
    }
}
