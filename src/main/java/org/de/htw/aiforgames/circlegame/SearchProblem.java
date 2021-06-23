package org.de.htw.aiforgames.circlegame;

import java.util.List;

public interface SearchProblem<S, A> {

    S startState();

    List<A> actions(S state);

    boolean isGoal(S state);

    S successor(S state, A action);

    float stepCost(S state, S statePrime);

}
