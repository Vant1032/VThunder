package logic;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveToLocal {
    byte[] bytes;
    File file;

    public SaveToLocal(byte[] bytes, File file) {
        this.bytes = bytes;
        this.file = file;
    }

    public void save() throws IOException {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            bufferedOutputStream.write(bytes);
        }
    }
}
