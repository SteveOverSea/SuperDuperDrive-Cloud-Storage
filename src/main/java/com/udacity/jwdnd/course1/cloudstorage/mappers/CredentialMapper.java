package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS")
    public ArrayList<Credential> getAll();

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid=#{id}")
    public Credential getCredential(Integer id);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES (#{url}, #{username}, #{key}, #{password}, #{userId})")
    public int save(Credential credential);

    @Update("UPDATE CREDENTIALS SET url=#{url}, username=#{username}, password=#{password} WHERE credentialid=#{credentialId}")
    public void update(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid=#{id}")
    void delete(Integer id);
}
