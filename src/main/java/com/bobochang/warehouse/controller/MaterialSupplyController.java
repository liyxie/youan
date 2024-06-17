package com.bobochang.warehouse.controller;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bobochang.warehouse.annotation.BusLog;
import com.bobochang.warehouse.constants.WarehouseConstants;
import com.bobochang.warehouse.entity.*;
import com.bobochang.warehouse.page.Page;
import com.bobochang.warehouse.service.MaterialService;
import com.bobochang.warehouse.service.MaterialSupplyService;
import com.bobochang.warehouse.service.SupplyService;
import com.bobochang.warehouse.utils.TokenUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author LI
 * @date 2023/11/17
 * 供应商
 */
@RequestMapping("/inspect")
@RestController
@Transactional
@BusLog(name = "材料管理")
@Slf4j
public class MaterialSupplyController {
    @Autowired
    private MaterialSupplyService materialSupplyService;
    
    @Autowired
    private TokenUtils tokenUtils;

    @Value("${file.material-upload-path}")
    private String uploadPath;
    
    @Autowired
    private MaterialService materialService;
    
    @Autowired
    private SupplyService supplyService;
    
    @RequestMapping("/inspect-page-list")
    public Result productPageList(@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token,
                                  Page page, MaterialSupply materialSupply) {
        String userCode = tokenUtils.getCurrentUser(token).getUserCode();
        //执行业务
        page = materialSupplyService.queryPage(page, materialSupply, userCode);
        //响应
        return Result.ok(page);
    }

    @RequestMapping("/inspect-add")
    @BusLog(descrip = "供应商添加材料")
    public Result addMaterial(@RequestBody MaterialSupply material,
                              @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token) {

        //获取当前登录的用户
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        
        Supply supply = supplyService.selectOneByUserId(currentUser.getUserId());
        
        //获取当前登录的用户id,即添加的用户id
        material.setSupplyId(supply.getSupplyId());
        material.setCreateBy(supply.getSupplyId());
        material.setCreateTime(DateTime.now());
        
        //设置检验状态为未审核
        material.setInspectionResult("0");

        //执行业务
        Result result = materialSupplyService.saveMaterial(material);

        //响应
        return result;
    }

    @CrossOrigin
    @PostMapping("/img-upload")
    @BusLog(descrip = "上传合同文件")
    public Result uploadImg(MultipartFile file) {
        try {
            System.out.println(file.getSize());
            //拿到图片保存到的磁盘路径
            long timestamp = Instant.now().toEpochMilli(); // 拿到当前时间戳作为图片保存的名称
            String flag = UUID.randomUUID().toString().substring(0,5); // 唯一标识符，防止毫秒间调用产生的相同时间戳
            String fileUploadPath = uploadPath + "/" + timestamp + "-" + flag + ".jpg";


            log.info(fileUploadPath);
            File targetFile = new File(fileUploadPath);

            // 如果文件已存在，先删除旧文件
            if (targetFile.exists()) {
                targetFile.delete();
            }

            //保存图片（如果文件不存在或者已删除，则进行保存）
            file.transferTo(targetFile);

            //成功响应
            return Result.ok(timestamp + "-" + flag + ".jpg");
        } catch (IOException e) {
            //失败响应
            return Result.err(Result.CODE_ERR_BUSINESS, "图片上传失败！");
        }
    }

    @PutMapping("/inspect-update")
    public Result updateContract(@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token,
                                 @RequestBody MaterialSupply materialSupply) {
        materialSupply.setUpdateTime(new Date());
        materialSupply.setUpdateBy(tokenUtils.getCurrentUser(token).getUserId());
        System.out.println(materialSupply);
        return materialSupplyService.updateMaterialSupplyById(materialSupply);
    }

    @RequestMapping("/inspect-delete/{id}")
    public Result deleteMaterial(@PathVariable Integer id) {

        //执行业务
        boolean result = materialSupplyService.removeById(id);
        if(result){
            return Result.ok("删除成功");
        }
        //响应
        return Result.err(500, "删除失败");
    }
    @PostMapping("/inspect-reject")
    public Result reject(@RequestBody MaterialSupply materialSupply){
        materialSupply.setInspectionResult("1");
        int i = materialSupplyService.updateState(materialSupply);
        if(i>0){
            return Result.ok("退回成功");
        }else{
            return Result.err(500,"退回失败");
        }
    }
    
    @PostMapping("/inspect-again")
    public Result again(@RequestBody MaterialSupply materialSupply){
        materialSupply.setInspectionResult("0");
        log.info(String.valueOf(materialSupply));
        int i = materialSupplyService.updateState(materialSupply);
        if(i>0){
            return Result.ok("提交成功");
        }else{
            return Result.err(500,"退回失败");
        }
    }

    @PostMapping("/inspect-agree")
    public Result agree(@RequestBody MaterialSupply materialSupply){
        materialSupply.setInspectionResult("2");
        // 先查找材料名
        String materialName = materialSupplyService.selectById(materialSupply.getId()).getMaterialName();
        // 根据材料名在材料表中查找
        // 有则将id填入
        // 无则将这个材料写入材料表中，并将id写入
        Material material = materialService.selectMaterialByName(materialName);
        if (material != null) {
            materialSupply.setMaterialId(material.getMaterialId());
            int result = materialSupplyService.updateMaterialIdById(materialSupply);
            if(!(result >0)){
                return Result.err(500,"操作失败");
            }
        }else{
            Material material1 = new Material();
            material1.setMaterialName(materialName);
            material1.setUnitId(8);
            materialService.saveMaterial(material1);
            materialSupply.setMaterialId(material1.getMaterialId());
            int result = materialSupplyService.updateMaterialIdById(materialSupply);
            if(!(result >0)){
                return Result.err(500,"操作失败");
            }
        }

        // 更改检验状态
        int i = materialSupplyService.updateState(materialSupply);
        if(i>0){
            return Result.ok("操作成功");
        }else{
            return Result.err(500,"操作失败");
        }
    }

    @SneakyThrows
    @GetMapping("/inline-image/{imgName}")
    public ResponseEntity<Resource> inlineImage(@PathVariable String imgName) throws IOException {
        //拿到图片保存到的磁盘路径
        String fileUploadPath = uploadPath + "/" + imgName;
        // 读取图片文件
        Resource resource = new FileSystemResource(fileUploadPath);

        // 设置响应头部信息，用于告诉浏览器以附件形式下载文件
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image.jpg");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @GetMapping("/download-images/{inlineId}")
    @CrossOrigin(origins = "http://localhost:3000") // 设置允许跨域请求的源
    @SneakyThrows
    public ResponseEntity<Resource> downloadImages(@PathVariable String inlineId) throws IOException {
        // 图片文件路径列表
        MaterialSupply materialSupply = materialSupplyService.selectById(Integer.valueOf((inlineId)));
        String[] fileNames = materialSupply.getFiles().split(",");

        // 创建一个字节数组输出流，用于存储生成的 zip 文件
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream);

        try {
            for (String fileName : fileNames) {
                String imagePath = uploadPath + "/" + fileName;
                File file = new File(imagePath);
                if (file.exists()) {
                    // 读取图片文件内容并添加到 zip 文件中
                    // 读取要添加到ZIP包中的文件
                    FileInputStream fileIn = new FileInputStream(imagePath);
                    // 创建一个ZipEntry对象，表示要添加到ZIP包中的文件
                    ZipEntry zipEntry = new ZipEntry(fileName);
                    // 将ZipEntry对象添加到ZIP包中
                    zipOut.putNextEntry(zipEntry);

                    // 将文件内容写入ZIP包
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fileIn.read(buffer)) != -1) {
                        zipOut.write(buffer, 0, bytesRead);
                    }
                    fileIn.close();
                }
            }
            // 关闭流
            zipOut.close();
        }catch (RuntimeException exception){
            throw new RuntimeException(exception);
        }

        // 设置响应头部信息，告诉浏览器以附件形式下载文件
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=images.zip");

        // 将生成的 zip 文件作为字节数组返回给前端
        ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
