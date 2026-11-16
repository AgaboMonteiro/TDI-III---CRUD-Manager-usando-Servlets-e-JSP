package model.dao;

import java.util.List;

import model.ModelException;
import model.User;

public interface UserDAO {
	boolean save(User user) throws ModelException ;
	boolean update(User user) throws ModelException;
	boolean delete(User user) throws ModelException;
	List<User> listAll() throws ModelException;
	User findById(int id) throws ModelException;
	
	User findByEmail(String email) throws ModelException; //add p login
	
    // Métodos para paginação
    List<User> listAll(int limit, int offset) throws ModelException;
    //limit: quantidade de usuários por página.
    //offset: quantidade de registros que devem ser ignorados.
    int countAll() throws ModelException;
    //Retorna a quantidade total de usuários cadastrados. Esse número será utilizado para calcular quantas páginas precisam ser exibidas.
}
