package com.thkmon.bbwiki.doc.mapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.thkmon.common.database.SimpleDBMapper;
import com.thkmon.common.prototype.BasicMap;
import com.thkmon.common.prototype.BasicMapList;
import com.thkmon.common.prototype.ObjList;

public class WikiDocHistoryMapper {
	
	
	public boolean addWikiDocHistory(Connection conn, String docId, String historyTime, String userId, String userName, String beforeTextId, String afterTextId) throws SQLException, Exception {
		
		int modCount = this.getMaxModCount(conn, docId);
		
		int nextModCount = modCount + 1;
		String historyId = docId + "~" + nextModCount;
		
		StringBuffer sqlBuff = new StringBuffer();
		sqlBuff.append(" INSERT INTO ");
		sqlBuff.append(" BBWK_DOC_HISTORY ");
		sqlBuff.append(" ( HISTORY_ID, HISTORY_TIME, DOC_ID, USER_ID, USER_NAME, ");
		sqlBuff.append("   BEFORE_TEXT_ID, AFTER_TEXT_ID, MOD_COUNT ) ");
		sqlBuff.append(" VALUES ");
		sqlBuff.append(" ( ?, ?, ?, ?, ?, ");
		sqlBuff.append("   ?, ?, ? ) ");
		
		ObjList objList = new ObjList();
		objList.addNotEmpty(historyId, "historyId");
		objList.addNotEmpty(historyTime, "historyTime");
		objList.addNotEmpty(docId, "docId");
		objList.addNotEmpty(userId, "userId");
		objList.addNotEmpty(userName, "userName");
		
		if (nextModCount > 1) {
			objList.addNotEmpty(beforeTextId, "beforeTextId");
		} else {
			objList.add(beforeTextId);
		}
		
		objList.addNotEmpty(afterTextId, "afterTextId");
		objList.add(nextModCount);
		
		SimpleDBMapper dbMapper = new SimpleDBMapper();
		return dbMapper.insert(conn, sqlBuff.toString(), objList);
	}
	
	
	public int getMaxModCount(Connection conn, String docId) throws SQLException, Exception {
		
		// 181221. 문서 수정 10번 초과하면 업데이트 안되는 버그 수정.
		StringBuffer sqlBuff = new StringBuffer();
		sqlBuff.append(" SELECT MAX(LPAD(MOD_COUNT, 6, '0')) ");
		sqlBuff.append(" FROM BBWK_DOC_HISTORY ");
		sqlBuff.append(" WHERE DOC_ID = ? ");
		
		ObjList objList = new ObjList();
		objList.add(docId);
		
		SimpleDBMapper dbMapper = new SimpleDBMapper();
		int modCount = dbMapper.selectFirstInt(conn, sqlBuff.toString(), objList, 0);
		return modCount;
	}
	
	
	public BasicMapList getHistoryList(Connection conn, String docId) throws SQLException, Exception {
		
		StringBuffer sqlBuff = new StringBuffer();
		sqlBuff.append(" SELECT * ");
		sqlBuff.append(" FROM BBWK_DOC_HISTORY ");
		sqlBuff.append(" WHERE DOC_ID = ? ");
		sqlBuff.append(" ORDER BY LPAD(MOD_COUNT, 6, '0') DESC ");
		
		ObjList objList = new ObjList();
		objList.add(docId);
		
		SimpleDBMapper dbMapper = new SimpleDBMapper();
		BasicMapList resultMapList = dbMapper.select(conn, sqlBuff.toString(), objList);
		return resultMapList;
	}
	
	
	public BasicMap getHistoryDoc(Connection conn, String docId, int modCount) throws SQLException, Exception {
		
		if (docId == null || docId.length() == 0) {
			throw new Exception("DocHistoryMapper getHistoryDoc : docId is null or empty.");
		}
		
		SimpleDBMapper dbMapper = new SimpleDBMapper();
		
		StringBuffer sqlBuff = new StringBuffer();
		sqlBuff.append(" SELECT ");
		sqlBuff.append("   D.*, ");
		sqlBuff.append("   (SELECT TEXT_OBJ ");
		sqlBuff.append("    FROM BBWK_TEXT T ");
		sqlBuff.append("    WHERE T.TEXT_ID = ");
		
		sqlBuff.append("      ( SELECT AFTER_TEXT_ID ");
		sqlBuff.append("      FROM BBWK_DOC_HISTORY ");
		sqlBuff.append("      WHERE DOC_ID = ? AND MOD_COUNT = ? ) ");
		
		sqlBuff.append("    ) TEXT_OBJ ");
		
		sqlBuff.append(" FROM BBWK_DOC D WHERE D.DOC_ID = ? ");
		
		ObjList objList = new ObjList();
		objList.add(docId);
		objList.add(modCount);
		objList.add(docId);
		
		BasicMap basicMap = dbMapper.selectFirstRow(conn, sqlBuff.toString(), objList);
		return basicMap;
	}
}
