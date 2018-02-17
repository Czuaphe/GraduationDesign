package com.graduation.test;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.graduation.dao.SelectedDao;
import com.graduation.entity.Selected;

public class SelectedDaoTest {

	private SelectedDao selectedDao = new SelectedDao();
	
	@Test
	public void saveTest() {

		Selected selected = new Selected();
		selected.setProblem_id(6);
		selected.setStu_id(10005);
		selected.setTime(new Timestamp(new Date().getTime()));
		
		boolean b = selectedDao.save(selected);
		System.out.println(b);
		
		System.out.println(selected.toString());
		
		assertEquals(b, true);
		
	}
	
	@Test
	public void queryBySelected_id() {
		
		Selected selected = selectedDao.queryBySelected_id(1);
		System.out.println(selected.toString());
		assertNotNull(selected);
		
	}
	
	@Test
	public void updateAllTest() {
		
		Selected selected = selectedDao.queryBySelected_id(1);
		System.out.println("Before: " +  selected.toString());
		selected.setProblem_id(5);
		
		boolean b = selectedDao.updateAll(selected);
		System.out.println("Update flag: " + b);
		
		System.out.println("After: " + selected.toString());
		
		assertEquals(b, true);
		
	}
	
	@Test
	public void removeTest() {
		
		boolean b = selectedDao.remove(1);
		
		System.out.println("Delete flag: " + b);
		
		assertEquals(b, true);
		
	}

}
