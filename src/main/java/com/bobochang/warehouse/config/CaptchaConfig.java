package com.bobochang.warehouse.config;

import com.bobochang.warehouse.utils.GlobalVariable;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class CaptchaConfig {

    @Bean(name = "captchaProducer")
    public DefaultKaptcha getKaptchaBean() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 是否有边框 默认为true 可设置为 yes或no
        properties.setProperty("kaptcha.border", "yes");
        // 设置边框颜色 默认为 Color.BLACK
        properties.setProperty("kaptcha.border.color", "105,179,90");
        // 设置验证码文本字符颜色 默认为 Color.BLACK
        properties.setProperty("kaptcha.textproducer.font.color", "blue");
        // 设置验证码图片宽度 默认为 200
        properties.setProperty("kaptcha.image.width", "120");
        // 设置验证码图片高度 默认为 50
        properties.setProperty("kaptcha.image.height", "40");
        // 设置文本字符大小 默认为 40
        properties.setProperty("kaptcha.textproducer.font.size", "32");
        // 设置session key 默认为 KAPTCHA_SESSION_KEY
        properties.setProperty("kaptcha.session.key", "kaptchaCode");
        // 设置验证码文本字符间距 默认为 2
        properties.setProperty("kaptcha.textproducer.char.space", "4");
        // 设置验证码文本字符长度 默认为 5
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        // 设置验证码文本字体样式 默认为 Arial, Courier
        properties.setProperty("kaptcha.textproducer.font.names", "Arial,Courier");
        // 设置验证码噪点颜色 默认为 black
        properties.setProperty("kaptcha.noise.color", "gray");

        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    @Bean
    public GlobalVariable globalVariable() {
        return new GlobalVariable();
    }
}
