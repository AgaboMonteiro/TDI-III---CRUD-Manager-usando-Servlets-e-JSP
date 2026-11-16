package model.dao;

import java.util.List;

import model.Equipamento;
import model.Manutencao;
import model.ModelException;

public interface ManutencaoDAO {
    boolean save(Manutencao manutencao) throws ModelException;
    boolean update(Manutencao manutencao) throws ModelException;
    boolean delete(Manutencao manutencao) throws ModelException;
    List<Manutencao> listAll() throws ModelException;
    Manutencao findById(int id) throws ModelException;
    List<Manutencao> listAll(int limit, int offset) throws ModelException;

    int countAll() throws ModelException;
}