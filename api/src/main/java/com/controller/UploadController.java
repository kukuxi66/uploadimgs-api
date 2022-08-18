package com.controller;

import com.Service.FileService;
import com.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;


@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private FileService fileService;

    @PostMapping("/picture")
    public Result fileUpload(@RequestParam("con") MultipartFile fileImage,
                             @RequestHeader("Authorization") String token) {
        return fileService.getFile(fileImage,token);

    }

}