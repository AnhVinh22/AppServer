package com.example.demo.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesStorageService {
    String saveFile(String email, MultipartFile file) throws IOException;

    Stream<Path> loadAll();

    Resource load(String fileName);
}
