package test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ZDT1;

public class ZDT1Test {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	private Class DoubleSolution;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEvalG() {
		ZDT1 p=new ZDT1();
		DoubleSolution solution=mock(DoubleSolution.class);
		when(solution.getVariableValue(1)).thenReturn(2.0);
		when(solution.getVariableValue(2)).thenReturn(3.0);
		when(solution.getNumberOfVariables()).thenReturn(2);

		double g=p.evalG(solution);
		assertEquals(g, 19.0, 0.0);
	}

}
