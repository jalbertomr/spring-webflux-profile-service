package com.bext.profileservice.io;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.util.function.Consumer;

@Log4j2
public class IoDemo {
    public static void main(String... args) {
        File fileDesktop = new File(System.getProperty("user.home"), "Desktop");
        File input = new File(fileDesktop, "input.txt");

        Consumer<BytesPayLoad> consumer = bytesPayLoad -> log.info(String.format("Bytes available got %d bytes.", bytesPayLoad.getLength()));

        Io iobase = new Io();

        log.info("--- syncrhonous read -----------------");
        iobase.synchronousRead(input, consumer);

        log.info("--- asyncrhonous read ----------------");
        iobase.synchronousRead(input, consumer);
    }

}
