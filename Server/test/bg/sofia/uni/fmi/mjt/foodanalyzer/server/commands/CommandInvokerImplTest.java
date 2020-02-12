package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.interfaces.CommandHandler;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.interfaces.CommandInvoker;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.interfaces.CommandMapper;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.utils.ServerMessages;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CommandInvokerImplTest {
    private CommandInvoker commandInvoker;
    private CommandMapper commandMapperMock;

    @Before
    public void setUp() {

        commandMapperMock = mock(CommandMapper.class);
        commandInvoker = new CommandInvokerImpl(commandMapperMock);
    }

    @Test
    public void invokeCommandShouldCallRightHandler() throws ExecutionException, InterruptedException {
        final var argumens = "arg1 arg2";
        final var commandName = "command";
        final var command = commandName + " " + argumens;
        final var expectedResponse = "response";

        var handlerMock = mock(CommandHandler.class);
        when(handlerMock.execute(argumens)).thenReturn(CompletableFuture.completedFuture(expectedResponse));
        when(commandMapperMock.getCommandHandler(commandName)).thenReturn(handlerMock);


        var actualResponse = commandInvoker.invokeCommand(command);
        assertEquals(expectedResponse, actualResponse.get());
    }

    @Test
    public void invokeCommandShouldReturnErrorIfCommandNull() throws ExecutionException, InterruptedException {
        var response = commandInvoker.invokeCommand(null);
        assertEquals(response.get(), ServerMessages.NO_COMMAND_ENTERED);
    }
}