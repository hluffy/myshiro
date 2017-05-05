package com.dk.controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dk.util.VerifyCodeUtil;

@Controller
@RequestMapping("login")
public class LoginController {
	@RequestMapping("get.ll")
	@ResponseBody
	public void getVerifyCodeImage(HttpServletRequest request, HttpServletResponse response) throws IOException {  
        //����ҳ�治����  
        response.setHeader("Pragma", "no-cache");  
        response.setHeader("Cache-Control", "no-cache");  
        response.setDateHeader("Expires", 0);  
        String verifyCode = VerifyCodeUtil.generateTextCode(VerifyCodeUtil.TYPE_NUM_ONLY, 4, null);  
        //����֤��ŵ�HttpSession����  
        request.getSession().setAttribute("verifyCode", verifyCode);  
        System.out.println("�������ɵ���֤��Ϊ[" + verifyCode + "],�Ѵ�ŵ�HttpSession��");  
        //������������ݵ�����ΪJPEGͼ��  
        response.setContentType("image/jpeg");  
        BufferedImage bufferedImage = VerifyCodeUtil.generateImageCode(verifyCode, 90, 30, 9, true, Color.WHITE, Color.BLACK, null);  
        //д�������  
        ImageIO.write(bufferedImage, "JPEG", response.getOutputStream());  
    }  

}
