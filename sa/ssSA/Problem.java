///////////////////////////////////////////////////////////////////////////////
///                 Steady State Genetic Algorithm v1.0                     ///
///                      by Enrique Alba, July 2000                         ///
///                                                                         ///
///         Problem Function AND Representation (IL, GN, Ranges)            ///
///////////////////////////////////////////////////////////////////////////////

package sa.ssSA;

import java.util.Random;


public abstract class Problem                    // Maximization task
{
    protected int IL=1;               	// Gene lenth in binary
    protected int IN=1;               	// Gene number in one string
    protected int SL=IN*IL;           	// Chromosome length
    protected long fitness_counter;   	// Number of evaluations
    protected double target_fitness;  	// Target fitness value -MAXIMUM-
    protected boolean tf_known;       	// Is the taret fitness known????
    protected static Random r = new Random();	// Random uniform variable
    
    public Problem() {
        SL              = IN*IL;
        fitness_counter = 0;
        tf_known        = false;
        target_fitness  = -999999.9;
    }
    
    public int     get_itemL()           { return IL; }
    public int     get_itemN()           { return IN; }
    public void    set_itemL(int il)     { IL = il; SL=IN*IL; }
    public void    set_itemN(int in)     { IN = in; SL=IN*IL; }
    public long    get_fitness_counter() { return fitness_counter; }
    public double  get_target_fitness()  { return target_fitness;  }
    public boolean tf_known()            { return tf_known;        }
    
    public void    set_target_fitness(double tf) {
        target_fitness = tf;
        tf_known       = true;
    }
    
    
    public double evaluateStep(Solution solution) {
            fitness_counter++ ;
            return Evaluate(solution) ;
    } 
    
    public abstract double Evaluate(Solution solution) ;
}
// END OF CLASS: Problem
