package com.example.uploadfile.controller;


import com.example.uploadfile.dto.UploadFileResponse;
import com.example.uploadfile.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
@RequestMapping( produces = MediaType.APPLICATION_JSON_VALUE)
public class UploadController {

    @Autowired
    private UploadService uploadService;


    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        //        Path path = Paths.get("c:\\data\\myfile.txt");
//        String group = "GROUP_NAME";
//        UserPrincipalLookupService lookupService = FileSystems.getDefault()
//                .getUserPrincipalLookupService();
//        GroupPrincipal groupPrincipal=null;
//        try {
//             groupPrincipal = lookupService.lookupPrincipalByGroupName(group);
//            Files.getFileAttributeView(path, PosixFileAttributeView.class, LinkOption.NOFOLLOW_LINKS).setGroup(groupPrincipal);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        File originalFile = new File("original.jpg"); // just as an example
//        try {
//            GroupPrincipal group = Files.readAttributes(originalFile.toPath(), PosixFileAttributes.class, LinkOption.NOFOLLOW_LINKS).group();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        uploadService.storeFile(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");
        return "redirect:/";
    }
}
