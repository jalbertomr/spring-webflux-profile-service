package com.bext.profileservice.io;

import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Consumer;

public class Synchronous implements Reader {
    @Override
    public void read(File file, Consumer<BytesPayLoad> consumer) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] dataBytes = new byte[FileCopyUtils.BUFFER_SIZE];
            int readBytes;
            while ((readBytes = fis.read(dataBytes, 0, dataBytes.length)) != -1) {
                consumer.accept(BytesPayLoad.from(dataBytes, readBytes));
            }
        }
    }
}
