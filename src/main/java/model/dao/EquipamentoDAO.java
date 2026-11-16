
package model.dao;

import java.util.List;

import model.ModelException;
import model.Company;
import model.Equipamento;

public interface EquipamentoDAO {
    boolean save(Equipamento equipamento) throws ModelException;
    boolean update(Equipamento equipamento) throws ModelException;
    boolean delete(Equipamento equipamento) throws ModelException;
    List<Equipamento> listAll() throws ModelException;
    Equipamento findById(int id) throws ModelException;
    List<Equipamento> listAll(int limit, int offset) throws ModelException;

    int countAll() throws ModelException;
}

