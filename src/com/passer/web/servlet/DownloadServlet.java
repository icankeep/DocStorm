package com.passer.web.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.passer.pdf.pdf2word.DocFormatUtil;

/**
 * @ClassName: DownloadServlet
 *             <p>
 * 
 * @description:
 *               <p>
 * 
 * @author: passer
 *          <p>
 * 
 * @Date: 2019年3月22日 下午9:54:44
 *        <p>
 */
@WebServlet("/download")
public class DownloadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		String fileName = (String) req.getAttribute("fileName");
		DocFormatUtil.pdf2Word(fileName);
		String newFileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".doc";
		resp.setContentType("application/msword;charset=utf-8");
		OutputStream os = resp.getOutputStream();
		InputStream is = new FileInputStream(newFileName);
		byte[] b = new byte[1024];
		int len = 0;
		try {
			while ((len = is.read(b)) > 0)
				os.write(b, 0, len);
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			is.close();
			os.close();
		}
	}
}
