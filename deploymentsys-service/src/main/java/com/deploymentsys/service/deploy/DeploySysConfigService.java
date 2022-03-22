package com.deploymentsys.service.deploy;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.deploymentsys.utils.FileUtil;

@Service
public class DeploySysConfigService {
	@Value("${filesTreeIgnore}")
	private String filesTreeIgnore;

	@Value("${deploySysTempDir}")
	private String deploySysTempDir;

	@Value("${deploySysTempDirForPack}")
	private String deploySysTempDirForPack;

	@Value("${uploadImagesLocalPath}")
	private String uploadImagesLocalPath;

	@Value("${uploadFilesLocalPath}")
	private String uploadFilesLocalPath;

	@Value("${uploadImagesVirtualUrlPrefix}")
	private String uploadImagesVirtualUrlPrefix;

	@Value("${staffDefaultPhotoVirtualUrl}")
	private String staffDefaultPhotoVirtualUrl;

	@Value("${excelImportTemplateUrl}")
	private String excelImportTemplateUrl;

	@Value("${excelOperatePath}")
	private String excelOperatePath;

	/**
	 * Excel操作所在的目录
	 * 
	 * @return
	 */
	public String getExcelOperatePath() {
		return FileUtil.filePathConvert(excelOperatePath);
	}

	/**
	 * excel数据导入模板的文件目录
	 * 
	 * @return
	 */
	public String getExcelImportTemplateUrl() {
		return excelImportTemplateUrl;
	}

	public String getDeploySysTempDir() {
		if (StringUtils.isBlank(deploySysTempDir)) {
			try {
				throw new Exception("找不到配置项deploySysTempDir");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		File file = new File(deploySysTempDir);
		if (!file.exists()) {
			file.mkdirs();
		}

		return FileUtil.filePathConvert(deploySysTempDir);
	}

	/**
	 * 上传图片存放在服务端的本地路径
	 * 
	 * @return
	 */
	public String getUploadImagesLocalPath() {
		return FileUtil.filePathConvert(uploadImagesLocalPath);
	}

	/**
	 * 上传文件存放在服务端的本地路径
	 * 
	 * @return
	 */
	public String getUploadFilesLocalPath() {
		return FileUtil.filePathConvert(uploadFilesLocalPath);
	}

	/**
	 * 访问上传图片的路径前缀
	 * 
	 * @return
	 */
	public String getUploadImagesVirtualUrlPrefix() {
		return uploadImagesVirtualUrlPrefix;
	}

	/**
	 * 人员默认照片虚拟路径
	 * 
	 * @return
	 */
	public String getStaffDefaultPhotoVirtualUrl() {
		return staffDefaultPhotoVirtualUrl;
	}

	public String getFilesTreeIgnore() {
		return filesTreeIgnore;
	}

	public String getDeploySysTempDirForPack() {
		if (StringUtils.isBlank(deploySysTempDirForPack)) {
			try {
				throw new Exception("找不到配置项deploySysTempDirForPack");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		File file = new File(deploySysTempDirForPack);
		if (!file.exists()) {
			file.mkdirs();
		}

		return FileUtil.filePathConvert(deploySysTempDirForPack);
	}

}
