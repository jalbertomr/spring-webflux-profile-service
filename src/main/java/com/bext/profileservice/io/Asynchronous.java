package com.bext.profileservice.io;

import lombok.extern.log4j.Log4j2;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Log4j2
public class Asynchronous implements Reader, CompletionHandler<Integer, ByteBuffer> {
    private int bytesRead;
    private long position;
    private AsynchronousFileChannel fileChannel;
    private Consumer<BytesPayLoad> consumer;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public void read(File file, Consumer<BytesPayLoad> consumer) throws IOException {
        this.consumer = consumer;
        Path path = file.toPath();
        AsynchronousFileChannel.open(path, Collections.singleton(StandardOpenOption.READ), this.executorService);
        ByteBuffer buffer = ByteBuffer.allocate(FileCopyUtils.BUFFER_SIZE);
        this.fileChannel.read(buffer, position, buffer, this);
        while (this.bytesRead > 0) {
            this.position = this.position + this.bytesRead;
            this.fileChannel.read(buffer, this.position, buffer, this);
        }
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        this.bytesRead = result;
        if (this.bytesRead < 0)
            return;
        attachment.flip();
        byte[] data = new byte[attachment.limit()];
        attachment.get(data);
        consumer.accept(BytesPayLoad.from(data, data.length));
        attachment.clear();
        this.position = this.position + this.bytesRead;
        this.fileChannel.read(attachment, this.position, attachment, this);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        log.error(exc);
    }

}
