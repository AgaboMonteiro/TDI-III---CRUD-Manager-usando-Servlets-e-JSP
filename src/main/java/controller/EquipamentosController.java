package controller;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Equipamento;
import model.ModelException;
import model.User;
import model.dao.EquipamentoDAO;
import model.dao.DAOFactory;
import java.math.BigDecimal;


@WebServlet(urlPatterns = {"/equipamentos", "/equipamento/form",
        "/equipamento/insert", "/equipamento/delete","/equipamento/update"})
public class EquipamentosController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getRequestURI();

        switch (action) {
        case "/crud-manager/equipamento/form": {
            CommonsController.listUsers(req);
            req.setAttribute("action", "insert");
            ControllerUtil.forward(req, resp, "/form-equipamento.jsp");
            break;
        }
        case "/crud-manager/equipamento/update": {
            String idStr = req.getParameter("equipamentoId");
            int idEquipamento = Integer.parseInt(idStr);

            EquipamentoDAO dao = DAOFactory.createDAO(EquipamentoDAO.class);

            Equipamento equipamento = null;

            try {
                equipamento = dao.findById(idEquipamento);
            } catch (ModelException e) {
                e.printStackTrace();
            }

            CommonsController.listUsers(req);
            req.setAttribute("action", "update");
            req.setAttribute("equipamento", equipamento);

            ControllerUtil.forward(req, resp, "/form-equipamento.jsp");
            break;
        }
        default:
            listEquipamentos(req);

            ControllerUtil.transferSessionMessagesToRequest(req);

            ControllerUtil.forward(req, resp, "/equipamentos.jsp");
        }
    }

    private void listEquipamentos(HttpServletRequest req) {
        EquipamentoDAO dao = DAOFactory.createDAO(EquipamentoDAO.class);

        int limit = 5;
        int currentPage = 1;

        String pageParameter = req.getParameter("page");

        if (pageParameter != null) {
            try {
                currentPage = Integer.parseInt(pageParameter);
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        if (currentPage < 1) {
            currentPage = 1;
        }

        try {
            int totalEquipamentos = dao.countAll();

            int totalPages = (int) Math.ceil((double) totalEquipamentos / limit);

            if (totalPages < 1) {
                totalPages = 1;
            }

            if (currentPage > totalPages) {
                currentPage = totalPages;
            }

            int offset = (currentPage - 1) * limit;

            List<Equipamento> equipamentos = dao.listAll(limit, offset);

            req.setAttribute("equipamentos", equipamentos);
            req.setAttribute("currentPage", currentPage);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("totalEquipamentos", totalEquipamentos);

        } catch (ModelException e) {
            e.printStackTrace();
            ControllerUtil.errorMessage(req, "Erro ao carregar a lista de equipamentos.");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getRequestURI();

        switch (action) {
        case "/crud-manager/equipamento/insert": {
            insertEquipamento(req, resp);
            break;
        }

        case "/crud-manager/equipamento/delete": {
            deleteEquipamento(req, resp);
            break;
        }

        case "/crud-manager/equipamento/update": {
            updateEquipamento(req, resp);
            break;
        }

        default:
            System.out.println("URL inválida " + action);
        }

        ControllerUtil.redirect(resp, req.getContextPath() + "/equipamentos");
    }
    
    private void updateEquipamento(HttpServletRequest req, HttpServletResponse resp) {

        String equipamentoIdStr = req.getParameter("equipamentoId");
        String nome = req.getParameter("nome");
        String tipo = req.getParameter("tipo");
        String dataAquisicao = req.getParameter("dataAquisicao");
        String valor = req.getParameter("valor");
        String descricao = req.getParameter("descricao");
        Integer userId = Integer.parseInt(req.getParameter("user"));

        Equipamento equipamento = new Equipamento(Integer.parseInt(equipamentoIdStr));

        equipamento.setNome(nome);
        equipamento.setTipo(tipo);
        equipamento.setDataAquisicao(ControllerUtil.formatDate(dataAquisicao));
        equipamento.setValor(new BigDecimal(valor));
        equipamento.setDescricao(descricao);
        equipamento.setUser(new User(userId));

        EquipamentoDAO dao = DAOFactory.createDAO(EquipamentoDAO.class);

        try {
            if (dao.update(equipamento)) {
                ControllerUtil.sucessMessage(req, "Equipamento '"
                        + equipamento.getNome() + "' atualizado com sucesso.");
            }
            else {
                ControllerUtil.errorMessage(req, "Equipamento '"
                        + equipamento.getNome() + "' não pode ser atualizado.");
            }
        } catch (ModelException e) {
            e.printStackTrace();
            ControllerUtil.errorMessage(req, e.getMessage());
        }
    }
    
    private void deleteEquipamento(HttpServletRequest req, HttpServletResponse resp) {

        String equipamentoIdParameter = req.getParameter("id");

        int equipamentoId = Integer.parseInt(equipamentoIdParameter);

        EquipamentoDAO dao = DAOFactory.createDAO(EquipamentoDAO.class);

        try {
            Equipamento equipamento = dao.findById(equipamentoId);

            if (equipamento == null)
                throw new ModelException("Equipamento não encontrado para deleção.");

            if (dao.delete(equipamento)) {
                ControllerUtil.sucessMessage(req, "Equipamento '"
                        + equipamento.getNome() + "' deletado com sucesso.");
            }
            else {
                ControllerUtil.errorMessage(req, "Equipamento '"
                        + equipamento.getNome() + "' não pode ser deletado. "
                        + "Há manutenções relacionadas ao equipamento.");
            }

        } catch (ModelException e) {
            if (e.getCause() instanceof
                    SQLIntegrityConstraintViolationException) {
                ControllerUtil.errorMessage(req, e.getMessage());
            }

            e.printStackTrace();
            ControllerUtil.errorMessage(req, e.getMessage());
        }
    }
    
    private void insertEquipamento(HttpServletRequest req, HttpServletResponse resp) {

        String nome = req.getParameter("nome");
        String tipo = req.getParameter("tipo");
        String dataAquisicao = req.getParameter("dataAquisicao");
        String valor = req.getParameter("valor");
        String descricao = req.getParameter("descricao");
        Integer userId = Integer.parseInt(req.getParameter("user"));

        Equipamento equipamento = new Equipamento();

        equipamento.setNome(nome);
        equipamento.setTipo(tipo);
        equipamento.setDataAquisicao(ControllerUtil.formatDate(dataAquisicao));
        equipamento.setValor(new BigDecimal(valor));
        equipamento.setDescricao(descricao);
        equipamento.setUser(new User(userId));

        EquipamentoDAO dao = DAOFactory.createDAO(EquipamentoDAO.class);

        try {
            if (dao.save(equipamento)) {
                ControllerUtil.sucessMessage(req, "Equipamento '" 
                        + equipamento.getNome() + "' salvo com sucesso.");
            }
            else {
                ControllerUtil.errorMessage(req, "Equipamento '" 
                        + equipamento.getNome() + "' não pode ser salvo.");
            }
        } catch (ModelException e) {
            e.printStackTrace();
            ControllerUtil.errorMessage(req, e.getMessage());
        }
    }
}
