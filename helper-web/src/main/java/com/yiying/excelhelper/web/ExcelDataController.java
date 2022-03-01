package com.yiying.excelhelper.web;

import com.yiying.excelhelper.entities.User;
import com.yiying.excelhelper.model.ExcelDataModel;
import com.yiying.excelhelper.service.ExcelDataService;
import com.yiying.excelhelper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author yiying
 */
@RestController("/excel")
public class ExcelDataController {
    @Autowired
    ExcelDataService dataService;
    @Autowired
    UserService userService;

    public ExcelDataController(ExcelDataService dataService) {
        this.dataService = dataService;
    }

    /**
     * 模板下载
     * @param response
     * @throws Exception
     */
    @GetMapping("template")
    public void download(HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("template", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        dataService.exportExcelTemplate(response.getOutputStream(), fileName, ExcelDataModel.class);
    }

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public String upload(MultipartFile file) throws IOException {
        dataService.read(file.getInputStream(), ExcelDataModel.class);
        return "success";
    }


    /**
     * 导出用户信息
     * @param response
     * @throws Exception
     */
    @GetMapping("userInfos")
    public void exportUsers(HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("template", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        ResponseEntity<Object> responseEntity = userService.search(new User(), Pageable.unpaged());
        if (responseEntity.hasBody()){
            Page<User> body = (Page<User>) responseEntity.getBody();
            dataService.write(response.getOutputStream(),ExcelDataModel.class, body.getContent());
        }
    }
}
