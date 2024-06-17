package com.bobochang.warehouse.controller;

import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RequestMapping("/captcha")
@RestController
public class VerificationCodeController {

    @Resource(name = "captchaProducer")
    private Producer producer;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/captchaImage")
    public void captchaImage(HttpServletResponse response) {
        ServletOutputStream out = null;
        try {
            //禁止浏览器缓存验证码图片的响应头
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.addHeader("Cache-Control", "post-check=0, pre-check=0");
            response.setHeader("Pragma", "no-cache");
            //响应正文为jpg图片即验证码图片
            response.setContentType("image/jpeg");
            //生成验证码文本
            String code = producer.createText();
            //生成验证码图片
            BufferedImage bi = producer.createImage(code);
            //将验证码文本存储到redis
            redisTemplate.opsForValue().set(code, code, 60 * 2, TimeUnit.SECONDS);
            //将验证码图片响应给浏览器
            out = response.getOutputStream();
            ImageIO.write(bi, "jpg", out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
