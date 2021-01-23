package com.thkmon.bbwiki.doc.controller;

import java.sql.Connection;
import java.sql.SQLException;

import com.thkmon.bbwiki.doc.mapper.WikiDocHistoryMapper;
import com.thkmon.bbwiki.doc.mapper.WikiDocMapper;
import com.thkmon.common.database.SimpleDBUtil;
import com.thkmon.common.prototype.BasicMap;
import com.thkmon.common.prototype.BasicMapList;

public class WikiDocHistoryController {
	
	public BasicMapList getHistoryList(String docId) throws SQLException, Exception {
		BasicMapList resultMapList = null;
		
		Connection conn = null;
		String title = "";
		
		try {
			if (docId == null || docId.length() == 0) {
				return null;
			}
			
			WikiDocHistoryMapper historyMapper = new WikiDocHistoryMapper();
			
			conn = SimpleDBUtil.getConnection();
			
			WikiDocMapper docMapper = new WikiDocMapper();
			BasicMap wikiDoc = docMapper.getWikiDoc(conn, docId);
			if (wikiDoc != null) {
				title = wikiDoc.getString("TITLE", "");
				
				resultMapList = historyMapper.getHistoryList(conn, docId);
				
				if (resultMapList != null && resultMapList.size() > 0) {
					resultMapList.get(0).setString("TITLE", title);
				}
			}
			
		} catch (Exception e) {
			throw e;
			
		} finally {
			SimpleDBUtil.rollbackAndClose(conn);
		}
		
		return resultMapList;
	}
	
	
	public BasicMap getHistoryDoc(String docId, int modCount) throws Exception {
		BasicMap resultMap = null;
		
		Connection conn = null;
		WikiDocHistoryMapper docMapper = null;
		
		try {
			if (docId == null || docId.length() == 0) {
				return null;
			}
			
			conn = SimpleDBUtil.getConnection();
			docMapper = new WikiDocHistoryMapper();
			resultMap = docMapper.getHistoryDoc(conn, docId, modCount);
			
		} catch (Exception e) {
			throw e;
			
		} finally {
			SimpleDBUtil.rollbackAndClose(conn);
		}
		
		return resultMap;
	}
}