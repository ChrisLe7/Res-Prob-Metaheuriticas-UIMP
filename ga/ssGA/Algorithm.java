///////////////////////////////////////////////////////////////////////////////
///               Steady State Genetic Algorithm v1.0                       ///
///                 by Enrique Alba, September 1999                         ///
///                                                                         ///
///  2TOURNAMENT+SPX(rand_parent)+Bit_Mutation+Replacement(Worst_Always)    ///
///////////////////////////////////////////////////////////////////////////////

package ga.ssGA;

import java.util.Random;

public class Algorithm
{
  private  int          chrom_length; // Alleles per chromosome
  private  int          gene_number;  // Number of genes in every chromosome
  private  int          gene_length;  // Number of bits per gene
  private  int          popsize;      // Number of individuals in the population
  private  double pc, pm;      // Probability of applying crossover and mutation
  private  int type_crossover, type_mutation;      // Probability of applying crossover and mutation
  private  Problem       problem;     // The problem being solved
  private  Population    pop;         // The population
  private  static Random r;           // Source for random values in this class
  private  Individual aux_indiv;  // Internal auxiliar individual being computed


  // CONSTRUCTOR
  public Algorithm(Problem p, int popsize, int gn, int gl, double pc, double pm, int type_crossover, int type_mutation)
  throws Exception
  {
    this.gene_number   = gn;
    this.gene_length   = gl;
    this.chrom_length  = gn*gl;
    this.popsize       = popsize;
    this.pc            = pc;
    this.pm            = pm;
    this.type_crossover           = type_crossover;
    this.type_mutation            = type_mutation;
    this.problem       = p;                     
    this.pop = new Population(popsize,chrom_length);// Create initial population
    this.r             = new Random();
    this.aux_indiv     = new Individual(chrom_length);

    for(int i=0;i<popsize;i++)
    pop.set_fitness(i,problem.evaluateStep(pop.get_ith(i)));
    pop.compute_stats();
  }

  // BINARY TOURNAMENT
  public Individual select_tournament() throws Exception
  {
    int p1, p2;

    p1 = (int)(Exe.rand.nextDouble()*
               (double)popsize + 0.5); // Round and then trunc to int
    
    if(p1>popsize-1) p1=popsize-1;
    do
    {  p2 = (int)(Exe.rand.nextDouble()*
                  (double)popsize + 0.5);  // Round and then trunc to int
      if(p2>popsize-1) p2=popsize-1;
    }
    while (p1==p2);
    if (pop.get_ith(p1).get_fitness()>pop.get_ith(p2).get_fitness())
    return pop.get_ith(p1);
    else
    return pop.get_ith(p2);
  }

  // SINGLE POINT CROSSOVER - ONLY ONE CHILD IS CREATED (RANDOMLY DISCARD 
  // DE OTHER)
  public Individual SPX (Individual p1, Individual p2)
  {
    int       rand;

    rand = (int)(Exe.rand.nextDouble()*
                 (double)chrom_length-1+0.5); // From 0 to L-1 rounded
    if(rand>chrom_length-1) rand=chrom_length-1;

    if(Exe.rand.nextDouble()>pc)  // If no crossover then randomly returns one parent
    return Exe.rand.nextDouble()>0.5?p1:p2;

    // Copy CHROMOSOME 1
    for (int i=0; i<rand; i++)
    {
      aux_indiv.set_allele(i,p1.get_allele(i));
    }
    // Copy CHROMOSOME 2
    for (int i=rand; i<chrom_length; i++)
    {
      aux_indiv.set_allele(i,p2.get_allele(i));
    }

    return aux_indiv;
  }

  // UNIFORM CROSSOVER - ONLY ONE CHILD IS CREATED (RANDOMLY DISCARD
  // DE OTHER)
  public Individual UXP(Individual p1, Individual p2) {
    // If no crossover then randomly returns one parent
    if(Exe.rand.nextDouble() > pc) {
      return Exe.rand.nextDouble() > 0.5 ? p1 : p2;
    }
    // Runs through each allele and randomly selects which parent to take the value from
    for (int i = 0; i < chrom_length; i++) {
      if (Exe.rand.nextDouble() < 0.5) {
        aux_indiv.set_allele(i, p1.get_allele(i));
      } else {
        aux_indiv.set_allele(i, p2.get_allele(i));
      }
    }

    return aux_indiv;
  }

  // MUTATE A BINARY CHROMOSOME
  public Individual mutate(Individual p1)
  {
    byte alelle=0;
    Random r = new Random();

    aux_indiv.assign(p1);

    for(int i=0; i<chrom_length; i++){
      if (Exe.rand.nextDouble()<=pm)  // Check mutation bit by bit...
      {
        if(aux_indiv.get_allele(i)==1)
          aux_indiv.set_allele(i,(byte)0);
        else
          aux_indiv.set_allele(i,(byte)1);
      }
    }


    return aux_indiv;

  }

  // SWAP MUTATION
  public Individual swapMutate(Individual p1) {
    if(Exe.rand.nextDouble() > pc) {
      return p1;
    }
    aux_indiv.assign(p1);

    // Selects two different random indexes on the chromosome
    int index1 = Exe.rand.nextInt(chrom_length);
    int index2 = Exe.rand.nextInt(chrom_length);
    while(index1 == index2) {
      index2 = Exe.rand.nextInt(chrom_length); // Ensures that index2 is different from index1
    }

    // Swap alleles at selected indices.
    byte allele1 = aux_indiv.get_allele(index1);
    byte allele2 = aux_indiv.get_allele(index2);

    aux_indiv.set_allele(index1, allele2);
    aux_indiv.set_allele(index2, allele1);

    return aux_indiv;
  }

  // REPLACEMENT - THE WORST INDIVIDUAL IS ALWAYS DISCARDED
  public void replace(Individual new_indiv) throws Exception
  {
    pop.set_ith(pop.get_worstp(),new_indiv);
    //pop.compute_stats();                  // Recompute avg, best, worst, etc.
  }

  // EVALUATE THE FITNESS OF AN INDIVIDUAL
  private double evaluateStep(Individual indiv)
  {
    return problem.evaluateStep(indiv);
  }

  public void go_one_step() throws Exception
  {
    Individual offspring = (this.type_crossover == 0) ? SPX(select_tournament(),select_tournament()) : UXP(select_tournament(),select_tournament());
    aux_indiv.assign(offspring);
    offspring = (this.type_mutation == 0) ? mutate(offspring) : swapMutate(offspring);
    aux_indiv.set_fitness(problem.evaluateStep(offspring));
    replace(aux_indiv);
  }

  public Individual get_solution() throws Exception
  {
    return pop.get_ith(pop.get_bestp());// The better individual is the solution
  }


public int    get_worstp() { return pop.get_worstp(); }
public int    get_bestp()  { return pop.get_bestp();  }
public double get_worstf() { return pop.get_worstf(); }
public double get_avgf()   { return pop.get_avgf();   }
public double get_bestf()  { return pop.get_bestf();  }
public double get_BESTF()  { return pop.get_BESTF();  }

  public Individual get_ith(int index) throws Exception
  {
    return pop.get_ith(index);
  }

  public void set_ith(int index, Individual indiv) throws Exception
  {
    pop.set_ith(index,indiv);
  }

  public Individual get_best_individual()
  {
    return aux_indiv;// The better individual is the solution
  }
}
// END OF CLASS: Algorithm

