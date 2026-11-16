
package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Equipamento;
import model.Manutencao;
import model.ModelException;
import model.dao.EquipamentoDAO;
import model.dao.ManutencaoDAO;
import model.dao.DAOFactory;

@WebServlet(urlPatterns = {
        "/manutencoes",
        "/manutencao/form",
        "/manutencao/insert",
        "/manutencao/delete",
        "/manutencao/update"
})
public class ManutencoesController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getRequestURI();

        switch (action) {

        case "/crud-manager/manutencao/insert": {
            insertManutencao(req, resp);
            break;
        }

        case "/crud-manager/manutencao/update": {
            updateManutencao(req, resp);
            break;
        }

        case "/crud-manager/manutencao/delete": {
            deleteManutencao(req, resp);
            break;
        }

        default:
            System.out.println("URL inválida " + action);
        }

        ControllerUtil.redirect(resp, req.getContextPath() + "/manutencoes");
    }

    private void insertManutencao(HttpServletRequest req, HttpServletResponse resp) {

        String titulo = req.getParameter("titulo");
        String tipo = req.getParameter("tipo");
        String dataManutencao = req.getParameter("dataManutencao");
        String custo = req.getParameter("custo");
        String descricao = req.getParameter("descricao");

        Integer equipamentoId = Integer.parseInt(
                req.getParameter("equipamento"));

        Manutencao manutencao = new Manutencao();

        manutencao.setTitulo(titulo);
        manutencao.setTipo(tipo);
        manutencao.setDataManutencao(
                ControllerUtil.formatDate(dataManutencao));
        manutencao.setCusto(new BigDecimal(custo));
        manutencao.setDescricao(descricao);
        manutencao.setEquipamento(new Equipamento(equipamentoId));

        ManutencaoDAO dao = DAOFactory.createDAO(ManutencaoDAO.class);

        try {

            if (dao.save(manutencao)) {
                ControllerUtil.sucessMessage(req,
                        "Manutenção '" + manutencao.getTitulo()
                        + "' salva com sucesso.");
            }
            else {
                ControllerUtil.errorMessage(req,
                        "Manutenção '" + manutencao.getTitulo()
                        + "' não pode ser salva.");
            }

        } catch (ModelException e) {
            e.printStackTrace();
            ControllerUtil.errorMessage(req, e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getRequestURI();

        switch (action) {

        case "/crud-manager/manutencao/form": {

            listEquipamentos(req);

            req.setAttribute("action", "insert");

            ControllerUtil.forward(req, resp, "/form-manutencao.jsp");

            break;
        }

        case "/crud-manager/manutencao/update": {

            String idStr = req.getParameter("manutencaoId");

            int idManutencao = Integer.parseInt(idStr);

            ManutencaoDAO dao = DAOFactory.createDAO(ManutencaoDAO.class);

            Manutencao manutencao = null;

            try {
                manutencao = dao.findById(idManutencao);
            } catch (ModelException e) {
                e.printStackTrace();
            }

            listEquipamentos(req);

            req.setAttribute("action", "update");
            req.setAttribute("manutencao", manutencao);

            ControllerUtil.forward(req, resp, "/form-manutencao.jsp");

            break;
        }

        default:

            listManutencoes(req);

            ControllerUtil.transferSessionMessagesToRequest(req);

            ControllerUtil.forward(req, resp, "/manutencoes.jsp");
        }
    }

    private void listEquipamentos(HttpServletRequest req) {

        EquipamentoDAO dao = DAOFactory.createDAO(EquipamentoDAO.class);

        List<Equipamento> equipamentos = null;

        try {
            equipamentos = dao.listAll();
        } catch (ModelException e) {
            e.printStackTrace();
        }

        if (equipamentos != null)
            req.setAttribute("equipamentos", equipamentos);
    }

    private void listManutencoes(HttpServletRequest req) {

        ManutencaoDAO dao = DAOFactory.createDAO(ManutencaoDAO.class);

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
            int totalManutencoes = dao.countAll();

            int totalPages = (int) Math.ceil((double) totalManutencoes / limit);

            if (totalPages < 1) {
                totalPages = 1;
            }

            if (currentPage > totalPages) {
                currentPage = totalPages;
            }

            int offset = (currentPage - 1) * limit;

            List<Manutencao> manutencoes = dao.listAll(limit, offset);

            req.setAttribute("manutencoes", manutencoes);
            req.setAttribute("currentPage", currentPage);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("totalManutencoes", totalManutencoes);

        } catch (ModelException e) {
            e.printStackTrace();
            ControllerUtil.errorMessage(req,
                    "Erro ao carregar a lista de manutenções.");
        }
    }

    private void updateManutencao(HttpServletRequest req, HttpServletResponse resp) {

        String manutencaoIdStr = req.getParameter("manutencaoId");
        String titulo = req.getParameter("titulo");
        String tipo = req.getParameter("tipo");
        String dataManutencao = req.getParameter("dataManutencao");
        String custo = req.getParameter("custo");
        String descricao = req.getParameter("descricao");

        Integer equipamentoId = Integer.parseInt(
                req.getParameter("equipamento"));

        Manutencao manutencao = new Manutencao(
                Integer.parseInt(manutencaoIdStr));

        manutencao.setTitulo(titulo);
        manutencao.setTipo(tipo);
        manutencao.setDataManutencao(
                ControllerUtil.formatDate(dataManutencao));
        manutencao.setCusto(new BigDecimal(custo));
        manutencao.setDescricao(descricao);
        manutencao.setEquipamento(new Equipamento(equipamentoId));

        ManutencaoDAO dao = DAOFactory.createDAO(ManutencaoDAO.class);

        try {

            if (dao.update(manutencao)) {
                ControllerUtil.sucessMessage(req,
                        "Manutenção '" + manutencao.getTitulo()
                        + "' atualizada com sucesso.");
            }
            else {
                ControllerUtil.errorMessage(req,
                        "Manutenção '" + manutencao.getTitulo()
                        + "' não pode ser atualizada.");
            }

        } catch (ModelException e) {
            e.printStackTrace();
            ControllerUtil.errorMessage(req, e.getMessage());
        }
    }

    private void deleteManutencao(HttpServletRequest req, HttpServletResponse resp) {

        String manutencaoIdParameter = req.getParameter("id");

        int manutencaoId = Integer.parseInt(manutencaoIdParameter);

        ManutencaoDAO dao = DAOFactory.createDAO(ManutencaoDAO.class);

        try {

            Manutencao manutencao = dao.findById(manutencaoId);

            if (manutencao == null)
                throw new ModelException(
                        "Manutenção não encontrada para deleção.");

            if (dao.delete(manutencao)) {
                ControllerUtil.sucessMessage(req,
                        "Manutenção '" + manutencao.getTitulo()
                        + "' deletada com sucesso.");
            }
            else {
                ControllerUtil.errorMessage(req,
                        "Manutenção '" + manutencao.getTitulo()
                        + "' não pode ser deletada.");
            }

        } catch (ModelException e) {
            e.printStackTrace();
            ControllerUtil.errorMessage(req, e.getMessage());
        }
    }
}
