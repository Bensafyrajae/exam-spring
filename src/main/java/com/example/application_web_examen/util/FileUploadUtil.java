package com.example.application_web_examen.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class FileUploadUtil {

    public static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    public static final String MEDIA_PATTERN = "([^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp|tif|webp|svg))$)|.*\\.(mp4|avi|mov|mkv|webm)$";
    public static final String DATE_FORMAT = "yyyyMMddHHmmss";
    public static final String FILE_NAME_FORMAT = "%s_%s";

    public static boolean isAllowedExtension(final String fileName, final String pattern) {
        Matcher matcher = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(fileName);
        return matcher.matches();
    }

    public static void assertAllowed(MultipartFile file, String pattern) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("Max file size is 5MB");
        }

        if (!isAllowedExtension(file.getOriginalFilename(), pattern)) {
            throw new RuntimeException("Invalid file type.");
        }
    }

    public static String getFileName(final String name) {
        return String.format(FILE_NAME_FORMAT, name, new SimpleDateFormat(DATE_FORMAT).format(System.currentTimeMillis()));
    }
}