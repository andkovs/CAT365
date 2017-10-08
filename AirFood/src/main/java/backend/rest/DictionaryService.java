package backend.rest;

import backend.dao.DictionaryDao;
import backend.dao.RoleDao;
import backend.dao.TokenDAO;
import backend.dao.TokenGenerator;
import backend.model.Dictionary;
import backend.model.ErrorMessage;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.ArrayList;

@Path("/dictionary")
public class DictionaryService {

    @GET
    public Response getDictionaryInJSON(@HeaderParam("token") String token) throws ParseException {
        String[] allowRoles = {"admin", "dispatcher", "agent", "review", "management"};
        if (token == null) {
            return Response.status(401).build();
        }
        if (TokenGenerator.checkToken(token) && TokenGenerator.checkTokenTime(token)) {
            ArrayList<String> roles = new RoleDao().getRolesByToken(token);
            if (roleContain(roles, allowRoles)) {
                Dictionary dictionary = new DictionaryDao().getAllData();
                String message = "Не удалось получить информацию по следующим позициям: ";
                boolean flag = false;
                if (dictionary.getAirports() == null) {
                    message = message + "аэропорты\n";
                    flag = true;
                }
                if (dictionary.getAirlines() == null) {
                    message = message + "авиакомпании\n";
                    flag = true;
                }
                if (dictionary.getBoards() == null) {
                    message = message + "борты\n";
                    flag = true;
                }
                if (dictionary.getFlights() == null) {
                    message = message + "номера рейсов\n";
                    flag = true;
                }
                if (flag) {
                    ErrorMessage error = new ErrorMessage(message);
                    return Response.status(400).entity(error).build();
                }
                new TokenDAO().updateTokenTimeByTokenInDB(token);
                return Response.ok().entity(dictionary).build();
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
