package sqlmodule.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataFileUtils {

    public static void createFile(File file) {
        try {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
               if (parentFile.mkdirs()){
                   SQLLogger.debug("Creating folder " + file.getName());
               }
            }

            if (!file.exists()) {
                if (file.createNewFile()){
                    SQLLogger.debug("Creating file " + file.getName());
                }
            }
        } catch (IOException e) {
            SQLLogger.warning(e.getMessage());
        }
    }

}
