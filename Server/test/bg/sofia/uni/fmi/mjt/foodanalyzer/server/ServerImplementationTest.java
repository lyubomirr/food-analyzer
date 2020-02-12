package bg.sofia.uni.fmi.mjt.foodanalyzer.server;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.interfaces.CommandInvoker;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashSet;

import static org.mockito.Mockito.*;

public class ServerImplementationTest  {
    private static final int SERVER_TIME = 500;
    private static final int POSITIVE_INVOCATION_COUNT = 1;
    private static final int NO_INVOCATION_COUNT = 0;

    private CommandInvoker commandInvokerMock;
    private ByteBuffer byteBufferMock;
    private Selector selectorMock;
    private ServerImplementation server;

    protected SelectionKey createKeyStub(SelectableChannel channel, int mode, boolean valid) {
        return createKeyStub(channel, mode, valid, null);
    }

    protected SelectionKey createKeyStub(SelectableChannel channel, int mode, boolean valid, Selector selector) {
        return new SelectionKey() {

            @Override
            public SelectableChannel channel() {
                return channel;
            }

            @Override
            public Selector selector() {
                return selector;
            }

            @Override
            public boolean isValid() {
                return valid;
            }

            @Override
            public void cancel() {

            }

            @Override
            public int interestOps() {
                return 0;
            }

            @Override
            public SelectionKey interestOps(int ops) {
                return null;
            }

            @Override
            public int readyOps() {
                return mode;
            }
        };
    }

    @Before
    public void setUp() throws IOException {
        final int readyChannels = 1;
        selectorMock = mock(Selector.class);
        when(selectorMock.select()).thenReturn(readyChannels);

        final int buffCapacity = 1024;
        byteBufferMock = ByteBuffer.allocate(buffCapacity);

        commandInvokerMock = mock(CommandInvoker.class);
        server = new ServerImplementation(byteBufferMock, commandInvokerMock);
    }

    private void addKeyToSelectorMock(SelectionKey keyStub) {
        var returnedKeys = new HashSet<SelectionKey>();
        returnedKeys.add(keyStub);
        when(selectorMock.selectedKeys()).thenReturn(returnedKeys);
    }

    private void startServerForSpecifiedTime() throws InterruptedException {
        new Thread(() -> {
            try {
                this.server.start(this.selectorMock);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(SERVER_TIME);
        this.server.stop();
    }

    @Test
    public void serverImplementationShouldProcessReadCorrectly() throws IOException, InterruptedException {
        final int bytesRead = 5;
        var keyChannelMock = mock(SocketChannel.class);
        when(keyChannelMock.read(this.byteBufferMock)).thenReturn(bytesRead);

        var keyStub = createKeyStub(keyChannelMock, SelectionKey.OP_READ, true);
        addKeyToSelectorMock(keyStub);

        startServerForSpecifiedTime();

        verify(keyChannelMock, times(POSITIVE_INVOCATION_COUNT)).read(this.byteBufferMock);
        verify(this.commandInvokerMock, times(POSITIVE_INVOCATION_COUNT)).invokeCommand(anyString());
    }

    @Test
    public void serverImplementationShouldCloseChannelIfNothingToRead() throws InterruptedException, IOException {
        var keyChannelMock = mock(SocketChannel.class);

        var keyStub = createKeyStub(keyChannelMock, SelectionKey.OP_READ, true);
        addKeyToSelectorMock(keyStub);

        startServerForSpecifiedTime();

        verify(keyChannelMock, times(POSITIVE_INVOCATION_COUNT)).read(this.byteBufferMock);
        verify(this.commandInvokerMock, times(NO_INVOCATION_COUNT)).invokeCommand(anyString());
    }

    @Test
    public void serverImplementationShouldNotProcessInvalidKey() throws InterruptedException, IOException {
        var keyChannelMock = mock(SocketChannel.class);

        var keyStub = createKeyStub(keyChannelMock, SelectionKey.OP_READ, false);
        addKeyToSelectorMock(keyStub);

        startServerForSpecifiedTime();

        verify(keyChannelMock, times(NO_INVOCATION_COUNT)).read(this.byteBufferMock);
        verify(this.commandInvokerMock, times(NO_INVOCATION_COUNT)).invokeCommand(anyString());
    }
}