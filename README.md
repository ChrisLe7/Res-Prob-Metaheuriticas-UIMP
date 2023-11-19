# Code and Documentation for Work 3.6 Comparison of a Population Algorithm and a Trajectory Algorithm (Knapsack)

The repository where all the code developed for the work proposed in the subject of Problem-Solving with Metaheuristics of the Master in Research in Artificial Intelligence at the International University Menéndez Pelayo can be found.

# Table of Contents
* [Usage Instructions](#usage-instructions)
* [Developed With](#developed-with)

# Usage Instructions
## Available options:
In this repository, both a genetic algorithm and a simulated annealing algorithm are implemented to solve the multidimensional knapsack problem.

### Genetic Algorithm

* -c (--crossover) N : Crossover probability.
* -m (--mutation) N : Mutation probability.
* -p (--population) N : Population size.
* -s (--seed) N : Random seed.
* -f (--file) VAL : Path of the file with the problem instance.

### Simulated Annealing
* -a (--annealingFactor) N : Factor used for decreasing the temperature.
* -p (--initialProbability) N : Initial probability of acceptance.
* -n (--numInitialEstimates) N : Initial number of estimates to adjust the initial temperature.
* -S (--stepsAnnealing) N : Factor used for steps for annealing.
* -s (--seed) N : Random seed.
* -f (--file) VAL : Path of the file with the problem instance.

## How to Compile

To compile the programs, use the following commands after unzipping the compressed file:

### Genetic Algorithm

```
javac -classpath .:args4j-2.33.jar ga/ssGA/*.java
```
### Simulated Annealing
```
javac -classpath .:args4j-2.33.jar sa/ssSA/*.java
```

## How to Run

To run the programs, execute the following commands:

### Genetic Algorithm

```
java -classpath .:args4j-2.33.jar ga.ssGA.Exe 
```
### Simulated Annealing
```
java -classpath .:args4j-2.33.jar sa.ssSA.Exe 
```
## Using Parameters

The above commands will use the default configurations. Below are commands that use parameters:

```
java -classpath .:args4j-2.33.jar ga.ssGA.Exe -f datasets/problem_mknap1.txt -s 25
```

```
java -classpath .:args4j-2.33.jar ga.ssGA.Exe -m 0.01 -c 0.99
```

```
java -classpath .:args4j-2.33.jar sa.ssSA.Exe -a 0.99 -s 50
```

```
java -classpath .:args4j-2.33.jar sa.ssSA.Exe -a 0.95 -s 25 -f datasets/problem_mknap1.txt
```

# Developed With
This work has been developed entirely with Java.