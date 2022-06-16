# [Genetic algorithm](E1)
Algorithm was created for problem of Facility Layout Optimization (examlpe below).

![image](https://user-images.githubusercontent.com/61067969/166144399-a3c3b088-5d77-4708-be16-4461b9816118.png)

Grid 3x4, 9 machines need to be set up, each pair has a cost depending on destination between machines on grid + extra parameters. Sample data is stored in .json files in [folder](flo_dane_v1.2), [README for .json](flo_dane_v1.2/README.pdf).

## Implemented genetic algorithm elements
- [selection](E1/src/zad2/Selection.java) - tournamet and roulette
- [crossover](E1/src/zad2/Crossover.java) - partially matched
- [mutation](E1/src/zad2/Mutation.java) - shuffling random part of flattened array

Sample genetic algorithm [implementation](E1/src/GeneticAlgorithm.java).

# [CSP](E2)
CSP implemented for solving two problems:
## Binary problem (and solution)
- 0 or 1 for each cell
- max sequence of same numbers of length less than 3
- each row and column is unique
- each row and column has same number of 0 and 1

![image](https://user-images.githubusercontent.com/61067969/166144568-c14062ca-f384-4ba3-bdf1-bda149fd2225.png)
![image](https://user-images.githubusercontent.com/61067969/166144580-b1c12903-078c-4d6b-99d2-58c9a98f06ed.png)

## Futoshiki problem (and solution)
Rules for solving futoshiki problem can be found [here](https://www.puzzlemix.com/rules-futoshiki.php).

![image](https://user-images.githubusercontent.com/61067969/166144515-57727c76-966a-41a1-a406-4f900bffc528.png)
![image](https://user-images.githubusercontent.com/61067969/166144537-0fea7585-b7d7-44f6-b981-2ede5034bdc5.png)

## Implementation
- [CSP_Problem](E2/src/CSP_Problem.java) - original problem, intact ([Binary](E2/src/Binary_Problem.java) or [Futoshiki](E2/src/Futoshiki_Problem.java))
- [CSP_PartialSolution](E2/src/CSP_PartialSolution.java) - attempt of solving CSP_Problem, not neccesserly completed or correct
- [CSP_Solver](E2/src/CSP_Solver.java) - one of two CSP solving approaches:
  - [Backtracking](E2/src/CSP_SolverBacktracking.java) - setting next element till constraints are broken, then go back
  - [ForwardChecking](E2/src/CSP_SolverForwardChecking.java) - after each step update domains for each cell to see wheather any element can be selected, if not then go back

![image](https://user-images.githubusercontent.com/61067969/174037630-6d2573a6-6853-4aaa-b843-6684b5b9cc7e.png)

Overview of interfaces and their implementations.

Additionaly for each problem are avaliable different heuristics for ordering cells to fill and ordering their domains.

Both problems:
- in order (left to right, up to down) (+ domain order swapped)
- smallest domain first (+ domain order swapped)

Binary:
- most in rows first (+ domain order swapped)

Futoshiki:
- most constraints first (+ domain order swapped)
- most constraints first unless domain size of any equals 1 (+ domain order swapped)

# [Checkers engine](E3)
Implementation of checkers engine for human player and bots. Avaliable bots:
- Random (for comparison)
- [Min-max](E3/src/BotMinMax.java)
- [Aplha-beta](E3/src/BotAlphaBeta.java)

![image](https://user-images.githubusercontent.com/61067969/174041830-f8e41b5c-33d5-4c52-987a-925ea8b58a15.png)

For accessment of grid avaliable are two approaches:
- [simple](E3/src/SimpleAccessor.java) - counting figures (with weights normal=1, crowned=5)
- [complex](E3/src/ComplexGridAccessor.java) - counting figures + the closer to the border the better

![image](https://user-images.githubusercontent.com/61067969/174041883-dbc5875a-8160-4bef-bbcb-1e3d55ea0b44.png)

Game is avaliable in version:
- only in console (good for bot vs bot): [implementation]()

![image](https://user-images.githubusercontent.com/61067969/166144316-3f26bd5e-9e7f-476e-9615-474e5ae7f4c9.png)
![image](https://user-images.githubusercontent.com/61067969/174045046-083d2060-7c95-42a7-a8da-34f4edb5c80e.png)

- clicable gui + console for displaying wrong inputs and past moves: [implementation]()

![image](https://user-images.githubusercontent.com/61067969/167789960-915d5e3c-cbb5-401d-a24f-75815a27535b.png)

# [Machine learning](E4)
