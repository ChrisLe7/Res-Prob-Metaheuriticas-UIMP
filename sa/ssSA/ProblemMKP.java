package sa.ssSA;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class ProblemMKP extends Problem {
    //Initialize with the default problem -> datasets/problem_mknap1.txt.
    static private double[][] weights_ = {
            {8,12,13,64,22,41},
            {8,12,13,75,22,41},
            {3,6,4,18,6,4},
            {5,10,8,32,6,12},
            {5,13,8,42,6,20},
            {5,13,8,48,6,20},
            {0,0,0,0,8,0},
            {3,0,4,0,8,0},
            {3,2,4,0,8,4},
            {3,2,4,8,8,4}
    };

    static private double [] capacities_ = {80,96,20,36,44,48,10,18,22,24};
    static private double [] profits_ = {100,600,1200,2400,500,2000};
    static private int nDims_ = 10; //number of dimensions

    @Override
    public double Evaluate(Solution solution) {
        return MDKP(solution);
    }

    public void readFile(String file_name) throws FileNotFoundException {
        File file = new File(file_name);
        if (!file.isFile()) {
            throw new FileNotFoundException("The file " + file_name + " does not exist.");
        }
        try (Scanner scanner = new Scanner(file).useLocale(Locale.US)) {
            set_itemL(1);
            set_itemN(scanner.nextInt());
            nDims_ = scanner.nextInt();
            double target_fitness = scanner.nextDouble();
            if (target_fitness != 0) {
                set_target_fitness(target_fitness);
            }

            profits_ = new double[SL];
            weights_ = new double[nDims_][SL];
            capacities_ = new double[nDims_];

            // read profits for each item
            for (int i = 0; i < SL; i++) {
                profits_[i] = scanner.nextDouble();
            }

            // read weight for each item in each dimension
            for (int i = 0; i < nDims_; i++) {
                for (int j = 0; j < SL; j++) {
                    weights_[i][j] = scanner.nextDouble();
                }
            }

            // read maximum capacity for each dimension
            for (int i = 0; i < nDims_; i++) {
                capacities_[i] = scanner.nextDouble();
            }
        }
    }

    public void set_weights(double [][] weights){
        for (int i = 0; i < nDims_; i++) {
            weights_[i] = Arrays.copyOf(weights[i], weights[i].length);
        }
    }

    public void set_capacities(double [] capacities){
        capacities_ = Arrays.copyOf(capacities, capacities.length);
    }

    public void set_profits(double [] profits){
        profits_ = Arrays.copyOf(profits, profits.length);
    }
    // PRIVATE METHODS

    /**
     * Calculates the total exceeded capacity for an individual's solution in the Multidimensional Knapsack Problem (MKP).
     * It sums the weights of each item selected by the individual and checks against the capacities for each dimension.
     * The total exceeded capacity is the sum of the differences between the sum weights and capacities across all dimensions
     * where the sum weights exceed the capacities.
     *
     * @param solution The individual solution for which the exceeded capacity is to be calculated.
     * @return The total exceeded capacity as a double value. If no capacities are exceeded, returns 0.0.
     */
    private double getExceededCapacityViolation(Solution solution){
        double exceeded_capacity = 0.0;
        double [] sum_weights = new double[nDims_];

        for (int j = 0; j < SL; j++){
            if (solution.get_item(j) == 1){
                for (int i = 0; i < nDims_; i++){
                    sum_weights[i] += weights_[i][j];
                }
            }
        }
        for (int i = 0; i <nDims_; i++){
            if (sum_weights[i] > capacities_[i]){
                exceeded_capacity += (sum_weights[i]-capacities_[i]);
            }
        }
        return exceeded_capacity;
    }

    private double calculateProfits(Solution solution){
        double profit = 0.0;
        for (int i = 0; i < SL; i++){
            if (solution.get_item(i) == 1){
                profit += profits_[i];
            }
        }
        return profit;
    }

    private double MDKP(Solution solution) {
        double fitness = getExceededCapacityViolation(solution) * -1.0;
        if (fitness == 0){
            fitness = calculateProfits(solution);
        }
        return fitness;
    }
}
