package com.graduation.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import com.graduation.dao.ProblemDao;
import com.graduation.entity.Problem;

public class ProblemDaoTest {

	private ProblemDao problemDao = new ProblemDao();
	
	@Test
	public void saveTest() {
		
		Problem problem = new Problem();
		
		problem.setName("课题题目测试5");
		problem.setMid(5);
		problem.setIs_new(0);
		problem.setType(0);
		problem.setSource(0);
		problem.setTea_id(20015);
		// 数据库中字段为时间类型，且不为空时，如果在插入时没有赋值，则数据库会自动赋值，且对象得不到
		// 所有在插入数据库之前，对不可为空的字段都要进行赋值。。
		problem.setProblem_time(new Timestamp(new Date().getTime()));
		System.out.println(problem.getProblem_time());
		
		problem.setIntroduction("课题简介测试");
		problem.setRequirement("课题要求测试");
		System.out.println(problem.toString());
		boolean b = problemDao.save(problem);
		System.out.println(b);
		System.out.println(problem.toString());
		assertNotNull(b);
		
	}
	
	@Test
	public void DeleteTest() {
		
		boolean b = problemDao.remove(1);
		System.out.println(b);
	}
	
	@Test
	public void queryByPrblem_idTest() {
		Problem problem = problemDao.queryByProblem_id(3);
		
		System.out.println(problem.toString());
		
		assertNotNull(problem);
	}
	
	@Test
	public void updateProblemTest() {
		
		Problem problem = problemDao.queryByProblem_id(3);
		problem.setName("选题题目名称修改测试");
		problem.setMid(4);
		boolean b = problemDao.updateAll(problem);
		System.out.println(b);
		assertNotNull(b);
		
	}
	
	@Test
	public void Test() {
		
		List<String> list = new ArrayList<>();
		
		list.add(null);
		list.add(null);
		
		System.out.println(list.size());
		
	}
	
	@Test
	public void exportXlsxFile() {
		// 输出成xlsx文件
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();
		XSSFRow titleRow = sheet.createRow(0);
		titleRow.createCell(0).setCellValue("测试");
				
		try {
			FileOutputStream outStream = new FileOutputStream(new File("./test.txt"));
			workbook.write(outStream);
			outStream.flush();
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
