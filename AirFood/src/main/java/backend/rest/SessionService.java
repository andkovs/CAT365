package backend.rest;

import backend.dao.RoleDao;
import backend.dao.TokenDAO;
import backend.dao.TokenGenerator;
import backend.dao.UserDao;
import backend.model.*;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.ArrayList;

@Path("/session")
public class SessionService {

    @GET
    public Response getSession(@HeaderParam("token") String token) throws ParseException {
        if (token == null || token == "") {
            return Response.status(401).build();
        }
        if (TokenGenerator.checkToken(token) && TokenGenerator.checkTokenTime(token)) {
            SessionDTO session = new SessionDTO();
            session.setRoles(new RoleDao().getRolesByToken(token));
            String login = new UserDao().getLoginByTokenFromDB(token);
            session.setUser(new UserDao().getUserByLogin(login));
            new TokenDAO().updateTokenTimeByTokenInDB(token);
            return Response.ok().entity(session).build();

        }
        return Response.status(401).build();
    }

    @POST
    @Path("/login")
    public Response login(LoginDTO login) {
        User user = new UserDao().getUserByLogin(login.getLogin());
        ArrayList<String> roles = new RoleDao().getRolesByLogin(login.getLogin());
        if(user.getLogin()==null){
            ErrorMessage err = new ErrorMessage("Неверный логин");
            return Response.status(402).entity(err).build();//неверный логин
        }
        if(login.getPassword().equals(user.getPassword())){
            user.setPassword(null);
            SessionDTO session = new SessionDTO();
            session.setUser(user);
            new TokenDAO().updateTokenTimeByLoginInDB(login.getLogin());
            TokenDTO token = new TokenDAO().getTokenByLoginFromDB(login.getLogin());
            session.setToken(token.getToken());
            session.setRoles(roles);
            return Response.status(200).entity(session).build();
        }
        else{
            ErrorMessage err = new ErrorMessage("Неверный пароль");
            return Response.status(402).entity(err).build();//неверный пароль
        }
    }

}
