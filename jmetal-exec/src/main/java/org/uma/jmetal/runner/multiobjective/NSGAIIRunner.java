package org.uma.jmetal.runner.multiobjective;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.runner.AbstractAlgorithmRunner;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to configure and run the NSGA-II algorithm
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class NSGAIIRunner extends AbstractAlgorithmRunner {
    /**
     * @param args Command line arguments.
     * @throws JMetalException
     * @throws FileNotFoundException Invoking command:
     *                               java org.uma.jmetal.runner.multiobjective.NSGAIIRunner problemName [referenceFront]
     */
    public static void main(String[] args) throws JMetalException, FileNotFoundException {
        Problem<DoubleSolution> problem;
        Algorithm<List<DoubleSolution>> algorithm;
        CrossoverOperator<DoubleSolution> crossover;
        MutationOperator<DoubleSolution> mutation;
        SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;
        String referenceParetoFront = "";

        String problemName;
        if (args.length == 1) {
            problemName = args[0];
        } else if (args.length == 2) {
            problemName = args[0];
            referenceParetoFront = args[1];
        } else {
            problemName = "org.uma.jmetal.problem.multiobjective.dtlz.DTLZ1";
            //referenceParetoFront = "jmetal-problem/src/test/resources/pareto_fronts/ZDT1.pf";
        }

        problem = ProblemUtils.<DoubleSolution>loadProblem(problemName);

        double crossoverProbability = 1;
        double crossoverDistributionIndex = 15.0;
        crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);

        double mutationProbability = 1.0 / problem.getNumberOfVariables();
        double mutationDistributionIndex = 20.0;
        mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

        selection = new BinaryTournamentSelection<DoubleSolution>(
                new RankingAndCrowdingDistanceComparator<DoubleSolution>());

        algorithm = new NSGAIIBuilder<DoubleSolution>(problem, crossover, mutation)
                .setSelectionOperator(selection)
                .setMaxEvaluations(500)
                .setPopulationSize(200)
                .build();

        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                .execute();

        List<DoubleSolution> population = algorithm.getResult();

        ArrayList<DoubleSolution> archive = new ArrayList<>();
        //---
        for (DoubleSolution ds : population) {
            updataArchive(ds, archive);
        }

        //---
        printArchive(archive);

        long computingTime = algorithmRunner.getComputingTime();

        JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

        printFinalSolutionSet(population);
        if (!referenceParetoFront.equals(""))

        {
            printQualityIndicators(population, referenceParetoFront);
        }
    }

    private static void printArchive(ArrayList<DoubleSolution> archive) {
        File f = new File("data.csv");
        try {
            FileWriter fw = new FileWriter(f);
            for (int i = 0; i < archive.get(0).getNumberOfObjectives(); i++) {
                fw.append("y" + i + ",");
            }
            for (int i = 0; i < archive.get(0).getNumberOfVariables(); i++) {
                if (i < archive.get(0).getNumberOfVariables() - 1) {
                    fw.append("x" + i + ",");
                } else {
                    fw.append("x" + i);
                }
            }
            fw.append("\r\n");
            for (DoubleSolution as : archive) {
                for (int i = 0; i < as.getNumberOfObjectives(); i++) {
                    fw.append(as.getObjective(i) + ",");
                }
                for (int i = 0; i < as.getNumberOfVariables(); i++) {
                    if (i < archive.get(0).getNumberOfVariables() - 1) {
                        fw.append(as.getVariableValue(i) + ",");
                    } else {
                        fw.append(as.getVariableValue(i).toString());
                    }
                }
                fw.append("\r\n");
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updataArchive(DoubleSolution ds, ArrayList<DoubleSolution> archive) {
        boolean is = false;
        for (DoubleSolution as : archive) {
            double trade = 0;
            for (int i = 0; i < ds.getNumberOfObjectives(); i++) {
                trade += Math.pow(ds.getObjective(i) - as.getObjective(i), 2);
            }
            if (trade < 0.00000001) {
                is = true;
                break;
            }
        }
        if (is == false) {
            archive.add((DoubleSolution) ds.copy());
        }
    }
}
