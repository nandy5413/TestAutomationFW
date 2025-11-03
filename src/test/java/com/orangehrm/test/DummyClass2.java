package com.orangehrm.test;

import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;


public class DummyClass2 extends BaseClass {
	
	@Test 
	public void DummyTest() {
		String title = driver.getTitle();
		assert title.equals("OrangeHRM") : "Test Failed - Title not matching";

		System.out.println(" The page title is OrangeHRM: passed");
	}

}
