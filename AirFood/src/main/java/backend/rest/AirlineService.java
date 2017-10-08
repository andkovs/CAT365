package backend.rest;

import backend.dao.AirlineDao;
import backend.dao.RoleDao;
import backend.dao.TokenDAO;
import backend.dao.TokenGenerator;
import backend.model.Airline;
import backend.model.ErrorMessage;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.ArrayList;

@Path("/airlines")
public class AirlineService {

    private final AirlineDao dao = new AirlineDao();

    @POST
    public Response setNewAirline(@HeaderParam("token") String token, Airline airline) throws ParseException {
        String[] allowRoles = {"admin", "dispatcher"};
        if (token == null) {
            return Response.status(401).build();
        }
        if (TokenGenerator.checkToken(token) && TokenGenerator.checkTokenTime(token)) {
            ArrayList<String> roles = new RoleDao().getRolesByToken(token);
            if (roleContain(roles, allowRoles)) {
                Airline air = dao.setAirlineInDB(airline);
                if (air == null) {
                    ErrorMessage error = new ErrorMessage("Не удалось сохранить информацию об авиакомпании. Возможно авиакомпания с таким именем уже существует.");
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
    public Response updateAirport(@HeaderParam("token") String token, @PathParam("id") int id, Airline airline) throws ParseException {
        String[] allowRoles = {"admin", "dispatcher"};
        if (token == null) {
            return Response.status(401).build();
        }
        if (TokenGenerator.checkToken(token) && TokenGenerator.checkTokenTime(token)) {
            ArrayList<String> roles = new RoleDao().getRolesByToken(token);
            if (roleContain(roles, allowRoles)) {
                String status = dao.updateAirlineInDB(id, airline);
                if (status == null) {
                    ErrorMessage error = new ErrorMessage("Не удалось изменить информацию об авиакомпании. Возможно авиакомпания с таким именем уже существует.");
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
                String status = dao.deleteAirlineFromDB(id);
                if (status == null) {
                    ErrorMessage error = new ErrorMessage("Не удалось удалить информацию об авиакомпании.");
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
