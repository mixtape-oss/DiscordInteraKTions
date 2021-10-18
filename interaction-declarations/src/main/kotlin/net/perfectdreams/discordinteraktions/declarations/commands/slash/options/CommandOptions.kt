package net.perfectdreams.discordinteraktions.declarations.commands.slash.options

import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.Channel
import net.perfectdreams.discordinteraktions.api.entities.Role
import net.perfectdreams.discordinteraktions.api.entities.User

open class CommandOptions {
    companion object {
        val NO_OPTIONS = object : CommandOptions() {}
    }

    val arguments = mutableListOf<CommandOption<*>>()

    private fun <T> argument(type: CommandOptionType, name: String, description: String) =
        CommandOptionBuilder<T>(type, name, description, mutableListOf())

    /* strings */
    fun string(name: String, description: String) =
        argument<String>(CommandOptionType.String, name, description)

    fun optionalString(name: String, description: String) =
        argument<String?>(CommandOptionType.NullableString, name, description)

    /* ints */
    fun integer(name: String, description: String) =
        argument<Long>(CommandOptionType.Integer, name, description)

    fun optionalInteger(name: String, description: String) =
        argument<Long?>(CommandOptionType.NullableInteger, name, description)

    /* numbers */
    fun number(name: String, description: String) =
        argument<Double>(CommandOptionType.Number, name, description)

    fun optionalNumber(name: String, description: String) =
        argument<Double?>(CommandOptionType.NullableNumber, name, description)

    /* booleans */
    fun boolean(name: String, description: String) =
        argument<Boolean>(CommandOptionType.Bool, name, description)

    fun optionalBoolean(name: String, description: String) =
        argument<Boolean?>(CommandOptionType.NullableBool, name, description)

    /* users */
    fun user(name: String, description: String) =
        argument<User>(CommandOptionType.User, name, description)

    fun optionalUser(name: String, description: String) =
        argument<User?>(CommandOptionType.NullableUser, name, description)

    /* channels */
    fun channel(name: String, description: String) =
        argument<Channel>(CommandOptionType.Channel, name, description)

    fun optionalChannel(name: String, description: String) =
        argument<Channel?>(CommandOptionType.NullableChannel, name, description)

    /* roles */
    fun role(name: String, description: String) =
        argument<Role>(CommandOptionType.Role, name, description)

    fun optionalRole(name: String, description: String) =
        argument<Role?>(CommandOptionType.NullableRole, name, description)

    /* mentionables */
    fun mentionable(name: String, description: String) =
        argument<Snowflake?>(CommandOptionType.Mentionable, name, description)

    fun optionalMentionable(name: String, description: String) =
        argument<Snowflake?>(CommandOptionType.NullableMentionable, name, description)

    fun <T> CommandOptionBuilder<T>.register(): CommandOption<T> {
        if (arguments.any { it.name == this.name })
            throw IllegalArgumentException("Duplicate argument!")

        val option = CommandOption(
            this.type,
            this.name,
            this.description,
            this.choices
        )

        arguments.add(option)
        return option
    }
}
