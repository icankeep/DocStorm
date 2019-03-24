package com.passer.web.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.passer.exception.LogicException;
import com.passer.utils.FileUtils;

/**
 * @ClassName: PDFServlet
 * @description: PDF文件上传到服务器转成DOC，并供用户下载
 * @author: passer
 * @Date: 2019年3月22日 下午4:10:00
 */
@WebServlet("/pdf2doc")
public class PDF2DocServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 判断是否是正常的上传操作，例如直接在URL地址栏敲localhost/upload则返回false
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		if (!isMultipart) {
			// 跳转到主页
			req.setAttribute("errorMsg", "请正确上传文件!");
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		}
		else {
			// 文件上传到服务器并转成相应格式
			try {
				//指定上传的文件为PDF类型（日后完善）
				String absoluteFileName = FileUtils.upload(req,"application/pdf");
				String transFileName = FileUtils.pdf2Word(absoluteFileName);
				FileUtils.download(req, resp, transFileName);
			}catch (LogicException e){
				req.setAttribute("errorMsg", e.getMessage());
				req.getRequestDispatcher("/index.jsp").forward(req, resp);
			}
		}
	}

}
