package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.handlers.NullCommand;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.interfaces.CommandHandler;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.interfaces.CommandMapper;

import java.util.Map;

public class CommandMapperImpl implements CommandMapper {
    private Map<String, CommandHandler> commandHandlerMap;

    public CommandMapperImpl(Map<String, CommandHandler> commandHandlerMap) {
        this.commandHandlerMap = commandHandlerMap;
    }

    @Override
    public CommandHandler getCommandHandler(String commandName) {
        if (!this.commandHandlerMap.containsKey(commandName)) {
            return new NullCommand();
        }

        return commandHandlerMap.get(commandName);
    }
}