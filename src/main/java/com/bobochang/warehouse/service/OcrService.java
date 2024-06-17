package com.bobochang.warehouse.service;

import lombok.AllArgsConstructor;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

@Service
@AllArgsConstructor
public class OcrService {

    private final Tesseract tesseract;

	/**
	 * 
	 * @param imageFilePath 图片文件所在路径
	 * @return
	 * @throws TesseractException
	 * @throws IOException
	 */
    public String recognizeText(String imageFilePath) throws TesseractException, IOException {
		File imageFile = new File(imageFilePath);
		FileInputStream fileInputStream = new FileInputStream(imageFile);
		byte[] buffer = new byte[(int) imageFile.length()];
		fileInputStream.read(buffer);
		// 转换
		InputStream sbs = new ByteArrayInputStream(buffer);
		BufferedImage bufferedImage = ImageIO.read(sbs);
//		tesseract.setOcrEngineMode(0);
//		tesseract.setLanguage();
		tesseract.setTessVariable("user_defined_dpi", "700");
        // 对图片进行文字识别
        return tesseract.doOCR(bufferedImage);
    }
}
