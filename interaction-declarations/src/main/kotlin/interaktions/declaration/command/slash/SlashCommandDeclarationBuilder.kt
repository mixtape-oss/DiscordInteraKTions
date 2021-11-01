package interaktions.declaration.command.slash

import interaktions.declaration.command.SlashCommandDeclaration
import interaktions.declaration.command.SlashCommandGroupDeclaration
import interaktions.declaration.command.application.ApplicationCommandDeclarationBuilder
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
public fun slashCommand(name: String, description: String, builder: SlashCommandDeclarationBuilder.() -> (Unit)): SlashCommandDeclaration {
    contract {
        callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
    }

    return SlashCommandDeclarationBuilder(name, description)
        .apply(builder)
        .build()
}

public open class SlashCommandDeclarationBuilder(public val name: String, public val description: String) : ApplicationCommandDeclarationBuilder {
    public var executor: SlashCommandExecutorDeclaration? = null
    public val subcommands: MutableList<SlashCommandDeclaration> = mutableListOf()
    public val subcommandGroups: MutableList<SlashCommandGroupDeclaration> = mutableListOf()

    public fun subcommand(name: String, description: String, block: SlashCommandDeclarationBuilder.() -> (Unit)) {
        subcommands += SlashCommandDeclarationBuilder(name, description).apply(block)
            .build()
    }

    public fun subcommandGroup(name: String, description: String, block: SlashCommandGroupDeclarationBuilder.() -> (Unit)) {
        subcommandGroups += SlashCommandGroupDeclarationBuilder(name, description).apply(block)
            .build()
    }

    override fun build(): SlashCommandDeclaration {
        return SlashCommandDeclaration(
            name,
            description,
            executor,
            subcommands,
            subcommandGroups,
        )
    }
}

public open class SlashCommandGroupDeclarationBuilder(public val name: String, public val description: String) {
    // Groups can't have executors!
    public val subcommands: MutableList<SlashCommandDeclaration> = mutableListOf()

    public fun subcommand(name: String, description: String, block: SlashCommandDeclarationBuilder.() -> (Unit)) {
        subcommands += SlashCommandDeclarationBuilder(name, description).apply(block).build()
    }

    public open fun build(): SlashCommandGroupDeclaration {
        return SlashCommandGroupDeclaration(
            name,
            description,
            subcommands
        )
    }
}
