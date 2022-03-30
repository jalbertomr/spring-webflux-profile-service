package com.bext.profileservice.io;

import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class Io {
    private final Synchronous synchronous = new Synchronous();

    public void synchronousRead(File file, Consumer<BytesPayLoad> consumer) {
        try {
            this.synchronous.read(file, consumer);
        } catch (IOException ioe) {
            ReflectionUtils.rethrowRuntimeException(ioe);
        }
    }

    public void asynchronousRead(File file, Consumer<BytesPayLoad> consumer) {
        try {
            Asynchronous asynchronous = new Asynchronous();
            asynchronous.read( file, consumer);
        } catch (IOException ioe) {
            ReflectionUtils.rethrowRuntimeException(ioe);
        }
    }
}


