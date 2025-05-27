/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servlets;


import com.google.gson.Gson;
import dao.ClienteJpaController;
import dto.Cliente;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ClienteServlet")
public class ClienteServlet extends HttpServlet {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("miUnidadDePersistencia");
    ClienteJpaController dao = new ClienteJpaController(emf);
    Gson gson = new Gson();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("application/json");
        List<Cliente> lista = dao.findClienteEntities();
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(lista));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String accion = request.getParameter("accion");

        try {
            switch (accion) {
                case "crear":
                    Cliente nuevo = new Cliente();
                    nuevo.setNdniClie(request.getParameter("ndniClie"));
                    nuevo.setAppaClie(request.getParameter("appaClie"));
                    nuevo.setApmaClie(request.getParameter("apmaClie"));
                    nuevo.setNombClie(request.getParameter("nombClie"));
                    nuevo.setFechNaciClie(sdf.parse(request.getParameter("fechNaciClie")));
                    nuevo.setLogiClie(request.getParameter("logiClie"));
                    nuevo.setPassClie(request.getParameter("passClie"));
                    dao.create(nuevo);
                    break;

                case "editar":
                    Cliente existente = dao.findCliente(Integer.parseInt(request.getParameter("codiClie")));
                    if (existente != null) {
                        existente.setNdniClie(request.getParameter("ndniClie"));
                        existente.setAppaClie(request.getParameter("appaClie"));
                        existente.setApmaClie(request.getParameter("apmaClie"));
                        existente.setNombClie(request.getParameter("nombClie"));
                        existente.setFechNaciClie(sdf.parse(request.getParameter("fechNaciClie")));
                        existente.setLogiClie(request.getParameter("logiClie"));
                        existente.setPassClie(request.getParameter("passClie"));
                        dao.edit(existente);
                    }
                    break;

                case "eliminar":
                    int id = Integer.parseInt(request.getParameter("codiClie"));
                    dao.destroy(id);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}

