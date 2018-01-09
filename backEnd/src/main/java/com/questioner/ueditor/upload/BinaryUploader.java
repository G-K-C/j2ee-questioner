package com.questioner.ueditor.upload;

import com.questioner.ueditor.ConfigManager;
import com.questioner.ueditor.PathFormat;
import com.questioner.ueditor.define.AppInfo;
import com.questioner.ueditor.define.BaseState;
import com.questioner.ueditor.define.FileType;
import com.questioner.ueditor.define.State;


import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class BinaryUploader {

	public BinaryUploader() {
	}

	public static final State save(HttpServletRequest request, Map<String, Object> conf) {
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
			MultipartFile multipartFile = multipartRequest.getFile(conf.get("fieldName").toString());
			String savePath = (String)conf.get("savePath");
			String originFileName = multipartFile.getOriginalFilename();
			String suffix = FileType.getSuffixByFilename(originFileName);
			originFileName = originFileName.substring(0, originFileName.length() - suffix.length());
			savePath = savePath + suffix;
			long maxSize = ((Long)conf.get("maxSize")).longValue();
			if (!validType(suffix, (String[])conf.get("allowFiles"))) {
				return new BaseState(false, 8);
			} else {
				savePath = PathFormat.parse(savePath, originFileName);
				String[] savePathBySplit_temp = savePath.split("/");
				String temp = "";
				String fileName = savePathBySplit_temp[savePathBySplit_temp.length - 1];

				for(int i = 1; i < savePathBySplit_temp.length - 1; ++i) {
					if (i != savePathBySplit_temp.length - 2) {
						temp = temp + savePathBySplit_temp[i] + "/";
					} else {
						temp = temp + savePathBySplit_temp[i];
					}
				}

				String pathTemp = request.getSession().getServletContext().getRealPath(temp);
				File targetFile = new File(pathTemp);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}

				State storageState = StorageManager.saveFileByInputStream(multipartFile.getInputStream(), pathTemp + "/" + fileName, maxSize);
				if (storageState.isSuccess()) {
					storageState.putInfo("url", PathFormat.format(savePath));
					storageState.putInfo("type", suffix);
					storageState.putInfo("original", originFileName + suffix);
				}

				return storageState;
			}
		} catch (Exception var15) {
			var15.printStackTrace();
			System.out.println(var15.getMessage());
			return new BaseState(false, 4);
		}
	}

	private static boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);
		return list.contains(type);
	}
}
