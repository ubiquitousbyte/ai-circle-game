package org.de.htw.aiforgames.circlegame;

import lenz.htw.coast.world.GraphNode;

import java.util.List;

public interface SearchStrategy<S, A> {

    List<A> search(SearchProblem<S, A> problem);
}
