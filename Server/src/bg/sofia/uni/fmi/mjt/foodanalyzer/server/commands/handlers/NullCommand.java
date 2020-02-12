package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.handlers;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.interfaces.CommandHandler;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.utils.ServerMessages;

import java.util.concurrent.CompletableFuture;

public class NullCommand implements CommandHandler {

    @Override
    public CompletableFuture<String> execute(String arguments) {
        return CompletableFuture.completedFuture(ServerMessages.NO_SUCH_COMMAND);
    }
}