package bg.sofia.uni.fmi.mjt.foodanalyzer.server;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.interfaces.CommandInvoker;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

class ServerImplementation {
    private final CommandInvoker commandInvoker;
    private final ByteBuffer buffer;
    private boolean isWorking = true;

    public ServerImplementation(ByteBuffer buffer, CommandInvoker commandInvoker) {
        this.buffer = buffer;
        this.commandInvoker = commandInvoker;
    }

    public void start(Selector selector) throws IOException {
        if (selector == null) {
            throw new IllegalArgumentException("Selector is null!");
        }
        while (isWorking) {
            int readyChannels = selector.select();
            if (readyChannels == 0) {
                continue;
            }

            var readyKeys = selector.selectedKeys();
            processReadyKeys(readyKeys, selector);
        }
    }

    public void stop() {
        this.isWorking = false;
    }

    private void processReadyKeys(Set<SelectionKey> readyKeys, Selector selector) {
        Iterator<SelectionKey> keyIterator = readyKeys.iterator();

        while (keyIterator.hasNext()) {
            var key = keyIterator.next();

            if (!key.isValid()) {
                keyIterator.remove();
                continue;
            }

            if (key.isReadable()) {
                processRead(key);

            } else if (key.isAcceptable()) {
                processAccept(key, selector);
            }

            keyIterator.remove();
        }
    }

    private void processAccept(SelectionKey key, Selector selector) {
        try {
            processAcceptInternal(key, selector);
        } catch (IOException e) {
            System.out.println("There was a problem while accepting a socket: " + e.getMessage());
            removeKey(key);
        }

    }

    private void processAcceptInternal(SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();

        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
    }

    private void processRead(SelectionKey key) {
        try {
            processReadInternal(key);
        } catch (IOException e) {
            System.out.println("There was a problem with reading from socket: " +
                    e.getMessage() + ". Closing the socket!");
            removeKey(key);
        }
    }

    private void processReadInternal(SelectionKey key) throws IOException {
        var channel = (SocketChannel) key.channel();

        buffer.clear();
        int r = channel.read(this.buffer);
        if (r <= 0) {
            System.out.println("nothing to read, will close channel");
            channel.close();
            return;
        }

        buffer.flip();

        var command = new String(buffer.array(), 0, buffer.limit());
        processCommand(command, key);
    }

    private void processCommand(String command, SelectionKey key) {
        this.commandInvoker.invokeCommand(command)
        .thenAccept(responseMessage -> {
            try {
                writeToClient(responseMessage, key);
            } catch (IOException e) {
                System.out.println("There was an error while writing to client: " + e.getMessage());
            }
        });
    }

    private void writeToClient(String message, SelectionKey clientKey) throws IOException {
        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();

        var channel = (SocketChannel) clientKey.channel();
        channel.write(buffer);
    }

    private void removeKey(SelectionKey key) {
        try {
            key.cancel();
            key.channel().close();
        } catch (IOException e) {
            System.out.println("Couldn't remove key:" + e.getMessage());
        }
    }
}