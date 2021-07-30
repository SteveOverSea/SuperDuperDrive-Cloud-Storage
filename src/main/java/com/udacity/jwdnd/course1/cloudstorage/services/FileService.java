package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Service
public class FileService {

    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public void store(File file) {
        fileMapper.saveFile(file);
    }

    public ArrayList<File> getAllFiles(Integer userid) {
        return fileMapper.getAllFiles(userid);
    }

    public File getFile(String filename) {
        return fileMapper.getFile(filename);
    }

    public void remove(String filename) {
        fileMapper.deleteFile(filename);
    }

    public boolean isDuplicate(String filename) {
        return fileMapper.getFile(filename) != null;
    }
}
