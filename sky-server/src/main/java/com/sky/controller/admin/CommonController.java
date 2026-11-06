package com.sky.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

//通用接口
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {
  @Autowired
  private AliOssUtil ossUtil;

  /**
   * 上传文件
   * 
   * @return
   */
  @PostMapping("/upload")
  @ApiOperation(value = "上传文件")
  public Result<String> upload(@RequestParam MultipartFile file) {
    log.info("上传文件：{}", file.getOriginalFilename());
    // 上传文件到OSS
    try {
      // 原始文件名
      String originalFilename = file.getOriginalFilename();
      // 截取文件名
      String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
      //构建新文件
      String fileName = UUID.randomUUID().toString() + extension;
      //文件请求路径
      String filePath = ossUtil.upload(file.getBytes(), fileName);
      return Result.success(filePath);
    } catch (Exception e) {
      log.error("文件上传失败:{}",e);
    }

    return Result.error(MessageConstant.UPLOAD_FAILED);
  }
}
