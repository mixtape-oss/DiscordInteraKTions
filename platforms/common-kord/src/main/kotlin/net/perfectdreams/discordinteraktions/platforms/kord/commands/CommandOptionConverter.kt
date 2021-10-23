package net.perfectdreams.discordinteraktions.platforms.kord.commands

import dev.kord.common.entity.CommandArgument
import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.interaction.*
import net.perfectdreams.discordinteraktions.declarations.commands.slash.options.CommandOption
import net.perfectdreams.discordinteraktions.declarations.commands.slash.options.CommandOptionType
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordChannel
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordRole
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordUser

public var optionConverters: MutableMap<CommandOptionType, CommandOptionConverter> = mutableMapOf<CommandOptionType, CommandOptionConverter>()
    .applyDefaults()

public interface CommandOptionConverter {
    public fun convert(builder: BaseInputChatBuilder, option: CommandOption<*>)

    public fun convert(option: CommandOption<*>, argument: CommandArgument<*>, interaction: DiscordInteraction): Any? {
        return argument.value
    }

    public object Integer : CommandOptionConverter {
        override fun convert(builder: BaseInputChatBuilder, option: CommandOption<*>) {
            builder.int(option.name, option.description) {
                required = !option.type.isNullable
                for (choice in option.choices) choice(choice.name, choice.value as Long)
            }
        }
    }

    public object Number : CommandOptionConverter {
        public override fun convert(builder: BaseInputChatBuilder, option: CommandOption<*>) {
            builder.number(option.name, option.description) {
                required = !option.type.isNullable
                for (choice in option.choices) choice(choice.name, choice.value as Double)
            }
        }
    }

    public object String : CommandOptionConverter {
        public override fun convert(builder: BaseInputChatBuilder, option: CommandOption<*>) {
            builder.string(option.name, option.description) {
                required = !option.type.isNullable
                for (choice in option.choices) {
                    choice(choice.name, choice.value as kotlin.String)
                }
            }
        }
    }

    public object Bool : CommandOptionConverter {
        public override fun convert(builder: BaseInputChatBuilder, option: CommandOption<*>) {
            builder.boolean(option.name, option.description) { required = !option.type.isNullable }
        }
    }

    public object User : CommandOptionConverter {
        public override fun convert(builder: BaseInputChatBuilder, option: CommandOption<*>) {
            builder.user(option.name, option.description) { required = !option.type.isNullable }
        }

        override fun convert(
            option: CommandOption<*>,
            argument: CommandArgument<*>,
            interaction: DiscordInteraction
        ): Any? {
            val userId = argument.value as Snowflake

            val resolved = interaction.data.resolved.value ?: return null
            val resolvedMap = resolved.users.value ?: return null
            val kordInstance = resolvedMap[userId] ?: return null

            /* Now we need to wrap the kord user in our own implementation! */
            return KordUser(kordInstance)
        }
    }

    public object Channel : CommandOptionConverter {
        public override fun convert(builder: BaseInputChatBuilder, option: CommandOption<*>) {
            builder.channel(option.name, option.description) { required = !option.type.isNullable }
        }

        override fun convert(
            option: CommandOption<*>,
            argument: CommandArgument<*>,
            interaction: DiscordInteraction
        ): Any? {
            val userId = argument.value as Snowflake

            val resolved = interaction.data.resolved.value ?: return null
            val resolvedMap = resolved.channels.value ?: return null
            val kordInstance = resolvedMap[userId] ?: return null

            /* Now we need to wrap the kord user in our own implementation! */
            return KordChannel(kordInstance)
        }
    }

    public object Role : CommandOptionConverter {
        public override fun convert(builder: BaseInputChatBuilder, option: CommandOption<*>) {
            builder.role(option.name, option.description) { required = !option.type.isNullable }
        }

        override fun convert(
            option: CommandOption<*>,
            argument: CommandArgument<*>,
            interaction: DiscordInteraction
        ): Any? {
            val userId = argument.value as Snowflake

            val resolved = interaction.data.resolved.value ?: return null
            val resolvedMap = resolved.roles.value ?: return null
            val kordInstance = resolvedMap[userId] ?: return null

            /* Now we need to wrap the kord user in our own implementation! */
            return KordRole(kordInstance)
        }
    }

    public object Mentionable : CommandOptionConverter {
        public override fun convert(builder: BaseInputChatBuilder, option: CommandOption<*>) {
            builder.mentionable(option.name, option.description) { required = !option.type.isNullable }
        }
    }
}

public fun MutableMap<CommandOptionType, CommandOptionConverter>.applyDefaults(): MutableMap<CommandOptionType, CommandOptionConverter> {
    put(CommandOptionType.Integer, CommandOptionConverter.Integer)
    put(CommandOptionType.NullableInteger, CommandOptionConverter.Integer)

    put(CommandOptionType.Number, CommandOptionConverter.Number)
    put(CommandOptionType.NullableNumber, CommandOptionConverter.Number)

    put(CommandOptionType.String, CommandOptionConverter.String)
    put(CommandOptionType.NullableString, CommandOptionConverter.String)

    put(CommandOptionType.Bool, CommandOptionConverter.Bool)
    put(CommandOptionType.NullableBool, CommandOptionConverter.Bool)

    put(CommandOptionType.User, CommandOptionConverter.User)
    put(CommandOptionType.NullableUser, CommandOptionConverter.User)

    put(CommandOptionType.Channel, CommandOptionConverter.Channel)
    put(CommandOptionType.NullableChannel, CommandOptionConverter.Channel)

    put(CommandOptionType.Role, CommandOptionConverter.Role)
    put(CommandOptionType.NullableRole, CommandOptionConverter.Role)

    put(CommandOptionType.Mentionable, CommandOptionConverter.Mentionable)
    put(CommandOptionType.NullableMentionable, CommandOptionConverter.Mentionable)

    return this
}
