package com.passer.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;

import com.passer.exception.LogicException;
import com.passer.file.CFile;

import lombok.Cleanup;

/**
 * @ClassName: FileUtils
 * @description:文件工具类，处理文件上传、下载及格式转换操作
 * @author: passer
 * @Date: 2019年3月24日 下午12:47:55
 */
public class FileUtils {
	/**
	 * 
	* <p>Description: PDF -- > DOC 文件转换 </p>
	* @param pdfFileName	文件绝对路径名 </p>
	* @return				返回修改之后文件的绝对路径 </p>	
	 */
	public static String pdf2Word(String pdfFileName) {
		try {
			@Cleanup
			PDDocument doc = PDDocument.load(new File(pdfFileName));
			//获取总页数
			int pagenumber = doc.getNumberOfPages();
			//转换之后的绝对路径
			String newFileName = pdfFileName.substring(0, pdfFileName.lastIndexOf(".")) + ".doc";
			@Cleanup
			FileOutputStream fos = new FileOutputStream(newFileName);
			@Cleanup
			Writer writer = new OutputStreamWriter(fos, "UTF-8");// 文件按字节读取，然后按照UTF-8的格式编码显示
			PDFTextStripper stripper = new PDFTextStripper();
			//排序
			stripper.setSortByPosition(true);
			//设置转换的开始页
			stripper.setStartPage(1);
			//设置转换的结束页
			stripper.setEndPage(pagenumber);
			stripper.writeText(doc, writer);
			return newFileName;
		} catch (Exception e) {
			throw new LogicException("文件转换出错，请重试！",e);
		}
	}
	
	public static String upload(HttpServletRequest req, String mimeType) {
		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		// Parse the request
		try {
			List<FileItem> items = upload.parseRequest(req);
			Iterator<FileItem> it = items.iterator();
			while (it.hasNext()) {
				FileItem item = it.next();
				String fileName = FilenameUtils.getName(item.getName());
				String fileExtension = FilenameUtils.getExtension(item.getName());
				String fileMimeType = req.getServletContext().getMimeType(fileName);
				if(!mimeType.startsWith(fileMimeType)) {
					throw new LogicException("请上传正确的文件格式!");
				}
				//使用UUID生成随机文件名
				fileName = UUID.randomUUID() + "." + fileExtension;
				//获取上传文件夹目录
				String dir = req.getServletContext().getRealPath("/WEB-INF/upload/");
				File file = new File(dir,fileName);
				//将文件写入指定文件
				item.write(file);
				return file.getAbsolutePath();
			}
		} catch(LogicException e) {
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void download(HttpServletRequest req, HttpServletResponse resp, String fileName) {
		if (fileName == null || "".equals(fileName.trim())) {
			throw new LogicException("请正确输入文件下载名");
		}
		//设置响应格式
		resp.setContentType("application/x-msdownload");
		//设置响应头
		resp.setHeader("Content-disposition", "attachment; filename=" + FilenameUtils.getName(fileName));
		try {
			copyFile(new File(fileName), resp.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static long copyFile(final File input, final OutputStream output) throws IOException {
		return org.apache.commons.io.FileUtils.copyFile(input, output);
    }

}
