package br.com.softblue.bluefood.application.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.softblue.bluefood.utils.IOUtils;

@Service
public class ImageService {

    @Value("${bluefood.files.logotipo}")
    private String logotipoDir;

    public void uploadLogotipo(MultipartFile multiPartFile, String fileName) {
        try {
            IOUtils.copy(multiPartFile.getInputStream(), fileName, logotipoDir);
        } catch (IOException e) {
            throw new ApplicationServiceException(e);
        }

        //Uma pequena mudança
    }
}