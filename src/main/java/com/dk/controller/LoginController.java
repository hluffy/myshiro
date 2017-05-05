package com.dk.controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

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
	
	/** 
     * �û���¼ 
     */  
    @RequestMapping("login.ll")  
    public String login(HttpServletRequest request){  
//        String resultPageURL = InternalResourceViewResolver.FORWARD_URL_PREFIX + "/";  
    	String resultPageURL = "";
        String username = request.getParameter("username");  
        String password = request.getParameter("password");  
        //��ȡHttpSession�е���֤��  
//        String verifyCode = (String)request.getSession().getAttribute("verifyCode");  
        //��ȡ�û���������������֤��  
//        String submitCode = WebUtils.getCleanParam(request, "verifyCode");  
//        System.out.println("�û�[" + username + "]��¼ʱ�������֤��Ϊ[" + submitCode + "],HttpSession�е���֤��Ϊ[" + verifyCode + "]");  
//        if (StringUtils.isEmpty(submitCode) || !StringUtils.equals(verifyCode, submitCode.toLowerCase())){
//            request.setAttribute("message_login", "��֤�벻��ȷ");  
//            return resultPageURL;  
//        }  
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);  
        token.setRememberMe(true);  
//        System.out.println("Ϊ����֤��¼�û�����װ��tokenΪ" + ReflectionToStringBuilder.toString(token, ToStringStyle.MULTI_LINE_STYLE));  
        //��ȡ��ǰ��Subject  
        Subject currentUser = SecurityUtils.getSubject();  
        try {  
            //�ڵ�����login������,SecurityManager���յ�AuthenticationToken,�����䷢�͸������õ�Realmִ�б������֤���  
            //ÿ��Realm�����ڱ�Ҫʱ���ύ��AuthenticationTokens������Ӧ  
            //������һ���ڵ���login(token)����ʱ,�����ߵ�MyRealm.doGetAuthenticationInfo()������,������֤��ʽ����˷���  
            System.out.println("���û�[" + username + "]���е�¼��֤..��֤��ʼ");  
            currentUser.login(token);  
            System.out.println("���û�[" + username + "]���е�¼��֤..��֤ͨ��");  
            resultPageURL = "main";  
        }catch(UnknownAccountException uae){  
            System.out.println("���û�[" + username + "]���е�¼��֤..��֤δͨ��,δ֪�˻�");  
            request.setAttribute("message_login", "δ֪�˻�");  
        }catch(IncorrectCredentialsException ice){  
            System.out.println("���û�[" + username + "]���е�¼��֤..��֤δͨ��,�����ƾ֤");  
            request.setAttribute("message_login", "���벻��ȷ");  
        }catch(LockedAccountException lae){  
            System.out.println("���û�[" + username + "]���е�¼��֤..��֤δͨ��,�˻�������");  
            request.setAttribute("message_login", "�˻�������");  
        }catch(ExcessiveAttemptsException eae){  
            System.out.println("���û�[" + username + "]���е�¼��֤..��֤δͨ��,�����������");  
            request.setAttribute("message_login", "�û�������������������");  
        }catch(AuthenticationException ae){  
            //ͨ������Shiro������ʱAuthenticationException�Ϳ��Կ����û���¼ʧ�ܻ��������ʱ���龰  
            System.out.println("���û�[" + username + "]���е�¼��֤..��֤δͨ��,��ջ�켣����");  
            ae.printStackTrace();  
            request.setAttribute("message_login", "�û��������벻��ȷ");  
        }  
        //��֤�Ƿ��¼�ɹ�  
        if(currentUser.isAuthenticated()){  
            System.out.println("�û�[" + username + "]��¼��֤ͨ��(������Խ���һЩ��֤ͨ�����һЩϵͳ������ʼ������)");  
        }else{  
            token.clear();  
        }  
        return resultPageURL;  
    }  
    
//    @RequestMapping("open.ll")
//    public String openLogin(){
//    	return "../login.jsp";
//    }

}
