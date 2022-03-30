package com.bext.profileservice.io;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BytesPayLoad {
    private final byte[] bytes;
    private final int length;

    public static BytesPayLoad from(byte[] bytes, int length) {
        return new BytesPayLoad(bytes, length);
    }
}
