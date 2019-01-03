package net.dunotech.venus.system.service.common;

import net.dunotech.venus.system.utils.RedisUtils;
import net.dunotech.venus.system.utils.ValidateImgUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class ValidateLoginService {

    @Autowired
    private RedisUtils redisUtils;

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        String verify = request.getParameter("uuid");

        //生成随机字串
        String verifyCode = ValidateImgUtils.generateVerifyCode(4);
        //存入redis
        redisUtils.set(verify,verifyCode,10*60);
        //生成图片
        int w = 200, h = 80;
        ValidateImgUtils.outputImage(w, h, response.getOutputStream(), verifyCode);
    }
}
