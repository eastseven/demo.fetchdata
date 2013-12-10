package cn.eastseven;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class PoiDemo {

	public static void main(String[] args) throws Exception {
		File file = new File("src/main/resources/sanguozhi12.xls");
		Workbook workbook = WorkbookFactory.create(file);
		
		Sheet sheet = workbook.getSheetAt(0);
		
		int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		System.out.println("physicalNumberOfRows="+physicalNumberOfRows+",firstRowNum="+firstRowNum+",lastRowNum="+lastRowNum);
		
		Row firstRow = sheet.getRow(firstRowNum);
		int physicalNumberOfCells = firstRow.getPhysicalNumberOfCells();
		System.out.println("physicalNumberOfCells="+physicalNumberOfCells);
		
		String createTable = "create table san_guo_zhi( \n";
		for(int index = 0; index < physicalNumberOfCells; index++) {
			createTable += ",column"+index+" varchar(32) \n";
		}
		createTable = createTable.replaceFirst(",", "");
		createTable += ") DEFAULT CHARSET=utf8;";
		System.out.println(createTable);
		
		String data = "";
		
		for(int indexRow = 0; indexRow < physicalNumberOfRows; indexRow++) {
			Row row = sheet.getRow(indexRow);
			
			String insert = "insert san_guo_zhi values( ";
			for(int indexCol = 0; indexCol < physicalNumberOfCells; indexCol++) {
				Cell cell = row.getCell(indexCol);
				if(cell == null) {
					insert += ",null";
				} else {
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_BLANK:
						insert += ",null";
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						insert += ",'"+cell.getBooleanCellValue()+"'";
						break;
					case Cell.CELL_TYPE_ERROR:
						insert += ",null";
						break;
					case Cell.CELL_TYPE_FORMULA:
						insert += ",null";
						break;
					case Cell.CELL_TYPE_NUMERIC:
						insert += ",'"+cell.getNumericCellValue()+"'";
						break;
					case Cell.CELL_TYPE_STRING:
						insert += ",'"+cell.getStringCellValue()+"'";
						break;
					default:
						break;
					}
				}
			}
			
			insert = insert.replaceFirst(",", "");
			insert += ");";
			//System.out.println(insert);
			data += insert + "\n";
		}
		
		File dataFile = new File("src/main/resources/data.sql");
		if(dataFile.exists()) {
			dataFile.deleteOnExit();
		}
		dataFile.createNewFile();
		
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(dataFile), "utf-8");
		out.write(data);
		out.flush();
		out.close();
	}

}
