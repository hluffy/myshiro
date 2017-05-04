package com.dk.shiro;

import junit.framework.Assert;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

public class Test {
	public static void main(String[] args) {
		Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
		System.out.println(factory);
		org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken("zhang","123");
		System.out.println(token);
		try {
			subject.login(token);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		Assert.assertEquals(true, subject.isAuthenticated());
		
		subject.logout();
		
		
	}
	

}
