
package model.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Equipamento;
import model.ModelException;
import model.User;

public class MySQLEquipamentoDAO implements EquipamentoDAO {
	
	@Override
	public boolean save(Equipamento equipamento) throws ModelException {
	    DBHandler db = new DBHandler();
	
	    String sqlInsert = "INSERT INTO equipamentos "
	            + "(nome, tipo, data_aquisicao, valor, descricao, user_id) "
	            + "VALUES (?, ?, ?, ?, ?, ?);";
	
	    db.prepareStatement(sqlInsert);//Prepara o comando SQL para receber os parâmetros.
	
	    db.setString(1, equipamento.getNome());
	    db.setString(2, equipamento.getTipo());
	
	    db.setDate(3, equipamento.getDataAquisicao() == null //Se a data estiver nula, utiliza a data atual.
	            ? new Date() : equipamento.getDataAquisicao()); //Caso contrário, utiliza a data informada.
	
	    db.setBigDecimal(4, equipamento.getValor());
	    db.setString(5, equipamento.getDescricao());
	    db.setInt(6, equipamento.getUser().getId());
	
	    return db.executeUpdate() > 0;
	}


	@Override
	public boolean update(Equipamento equipamento) throws ModelException {
	
	    DBHandler db = new DBHandler();//Cria o objeto responsável pela comunicação com o banco de dados.
	
	    String sqlUpdate = "UPDATE equipamentos "
	            + " SET nome = ?, "
	            + " tipo = ?, "
	            + " data_aquisicao = ?, "
	            + " valor = ?, "
	            + " descricao = ?, "
	            + " user_id = ? "
	            + " WHERE id = ?;";
	
	    db.prepareStatement(sqlUpdate);
	
	    db.setString(1, equipamento.getNome());
	    db.setString(2, equipamento.getTipo());
	
	    db.setDate(3, equipamento.getDataAquisicao() == null
	            ? new Date() : equipamento.getDataAquisicao());
	
	    db.setBigDecimal(4, equipamento.getValor());
	    db.setString(5, equipamento.getDescricao());
	    db.setInt(6, equipamento.getUser().getId());
	    db.setInt(7, equipamento.getId());
	
	    return db.executeUpdate() > 0; //Retorna true quando pelo menos uma linha é afetada.
	}
	

	@Override
	public boolean delete(Equipamento equipamento) throws ModelException {
	
	    DBHandler db = new DBHandler();
	
	    String sqlDelete = " DELETE FROM equipamentos "
	            + " WHERE id = ?;";
	
	    db.prepareStatement(sqlDelete);
	    db.setInt(1, equipamento.getId());
	
	    return db.executeUpdate() > 0;
	}
	
	@Override
	public List<Equipamento> listAll(int limit, int offset) throws ModelException {
	    DBHandler db = new DBHandler();

	    List<Equipamento> equipamentos = new ArrayList<Equipamento>();

	    String sqlQuery = "SELECT e.id AS 'equipamento_id', e.*, u.* "
	            + "FROM equipamentos e "
	            + "INNER JOIN users u "
	            + "ON e.user_id = u.id "
	            + "ORDER BY e.id "
	            + "LIMIT ? OFFSET ?;";

	    db.prepareStatement(sqlQuery);
	    db.setInt(1, limit);
	    db.setInt(2, offset);
	    db.executeQuery();

	    while (db.next()) {
	        User user = new User(db.getInt("user_id"));
	        user.setName(db.getString("nome"));
	        user.setGender(db.getString("sexo"));
	        user.setEmail(db.getString("email"));

	        Equipamento equipamento = new Equipamento(db.getInt("equipamento_id"));
	        equipamento.setNome(db.getString("nome"));
	        equipamento.setTipo(db.getString("tipo"));
	        equipamento.setDataAquisicao(db.getDate("data_aquisicao"));
	        equipamento.setValor(db.getBigDecimal("valor"));
	        equipamento.setDescricao(db.getString("descricao"));
	        equipamento.setUser(user);

	        equipamentos.add(equipamento);
	    }

	    return equipamentos;
	}

	@Override
	public int countAll() throws ModelException {
	    DBHandler db = new DBHandler();

	    String sqlQuery = "SELECT COUNT(*) AS total FROM equipamentos";

	    db.createStatement();
	    db.executeQuery(sqlQuery);

	    if (db.next()) {
	        return db.getInt("total");
	    }

	    return 0;
	}

	@Override
	public List<Equipamento> listAll() throws ModelException {
	    DBHandler db = new DBHandler();

	    List<Equipamento> equipamentos = new ArrayList<Equipamento>();

	    String sqlQuery = " SELECT e.id as 'equipamento_id', e.*, u.* \n"
	            + " FROM equipamentos e \n"
	            + " INNER JOIN users u \n"
	            + " ON e.user_id = u.id;";

	    db.createStatement();
	    db.executeQuery(sqlQuery);

	    while (db.next()) {
	        User user = new User(db.getInt("user_id"));
	        user.setName(db.getString("nome"));
	        user.setGender(db.getString("sexo"));
	        user.setEmail(db.getString("email"));

	        Equipamento equipamento = new Equipamento(db.getInt("equipamento_id")); //Cria o objeto com o ID do equipamento e, em seguida, preenche seus atributos
	        equipamento.setNome(db.getString("nome"));
	        equipamento.setTipo(db.getString("tipo"));
	        equipamento.setDataAquisicao(db.getDate("data_aquisicao"));
	        equipamento.setValor(db.getBigDecimal("valor"));
	        equipamento.setDescricao(db.getString("descricao"));
	        equipamento.setUser(user);

	        equipamentos.add(equipamento);
	    }

	    return equipamentos;
	}
	
	@Override
	public Equipamento findById(int id) throws ModelException {
	    DBHandler db = new DBHandler();

	    String sql = "SELECT * FROM equipamentos WHERE id = ?;";//O ? é um parâmetro da consulta. O setInt(1, id) substitui esse parâmetro pelo ID recebido

	    db.prepareStatement(sql);
	    db.setInt(1, id);
	    db.executeQuery();

	    Equipamento equipamento = null;

	    while (db.next()) {
	        equipamento = new Equipamento(id);
	        equipamento.setNome(db.getString("nome"));
	        equipamento.setTipo(db.getString("tipo"));
	        equipamento.setDataAquisicao(db.getDate("data_aquisicao"));
	        equipamento.setValor(db.getBigDecimal("valor"));
	        equipamento.setDescricao(db.getString("descricao"));

	        UserDAO userDAO = DAOFactory.createDAO(UserDAO.class);//Cria um UserDAO usando a DAOFactory.
	        User user = userDAO.findById(db.getInt("user_id"));//Busca o usuário pelo ID da chave estrangeira.

	        equipamento.setUser(user); //Associa o usuário ao equipamento.

	        break;
	    }

	    return equipamento;
	}

}
