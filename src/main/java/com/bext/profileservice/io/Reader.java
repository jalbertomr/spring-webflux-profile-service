package com.bext.profileservice.io;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public interface Reader {
   public abstract void read(File file, Consumer<BytesPayLoad> consumer) throws IOException;
}
