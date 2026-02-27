package org.example;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@UtilityClass
@Log4j2
public class FileLoaderHelper {
    private static final Logger log = LoggerFactory.getLogger(FileLoaderHelper.class);

    /*
     * Указывать путь от папки test/resources, например json/и далее...
     */
    public String loadStringFromFile(String path) {
        var data = "";
        try (var is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            log.info("Loading data from file \"{}\"", path);
            data = IOUtils.toString(Objects.requireNonNull(is), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
