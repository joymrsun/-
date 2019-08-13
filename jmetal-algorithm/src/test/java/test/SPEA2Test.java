package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.uma.jmetal.algorithm.multiobjective.spea2.SPEA2;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

public class SPEA2Test {
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	Problem<DoubleSolution> problem;
	CrossoverOperator<DoubleSolution> crossover;
    MutationOperator<DoubleSolution> mutation;
    SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;
    SolutionListEvaluator<DoubleSolution> evaluator;
    
    SPEA2 s;

	@Before
	public void setUp() throws Exception {
	    problem = ProblemUtils.loadProblem("org.uma.jmetal.problem.multiobjective.zdt.ZDT1");
	    double crossoverProbability = 0.9 ;
	    double crossoverDistributionIndex = 20.0 ;
	    crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex) ;

	    double mutationProbability = 1.0 / problem.getNumberOfVariables() ;
	    double mutationDistributionIndex = 20.0 ;
	    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex) ;

	    selection = new BinaryTournamentSelection<DoubleSolution>(new RankingAndCrowdingDistanceComparator<DoubleSolution>());
	    
	    evaluator = new SequentialSolutionListEvaluator<DoubleSolution>();
	    
		s=new SPEA2(problem,100,100,crossover,mutation,selection,evaluator);
	}

	@Test
	public void testIsStoppingConditionReached() {
		
		s.run();
		
		boolean tf=s.isStoppingConditionReached();
		assertEquals(tf, true);
	}

	@Test
	public void testGetName() {
		String str=s.getName();
		assertEquals(str, "SPEA2");
	}

	@Test
	public void testGetDescription() {
		String str=s.getDescription();
		assertEquals(str, "Strength Pareto. Evolutionary Algorithm");
	}

}
