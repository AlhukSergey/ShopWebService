package by.teachmeskills.shopwebservice.utils;

import by.teachmeskills.shopwebservice.entities.BaseEntity;
import by.teachmeskills.shopwebservice.exceptions.ExportToFIleException;
import org.springframework.stereotype.Component;

import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
public class FileService<T extends BaseEntity> {
    public String writeToFile(String fileName, List<T> data) throws ExportToFIleException {
        try (Writer writer = Files.newBufferedWriter(Paths.get(fileName))) {
            writer.write(data.toString());
            return String.format("Файл успешно создан по пути: %s", fileName);
        } catch (Exception e) {
            throw new ExportToFIleException(String.format("Произошла ошибка при записи в файл: %s", e.getMessage()));
        }
    }
}
