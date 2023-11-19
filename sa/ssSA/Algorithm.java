package sa.ssSA;

public class Algorithm {
    private double initial_temperature;
    private double temperature;
    private int item_number;
    private int item_length;
    private int solution_lenght;
    private double initial_probability;
    private double annealing_factor;
    private int steps_for_annealing;
    private Problem problem;
    private Solution current_solution;
    private Solution best_solution;
    private int iters_without_accept;

    public Algorithm(Problem problem, int item_number, int item_length, double initial_probability,
                     int num_initial_estimates, double annealing_factor, int steps_for_annealing){
        this.problem = problem;
        this.item_number = item_number;
        this.item_length = item_length;
        this.solution_lenght = item_number * item_length;
        this.current_solution = new Solution(this.solution_lenght);
        this.annealing_factor = annealing_factor;
        this.steps_for_annealing = steps_for_annealing;
        this.initial_probability = initial_probability;

        this.current_solution.set_fitness(problem.evaluateStep(this.current_solution));

        this.best_solution = new Solution(this.solution_lenght);
        this.best_solution.assign(this.current_solution); // Actualmente la mejor es la única conocida

        double average_fitness_diffs = 0.0;
        this.iters_without_accept = 0;
        for (int i = 0; i < num_initial_estimates; i++){
            Solution sol = new Solution(this.solution_lenght);
            sol.set_fitness(problem.Evaluate(sol));
            Solution neigh  = generateNeighbour(sol);
            neigh.set_fitness(problem.Evaluate(neigh));
            double deltaFitness =  neigh.get_fitness() - sol.get_fitness();

            average_fitness_diffs += Math.abs(deltaFitness);
        }

        average_fitness_diffs /= num_initial_estimates;

        this.temperature = -1. * average_fitness_diffs / Math.log(this.initial_probability);
        this.initial_temperature = this.temperature;
    }

    public Solution generateNeighbour(Solution solution){
        Solution neightbour = new Solution(this.solution_lenght);
        int index_to_change = Exe.rand.nextInt(this.solution_lenght);
        byte value_to_set = solution.get_item(index_to_change) == (byte) (1) ? (byte) (0) : (byte) 1;
        neightbour.set_solution_structure(solution.get_solution_structure());
        neightbour.set_item(index_to_change, value_to_set);
        return neightbour;
    }

    public boolean accept (double delta_fitness){
        // if the problem is one of minimization, then deltaFitness has to be multiplied by -1
        double probability = Math.exp(delta_fitness / this.temperature);
        double rand_sample = Exe.rand.nextDouble();
        return (rand_sample < probability);
    }

    public double evaluateStep(Solution solution) {
        return this.problem.evaluateStep(solution);
    }

    public void go_one_step() throws Exception {
        Solution neighbour = generateNeighbour(this.current_solution);
        neighbour.set_fitness(evaluateStep(neighbour));
        double delta_fitness = neighbour.get_fitness() - this.current_solution.get_fitness();
        // Check if accept the new solution
        if (accept(delta_fitness)){
            this.current_solution.assign(neighbour);
            this.iters_without_accept = 0;
            if (this.current_solution.get_fitness() > this.best_solution.get_fitness()){
                this.best_solution.assign(this.current_solution);
            }
        }
        else{
            iters_without_accept ++;
        }

        // Check number of stepts to cool
        if (this.problem.get_fitness_counter() % this.steps_for_annealing == 0) {
            this.temperature *= annealing_factor;
        }
        // Limite de iteraciones sin cambios
        if (iters_without_accept == 500){
            this.temperature = this.initial_temperature;
            this.iters_without_accept = 0;
        }
    }

    public Solution get_best() {
        return this.best_solution;
    }

    public Solution get_current(){
        return this.current_solution;
    }
}