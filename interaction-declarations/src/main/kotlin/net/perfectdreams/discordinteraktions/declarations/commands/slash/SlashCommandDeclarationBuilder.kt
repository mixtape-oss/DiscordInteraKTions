package net.perfectdreams.discordinteraktions.declarations.commands.slash

import net.perfectdreams.discordinteraktions.declarations.commands.SlashCommandDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.SlashCommandGroupDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.application.ApplicationCommandDeclarationBuilder
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun slashCommand(name: String, description: String, builder: SlashCommandDeclarationBuilder.() -> (Unit)): SlashCommandDeclaration {
    contract {
        callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
    }

    return SlashCommandDeclarationBuilder(name, description)
        .apply(builder)
        .build()
}

open class SlashCommandDeclarationBuilder(val name: String, val description: String) : ApplicationCommandDeclarationBuilder {
    var executor: SlashCommandExecutorDeclaration? = null
    val subcommands = mutableListOf<SlashCommandDeclaration>()
    val subcommandGroups = mutableListOf<SlashCommandGroupDeclaration>()

    fun subcommand(name: String, description: String, block: SlashCommandDeclarationBuilder.() -> (Unit)) {
        subcommands += SlashCommandDeclarationBuilder(name, description).apply(block)
            .build()
    }

    fun subcommandGroup(name: String, description: String, block: SlashCommandGroupDeclarationBuilder.() -> (Unit)) {
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

open class SlashCommandGroupDeclarationBuilder(val name: String, val description: String) {
    // Groups can't have executors!
    val subcommands = mutableListOf<SlashCommandDeclaration>()

    fun subcommand(name: String, description: String, block: SlashCommandDeclarationBuilder.() -> (Unit)) {
        subcommands += SlashCommandDeclarationBuilder(name, description).apply(block).build()
    }

    open fun build(): SlashCommandGroupDeclaration {
        return SlashCommandGroupDeclaration(
            name,
            description,
            subcommands
        )
    }
}
