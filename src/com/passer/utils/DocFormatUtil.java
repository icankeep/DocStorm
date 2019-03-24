package com.passer.pdf.pdf2word;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;

/** 
 * @ClassName: PDF2Word<p>

 * @description: <p>

 * @author: passer<p>

 * @Date: 2019年1月3日 下午12:21:52<p>
 */
public class DocFormatUtil {
	
		public static void pdf2Word(String pdfFileName) {
			PDDocument doc = null;
			try {
				doc = PDDocument.load(new File(pdfFileName));
			} catch (InvalidPasswordException e2) {
				e2.printStackTrace();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			int pagenumber=doc.getNumberOfPages();//获取总页数
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(pdfFileName.substring(0, pdfFileName.lastIndexOf("."))+".doc");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			Writer writer = null;
			try {
				writer = new OutputStreamWriter(fos,"UTF-8");//文件按字节读取，然后按照UTF-8的格式编码显示
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			PDFTextStripper stripper = null;
			try {
				stripper = new PDFTextStripper();
				stripper.setSortByPosition(true);//排序
				stripper.setStartPage(1);//设置转换的开始页
				stripper.setEndPage(pagenumber);//设置转换的结束页
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}//生成PDF文档内容剥离器
			
			try {
				stripper.writeText(doc,writer);
				writer.close();
				doc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
}
