package com.passer.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.passer.pdf.pdf2word.DocFormatUtil;

/**
 * @ClassName: PDFServlet
 *             <p>
 * 
 * @description:
 *               <p>
 * 
 * @author: passer
 *          <p>
 * 
 * @Date: 2019年3月22日 下午4:10:00
 *        <p>
 */
@WebServlet("/upload")
public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		String fileName = doUpload(req, resp);
		doDownload(req, resp,fileName);
	}

	private void doDownload(HttpServletRequest req, HttpServletResponse resp,String fileName) 
			throws IOException {
		DocFormatUtil.pdf2Word(fileName);
		String newFileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".doc";
		resp.setContentType("application/msword;charset=utf-8");
		OutputStream os = resp.getOutputStream();
		InputStream is = new FileInputStream(newFileName);
		byte[] b = new byte[1024 * 1024];
		int len = 0;
		try {
			while ((len = is.read(b)) > 0)
				os.write(b, 0, len);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			is.close();
			os.close();
		}
	}

	private String doUpload(HttpServletRequest req, HttpServletResponse resp) {
		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(1024 * 1024);
		factory.setRepository(new File("D:/"));

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// Parse the request
		List<FileItem> items = null;
		String fileName = null;
		try {
			items = upload.parseRequest(req);
			Iterator<FileItem> it = items.iterator();
			while (it.hasNext()) {
				FileItem item = it.next();
				fileName = factory.getRepository() + item.getName();
				item.write(new File(fileName));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(fileName);
		System.out.println("successful!");
		return fileName;
	}
}
