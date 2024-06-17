package com.bobochang.warehouse.controller;

import com.bobochang.warehouse.annotation.BusLog;
import com.bobochang.warehouse.constants.WarehouseConstants;
import com.bobochang.warehouse.dto.ContractReasonDto;
import com.bobochang.warehouse.dto.EginnerContractDto;
import com.bobochang.warehouse.dto.MaterialNumDto;
import com.bobochang.warehouse.entity.*;
import com.bobochang.warehouse.page.Page;
import com.bobochang.warehouse.service.ActivitiService;
import com.bobochang.warehouse.service.ContractEginnerService;
import com.bobochang.warehouse.service.ContractService;
import com.bobochang.warehouse.utils.TokenUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.core.io.ByteArrayResource;

/**
 * @author bobochang
 * @Description
 * @Date 2023/9/19 - 11:11
 */
@RequestMapping("/contract")
@RestController
@Transactional
@BusLog(name = "合同管理")
@Slf4j
public class ContractController {

    @Autowired
    private ContractService contractService;
    
    @Autowired
    private ContractEginnerService contractEginnerService;

    @Value("${file.upload-path}")
    private String uploadPath;
    
    @Autowired
    private TokenUtils tokenUtils;
    
    private String abc;
    
    @Autowired
    private ActivitiService activitiService;

    /**
     * 分页查询仓库的url接口/contract/contract-list
     * <p>
     * 参数Page对象用于接收请求参数页码pageNum、每页行数pageSize;
     * 参数Store对象用于接收请求参数合同名称storeName、合同状态storeAddress、
     * 联系人concat、联系电话phone;
     * <p>
     * 返回值Result对象向客户端响应组装了所有分页信息的Page对象;
     */
    @GetMapping("/contract-list")
    public Result findAllContract(Page page, Contract contract) {
        page = contractService.queryContractPage(page, contract);
        return Result.ok(page);
    }

    @PostMapping("/addContract")
    @BusLog(descrip = "添加合同")
    public Result addContract(@RequestBody EginnerContractDto contract) {
        return contractService.saveContract(contract);
    }

    @PutMapping("/updateState")
    @BusLog(descrip = "更新合同状态")
    public Result updateContractState(@RequestBody Contract contract) {
        contract.setUpdateTime(new Date());
        int i = contractService.updateContractState(contract);
        if(i>0){
            return Result.ok("更新成功！");
        }
        return Result.err(500,"修改失败");
    }

    @PutMapping("/updateContract")
    @BusLog(descrip = "更新合同")
    public Result updateContract(@RequestBody Contract contract) {
        contract.setUpdateTime(new Date());
        return contractService.updateContractById(contract);
    }

    /**
     * 合同上传文件的接口
     *
     * @param file 上传的文件
     * @return
     */
    @CrossOrigin
    @PostMapping("/img-upload")
    @BusLog(descrip = "上传合同文件")
    public Result uploadImg(MultipartFile file) {

        try {
            System.out.println(file.getSize());
            //拿到图片保存到的磁盘路径
            long timestamp = Instant.now().toEpochMilli(); // 拿到当前时间戳作为图片保存的名称
            String flag = UUID.randomUUID().toString().substring(0,5); // 唯一标识符，防止毫秒间调用产生的相同时间戳
            String fileUploadPath = uploadPath + "/" + timestamp + "-" + flag + ".pdf";
            

            log.info(fileUploadPath);
            File targetFile = new File(fileUploadPath);

            // 如果文件已存在，先删除旧文件
            if (targetFile.exists()) {
                targetFile.delete();
            }

            //保存图片（如果文件不存在或者已删除，则进行保存）
            file.transferTo(targetFile);

            //成功响应
            return Result.ok(timestamp + "-" + flag + ".pdf");
        } catch (IOException e) {
            //失败响应
            return Result.err(Result.CODE_ERR_BUSINESS, "图片上传失败！");
        }
    }

    /**
     * 分页查询仓库的url接口/contract/contract-list
     * <p>
     * 参数Page对象用于接收请求参数页码pageNum、每页行数pageSize;
     * 参数Store对象用于接收请求参数合同名称storeName、合同状态storeAddress、
     * 联系人concat、联系电话phone;
     * <p>
     * 返回值Result对象向客户端响应组装了所有分页信息的Page对象;
     */
    @RequestMapping("/exportTable")
    public Result exportTable(Page page, Contract contract) {
        //分页查询仓库
        page = contractService.queryContractPage(page, contract);
        //拿到当前页数据
        List<?> resultList = page.getResultList();
        //响应
        return Result.ok(resultList);
    }

    /**
     * 下载合同图片 /contract/download-image?contractId
     *
     * @param contractId 合同id
     * @return 图片资源文件
     * @throws IOException
     */
    @GetMapping("/download-images/{contractId}")
    @CrossOrigin(origins = "http://localhost:3000") // 设置允许跨域请求的源
    @SneakyThrows
    public ResponseEntity<Resource> downloadImages(@PathVariable String contractId) throws IOException {
        // 图片文件路径列表
        Contract contract = contractService.findContractById(Integer.valueOf(contractId));
        String[] fileNames = contract.getFiles().split(",");

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
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=附件.zip");

        // 将生成的 zip 文件作为字节数组返回给前端
        ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @SneakyThrows
    @GetMapping("/inline-image/{imgName}")
    public ResponseEntity<Resource> inlineImage(@PathVariable String imgName) throws IOException {
        System.out.println(imgName);
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
    
    @GetMapping("/contract-all")
    public Result selectAllContract(){
        return contractService.selectAllContract();
    }

    /**
     * 审核合同，如果同意
     * @param token
     * @param contract 包含合同id
     * @return
     */
    @PostMapping("/contract-agree")
    @BusLog(descrip = "同意合同")
    public Result contractAgree(@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token,
                               @RequestBody Contract contract){
        String userCode = tokenUtils.getCurrentUser(token).getUserCode();
        contract.setContractState("2");
        int i = contractService.updateContractState(contract);
        if(i>0){
            Flow flow = new Flow();
            flow.setContractId(contract.getContractId());
            return activitiService.completeTask(userCode, flow);
        }else{
            return Result.err(500,"修改合同状态失败");
        }
    }

    /**
     * 重新递交合同进行审核
     * @param token
     * @param contract 包括合同id
     * @return
     */
    @PostMapping("/contract-again")
    @BusLog(descrip = "重新审核")
    public Result contractAgain(@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token,
                               @RequestBody Contract contract){
        String userCode = tokenUtils.getCurrentUser(token).getUserCode();
        contract.setContractState("0");
        int i = contractService.updateContractState(contract);
        if(i>0){
            Flow flow = new Flow();
            flow.setContractId(contract.getContractId());
            return activitiService.completeTask(userCode, flow);
        }else{
            return Result.err(500,"修改合同状态失败");
        }
    }

    /**
     * 驳回合同
     * @param token
     * @param contractReasonDto 包括驳回原因
     * @return
     * @throws Exception
     */
    @PostMapping("/contract-reject")
    @BusLog(descrip = "合同驳回")
    public Result contractReject(@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token,
                                 @RequestBody ContractReasonDto contractReasonDto) throws Exception {
        String userCode = tokenUtils.getCurrentUser(token).getUserCode();
        Contract contract = new Contract();
        contract.setContractId(contractReasonDto.getContractId());
        contract.setContractState("1");
        int i = contractService.updateContractState(contract);
        if (i > 0) {
            Flow flow = new Flow();
            flow.setContractId(contract.getContractId());
            return activitiService.skipTask(userCode, contractReasonDto);
        } else {
            return Result.err(500, "修改合同状态失败");
        }
    }

    /**
     * 查询该合同生产产品所需要的原材料的用量
     * @param materialNumDto 包含合同id，材料id
     * @return
     */
    @GetMapping("/material-num")
    public Result getNeedMaterialNum(MaterialNumDto materialNumDto){
        return contractService.getNeedMaterialNum(materialNumDto);
    }
    
    @GetMapping("/contract-id")
    public Result getContractById(Contract contract){
        contract = contractService.findContractById(contract.getContractId());
        return Result.ok(contract);
    }
    
    @PostMapping("/contract-eginner-add")
    public Result addContractEginner(@RequestBody EginnerContractDto contractDto){
        System.out.println(contractDto);
        System.out.println(contractDto.getContractEginnerList());
        contractService.saveContractEginner(contractDto);
        return Result.ok();
    }

    @GetMapping("/contract-eginner-productNum")
    public Result contractEginnerProductNum(ContractEginner contractEginner){
        List<ContractEginner> contractEginnerList = contractEginnerService.selectContractEginnerProductNum(contractEginner);
        return Result.ok(contractEginnerList);
    }
}
    