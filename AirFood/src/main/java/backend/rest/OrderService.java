package backend.rest;

import backend.dao.*;
import backend.model.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.ArrayList;


@Path("/orders")
public class OrderService {

    private final OrderDao dao = new OrderDao();

    @GET
    public Response getAllOrders(@HeaderParam("token") String token, @QueryParam("from") String from, @QueryParam("to") String to) throws ParseException {
        String[] allowRoles = {"admin", "dispatcher", "agent", "management", "review"};
        if (token == null) {
            return Response.status(401).build();
        }
        if (TokenGenerator.checkToken(token) && TokenGenerator.checkTokenTime(token)) {
            ArrayList<String> roles = new RoleDao().getRolesByToken(token);
            if (roleContain(roles, allowRoles)) {
                Integer idByToken = new UserDao().getUserIdByToken(token);
                if (idByToken == null) {
                    return Response.status(401).build();
                }
                OrderShortResponse orderShortResponse = dao.getShortOrders(from, to, idByToken);
                if (orderShortResponse == null) {
                    ErrorMessage error = new ErrorMessage("Не удалось получить информацию о заказах.");
                    return Response.status(400).entity(error).build();
                }
                new TokenDAO().updateTokenTimeByTokenInDB(token);
                return Response.ok().entity(orderShortResponse).build();
            }
        }
        return Response.status(401).build();
    }

    @GET
    @Path("/preview/{id}")
    public Response getOrderPreviewById(@HeaderParam("token") String token, @PathParam("id") Long id) throws ParseException {
        String[] allowRoles = {"admin", "dispatcher", "agent", "management", "review"};
        if (token == null) {
            return Response.status(401).build();
        }
        if (TokenGenerator.checkToken(token) && TokenGenerator.checkTokenTime(token)) {
            ArrayList<String> roles = new RoleDao().getRolesByToken(token);
            if (roleContain(roles, allowRoles)) {
                OrderPreview orderPreview = dao.getOrderPreviewById(id);
                if (orderPreview == null) {
                    ErrorMessage error = new ErrorMessage("Не получить сохранить информацию о заказе.");
                    return Response.status(400).entity(error).build();
                }
                new TokenDAO().updateTokenTimeByTokenInDB(token);
                return Response.ok().entity(orderPreview).build();
            }
        }
        return Response.status(401).build();
    }

    @GET
    @Path("/{id}")
    public Response getOrderByIdInJSON(@HeaderParam("token") String token, @PathParam("id") Long id) throws ParseException {
        String[] allowRoles = {"admin", "dispatcher"};
        if (token == null) {
            return Response.status(401).build();
        }
        if (TokenGenerator.checkToken(token) && TokenGenerator.checkTokenTime(token)) {
            ArrayList<String> roles = new RoleDao().getRolesByToken(token);
            if (roleContain(roles, allowRoles)) {
                Order order = dao.getOrderById(id);
                if (order == null) {
                    ErrorMessage error = new ErrorMessage("Не удалось получить информацию о заказе.");
                    return Response.status(400).entity(error).build();
                }
                new TokenDAO().updateTokenTimeByTokenInDB(token);
                return Response.ok().entity(order).build();
            }
        }
        return Response.status(401).build();

    }

    @POST
    public Response setNewOrEditCurrentOrder(@HeaderParam("token") String token, Order order) throws ParseException {
        String[] allowRoles = {"admin", "dispatcher"};
        if (token == null) {
            return Response.status(401).build();
        }
        if (TokenGenerator.checkToken(token) && TokenGenerator.checkTokenTime(token)) {
            ArrayList<String> roles = new RoleDao().getRolesByToken(token);
            if (roleContain(roles, allowRoles)) {
                Long id = dao.setNewOrEditOrderInDB(order);
                if (id == null) {
                    ErrorMessage error = new ErrorMessage("Не удалось сохранить информацию о заказе.");
                    return Response.status(400).entity(error).build();
                }
                new TokenDAO().updateTokenTimeByTokenInDB(token);
                return Response.ok().entity(id).build();
            }
        }
        return Response.status(401).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteOrder(@HeaderParam("token") String token, @PathParam("id") int id) throws ParseException {
        String[] allowRoles = {"admin", "dispatcher"};
        if (token == null) {
            return Response.status(401).build();
        }
        if (TokenGenerator.checkToken(token) && TokenGenerator.checkTokenTime(token)) {
            ArrayList<String> roles = new RoleDao().getRolesByToken(token);
            if (roleContain(roles, allowRoles)) {
                String status = dao.deleteOrderFromDB(id);
                if (status == null) {
                    ErrorMessage error = new ErrorMessage("Не удалось удалить информацию о заказе.");
                    return Response.status(400).entity(error).build();
                }
                new TokenDAO().updateTokenTimeByTokenInDB(token);
                return Response.ok().entity(id).build();
            }
        }
        return Response.status(401).build();
    }

    private boolean roleContain(ArrayList<String> roles, String[] allowRoles) {
        for (String a :
                allowRoles) {
            for (String r :
                    roles) {
                if (a.equals(r)) {
                    return true;
                }
            }
        }
        return false;
    }
}
