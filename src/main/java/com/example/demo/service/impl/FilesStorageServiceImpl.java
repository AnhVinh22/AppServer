package com.example.demo.service.impl;

import com.example.demo.service.FilesStorageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

    private final Path root = Paths.get("temp"); // temp

    // create foldler
    public FilesStorageServiceImpl() throws IOException {
        if (!Files.exists(root)) {
            Files.createDirectory(root);
        }
    }

    @Override
    public String saveFile(String email, MultipartFile file) throws IOException {
        // TODO: temp/{userId}
        //  temp/1234, temp/4567
        Path userPath = root.resolve(email);
        Files.createDirectory(userPath);

        // TODO: temp/{userId}/{file_name}
        //  temp/1234/abc.jpeg, temp/4567/sample.jpg, temp/1234/sample.jpg
        Path filePath = userPath.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), filePath);

        // Return path of stored file
        return filePath.toAbsolutePath().toString();
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root))
                    .map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("COULD NOT LOAD THE FILES!");
        }
    }

    @Override
    public Resource load(String fileName) {
        try {
            Path file = root.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("COULD NOT READ THE FILE");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("ERROR:" + e.getMessage());

        }
    }

}
