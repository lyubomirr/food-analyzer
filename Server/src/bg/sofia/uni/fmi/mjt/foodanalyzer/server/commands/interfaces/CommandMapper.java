package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.interfaces;

public interface CommandMapper {
    CommandHandler getCommandHandler(String commandName);
}
