/**
 * 
 */
package com.shihui.openpf.home.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author zhouqisheng
 *
 */
public class DataExportUtils {

	private DataExportUtils() {
	};

	/**
	 * 生成excel
	 * 
	 * @param fileName
	 *            文件名称
	 * @param sheetName
	 *            工作表名称
	 * @param title
	 *            数据标题
	 * @param data
	 *            数据
	 * @param charset
	 *            编码
	 * @return 生成文件路径
	 * @throws IOException
	 */
	public static String genExcel(String fileName, String sheetName, List<String> title, List<List<Object>> data,
			String charset) throws IOException {
		SXSSFWorkbook wb = new SXSSFWorkbook(100); // keep 100 rows in memory,
													// exceeding rows will be
													// flushed to disk
		Sheet sh = wb.createSheet(sheetName);
		Row rowTitle = sh.createRow(0);
		for (int cellnum = 0; cellnum < title.size(); cellnum++) {
			Cell cell = rowTitle.createCell(cellnum);
			cell.setCellValue(title.get(cellnum));
		}
		for (int rownum = 0; rownum < data.size(); rownum++) {
			Row row = sh.createRow(rownum + 1);
			for (int cellnum = 0; cellnum < title.size(); cellnum++) {
				Cell cell = row.createCell(cellnum);
				Object value = data.get(rownum).get(cellnum);
				if (value == null) {
					cell.setCellValue("");
				} else {
					cell.setCellValue(new String(value.toString().getBytes(), charset));
				}
			}
		}
		String tempdir = System.getProperty("java.io.tmpdir", "../temp/");
		File tempDir = new File(tempdir);
		if (!tempDir.exists())
			tempDir.mkdirs();
		String filePath = tempdir + fileName;

		try (FileOutputStream out = new FileOutputStream(filePath)) {
			wb.write(out);
		} catch (IOException e) {
			// dispose of temporary files backing this workbook on disk
			wb.dispose();
			wb.close();
			throw e;
		}
		// dispose of temporary files backing this workbook on disk
		wb.dispose();
		wb.close();
		return filePath;
	}


}
