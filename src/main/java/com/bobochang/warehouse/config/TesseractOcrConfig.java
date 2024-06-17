package com.bobochang.warehouse.config;

import net.sourceforge.tess4j.Tesseract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author LI
 * @date 2023/12/21
 */
@Configuration
public class TesseractOcrConfig {
    private String dataPath = "D:\\chi_sim.traineddata";

    @Bean
    public Tesseract tesseract() {

        Tesseract tesseract = new Tesseract();
        // 设置训练数据文件夹路径
        tesseract.setDatapath("D:\\tessdata");
        // 设置为中文简体
        tesseract.setLanguage("chi_sim");
        return tesseract;
    }
}
