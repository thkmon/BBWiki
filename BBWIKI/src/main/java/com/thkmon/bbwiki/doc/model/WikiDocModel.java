package com.thkmon.bbwiki.doc.model;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thkmon.bbwiki.doc.controller.WikiDocController;
import com.thkmon.bbwiki.doc.controller.WikiDocHistoryController;
import com.thkmon.bbwiki.doc.helper.WikiDocTextHelper;
import com.thkmon.common.prototype.BasicMap;
import com.thkmon.common.prototype.BasicMapList;
import com.thkmon.common.util.StringUtil;

@Controller
public class WikiDocModel {
	
	
	@RequestMapping(value = "/wiki/doc/list.do", method = {RequestMethod.GET, RequestMethod.POST})
	public String docList(Locale locale, Model model, HttpServletRequest request) throws Exception {
		
		System.out.println("docList");
		
		String docListTitle = "";
		
		try {
			StringBuffer buff = new StringBuffer();
			
			int pageNum = 0;
			int totalCount = 0;
			int totalPage = 0;
			String searchMode = "";
			String searchTitle = "";
			
			int fromReadPage = StringUtil.parseNumber(request.getParameter("from_read_page"), 0);
			if (fromReadPage == 1) {
				pageNum = StringUtil.parseNumber(request.getSession().getAttribute("pageNum"), 1);
				searchMode = StringUtil.parseString(request.getSession().getAttribute("searchMode"));
				searchTitle = StringUtil.parseString(request.getSession().getAttribute("searchTitle"));
				
			} else {
				pageNum = StringUtil.parseNumber(request.getParameter("page_num"), 1);
				searchMode = StringUtil.parseString(request.getParameter("search_mode"));
				searchTitle = StringUtil.parseString(request.getParameter("search_title"));
				
				request.getSession().setAttribute("pageNum", pageNum);
				request.getSession().setAttribute("searchMode", searchMode);
				request.getSession().setAttribute("searchTitle", searchTitle);
			}
			
			// 파라미터 예외처리
			if (pageNum < 1) {
				pageNum = 1;
			}
			
			if (searchTitle != null && searchTitle.trim().length() > 0) {
				searchTitle = searchTitle.trim();
				
				if (searchTitle.indexOf("%") > -1) {
					searchTitle = searchTitle.replace("%", "");
				}
			}
			
			boolean bSearchUncomp = (searchMode != null && searchMode.equals("uncomp"));
			
			if (bSearchUncomp) {
				docListTitle = "완성을 기다리는 위키 목록";
				
			} else if (searchTitle != null && searchTitle.length() > 0) {
				docListTitle = "위키 검색결과";
					
			} else {
				docListTitle = "지금까지 기록된 위키 목록";
			}
			
			WikiDocController docCtrl = new WikiDocController();
			BasicMapList resultMapList = docCtrl.getWikiDocList(searchTitle, pageNum, bSearchUncomp);
			
			if (resultMapList != null && resultMapList.size() > 0) {
				
				BasicMap pageMap = null;
				
				// 페이징 처리
				if (!bSearchUncomp) {
					pageMap = resultMapList.get(resultMapList.getSize() - 1);
					resultMapList.remove(resultMapList.getSize() - 1);
					totalCount = pageMap.getInt("totalCount", 0);
					totalPage = pageMap.getInt("totalPage", 0);
				}
				
				int count = resultMapList.size();
				for (int i=0; i<count; i++) {
					BasicMap map = resultMapList.get(i);
					if (map == null) {
						continue;
					}
					
					String docId = map.getString("DOC_ID", "").trim();
					if (docId == null || docId.length() == 0) {
						continue;
					}
					
					String title = map.getString("TITLE", "").trim();
					if (title == null || title.length() == 0) {
						continue;
					}
					
					String summary = "";
					
					if (bSearchUncomp) {
						// 미완성위키 조회할 경우 내용을 같이 표시해준다.
						summary = map.getString("SUMMARY", "").trim();
						if (summary.length() > 100) {
							summary = summary.substring(0, 100) + "...";
						}
						
						summary = StringUtil.removeTag(summary);
					}
					
					buff.append("<div class=\"doc_box\">");
					buff.append("<a href=\"/wiki/doc/read.do?doc_id=" + docId + "\">" + title + "</a>");
					
					if (summary != null && summary.length() > 0) {
						buff.append(" : " + summary);
					}
					
					buff.append("</div>");
				}
				
				if (count >= 50 && bSearchUncomp) {
					buff.append("<br>");
					buff.append("<br>");
					buff.append("* 50개까지만 표시합니다.");
					buff.append("<br>");
				}
			}
			
			model.addAttribute("pageNum", pageNum);
			model.addAttribute("totalCount", totalCount);
			model.addAttribute("totalPage", totalPage);
			
			model.addAttribute("searchMode", searchMode);
			model.addAttribute("searchTitle", searchTitle);
			model.addAttribute("docList", buff.toString());
			model.addAttribute("docListTitle", docListTitle);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "wiki/doc/list/wikidoc_list.jsp";
	}
	
	
	@RequestMapping(value = "/wiki/doc/write.do", method = {RequestMethod.GET, RequestMethod.POST})
	public String docWrite(Locale locale, Model model, HttpServletRequest request) throws Exception {
		
		System.out.println("docWrite");
		
		String docId = StringUtil.parseString(request.getParameter("doc_id"));
		if (docId != null && docId.length() > 0) {
			WikiDocController docCtrl = new WikiDocController();
			BasicMap resultMap = docCtrl.getWikiDocForModify(docId);
			if (resultMap != null) {
				String title = StringUtil.parseString(resultMap.get("TITLE"));
				String textObj = StringUtil.parseString(resultMap.get("TEXT_OBJ"));
				String maxModCount = StringUtil.parseString(resultMap.get("MAX_MOD_COUNT"));
				
				model.addAttribute("docId", docId);
				model.addAttribute("title", title);
				model.addAttribute("textObj", textObj);
				model.addAttribute("maxModCount", maxModCount);
				model.addAttribute("modifyMode", "1");
			}
		}
		
		return "wiki/doc/write/wikidoc_write.jsp";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/wiki/doc/write_req.do", produces="application/text; charset=UTF8", method = {RequestMethod.GET, RequestMethod.POST})
	public String docWriteReq(Locale locale, Model model, HttpServletRequest request) throws Exception {
		
		System.out.println("docWriteReq");
		
		String docTitle = StringUtil.parseString(request.getParameter("doc_title"));
		String docContent = StringUtil.parseString(request.getParameter("doc_content"));
		String regUserName = StringUtil.parseString(request.getParameter("reg_user_name"));
		
		String docId = "";
		int maxModCount = 0;
		boolean bModifyMode = StringUtil.parseString(request.getParameter("modify_mode")).equals("1");
		if (bModifyMode) {
			docId = StringUtil.parseString(request.getParameter("doc_id"));
			maxModCount = StringUtil.parseNumber(request.getParameter("max_mod_count"), 0);
		}
		
		WikiDocController docCtrl = new WikiDocController();
		String result = docCtrl.writeWikiDoc(request, docTitle, docId, docContent, regUserName, maxModCount, bModifyMode);
		return result;
	}
	
	
	@RequestMapping(value = "/wiki/doc/read.do", method = {RequestMethod.GET, RequestMethod.POST})
	public String docRead(Locale locale, Model model, HttpServletRequest request) throws Exception {
		
		System.out.println("docRead");
		
		String docId = "";
		String title = "문서가 존재하지 않습니다.";
		String textObj = "";
		String readMode = "0";
		int modCount = -1;
		
		BasicMap resultMap = null;
		
		// 1. docId로 가져오기
		if (resultMap == null) {
			docId = StringUtil.parseString(request.getParameter("doc_id"));
			if (docId != null && docId.length() > 0) {
				modCount = StringUtil.parseNumber(request.getParameter("mod_count"), -1);
				if (modCount == -1) {
					WikiDocController docCtrl = new WikiDocController();
					resultMap = docCtrl.getWikiDoc(docId);
				} else {
					WikiDocHistoryController historyCtrl = new WikiDocHistoryController();
					resultMap = historyCtrl.getHistoryDoc(docId, modCount);
				}
			}
		}
		
		// 2. docLink로 가져오기 (우선 제목으로 찾고, 다음으로 아이디로 찾는다)
		if (resultMap == null) {
			String docLink = StringUtil.parseString(request.getParameter("doc_link"));
			if (docLink != null && docLink.length() > 0) {
				WikiDocController docCtrl = new WikiDocController();
				resultMap = docCtrl.getWikiDocByTitleOrId(docLink);
				if (resultMap != null) {
					docId = StringUtil.parseString(resultMap.get("DOC_ID"));
				}
			}
		}
		
		
		if (resultMap != null && docId != null && docId.length() > 0) {
			title = StringUtil.parseString(resultMap.get("TITLE"));
			textObj = StringUtil.parseString(resultMap.get("TEXT_OBJ"));
			
			textObj = WikiDocTextHelper.replaceWikiMarkForLine(textObj);
			textObj = WikiDocTextHelper.replaceWikiMarkForTitle(textObj, 5);
			textObj = WikiDocTextHelper.replaceWikiMarkForTitle(textObj, 4);
			textObj = WikiDocTextHelper.replaceWikiMarkForTitle(textObj, 3);
			textObj = WikiDocTextHelper.replaceWikiMarkForTitle(textObj, 2);
			textObj = WikiDocTextHelper.replaceWikiMarkForLink(textObj);
			
			readMode = "1";
		}
		
		model.addAttribute("docId", docId);
		model.addAttribute("title", title);
		model.addAttribute("textObj", textObj);
		model.addAttribute("readMode", readMode);
		model.addAttribute("modCount", modCount);
		
		return "wiki/doc/read/wikidoc_read.jsp";
	}
}