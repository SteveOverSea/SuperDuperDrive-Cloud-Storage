package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    public ArrayList<Credential> getCredentials(Integer userid) {
        return credentialMapper.getAll(userid);
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
        String keyString = Base64.getEncoder().encodeToString(key);
        return keyString;
    }
}
