package com.example.demo.controller;

import com.example.demo.domain.Image;
import com.example.demo.response.ResponseObject;
import com.example.demo.service.FilesStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("demo/v1/file/")
@RequiredArgsConstructor
public class FileUploadController {

    private final FilesStorageService filesStorageService;

    @GetMapping("")
    public ResponseEntity<List<Image>> getListFile() {
        List<Image> fileImage = filesStorageService.loadAll()
                .map(path -> {
                    String fileName = path.getFileName().toString();
                    String url = MvcUriComponentsBuilder
                            .fromMethodName(FileUploadController.class, "getFile",
                                    path.getFileName().toString()).build().toString();
                        return new Image(fileName, url);
                }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(fileImage);
    }

    @GetMapping("file/{fileName:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        Resource file = filesStorageService.load(fileName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

}
