///////////////////////////////////////////////////////////////////////////////
///            Steady State Genetic Algorithm v1.0                          ///
///                by Enrique Alba, July 2000                               ///
///                                                                         ///
///   Executable: set parameters, problem, and execution details here       ///
///////////////////////////////////////////////////////////////////////////////

package ga.ssGA;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import java.io.IOException;
import java.util.Random;

public class Exe
{
  public static Random rand;

  @Option(name="-m", aliases="--mutation", usage="Mutation probability.")
  private double pm = -1;
  @Option(name="-c", aliases="--crossover", usage="Crossover probability.")
  private double pc = -1;
  @Option(name="-M", aliases="--mutationType", usage="Mutation probability.")
  private int type_mutation = -1;
  @Option(name="-C", aliases="--crossoverType", usage="Crossover probability.")
  private int type_crossover = -1;

  @Option(name="-p", aliases="--population", usage="Size population.")
  private int population_size = -1;
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

    Problem   problem;                                // The problem being solved

    //Parameters MKP
    int gn;                                           // Gene number
    int gl;                                           // Gene length
    if (population_size <= 0) population_size = 15;  // Population size
    if (pc == -1) pc = 0.8;                           // Crossover probability
    if (pm == -1) pm  = 0.1;                          // Mutation probability

    if ((type_crossover == -1) || (type_mutation != 0 && type_mutation != 1)) type_crossover = 0;     // Default Crossover Type (SPX)
    if ((type_mutation == -1) || (type_mutation != 0 && type_mutation != 1)) type_mutation = 0;      // Default Crossover Type (SPX)

    long MAX_ISTEPS = 50000;
    long MAX_EVALUATIONS = 50000;


    //Default MKP problem parameters. If the MKP problem changes, these parameters have to been changed too.
    if (file_name.isEmpty()){
      problem = new ProblemMKP();
      gn = 6;                          // Gene number
      gl = 1;                           // Gene length
      double tf = 3800.0;               // Target fitness being sought
      problem.set_geneN(gn);
      problem.set_geneL(gl);
      problem.set_target_fitness(tf);
    }else{
      //Extern problem read from a file.
      ProblemMKP problem_aux = new ProblemMKP();
      problem_aux.readFile(file_name);
      gn = problem_aux.get_geneN();
      gl = problem_aux.get_geneL();
      problem = problem_aux;
    }

    Algorithm ga;          // The ssGA being used
    ga = new Algorithm(problem, population_size, gn, gl, pc, pm, type_crossover, type_mutation);
    int step;
    for (step=0; step<MAX_ISTEPS && problem.get_fitness_counter() < MAX_EVALUATIONS; step++){
      ga.go_one_step();
      System.out.print(step); System.out.print("  ");
      System.out.println(ga.get_bestf());
      if ((problem.tf_known()) & ga.get_solution().get_fitness()>=(problem.get_target_fitness())) {
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
    System.out.print("Evaluaciones: " + problem.get_fitness_counter()); System.out.print("\t");
    System.out.println("Fitness: " + ga.get_solution().get_fitness());

    System.out.println("Solución:");

    // Print the solution
    for(int i=0;i<gn*gl;i++)
      System.out.print( (ga.get_solution()).get_allele(i) ); System.out.println();
  }

}
// END OF CLASS: Exe
