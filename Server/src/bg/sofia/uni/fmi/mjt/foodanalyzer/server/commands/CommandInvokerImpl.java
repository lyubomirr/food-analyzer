package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.interfaces.CommandInvoker;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.interfaces.CommandMapper;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.utils.Constants;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.utils.ServerMessages;

import java.util.concurrent.CompletableFuture;

public class CommandInvokerImpl implements CommandInvoker {
    private static final int COMMAND_NAME_INDEX = 0;
    private static final int COMMAND_ARGS_INDEX = 1;
    private static final int COMMAND_SPLIT_LIMIT = 2;

    private final CommandMapper commandMapper;

    public CommandInvokerImpl(CommandMapper commandMapper) {
        this.commandMapper = commandMapper;
    }

    @Override
    public CompletableFuture<String> invokeCommand(String commandLine) {
        if (commandLine == null || commandLine.isBlank()) {
            return CompletableFuture.completedFuture(ServerMessages.NO_COMMAND_ENTERED);
        }

        var command = createCommandFromCommandLine(commandLine);

        var commandHandler = commandMapper.getCommandHandler(command.getCommandName());
        return commandHandler.execute(command.getArguments());
    }

    private CommandEntity createCommandFromCommandLine(String commandLine) {
        var commandSplit = commandLine.trim().split(Constants.WHITESPACE_SPLIT_REGEX, COMMAND_SPLIT_LIMIT);

        var commandName = commandSplit[COMMAND_NAME_INDEX];
        var commandArguments = getCommandArguments(commandSplit);

        return new CommandEntity(commandName, commandArguments);
    }

    private String getCommandArguments(String[] commandSplit) {
        //Check if there are arguments after the command.
        if (commandSplit.length == COMMAND_SPLIT_LIMIT) {
            return commandSplit[COMMAND_ARGS_INDEX];
        }

        return Constants.EMPTY_STRING;
    }

    private static class CommandEntity {

        private final String commandName;
        private final String arguments;

        public CommandEntity(String commandName, String arguments) {
            this.commandName = commandName;
            this.arguments = arguments;
        }

        public String getArguments() {
            return arguments;
        }

        public String getCommandName() {
            return commandName;
        }
    }
}