package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.handlers;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.interfaces.CommandHandler;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.ClientMessageException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.foodapi.FoodApiClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.utils.ServerMessages;

public abstract class BaseCommandHandler implements CommandHandler {
    protected final FoodApiClient foodApiClient;

    protected BaseCommandHandler(FoodApiClient foodApiClient) {
        this.foodApiClient = foodApiClient;
    }

    String exceptionHandler(Throwable exception) {
        var cause = exception.getCause();

        if(cause != null && cause instanceof ClientMessageException) {
            return cause.getMessage();
        }

        exception.printStackTrace();
        return ServerMessages.SERVER_EXCEPTION_MSG;
    }
}