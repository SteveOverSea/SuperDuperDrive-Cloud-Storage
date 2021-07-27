package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/home")
public class HomeController {

    private FileService fileService;
    private UserService userService;
    private NoteService noteService;
    private CredentialService credentialService;
    private EncryptionService encryptionService;

    public HomeController(FileService fileService, UserService userService, NoteService noteService,
                          CredentialService credentialService, EncryptionService encryptionService) {
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping()
    public String homeView(Model model) {
        model.addAttribute("files", fileService.getAllFiles());
        model.addAttribute("notes", noteService.getNotes());
        model.addAttribute("credentials", credentialService.getCredentials());
        model.addAttribute("encryptionService", encryptionService);
        return "home";
    }

    // FILE

    @PostMapping("/file")
    public String postFile(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication authentication, Model model) throws IOException {
        if (fileUpload != null) {

            Integer userId = userService.getUser(authentication.getName()).getUserId();
            File file = new File(null, fileUpload.getOriginalFilename(), fileUpload.getContentType(), Long.toString(fileUpload.getSize()), userId, fileUpload.getBytes());
            fileService.store(file);
            model.addAttribute("files", fileService.getAllFiles());
        }

        return "home";
    }

    @GetMapping("/file/delete/{filename}")
    public String deleteFile(@PathVariable String filename) {
        fileService.remove(filename);
        return "redirect:/home";
    }

    // From Vijay https://knowledge.udacity.com/questions/355003
    @GetMapping("/file/view/{filename}")
    public StreamingResponseBody viewFile(@PathVariable String filename, HttpServletResponse response) throws IOException {
        File file = fileService.getFile(filename);
        response.setContentType(file.getContentType());
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment:filename=\"" + file.getFilename());
        return outputStream -> {
            int bytesRead;
            byte[] buffer = new byte[10000];
            InputStream inputStream = new ByteArrayInputStream(file.getFileData());
            while((bytesRead = inputStream.read(buffer)) != -1){
                outputStream.write(buffer, 0, bytesRead);
            }
        };
    }

    // NOTE

    @PostMapping("/note")
    public String postNote(@RequestParam("noteId") String noteId, @RequestParam("noteTitle") String noteTitle, @RequestParam("noteDescription") String noteDescription, Authentication authentication, Model model) {
        if (noteId.length() == 0) {
            Note note = new Note(null, noteTitle, noteDescription, userService.getUser(authentication.getName()).getUserId());
            noteService.save(note);
            model.addAttribute("notes", noteService.getNotes());
        } else {
            Note note = new Note(Integer.parseInt(noteId), noteTitle, noteDescription, null);
            noteService.update(note);
        }

        return "redirect:/home#nav-notes";
    }

    @GetMapping("/note/delete/{noteid}")
    public String deleteNote(@PathVariable String noteid) {
        Integer id = Integer.parseInt(noteid);
        noteService.delete(id);
        return "redirect:/home";
    }

    // CREDENTIAL

    @PostMapping("/credential")
    public String postCredential(@RequestParam("credentialId") String credentialId, @RequestParam("url") String url,
                                 @RequestParam("username") String username, @RequestParam("password") String password,
                                 Model model, Authentication authentication) {
        if (credentialId.length() == 0) {
            String key = credentialService.generateKey();
            String encryptedPassword = encryptionService.encryptValue(password, key);
            Credential credential = new Credential(null, url, username, key, encryptedPassword, userService.getUser(authentication.getName()).getUserId());
            credentialService.save(credential);
            model.addAttribute("credentials", credentialService.getCredentials());
        } else {
            String key = credentialService.getCredential(Integer.parseInt(credentialId)).getKey();
            String encryptedPassword = encryptionService.encryptValue(password, key);
            Credential credential = new Credential(Integer.parseInt(credentialId), url, username, null, encryptedPassword, null);
            credentialService.update(credential);
        }

        return "redirect:/home#nav-notes";
    }

    @GetMapping("/credential/delete/{credentialid}")
    public String deleteCredential(@PathVariable String credentialid) {
        Integer id = Integer.parseInt(credentialid);
        credentialService.delete(id);
        return "redirect:/home";
    }
}
