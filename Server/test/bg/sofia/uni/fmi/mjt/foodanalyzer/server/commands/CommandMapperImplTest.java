package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.handlers.NullCommand;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.interfaces.CommandHandler;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.interfaces.CommandMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class CommandMapperImplTest {
    private CommandMapper commandMapper;
    private CommandHandler commandHandlerMock;
    private String commandName = "command";

    @Before
    public void setUp() {

        var map = new HashMap<String, CommandHandler>();
        commandHandlerMock = mock(CommandHandler.class);
        map.put(commandName, commandHandlerMock);

        commandMapper = new CommandMapperImpl(map);
    }

    @Test
    public void getCommandHandlerShouldFindRightHandler() {
        var handler = commandMapper.getCommandHandler(commandName);
        Assert.assertEquals(commandHandlerMock, handler);
    }

    @Test
    public void getCommandHandlerShouldReturnNullHandlerIfNotFound()
            throws ExecutionException, InterruptedException {
        final var invalidCommand = "invalid";
        final var args = "args";

        var nullCommand = new NullCommand();
        var expectedResponse = nullCommand.execute(args);

        var handler = commandMapper.getCommandHandler(invalidCommand);

        assertEquals(expectedResponse.get(), handler.execute(args).get());
    }

}