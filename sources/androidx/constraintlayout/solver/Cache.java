package androidx.constraintlayout.solver;

public class Cache {
    public Pools$Pool<ArrayRow> arrayRowPool = new Pools$SimplePool(256);
    public Pools$Pool<Object> goalVariablePool = new Pools$SimplePool(64);
    public SolverVariable[] mIndexedVariables = new SolverVariable[32];
    public Pools$Pool<SolverVariable> solverVariablePool = new Pools$SimplePool(256);
}
