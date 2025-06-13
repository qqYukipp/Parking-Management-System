package com.cgr.utils;

import com.alibaba.fastjson2.JSON;
import com.cgr.ResponseModel;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static com.cgr.constant.Constants.CONTENT_TYPE_JSON;

public class ResponseUtil {
    public static void write(HttpServletResponse response, ResponseModel model) throws IOException {
        //响应头设置JSON
        response.setContentType(CONTENT_TYPE_JSON);
        //创建输出流对象
        PrintWriter writer = response.getWriter();
        //构建JSON格式数据
        String json = JSON.toJSONString(model);
        //输出JSON格式数据
        writer.println(json);
    }
}
