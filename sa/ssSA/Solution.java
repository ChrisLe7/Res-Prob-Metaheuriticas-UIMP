package sa.ssSA;

import java.io.Serializable;

public class Solution implements Serializable {

    private SolutionStructure solution;
    private int L;

    private double fitness;

    public Solution(int lenght){
        this.fitness = 0.0;
        this.L = lenght;
        this.solution = new SolutionStructure(lenght);
    }

    public void print(){
        solution.print();
        System.out.print("    " + this.fitness + "\n");
    }

    public int get_length()
    {
        return L;
    }

    public void set_fitness(double fit)
    {
        fitness = fit;
    }

    public double get_fitness()
    {
        return fitness;
    }

    public void set_item(int index, byte value) {
        solution.set_item(index, value);
    }

    public byte get_item(int index) {
        return solution.get_item(index);
    }

    private void copy(SolutionStructure source, SolutionStructure destination) {
        for (int i = 0; i < L; i ++) {
            destination.set_item(i, source.get_item(i));
        }
    }

    public void set_solution_structure(SolutionStructure c)
    {
        copy(c, solution);
    }

    public SolutionStructure get_solution_structure()
    {
        return solution;
    }

    public void assign(Solution solution)
    {
        copy(solution.get_solution_structure(), this.solution);
        fitness = solution.get_fitness();
        L = solution.get_length();
    }

}
