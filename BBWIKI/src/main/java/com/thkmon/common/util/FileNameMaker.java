package com.thkmon.common.util;

import java.io.File;

import com.oreilly.servlet.multipart.ExceededSizeException;
import com.oreilly.servlet.multipart.FileRenamePolicy;
import com.thkmon.common.prototype.StringList;

public class FileNameMaker implements FileRenamePolicy {

	private StringList originFilePathList = new StringList();
	private StringList newFilePathList = new StringList();
	
	public StringList getOriginFilePathList() {
		return originFilePathList;
	}

	public void setOriginFilePathList(StringList originFilePathList) {
		this.originFilePathList = originFilePathList;
	}

	public StringList getNewFilePathList() {
		return newFilePathList;
	}

	public void setNewFilePathList(StringList newFilePathList) {
		this.newFilePathList = newFilePathList;
	}

	@Override
	public File rename(File file) {
		
		System.out.println("기존 파일 경로 : " + file.getAbsolutePath());
		if (originFilePathList != null) {
			originFilePathList.add(file.getAbsolutePath());
		}
		
		String fileName = file.getName();
		int lastDot = fileName.lastIndexOf(".");
		
		String dotAndExt = "";
		
		if (lastDot >= 0) {
			dotAndExt = fileName.substring(lastDot);
			dotAndExt = dotAndExt.toLowerCase();
		} else {
			dotAndExt = "";
		}
		
		StringBuffer result = null;
		File newFile = null;
		
		boolean bSuccess = false;
		
		int loopCount = 0;
		while (loopCount < 100) {
			
			loopCount++;
			
			try {
				result = new StringBuffer();
				result.append(DateUtil.getYearMonthDay());
				result.append(DateUtil.getHourMinuteSecond());
				result.append(DateUtil.getMiliSecond());
				result.append(dotAndExt);
				
				newFile = new File(file.getParent(), result.toString());
				if (newFile.exists()) {
					continue;
				}
				
				newFile.createNewFile();
				bSuccess = true;
				break;
				
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		
		if (bSuccess) {
			System.out.println("새 파일 경로 : " + newFile.getAbsolutePath());
			if (newFilePathList != null) {
				newFilePathList.add(newFile.getAbsolutePath());
			}
			
			return newFile;
		}
		
		return null;
	}
}
