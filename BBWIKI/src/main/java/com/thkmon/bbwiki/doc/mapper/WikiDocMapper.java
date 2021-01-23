package com.thkmon.bbwiki.doc.mapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.thkmon.common.database.SimpleDBMapper;
import com.thkmon.common.prototype.BasicMap;
import com.thkmon.common.prototype.BasicMapList;
import com.thkmon.common.prototype.ObjList;

public class WikiDocMapper {
	
	public boolean checkExistingWikiDoc(Connection conn, String docId) throws SQLException, Exception {
		if (docId == null || docId.length() == 0) {
			throw new Exception("DocMapper checkExistingWikiDoc : docId is null or empty.");
		}
		
		SimpleDBMapper dbMapper = new SimpleDBMapper();
			
		StringBuffer sqlBuff = new StringBuffer();
		sqlBuff.append(" SELECT COUNT(DOC_ID) FROM BBWK_DOC WHERE DOC_ID = ? ");
		
		ObjList objList = new ObjList();
		objList.add(docId);
		
		int docCount = dbMapper.selectFirstInt(conn, sqlBuff.toString(), objList, 0);
		if (docCount > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * 새 제목으로 이미 존재하는 제목을 가져온다. 띄어쓰기를 제외하고 1개만 찾는다.
	 * 
	 * @param conn
	 * @param docTitle
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public String getExistingWikiDocByTitle(Connection conn, String docTitle) throws SQLException, Exception {
		if (docTitle == null || docTitle.length() == 0) {
			throw new Exception("DocMapper getExistingWikiDocByTitle : docTitle is null or empty.");
		}
		
		SimpleDBMapper dbMapper = new SimpleDBMapper();
			
		StringBuffer sqlBuff = new StringBuffer();
		sqlBuff.append(" SELECT TITLE FROM BBWK_DOC WHERE LOWER(REPLACE(TITLE, ' ', '')) = ? LIMIT 1 ");
		
		ObjList objList = new ObjList();
		objList.add(docTitle.replace(" ", "").toLowerCase());
		
		String oldTitle = dbMapper.selectFirstString(conn, sqlBuff.toString(), objList, "");
		return oldTitle;
	}
	
	
	public String getDocIdByTitle(Connection conn, String docTitle) throws SQLException, Exception {
		if (docTitle == null || docTitle.length() == 0) {
			throw new Exception("DocMapper getDocIdByTitle : docTitle is null or empty.");
		}
		
		SimpleDBMapper dbMapper = new SimpleDBMapper();
			
		StringBuffer sqlBuff = new StringBuffer();
		sqlBuff.append(" SELECT DOC_ID FROM BBWK_DOC WHERE LOWER(REPLACE(TITLE, ' ', '')) = ? LIMIT 1 ");
		
		ObjList objList = new ObjList();
		objList.add(docTitle.replace(" ", ""));
		
		String docId = dbMapper.selectFirstString(conn, sqlBuff.toString(), objList, "");
		return docId;
	}
	
	
	public boolean addWikiDoc(Connection conn, String docId, String curDateTime, String docTitle, String docContent,
							  String regUserId, String regUserName, boolean bModifyMode) throws SQLException, Exception {
		
		if (docId == null || docId.length() == 0) {
			throw new Exception("DocMapper addWikiDoc : docId is null or empty.");
		}
		
		String docSummary = "";
		if (docContent.length() > 2000) {
			docSummary = docContent.substring(0, 2000);
		} else {
			docSummary = docContent;
		}
		
		String textLen = String.valueOf(docContent.length());
		
		String beforeTextId = "empty";
		String afterTextId = "T" + curDateTime;
		
		boolean bResult = false;
		SimpleDBMapper dbMapper = new SimpleDBMapper();
		
		if (!bModifyMode) {
			StringBuffer sqlBuff = new StringBuffer();
			sqlBuff.append(" INSERT INTO ");
			sqlBuff.append(" BBWK_DOC ");
			sqlBuff.append(" ( DOC_ID, TITLE, SUMMARY, TEXT_ID, ");
			sqlBuff.append("   REG_USER_ID, REG_USER_NAME, REG_TIME, ");
			sqlBuff.append("   LAST_MOD_USER_ID, LAST_MOD_USER_NAME, LAST_MOD_TIME, ");
			sqlBuff.append("   IS_DELETE, TEXT_LEN ) ");
			sqlBuff.append(" VALUES ");
			sqlBuff.append(" ( ?, ?, ?, ?, ");
			sqlBuff.append("   ?, ?, ?, ");
			sqlBuff.append("   ?, ?, ?, ");
			sqlBuff.append("   ?, ? ) ");
			
			ObjList objList = new ObjList();
			objList.addNotEmpty(docId, "docId");
			objList.addNotEmpty(docTitle, "docTitle");
			objList.addNotEmpty(docSummary, "docSummary");
			objList.addNotEmpty(afterTextId, "textId");
			
			objList.addNotEmpty(regUserId, "regUserId");
			objList.addNotEmpty(regUserName, "regUserName");
			objList.addNotEmpty(curDateTime, "curDateTime");
			
			objList.add("");
			objList.add("");
			// 181221. LAST_MOD_TIME 에 등록일자 넣도록 수정. 이유는 최초등록시 정렬이 안되기 때문.
			objList.addNotEmpty(curDateTime, "lastModTime");
			
			objList.add("0");
			objList.addNotEmpty(textLen, "textLen");
			
			bResult = dbMapper.insert(conn, sqlBuff.toString(), objList);
			
		} else {
			StringBuffer sqlBuff = new StringBuffer();
			sqlBuff.append(" SELECT TEXT_ID FROM BBWK_DOC WHERE DOC_ID = ? ");
			
			ObjList objList = new ObjList();
			objList.addNotEmpty(docId, "docId");
			beforeTextId = dbMapper.selectFirstString(conn, " SELECT TEXT_ID FROM BBWK_DOC WHERE DOC_ID = ? ", objList, "unknown");
			
			
			sqlBuff = new StringBuffer();
			sqlBuff.append(" UPDATE BBWK_DOC SET ");
			sqlBuff.append("   SUMMARY = ?, ");
			sqlBuff.append("   TEXT_ID = ?, ");
			
			sqlBuff.append("   LAST_MOD_USER_ID = ?, ");
			sqlBuff.append("   LAST_MOD_USER_NAME = ?, ");
			sqlBuff.append("   LAST_MOD_TIME = ?, ");
			sqlBuff.append("   TEXT_LEN = ? ");
			
			sqlBuff.append(" WHERE DOC_ID = ? ");
			
			objList = new ObjList();
			objList.addNotEmpty(docSummary, "docSummary");
			objList.addNotEmpty(afterTextId, "textId");
			
			objList.addNotEmpty(regUserId, "regUserId");
			objList.addNotEmpty(regUserName, "regUserName");
			objList.addNotEmpty(curDateTime, "curDateTime");
			objList.addNotEmpty(textLen, "textLen");
			
			objList.addNotEmpty(docId, "docId");
			
			bResult = dbMapper.insert(conn, sqlBuff.toString(), objList);
		}
		
		if (bResult) {
			WikiDocTextMapper docTextMapper = new WikiDocTextMapper();
			bResult = docTextMapper.addWikiText(conn, afterTextId, curDateTime, docContent, docId);
			
			if (bResult) {
				WikiDocHistoryMapper historyMapper = new WikiDocHistoryMapper();
				bResult = historyMapper.addWikiDocHistory(conn, docId, curDateTime, regUserId, regUserName, beforeTextId, afterTextId);
			}
		}
		
		return bResult;
	}
	
	
	public BasicMap getWikiDoc(Connection conn, String docId) throws SQLException, Exception {
		
		if (docId == null || docId.length() == 0) {
			throw new Exception("DocMapper getWikiDoc : docId is null or empty.");
		}
		
		SimpleDBMapper dbMapper = new SimpleDBMapper();
		
		StringBuffer sqlBuff = new StringBuffer();
		sqlBuff.append(" SELECT ");
		sqlBuff.append("   D.*, ");
		sqlBuff.append("   (SELECT TEXT_OBJ ");
		sqlBuff.append("    FROM BBWK_TEXT T ");
		sqlBuff.append("    WHERE T.TEXT_ID = D.TEXT_ID) TEXT_OBJ ");
		sqlBuff.append(" FROM BBWK_DOC D WHERE D.DOC_ID = ? ");
		
		ObjList objList = new ObjList();
		objList.addNotEmpty(docId, "docId");
		
		BasicMap basicMap = dbMapper.selectFirstRow(conn, sqlBuff.toString(), objList);
		return basicMap;
	}
	
	
	/**
	 * 문서 목록 가져오기
	 * 
	 * @param conn
	 * @param searchValue
	 * @param pageNum
	 * @param bSearchUncomp
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public BasicMapList getWikiDocList(Connection conn, String searchValue, int pageNum, boolean bSearchUncomp) throws SQLException, Exception {
		
		SimpleDBMapper dbMapper = new SimpleDBMapper();
		
		StringBuffer sqlBuff = new StringBuffer();
		ObjList objList = null;
		
		// 1. 미완성위키 조회이면 검색어 무시
		if (bSearchUncomp) {
			sqlBuff.append(" SELECT ");
			sqlBuff.append("   D.* ");
			sqlBuff.append(" FROM BBWK_DOC D ");
			sqlBuff.append(" ORDER BY LPAD(TEXT_LEN, 10, '0') ASC LIMIT 50 ");
		
		} else {
			int endRow = (pageNum * 15);
			int beginRow = endRow - 14;
			
			objList= new ObjList();
			
			sqlBuff.append(" SELECT ");
			sqlBuff.append("   A.* ");
			sqlBuff.append(" FROM ( ");
			sqlBuff.append("   SELECT ");
			sqlBuff.append("     D.*, ");
			sqlBuff.append("     @rownum:=@rownum+1 rnum ");
			sqlBuff.append("   FROM BBWK_DOC D ");
			if (searchValue != null && searchValue.length() > 0) {
				sqlBuff.append("   WHERE REPLACE(LOWER(TITLE), ' ', '') LIKE ? ");
				objList.add("%" + searchValue.toLowerCase().replace(" ", "") + "%");
			}
			sqlBuff.append("   ORDER BY LAST_MOD_TIME DESC ");
			sqlBuff.append(" ) A ");
			sqlBuff.append(" WHERE ? <= A.rnum AND A.rnum <= ? ");
			objList.add(beginRow);
			objList.add(endRow);
			sqlBuff.append(" ORDER BY A.LAST_MOD_TIME DESC ");
		}
		
		BasicMapList basicMapList = dbMapper.select(conn, sqlBuff.toString(), objList);
		return basicMapList;
	}
	
	
	/**
	 * 문서 카운트 가져오기
	 * 
	 * @param conn
	 * @param searchValue
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public int getWikiDocCount(Connection conn, String searchValue) throws SQLException, Exception {
		
		SimpleDBMapper dbMapper = new SimpleDBMapper();
		
		StringBuffer sqlBuff = new StringBuffer();
		ObjList objList = null;
		
		sqlBuff.append("   SELECT ");
		sqlBuff.append("     COUNT(DOC_ID) ");
		sqlBuff.append("   FROM BBWK_DOC D ");
		if (searchValue != null && searchValue.length() > 0) {
			sqlBuff.append("   WHERE REPLACE(LOWER(TITLE), ' ', '') LIKE ? ");
			objList= new ObjList();
			objList.add("%" + searchValue.toLowerCase().replace(" ", "") + "%");
		}
		
		int totalCount = dbMapper.selectFirstInt(conn, sqlBuff.toString(), objList, 0);
		return totalCount;
	}
}
