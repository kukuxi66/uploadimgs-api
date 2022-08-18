package com.Service;

import com.utils.Result;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    Result getFile(MultipartFile fileImage , String token);

}
