package com.neuedu.controller.portal;

import com.neuedu.common.ServerResponse;
import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/manage/product")
public class UploadController {
    @Autowired
    IProductService productService;

    /*浏览器通过get请求访问*/
    @RequestMapping(value = "/upload",method = RequestMethod.GET)
    public String upload(){
        return "upload";//逻辑视图  Spring通过前缀+逻辑视图+后缀寻找
    }


    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload2(@RequestParam(value = "upload_file",required = false) MultipartFile file){

            String path = "F:\\business\\picture";
            return productService.upload(file,path);//逻辑视图  Spring通过前缀+逻辑视图+后缀寻找
        }




}
