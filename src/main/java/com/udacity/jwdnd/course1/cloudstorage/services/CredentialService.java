package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    public ArrayList<Credential> getCredentials() {
        return credentialMapper.getAll();
    }

    public Credential getCredential(Integer id) {
        return credentialMapper.getCredential(id);
    }

    public void save(Credential credential) {
        credentialMapper.save(credential);
    }

    public void update(Credential credential) {
        credentialMapper.update(credential);
    }

    public void delete(Integer id) {
        credentialMapper.delete(id);
    }

    public String generateKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String keyString = new String(key, StandardCharsets.UTF_8);
        return keyString;
    }
}
