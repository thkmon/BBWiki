package com.thkmon.ddoc.doc.model;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DDocReadModel {
	
	
	@RequestMapping(value = "/ddoc/doc/write.do", method = {RequestMethod.GET, RequestMethod.POST})
	public String ddocWrite(Locale locale, Model model, HttpServletRequest request) throws Exception {
				
		return "ddoc/doc/write/ddoc_write.jsp";
	}
}
