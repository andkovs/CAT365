package backend.rest;

import backend.dao.BoardDao;
import backend.dao.RoleDao;
import backend.dao.TokenDAO;
import backend.dao.TokenGenerator;
import backend.model.Board;
import backend.model.ErrorMessage;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.ArrayList;

@Path("/boards")
public class BoardService {

    private final BoardDao dao = new BoardDao();

    @POST
    public Response setNewBoard(@HeaderParam("token") String token, Board board) throws ParseException {
        String[] allowRoles = {"admin", "dispatcher"};
        if (token == null) {
            return Response.status(401).build();
        }
        if (TokenGenerator.checkToken(token) && TokenGenerator.checkTokenTime(token)) {
            ArrayList<String> roles = new RoleDao().getRolesByToken(token);
            if (roleContain(roles, allowRoles)) {
                Board brd = dao.setBoardInDB(board);
                if (brd == null) {
                    ErrorMessage error = new ErrorMessage("Не удалось сохранить информацию о борте. Возможно борт с таким номером для этой авиакомпании уже существует.");
                    return Response.status(400).entity(error).build();
                }
                new TokenDAO().updateTokenTimeByTokenInDB(token);
                return Response.ok().entity(brd).build();
            }
        }
        return Response.status(401).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateBoard(@HeaderParam("token") String token, @PathParam("id") int id, Board board) throws ParseException {
        String[] allowRoles = {"admin", "dispatcher"};
        if (token == null) {
            return Response.status(401).build();
        }
        if (TokenGenerator.checkToken(token) && TokenGenerator.checkTokenTime(token)) {
            ArrayList<String> roles = new RoleDao().getRolesByToken(token);
            if (roleContain(roles, allowRoles)) {
                String status = dao.updateBoardInDB(id, board);
                if (status == null) {
                    ErrorMessage error = new ErrorMessage("Не удалось изменить информацию о борте. Возможно борт с таким номером для этой авиакомпании уже существует.");
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
    public Response deleteBoard(@HeaderParam("token") String token, @PathParam("id") int id) throws ParseException {
        String[] allowRoles = {"admin", "dispatcher"};
        if (token == null) {
            return Response.status(401).build();
        }
        if (TokenGenerator.checkToken(token) && TokenGenerator.checkTokenTime(token)) {
            ArrayList<String> roles = new RoleDao().getRolesByToken(token);
            if (roleContain(roles, allowRoles)) {
                String status = dao.deleteBoardFromDB(id);
                if (status == null) {
                    ErrorMessage error = new ErrorMessage("Не удалось удалить информацию о борте.");
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
