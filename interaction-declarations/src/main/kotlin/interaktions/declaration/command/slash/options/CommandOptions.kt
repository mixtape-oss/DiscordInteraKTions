package interaktions.declaration.command.slash.options

import dev.kord.common.entity.Snowflake
import interaktions.api.entity.Channel
import interaktions.api.entity.Role
import interaktions.api.entity.User

public open class CommandOptions {
    public companion object {
        public val NO_OPTIONS: CommandOptions = object : CommandOptions() {}
    }

    public val arguments: MutableList<CommandOption<*>> = mutableListOf()

    public fun <T> argument(type: CommandOptionType, name: String, description: String): CommandOptionBuilder<T> =
        CommandOptionBuilder(type, name, description, mutableListOf())

    public fun <T> CommandOptionBuilder<T>.register(): CommandOption<T> {
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

/* strings */
public fun CommandOptions.string(name: String, description: String): CommandOptionBuilder<String> =
    argument(CommandOptionType.String, name, description)

public fun CommandOptions.optionalString(name: String, description: String): CommandOptionBuilder<String?> =
    argument(CommandOptionType.NullableString, name, description)

/* ints */
public fun CommandOptions.integer(name: String, description: String): CommandOptionBuilder<Long> =
    argument(CommandOptionType.Integer, name, description)

public fun CommandOptions.optionalInteger(name: String, description: String): CommandOptionBuilder<Long?> =
    argument(CommandOptionType.NullableInteger, name, description)

/* numbers */
public fun CommandOptions.number(name: String, description: String): CommandOptionBuilder<Double> =
    argument(CommandOptionType.Number, name, description)

public fun CommandOptions.optionalNumber(name: String, description: String): CommandOptionBuilder<Double?> =
    argument(CommandOptionType.NullableNumber, name, description)

/* booleans */
public fun CommandOptions.boolean(name: String, description: String): CommandOptionBuilder<Boolean> =
    argument(CommandOptionType.Bool, name, description)

public fun CommandOptions.optionalBoolean(name: String, description: String): CommandOptionBuilder<Boolean?> =
    argument(CommandOptionType.NullableBool, name, description)

/* users */
public fun CommandOptions.user(name: String, description: String): CommandOptionBuilder<User> =
    argument(CommandOptionType.User, name, description)

public fun CommandOptions.optionalUser(name: String, description: String): CommandOptionBuilder<User?> =
    argument(CommandOptionType.NullableUser, name, description)

/* channels */
public fun CommandOptions.channel(name: String, description: String): CommandOptionBuilder<Channel> =
    argument(CommandOptionType.Channel, name, description)

public fun CommandOptions.optionalChannel(name: String, description: String): CommandOptionBuilder<Channel?> =
    argument(CommandOptionType.NullableChannel, name, description)

/* roles */
public fun CommandOptions.role(name: String, description: String): CommandOptionBuilder<Role> =
    argument(CommandOptionType.Role, name, description)

public fun CommandOptions.optionalRole(name: String, description: String): CommandOptionBuilder<Role?> =
    argument(CommandOptionType.NullableRole, name, description)

/* mentionables */
public fun CommandOptions.mentionable(name: String, description: String): CommandOptionBuilder<Snowflake> =
    argument(CommandOptionType.Mentionable, name, description)

public fun CommandOptions.optionalMentionable(name: String, description: String): CommandOptionBuilder<Snowflake?> =
    argument(CommandOptionType.NullableMentionable, name, description)
