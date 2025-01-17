package interaktions.declaration.command.user

import interaktions.declaration.command.UserCommandDeclaration
import interaktions.declaration.command.application.ApplicationCommandDeclarationBuilder
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
public fun userCommand(name: String, builder: UserCommandDeclarationBuilder.() -> Unit): UserCommandDeclaration {
    contract {
        callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
    }

    return UserCommandDeclarationBuilder(name)
        .apply(builder)
        .build()
}

public class UserCommandDeclarationBuilder(public val name: String) : ApplicationCommandDeclarationBuilder {
    public var executor: UserCommandExecutorDeclaration? = null

    override fun build(): UserCommandDeclaration {
        require (executor != null) { "An executor must be provided" }

        return UserCommandDeclaration(name, executor!!)
    }
}
