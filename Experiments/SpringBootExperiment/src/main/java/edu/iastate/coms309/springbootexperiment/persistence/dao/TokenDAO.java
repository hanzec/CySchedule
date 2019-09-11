package edu.iastate.coms309.springbootexperiment.persistence.dao;

import edu.iastate.coms309.springbootexperiment.persistence.model.Token;

public interface TokenDAO {

    public void save(Token token);

    public void delete(Token token);
}
