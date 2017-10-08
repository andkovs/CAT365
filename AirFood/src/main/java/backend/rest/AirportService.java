package backend.rest;

import backend.dao.*;
import backend.model.Airport;
import backend.model.ErrorMessage;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.ArrayList;

@Path("/airports")
public class AirportService {

    private final AirportDao dao = new AirportDao();

    @GET
    public Response getAllAirports(@HeaderParam("token") String token) throws ParseException {
        String[] allowRoles = {"admin", "dispatcher"};
        if (token == null) {
            return Response.status(401).build();
        }
        if (TokenGenerator.checkToken(token) && TokenGenerator.checkTokenTime(token)) {
            ArrayList<String> roles = new RoleDao().getRolesByToken(token);
            if (roleContain(roles, allowRoles)) {
                ArrayList<Airport> airports = dao.getAllAirports();
                if (airports == null) {
                    ErrorMessage error = new ErrorMessage("Не удалось получить " +
                            "информацию об аэропортах.");
                    return Response.status(400).entity(error).build();
                }
                new TokenDAO().updateTokenTimeByTokenInDB(token);
                return Response.ok().entity(airports).build();
            }
        }
        return Response.status(401).build();
    }

    @GET
    @Path("/user")
    public Response getAllAirportsByUser(@HeaderParam("token") String token) throws ParseException {
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
                ArrayList<Airport> airports = dao.getAllAirportsByUserId(idByToken);
                if (airports == null) {
                    ErrorMessage error = new ErrorMessage("Не удалось получить " +
                            "информацию об аэропортах.");
                    return Response.status(400).entity(error).build();
                }
                new TokenDAO().updateTokenTimeByTokenInDB(token);
                return Response.ok().entity(airports).build();
            }
        }
        return Response.status(401).build();
    }

    @POST
    public Response setNewAirport(@HeaderParam("token") String token, Airport airport) throws ParseException {
        String[] allowRoles = {"admin", "dispatcher"};
        if (token == null) {
            return Response.status(401).build();
        }
        if (TokenGenerator.checkToken(token) && TokenGenerator.checkTokenTime(token)) {
            ArrayList<String> roles = new RoleDao().getRolesByToken(token);
            if (roleContain(roles, allowRoles)) {
                Airport air = dao.setAirportInDB(airport);
                if (air == null) {
                    ErrorMessage error = new ErrorMessage("Не удалось сохранить информацию об аэропорте. " +
                            "Возможно аэропорт с таким именем уже существует.");
                    return Response.status(400).entity(error).build();
                }
                new TokenDAO().updateTokenTimeByTokenInDB(token);
                return Response.ok().entity(air).build();
            }
        }
        return Response.status(401).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateAirport(@HeaderParam("token") String token, @PathParam("id") int id, Airport airport) throws ParseException {
        String[] allowRoles = {"admin", "dispatcher"};
        if (token == null) {
            return Response.status(401).build();
        }
        if (TokenGenerator.checkToken(token) && TokenGenerator.checkTokenTime(token)) {
            ArrayList<String> roles = new RoleDao().getRolesByToken(token);
            if (roleContain(roles, allowRoles)) {
                String status = new AirportDao().updateAirportInDB(id, airport);
                if (status == null) {
                    ErrorMessage error = new ErrorMessage("Не удалось изменить информацию об аэропорте. " +
                            "Возможно аэропорт с таким именем уже существует.");
                    return Response.status(400).entity(error).build();
                }
                new TokenDAO().updateTokenTimeByTokenInDB(token);
                return Response.ok().build();
            }
        }
        return Response.status(401).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAirport(@HeaderParam("token") String token, @PathParam("id") int id) throws ParseException {
        String[] allowRoles = {"admin", "dispatcher"};
        if (token == null) {
            return Response.status(401).build();
        }
        if (TokenGenerator.checkToken(token) && TokenGenerator.checkTokenTime(token)) {
            ArrayList<String> roles = new RoleDao().getRolesByToken(token);
            if (roleContain(roles, allowRoles)) {
                String status = new AirportDao().deleteAirportFromDB(id);
                if (status == null) {
                    ErrorMessage error = new ErrorMessage("Не удалось удалить информацию об аэропорте.");
                    return Response.status(400).entity(error).build();
                }
                new TokenDAO().updateTokenTimeByTokenInDB(token);
                return Response.ok().build();
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
