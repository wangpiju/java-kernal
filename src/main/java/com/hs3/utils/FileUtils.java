package com.hs3.utils;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
    public static String save(String realPath, String path, MultipartFile file)
            throws IOException {
        String fullName = file.getOriginalFilename();
        String extName = fullName.substring(fullName.lastIndexOf('.'));
        String webPath = path + StrUtils.getGuid() + extName;

        String filePath = realPath + webPath;

        File saveFile = new File(filePath);
        File parent = saveFile.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        file.transferTo(saveFile);
        return webPath;
    }
}
