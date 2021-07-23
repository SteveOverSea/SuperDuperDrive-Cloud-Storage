package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.File;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES")
    ArrayList<File> getAllFiles();

    @Select("SELECT * FROM FILES WHERE filename=#{filename}")
    File getFile(String filename);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userId, filedata) VALUES (#{filename}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int saveFile(File file);

    @Delete("DELETE FROM FILES WHERE filename=#{filename}")
    void deleteFile(String filename);
}
