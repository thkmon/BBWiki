package com.thkmon.bbwiki.doc.controller;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.thkmon.bbwiki.doc.mapper.WikiDocHistoryMapper;
import com.thkmon.bbwiki.doc.mapper.WikiDocMapper;
import com.thkmon.common.database.SimpleDBUtil;
import com.thkmon.common.prototype.BasicMap;
import com.thkmon.common.prototype.BasicMapList;
import com.thkmon.common.util.DateUtil;
import com.thkmon.common.util.PageUtil;
import com.thkmon.common.util.StringUtil;

public class WikiDocController {
	
	public String writeWikiDoc(HttpServletRequest request, String docTitle, String docId, String docContent, String regUserName, int maxModCount, boolean bModifyMode) throws Exception {

		// 181222. 문서 작성시에만 문서 제목 체크
		if (!bModifyMode) {
			// 문서 제목 길이 체크
			if (docTitle == null || docTitle.trim().length() == 0) {
				return "문서 제목을 입력해주세요.";
				
			} else {
				docTitle = docTitle.trim();
				
				if (docTitle.indexOf("\r") > -1) {
					docTitle = docTitle.replace("\r", "");
				}
				
				if (docTitle.indexOf("\n") > -1) {
					docTitle = docTitle.replace("\n", "");
				}
				
				if (docTitle.length() > 80) {
					return "문서 제목을 80자 이내로 입력해주세요.";
				}
				
				if (docTitle.indexOf("<") > -1 || docTitle.indexOf(">") > -1) {
					return "문서 제목은 태그(< 또는 >)를 허용하지 않습니다. 다시 입력해주세요.";
				}
				
				// 181221. 문서 제목에 콜론 금지.
				if (docTitle.indexOf(":") > -1) {
					return "문서 제목은 콜론(:)을 허용하지 않습니다. 다시 입력해주세요.";
				}
				
				// 181221. 문서 제목에 탭 금지.
				if (docTitle.indexOf("\t") > -1) {
					return "문서 제목은 탭(Tab)을 허용하지 않습니다. 다시 입력해주세요.";
				}
			}
		}
		
		// 181222. 문서 수정시에만 문서 아이디 체크
		if (bModifyMode) {
			// 문서 아이디 길이 체크
			if (docId == null || docId.trim().length() == 0) {
				return "문서 수정 실패. 문서 아이디를 알 수 없습니다.";
				
			} else {
				docId = docId.trim();
				
				if (docId.length() > 50) {
					return "문서 수정 실패. 문서 아이디가 50자를 초과했습니다. docId == [" + docId + "]";
				}
				
				if (!StringUtil.checkEngNumber(docId)) {
					return "문서 수정 실패. 문서 아이디는 영어와 숫자만 허용합니다. docId == [" + docId + "]";
				}
			}
		}
		
		// 문서 내용 길이 체크
		if (docContent == null || docContent.trim().length() == 0) {
			return "문서 내용을 입력해주세요.";
			
		} else {
			docContent = docContent.trim();
			
			if (docContent.length() > 100000) {
				return "문서 내용을 10만자 이내로 입력해주세요.";
			}
		}
		
		String regUserId = "";
		if (request != null && request.getSession() != null) {
			// 작성자 아이디 : 세션이 우선한다.
			String hdwkUserId = StringUtil.parseString(request.getSession().getAttribute("hdwkUserId"));
			if (hdwkUserId != null && hdwkUserId.length() > 0) {
				regUserId = hdwkUserId;
			}

			// 작성자 이름 : 세션이 우선한다.
			String hdwkUserName = StringUtil.parseString(request.getSession().getAttribute("hdwkUserName"));
			if (hdwkUserName != null && hdwkUserName.length() > 0) {
				regUserName = hdwkUserName;
			}
		}
		
		// 작성자 아이디 : 클라이언트 전달값은 차선이다.
		if (regUserId == null || regUserId == "") {
			regUserId = "비회원";
		}
		
		// 작성자 이름 : 클라이언트 전달값은 차선이다.
		if (regUserName == null || regUserName.trim().length() == 0) {
			return "작성자를 입력해주세요.";
			
		} else {
			regUserName = regUserName.trim();
			
			if (regUserName.length() > 20) {
				return "작성자를 20자 이내로 입력해주세요.";
			}
		}
		
		String result = "0";
		
		Connection conn = null;
		WikiDocMapper docMapper = null;
		
		try {
			conn = SimpleDBUtil.getConnection();
			docMapper = new WikiDocMapper();
			
			String curDateTime = DateUtil.getDateTime();
			
			if (!bModifyMode) {
				// 181222. 작성 모드일 경우 독아이디를 새로 딴다.
				docId = "d" + curDateTime;
				
				boolean bDocExisting = docMapper.checkExistingWikiDoc(conn, docId);
				if (bDocExisting) {
					return "문서 아이디 생성에 실패했습니다. 잠시후 다시 시도해주세요. docId == [" + docId + "]";
				}
				
				String existingTitle = docMapper.getExistingWikiDocByTitle(conn, docTitle);
				if (existingTitle != null && existingTitle.length() > 0) {
					return "문서 생성에 실패했습니다. 이미 존재하는 제목입니다. docTitle == [" + existingTitle + "]";
				}
				
			} else {
				// 181222. 수정 모드일 경우 최신 상태인지 체크한다. 엎어쳐지는 현상 방지.
				WikiDocHistoryMapper docHistoryMapper = new WikiDocHistoryMapper();
				int oldMaxModCount = docHistoryMapper.getMaxModCount(conn, docId);
				if (maxModCount != oldMaxModCount) {
					return "최신 상태의 문서가 아닙니다! 중간에 다른 사용자가 수정했습니다. 본문을 잃어버리지 않도록 잘 복사해두시고, 문서를 다시 열어 작업해주세요.";
				}
			}

			boolean bAdding = docMapper.addWikiDoc(conn, docId, curDateTime, docTitle, docContent, regUserId, regUserName, bModifyMode);
			if (!bAdding) {
				return "문서 추가에 실패했습니다. 잠시후 다시 시도해주세요. docId == [" + docId + "]";
			}
			
			// 작성/수정 직후 문서로 이동할 수 있도록 독아이디를 내려준다.
			result = "1/" + docId;
			SimpleDBUtil.commitAndClose(conn);
			
		} catch (Exception e) {
			return "[SERVER ERROR] " + e.getMessage();
			
		} finally {
			SimpleDBUtil.rollbackAndClose(conn);
		}
		
		return result;
	}

	
	public BasicMap getWikiDoc(String docId) throws Exception {
		BasicMap resultMap = null;
		
		Connection conn = null;
		WikiDocMapper docMapper = null;
		
		try {
			if (docId == null || docId.length() == 0) {
				return null;
			}
			
			conn = SimpleDBUtil.getConnection();
			docMapper = new WikiDocMapper();
			resultMap = docMapper.getWikiDoc(conn, docId);
			
		} catch (Exception e) {
			throw e;
			
		} finally {
			SimpleDBUtil.rollbackAndClose(conn);
		}
		
		return resultMap;
	}
	
	
	public BasicMap getWikiDocByTitleOrId(String docLink) throws Exception {
		BasicMap resultMap = null;
		
		Connection conn = null;
		WikiDocMapper docMapper = null;
		
		try {
			if (docLink == null || docLink.length() == 0) {
				return null;
			}
			
			conn = SimpleDBUtil.getConnection();
			docMapper = new WikiDocMapper();
			
			// 1. 문서 제목으로 아이디 가져온다.
			String docId = docMapper.getDocIdByTitle(conn, docLink);
			if (docId != null && docId.length() > 0) {
				resultMap = docMapper.getWikiDoc(conn, docId);
				
			} else {
				// 2. 문서 제목으로 아이디 못 찾으면, 넘어온 값을 아이디로 간주하고 문서 가져오기 시도한다.
				docId = docLink;
				resultMap = docMapper.getWikiDoc(conn, docId);
			}
			
		} catch (Exception e) {
			throw e;
			
		} finally {
			SimpleDBUtil.rollbackAndClose(conn);
		}
		
		return resultMap;
	}
	
	
	public BasicMap getWikiDocForModify(String docId) throws Exception {
		BasicMap resultMap = null;
		
		Connection conn = null;
		WikiDocMapper docMapper = null;
		WikiDocHistoryMapper docHistoryMapper = null;
		
		try {
			if (docId == null || docId.length() == 0) {
				return null;
			}
			
			conn = SimpleDBUtil.getConnection();
			
			docMapper = new WikiDocMapper();
			resultMap = docMapper.getWikiDoc(conn, docId);
			
			docHistoryMapper = new WikiDocHistoryMapper();
			String maxModCount = StringUtil.parseString(docHistoryMapper.getMaxModCount(conn, docId), "0");
			resultMap.put("MAX_MOD_COUNT", maxModCount);
			
		} catch (Exception e) {
			throw e;
			
		} finally {
			SimpleDBUtil.rollbackAndClose(conn);
		}
		
		return resultMap;
	}
	
	
	public BasicMapList getWikiDocList(String searchValue, int pageNum, boolean bSearchUncomp) throws SQLException, Exception {
		BasicMapList resultMapList = null;
		
		Connection conn = null;
		WikiDocMapper docMapper = null;
		
		try {
			conn = SimpleDBUtil.getConnection();
			docMapper = new WikiDocMapper();
			
			resultMapList = docMapper.getWikiDocList(conn, searchValue, pageNum, bSearchUncomp);
			
			if (resultMapList != null && resultMapList.size() > 0) {
				if (!bSearchUncomp) {
					int totalCount = docMapper.getWikiDocCount(conn, searchValue);
					int totalPage = PageUtil.getTotalPage(totalCount, 15);
					
					BasicMap pageMap = new BasicMap();
					pageMap.put("totalCount", totalCount);
					pageMap.put("totalPage", totalPage);
					resultMapList.add(pageMap);
				}
			}
			
		} catch (Exception e) {
			throw e;
			
		} finally {
			SimpleDBUtil.rollbackAndClose(conn);
		}
		
		return resultMapList;
	}
}
