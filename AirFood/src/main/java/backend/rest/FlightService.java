package backend.rest;

import backend.dao.FlightDao;
import backend.dao.RoleDao;
import backend.dao.TokenDAO;
import backend.dao.TokenGenerator;
import backend.model.ErrorMessage;
import backend.model.Flight;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.ArrayList;

@Path("/flights")
public class FlightService {

    private final FlightDao dao = new FlightDao();

    @POST
    public Response setNewFlight(@HeaderParam("token") String token, Flight flight) throws ParseException {
        String[] allowRoles = {"admin", "dispatcher"};
        if (token == null) {
            return Response.status(401).build();
        }
        if (TokenGenerator.checkToken(token) && TokenGenerator.checkTokenTime(token)) {
            ArrayList<String> roles = new RoleDao().getRolesByToken(token);
            if (roleContain(roles, allowRoles)) {
                Flight fl = dao.setFlightInDB(flight);
                if (fl == null) {
                    ErrorMessage error = new ErrorMessage("Не удалось сохранить информацию о рейсе. Возможно рейс с таким номером уже существует.");
                    return Response.status(400).entity(error).build();
                }
                new TokenDAO().updateTokenTimeByTokenInDB(token);
                return Response.ok().entity(fl).build();
            }
        }
        return Response.status(401).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateFlight(@HeaderParam("token") String token, @PathParam("id") int id, Flight flight) throws ParseException {
        String[] allowRoles = {"admin", "dispatcher"};
        if (token == null) {
            return Response.status(401).build();
        }
        if (TokenGenerator.checkToken(token) && TokenGenerator.checkTokenTime(token)) {
            ArrayList<String> roles = new RoleDao().getRolesByToken(token);
            if (roleContain(roles, allowRoles)) {
                String status = dao.updateFlightInDB(id, flight);
                if (status == null) {
                    ErrorMessage error = new ErrorMessage("Не удалось изменить информацию о рейсе. Возможно рейс с таким номером уже существует.");
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
    public Response deleteFlight(@HeaderParam("token") String token, @PathParam("id") int id) throws ParseException {
        String[] allowRoles = {"admin", "dispatcher"};
        if (token == null) {
            return Response.status(401).build();
        }
        if (TokenGenerator.checkToken(token) && TokenGenerator.checkTokenTime(token)) {
            ArrayList<String> roles = new RoleDao().getRolesByToken(token);
            if (roleContain(roles, allowRoles)) {
                String status = new FlightDao().deleteFlightFromDB(id);
                if (status == null) {
                    ErrorMessage error = new ErrorMessage("Не удалось удалить информацию о рейсе.");
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
