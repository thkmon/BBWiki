package com.thkmon.bbwiki.login.model;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.thkmon.common.util.StringUtil;

@Controller
public class WikiLoginModel {
	
	@RequestMapping(value = "/wiki/login/xe.do", method = {RequestMethod.GET, RequestMethod.POST})
	public String docList(Locale locale, Model model, HttpServletRequest request) throws Exception {
		
		System.out.println("login");
		
		try {
			String encId = StringUtil.parseString(request.getParameter("encId"));
			String encName = request.getParameter("encName");
			
			if (encId != null && encId.length() > 0) {
				if (encName != null && encName.length() > 0) {
					System.out.println("setAttribute / hdwkUserId == [" + encId + "]");
					System.out.println("setAttribute / hdwkUserName == [" + encName + "]");
					
					request.getSession().setAttribute("hdwkUserId", encId);
					request.getSession().setAttribute("hdwkUserName", encName);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "wiki/doc/list/wikidoc_pre_list.jsp";
	}
}
