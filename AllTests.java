package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.Test;
import junit.framework.TestSuite;

@RunWith(Suite.class)
@SuiteClasses({ SPEA2Test.class, ZDT1Test.class })
public class AllTests {
	public static Test suite() {
		TestSuite suite = new TestSuite("All Test");
		
		suite.addTest(new TestSuite(SPEA2Test.class));
		
		suite.addTest(new TestSuite(ZDT1Test.class));
		
		return suite;
	}

}
