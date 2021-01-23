package com.thkmon.bbwiki.doc.mapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.thkmon.common.database.SimpleDBMapper;
import com.thkmon.common.prototype.ObjList;

public class WikiDocTextMapper {

	
	public boolean addWikiText(Connection conn, String textId, String regTime, String textObj, String docId) throws SQLException, Exception {
		
		SimpleDBMapper dbMapper = new SimpleDBMapper();
		
		StringBuffer sqlBuff = new StringBuffer();
		sqlBuff.append(" INSERT INTO ");
		sqlBuff.append(" BBWK_TEXT ");
		sqlBuff.append(" ( TEXT_ID, REG_TIME, TEXT_OBJ, DOC_ID ) ");
		sqlBuff.append(" VALUES ");
		sqlBuff.append(" ( ?, ?, ?, ? ) ");
		
		ObjList objList = new ObjList();
		objList.addNotEmpty(textId, "textId");
		objList.addNotEmpty(regTime, "regTime");
		objList.addNotEmpty(textObj, "textObj");
		objList.addNotEmpty(docId, "docId");
		
		return dbMapper.insert(conn, sqlBuff.toString(), objList);
	}
}
