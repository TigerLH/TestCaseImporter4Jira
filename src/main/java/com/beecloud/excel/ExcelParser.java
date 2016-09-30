/**
 * 
 */
package com.beecloud.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beecloud.model.TestCase;
import com.beecloud.model.TestStep;
import com.google.gson.Gson;

/**
 * @description //Excel操作类
 * @author hong.lin@beecloud.com
 * @date 2016年9月7日 下午2:37:30
 * @version v1.0
 */
public class ExcelParser {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private String filePath;
    private Workbook workBook;
    private Sheet sheet;
    private Row row;
    private Cell cell;
    public ExcelParser(String filePath) throws Exception {
    	this.filePath = filePath;
    	this.initWorkBook();
    }
    
    /**
     * 初始化workbook
     * @throws Exception 
     */
    private void initWorkBook() throws Exception {
    	File file = new File(filePath);
    	if(!file.exists()) {
    		throw new Exception("File Not Found,Please Check Your File Path");
    	}
    	if(!filePath.endsWith(".xls")&&!filePath.endsWith(".xlss")) {
    		throw new Exception("Invalid File Format,Please Check Your file");
    	}
    	FileInputStream inStream = null;
        try {
        	inStream = new FileInputStream(filePath);
        	workBook = WorkbookFactory.create(inStream);    
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(inStream!=null){
                    inStream.close();
                }                
            } catch (IOException e) {                
                e.printStackTrace();
            }
        }
    }
    
    
    /**
     * 加载选择的sheet页
     */
    private void loadSheet(String sheetName) {
    		sheet = workBook.getSheet(sheetName); 
    }
        
    /**
     * 获取Cell对应的值
     * @param cell
     * @return
     */
    private String getCellValue(int rowNum, int column){
    	row = sheet.getRow(rowNum);
        cell = row.getCell(column,Row.RETURN_BLANK_AS_NULL);//Excel中如果有空格返回将不为空,需设置Row.RETURN_BLANK_AS_NULL
        if(cell==null){
        	return null;
        }
    	Object VALUE = null;
        switch (cell.getCellType()) {
            // 字符串类型
            case Cell.CELL_TYPE_STRING:
            	VALUE = cell.getRichStringCellValue().getString();
                break;
            // 数值类型
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                	VALUE = cell.getDateCellValue().toString();
                } else {
                	VALUE = (int)cell.getNumericCellValue();
                }
                break;
            // 布尔类型
            case Cell.CELL_TYPE_BOOLEAN:
            	VALUE = cell.getBooleanCellValue();
                break;
            // 数学公式类型
            case Cell.CELL_TYPE_FORMULA:
            	VALUE = cell.getCellFormula();
                break;
        }
        return VALUE.toString();
    }
    
    /**
     * 获取列名List
     * @return
     * @throws Exception 
     */
    private List<String> getCloumnHeaders() throws Exception {
    	List<String> headers = new ArrayList<String>();
		row = sheet.getRow(0);
		if(row ==null) {
			throw new Exception("Sheet content is null");
		}
    	for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
             String value = this.getCellValue(0, i);
             headers.add(value);
         }
    	return headers;
    }
    
    /**
     * @param sheetName
     * @throws Exception 
     */
    public List<TestCase> transRowsToCase(String sheetName) throws Exception {
    	this.loadSheet(sheetName);
    	if(sheet==null) {
    		throw new Exception("No Sheet Found by Name:"+sheetName);
    	}
    	List<TestCase> list = new ArrayList<TestCase>();
    	List<String> headers = this.getCloumnHeaders();
        for (int x = 1; x < sheet.getLastRowNum()+1; x++) {
        	TestCase testCase = new TestCase();
            row = sheet.getRow(x);
            for (int y = 0; y < headers.size(); y++) {	//row.getPhysicalNumberOfCells()存在异常
                String value = this.getCellValue(x, y);
                if (value != null) {
                    String key = headers.get(y);
                    switch(key) {
                    	case "CASENAME":
                    		testCase.setCaseName(value);
                    		break;
                    	case "REPORTER":
                    		testCase.setReporter(value);
                    		break;
                    	case "ASSIGNEE":
                    		testCase.setAssignee(value);
                    		break;
                    	case "DESCRIPTION":
                    		testCase.setDescription(value);
                    		break;
                    	case "COMPONENT":
                    		testCase.setComponent(value.split("&"));
                    		break;
                    	case "LABELS":
                    		testCase.setLabels(value.split("&"));
                    		break;
                    	case "PRIROTY":
                    		testCase.setPriorityName(value);
                    		break;
                    	case "TESTSTEPS":
                    		List<TestStep> STEP_LIST = new ArrayList<TestStep>();
                    		String[] steps = value.split("&");
                    		Gson gson = new Gson(); 
                    		for(String step:steps) {
                    			STEP_LIST.add(gson.fromJson(step, TestStep.class));
                    		}
                    		testCase.setSteps(STEP_LIST);
                    		break;
                    	case "SPRINT":
                    		testCase.setSprint(value);
                    		break;
                    	case "COMMIT":
                    		testCase.setCommit(value);
                    		break;
                    }
                }
            }
            list.add(testCase);
        }
        return list;
    }
    
    public static void main(String[] args) {
    	try {
			ExcelParser ep = new ExcelParser("TestCase.xls");
			ep.transRowsToCase("abc");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
