package com.bobochang.warehouse.service;

import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.cli.ParseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public interface FileService {
    void pdf2Image(String targetPdf, String targetImage);

    String getTable(File targetImageFolder) throws IOException;

    String recognizeText(String path) throws IOException, TesseractException;
    
    String getText(File targetImageFolder) throws IOException;
    
    String getFileContent(File targetImageFolder, String requestName) throws IOException;

    Map<String, Object> getContractContentByPdf(String targetPdf) throws ParseException;

    Map<String, Object> getContractContentByDoc(String targetDoc) throws IOException;
}
