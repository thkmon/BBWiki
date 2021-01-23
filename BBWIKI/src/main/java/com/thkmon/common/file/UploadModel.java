package com.thkmon.common.file;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oreilly.servlet.MultipartRequest;
import com.thkmon.common.prototype.StringList;
import com.thkmon.common.util.FileNameMaker;

@Controller
public class UploadModel {
	
	private static int size = 1024 * 1024 * 100; // 100메가
	private static String encode = "UTF-8";
	private static String targetDirectory = "";

	
	/**
	 * 파일 업로드
	 */
	@ResponseBody
	@RequestMapping(value = "/wiki/file/upload.do", method = {RequestMethod.GET, RequestMethod.POST})
	private String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String resultPath = "";

		try {
			if (targetDirectory == null || targetDirectory.length() == 0) {
				if (new File("C:/").exists()) {
					throw new Exception("윈도우 환경에서는 업로드할 수 없습니다.");
					
				} else {
					targetDirectory = "/home/hdwk/httpd/htdocs/pds/";
				}
				
				File targetDirObj = new File(targetDirectory);
				if (!targetDirObj.exists()) {
					targetDirObj.mkdirs();
				}
			}
			
			FileNameMaker fileNameMaker = new FileNameMaker();
			MultipartRequest multiReq = new MultipartRequest(request, targetDirectory, size, encode, fileNameMaker);

			// StringList originFilePathList = fileNameMaker.getOriginFilePathList();
			StringList newFilePathList = fileNameMaker.getNewFilePathList();
			
			if (newFilePathList != null && newFilePathList.size() > 0) {
//				resultPath = "1/" + newFilePathList.get(0);
				String path = newFilePathList.get(0);
				path = path.replace("\\", "/");
				
				if (path.indexOf("/pds/") > -1) {
					path = path.substring(path.indexOf("/pds/"));
				}
				
				resultPath = "<script>parent.uploadCallBack(\"" + path + "\");</script>";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			resultPath = "<script>alert(\"" + e.getMessage() + "\");</script>";
		}

		return resultPath;
	}
}