///////////////////////////////////////////////////////////////////////////////
///            Steady State Genetic Algorithm v1.0                          ///
///                by Enrique Alba, July 2000                               ///
///                                                                         ///
///   Executable: set parameters, problem, and execution details here       ///
///////////////////////////////////////////////////////////////////////////////

package sa.ssSA;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.util.Random;

public class Exe
{
  public static Random rand;

  @Option(name="-a", aliases="--annealingFactor", usage="Factor used for decreasing the temperature.")
  private double annealing_factor = -1;
  @Option(name="-p", aliases="--initialProbability", usage="Initial probability of acceptance.")
  private double initial_probability = -1;
  @Option(name="-n", aliases="--numInitialEstimates", usage="Initial number of estimates to adjust the initial temperature.")
  private int num_initial_estimates = -1;
  @Option(name="-S", aliases="--stepsAnnealing", usage="Factor used for steps for annealing.")
  private int steps_for_annealing = -1;
  @Option(name="-s", aliases="--seed", usage="Random seed.")
  private int seed = -1;
  @Option(name="-f", aliases="--file", usage="Path of the file with the problem instance.", required=false)
  private String file_name = "";

  public static void main(String[] args) throws Exception {
    new Exe().doMain(args);
  }

  private void doMain(String [] args) throws Exception
  {
    CmdLineParser parser = new CmdLineParser(this);

    try {
      // parse the arguments.
      parser.parseArgument(args);

    } catch( CmdLineException e ) {
      // if there's a problem in the command line,
      // you'll get this exception. this will report
      // an error message.
      System.err.println(e.getMessage());
      System.err.println("java SampleMain [options...] arguments...");
      // print the list of available options
      parser.printUsage(System.err);
      System.err.println();

      return;
    }

    if (seed == -1)
      rand = new Random();
    else
      rand = new Random(seed);

    long inicio = System.currentTimeMillis();

    Problem problem;                                // The problem being solved

    //Parameters MKP
    int in;                                           // Item number
    int il;                                           // Item length
    long MAX_ISTEPS = 50000;
    if (annealing_factor == -1 ) annealing_factor = 0.9;
    if (initial_probability == -1 ) initial_probability = 0.9999;
    if (num_initial_estimates == -1 ) num_initial_estimates = 10;
    if (steps_for_annealing == -1 ) steps_for_annealing = 5;


    //Default MKP problem parameters. If the MKP problem changes, these parameters have to been changed too.
    if (file_name.isEmpty()){
      problem = new ProblemMKP();
      in = 6;                          // Item number
      il = 1;                           // Item length
      double tf = 3800.0;               // Target fitness being sought
      problem.set_itemN(in);
      problem.set_itemL(il);
      problem.set_target_fitness(tf);
    }else{
      //Extern problem read from a file.
      ProblemMKP problem_aux = new ProblemMKP();
      problem_aux.readFile(file_name);
      in = problem_aux.get_itemN();
      il = problem_aux.get_itemL();
      problem = problem_aux;
    }

    Algorithm sa;          // The ssSA being used
    sa = new Algorithm(problem, in, il, initial_probability, num_initial_estimates, annealing_factor, steps_for_annealing);
    int step;
    for (step=0; step<MAX_ISTEPS; step++){
      sa.go_one_step();
      System.out.print(step); System.out.print("  ");
      System.out.println(sa.get_current().get_fitness());
      if ((problem.tf_known()) & sa.get_current().get_fitness()>=(problem.get_target_fitness())) {
        System.out.print("Solution Found! After ");
        System.out.print(problem.get_fitness_counter());
        System.out.println(" evaluations");
        break;
      }
    }

    long fin = System.currentTimeMillis();
    double tiempo = (double) ((fin - inicio));

    System.out.print("Tiempo (ms): "+tiempo+"\t");
    System.out.print("Iteraciones: " + step); System.out.print("\t");
    System.out.println("Fitness: " + sa.get_best().get_fitness());

    System.out.println("Solución:");

    // Print the solution
    for(int i=0;i<in*il;i++)
      System.out.print( (sa.get_best()).get_item(i) ); System.out.println();
  }

}
// END OF CLASS: Exe
