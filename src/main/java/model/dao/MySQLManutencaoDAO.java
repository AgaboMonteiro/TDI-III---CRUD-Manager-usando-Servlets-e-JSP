package model.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Equipamento;
import model.Manutencao;
import model.ModelException;

public class MySQLManutencaoDAO implements ManutencaoDAO {

	@Override
	public boolean save(Manutencao manutencao) throws ModelException {
	    DBHandler db = new DBHandler();

	    String sqlInsert = "INSERT INTO manutencoes "
	            + "(titulo, tipo, data_manutencao, custo, descricao, equipamento_id) "
	            + "VALUES (?, ?, ?, ?, ?, ?);";

	    db.prepareStatement(sqlInsert);
	    db.setString(1, manutencao.getTitulo());
	    db.setString(2, manutencao.getTipo());
	    db.setDate(3, manutencao.getDataManutencao() == null
	            ? new Date() : manutencao.getDataManutencao());
	    db.setBigDecimal(4, manutencao.getCusto());
	    db.setString(5, manutencao.getDescricao());
	    db.setInt(6, manutencao.getEquipamento().getId());

	    return db.executeUpdate() > 0;
	}
	
	@Override
	public boolean update(Manutencao manutencao) throws ModelException {
	    DBHandler db = new DBHandler();

	    String sqlUpdate = "UPDATE manutencoes "
	            + " SET titulo = ?, "
	            + " tipo = ?, "
	            + " data_manutencao = ?, "
	            + " custo = ?, "
	            + " descricao = ?, "
	            + " equipamento_id = ? "
	            + " WHERE id = ?;";

	    db.prepareStatement(sqlUpdate);
	    db.setString(1, manutencao.getTitulo());
	    db.setString(2, manutencao.getTipo());
	    db.setDate(3, manutencao.getDataManutencao() == null
	            ? new Date() : manutencao.getDataManutencao());
	    db.setBigDecimal(4, manutencao.getCusto());
	    db.setString(5, manutencao.getDescricao());
	    db.setInt(6, manutencao.getEquipamento().getId());
	    db.setInt(7, manutencao.getId());

	    return db.executeUpdate() > 0;
	}
	
	@Override
	public boolean delete(Manutencao manutencao) throws ModelException {
	    DBHandler db = new DBHandler();

	    String sqlDelete = " DELETE FROM manutencoes "
	            + " WHERE id = ?;";

	    db.prepareStatement(sqlDelete);
	    db.setInt(1, manutencao.getId());

	    return db.executeUpdate() > 0;
	}
	
	@Override
	public List<Manutencao> listAll() throws ModelException {
	    DBHandler db = new DBHandler();

	    List<Manutencao> manutencoes = new ArrayList<Manutencao>();

	    //Na consulta usa m.* e e.*. As duas tabelas possuem colunas chamadas id, tipo e descricao.
	    //Isso pode gerar ambiguidade na leitura do ResultSet.
	    //Para evitar esse problema, a versão mais segura é explicitar os campos e utilizar aliases:
	    String sqlQuery = " SELECT "
	            + " m.id AS manutencao_id, "
	            + " m.titulo, "
	            + " m.tipo AS manutencao_tipo, "
	            + " m.data_manutencao, "
	            + " m.custo, "
	            + " m.descricao AS manutencao_descricao, "
	            + " e.id AS equipamento_id, "
	            + " e.nome AS equipamento_nome, "
	            + " e.tipo AS equipamento_tipo, "
	            + " e.data_aquisicao, "
	            + " e.valor, "
	            + " e.descricao AS equipamento_descricao "
	            + " FROM manutencoes m "
	            + " INNER JOIN equipamentos e "
	            + " ON m.equipamento_id = e.id;";

	    db.createStatement();
	    db.executeQuery(sqlQuery);

	    while (db.next()) {
	        Equipamento equipamento = new Equipamento(db.getInt("equipamento_id"));
	        equipamento.setNome(db.getString("equipamento_nome"));
	        equipamento.setTipo(db.getString("equipamento_tipo"));
	        equipamento.setDataAquisicao(db.getDate("data_aquisicao"));
	        equipamento.setValor(db.getBigDecimal("valor"));
	        equipamento.setDescricao(db.getString("equipamento_descricao"));

	        Manutencao manutencao = new Manutencao(db.getInt("manutencao_id"));
	        manutencao.setTitulo(db.getString("titulo"));
	        manutencao.setTipo(db.getString("manutencao_tipo"));
	        manutencao.setDataManutencao(db.getDate("data_manutencao"));
	        manutencao.setCusto(db.getBigDecimal("custo"));
	        manutencao.setDescricao(db.getString("manutencao_descricao"));
	        manutencao.setEquipamento(equipamento);

	        manutencoes.add(manutencao);
	    }

	    return manutencoes;
	}
	
	@Override
	public List<Manutencao> listAll(int limit, int offset) throws ModelException {
	    DBHandler db = new DBHandler();

	    List<Manutencao> manutencoes = new ArrayList<Manutencao>();

	    String sqlQuery = "SELECT "
	            + " m.id AS manutencao_id, "
	            + " m.titulo, "
	            + " m.tipo AS manutencao_tipo, "
	            + " m.data_manutencao, "
	            + " m.custo, "
	            + " m.descricao AS manutencao_descricao, "
	            + " e.id AS equipamento_id, "
	            + " e.nome AS equipamento_nome, "
	            + " e.tipo AS equipamento_tipo, "
	            + " e.data_aquisicao, "
	            + " e.valor, "
	            + " e.descricao AS equipamento_descricao "
	            + " FROM manutencoes m "
	            + " INNER JOIN equipamentos e "
	            + " ON m.equipamento_id = e.id "
	            + " ORDER BY m.id "
	            + " LIMIT ? OFFSET ?;";

	    db.prepareStatement(sqlQuery);
	    db.setInt(1, limit);
	    db.setInt(2, offset);
	    db.executeQuery();

	    while (db.next()) {
	        Equipamento equipamento = new Equipamento(db.getInt("equipamento_id"));
	        equipamento.setNome(db.getString("equipamento_nome"));
	        equipamento.setTipo(db.getString("equipamento_tipo"));
	        equipamento.setDataAquisicao(db.getDate("data_aquisicao"));
	        equipamento.setValor(db.getBigDecimal("valor"));
	        equipamento.setDescricao(db.getString("equipamento_descricao"));

	        Manutencao manutencao = new Manutencao(db.getInt("manutencao_id"));
	        manutencao.setTitulo(db.getString("titulo"));
	        manutencao.setTipo(db.getString("manutencao_tipo"));
	        manutencao.setDataManutencao(db.getDate("data_manutencao"));
	        manutencao.setCusto(db.getBigDecimal("custo"));
	        manutencao.setDescricao(db.getString("manutencao_descricao"));
	        manutencao.setEquipamento(equipamento);

	        manutencoes.add(manutencao);
	    }

	    return manutencoes;
	}
	
	@Override
	public int countAll() throws ModelException {
	    DBHandler db = new DBHandler();

	    String sqlQuery = "SELECT COUNT(*) AS total FROM manutencoes";

	    db.createStatement();
	    db.executeQuery(sqlQuery);

	    if (db.next()) {
	        return db.getInt("total");
	    }

	    return 0;
	}
	
	@Override
	public Manutencao findById(int id) throws ModelException {
	    DBHandler db = new DBHandler();

	    String sql = "SELECT "
	            + " m.id AS manutencao_id, "
	            + " m.titulo, "
	            + " m.tipo AS manutencao_tipo, "
	            + " m.data_manutencao, "
	            + " m.custo, "
	            + " m.descricao AS manutencao_descricao, "
	            + " e.id AS equipamento_id, "
	            + " e.nome AS equipamento_nome, "
	            + " e.tipo AS equipamento_tipo, "
	            + " e.data_aquisicao, "
	            + " e.valor, "
	            + " e.descricao AS equipamento_descricao "
	            + " FROM manutencoes m "
	            + " INNER JOIN equipamentos e "
	            + " ON m.equipamento_id = e.id "
	            + " WHERE m.id = ?;";

	    db.prepareStatement(sql);
	    db.setInt(1, id);
	    db.executeQuery();

	    Manutencao manutencao = null;

	    while (db.next()) {
	        Equipamento equipamento = new Equipamento(db.getInt("equipamento_id"));
	        equipamento.setNome(db.getString("equipamento_nome"));
	        equipamento.setTipo(db.getString("equipamento_tipo"));
	        equipamento.setDataAquisicao(db.getDate("data_aquisicao"));
	        equipamento.setValor(db.getBigDecimal("valor"));
	        equipamento.setDescricao(db.getString("equipamento_descricao"));

	        manutencao = new Manutencao(db.getInt("manutencao_id"));
	        manutencao.setTitulo(db.getString("titulo"));
	        manutencao.setTipo(db.getString("manutencao_tipo"));
	        manutencao.setDataManutencao(db.getDate("data_manutencao"));
	        manutencao.setCusto(db.getBigDecimal("custo"));
	        manutencao.setDescricao(db.getString("manutencao_descricao"));
	        manutencao.setEquipamento(equipamento);

	        break;
	    }

	    return manutencao;
	}
	
	
}