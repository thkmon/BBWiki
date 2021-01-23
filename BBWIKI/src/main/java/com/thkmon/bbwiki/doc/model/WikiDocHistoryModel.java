package com.thkmon.bbwiki.doc.model;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.thkmon.bbwiki.doc.controller.WikiDocHistoryController;
import com.thkmon.common.prototype.BasicMap;
import com.thkmon.common.prototype.BasicMapList;
import com.thkmon.common.util.StringUtil;

@Controller
public class WikiDocHistoryModel {
	
	
	@RequestMapping(value = "/wiki/doc/history.do", method = {RequestMethod.GET, RequestMethod.POST})
	public String docHistory(Locale locale, Model model, HttpServletRequest request) throws Exception {
		
		System.out.println("docHistory");
		
		StringBuffer buff = new StringBuffer();
		
		String docTitle = "";
		String docId = StringUtil.parseString(request.getParameter("doc_id"));
		
		try {
			WikiDocHistoryController historyCtrl = new WikiDocHistoryController();
			BasicMapList resultMapList = historyCtrl.getHistoryList(docId);
			
			if (resultMapList != null && resultMapList.size() > 0) {
				docTitle = resultMapList.get(0).getString("TITLE", "");
				
				int count = resultMapList.size();
				for (int i=0; i<count; i++) {
					BasicMap map = resultMapList.get(i);
					if (map == null) {
						continue;
					}
					
					String historyTime = map.getString("HISTORY_TIME", "").trim();
					
					String userId = map.getString("USER_ID", "").trim();
					String userName = map.getString("USER_NAME", "").trim();
					
					String modCount = map.getString("MOD_COUNT", "").trim();
					
					buff.append("<div class=\"doc_box\">");
					buff.append("<a href=\"/wiki/doc/read.do?doc_id=" + docId + "&mod_count=" + modCount + "\">" + "[리비전 " + modCount + "] " + StringUtil.getDateString(historyTime) + " : " + userName + "(" + userId + ") 수정" + "</a>");
					buff.append("</div>");
				}
				
			} else {
				buff.append("히스토리가 존재하지 않습니다.");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("docTitle", docTitle);
		model.addAttribute("historyList", buff.toString());
		model.addAttribute("docId", docId);
		
		return "wiki/doc/history/wikidoc_history.jsp";
	}
}

