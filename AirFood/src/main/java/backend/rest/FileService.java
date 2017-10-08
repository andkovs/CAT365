package backend.rest;

import backend.dao.*;
import backend.model.UploadFile;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.file.*;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;

@SuppressWarnings("Since15")
@Path("/files")
public class FileService {

    public static final String MY_JERSEY_EXCEL_FILE_XLSX = "MyJerseyExcelFile.xlsx";
    public static final String ORDERS_XLSX = "Orders.xlsx";
    public static final String EXPORT_FOLDER = "/var/cat/excl/";
    //public static final String EXPORT_FOLDER = "C://IdeaProjects//airfoodFiles//";
    //private static final String SERVER_UPLOAD_LOCATION_FOLDER = "C://IdeaProjects//airfoodFiles//";
    private static final String SERVER_UPLOAD_LOCATION_FOLDER = "/var/cat/docs/";

    @GET
    @Path("/excel/{id}")
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getExcelOrderById(@HeaderParam("token") String token, @PathParam("id") Long id) throws ParseException {
        String[] allowRoles = {"admin", "dispatcher", "agent", "management", "review"};
        if (token == null) {
            return Response.status(401).build();
        }
        if (TokenGenerator.checkToken(token) && TokenGenerator.checkTokenTime(token)) {
            ArrayList<String> roles = new RoleDao().getRolesByToken(token);
            if (roleContain(roles, allowRoles)) {
                File file = new File(EXPORT_FOLDER + MY_JERSEY_EXCEL_FILE_XLSX);
                Response.ResponseBuilder response = null;
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    XSSFWorkbook workbook = new FileDao().getExcelById(id);
                    workbook.write(out);
                    out.close();
                    response = Response.ok((Object) file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                response.header("Content-Disposition", "attachment; filename=\"MyJerseyExcelFile.xlsx\"");
                new TokenDAO().updateTokenTimeByTokenInDB(token);
                return response.build();
            }
        }
        return Response.status(401).build();
    }

    @GET
    @Path("/excel")
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getExcelOrders(@HeaderParam("token") String token, @QueryParam("from") String from, @QueryParam("to") String to, @QueryParam("airport") int airportId) throws ParseException {
        String[] allowRoles = {"admin", "dispatcher", "agent", "management", "review"};
        if (token == null) {
            return Response.status(401).build();
        }
        if (TokenGenerator.checkToken(token) && TokenGenerator.checkTokenTime(token)) {
            ArrayList<String> roles = new RoleDao().getRolesByToken(token);
            if (roleContain(roles, allowRoles)) {
                File file = new File(EXPORT_FOLDER + ORDERS_XLSX);
                Response.ResponseBuilder response = null;
                Integer idByToken = new UserDao().getUserIdByToken(token);
                if (idByToken == null) {
                    return Response.status(401).build();
                }
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    String[] reviewArr = {"review"};
                    XSSFWorkbook workbook = null;
                    if(roleContain(roles, reviewArr)){
                        workbook = new FileDao().getExcelOrdersForReview(from, to, airportId, idByToken);
                    }else{
                        workbook = new FileDao().getExcelOrders(from, to, airportId, idByToken);
                    }
                    workbook.write(out);
                    out.close();
                    response = Response.ok((Object) file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                response.header("Content-Disposition", "attachment; filename=\"Orders.xlsx\"");
                new TokenDAO().updateTokenTimeByTokenInDB(token);
                return response.build();
            }
        }
        return Response.status(401).build();
    }

    //Upload a File

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(
            @FormDataParam("token") String token,
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @FormDataParam("id") Long id,
            @FormDataParam("name") String name,
            @FormDataParam("type") String type
    ) throws ParseException {
        String[] allowRoles = {"admin", "dispatcher", "agent"};
        if (token == null) {
            return Response.status(401).build();
        }
        if (TokenGenerator.checkToken(token) && TokenGenerator.checkTokenTime(token)) {
            ArrayList<String> roles = new RoleDao().getRolesByToken(token);
            if (roleContain(roles, allowRoles)) {
                UploadFile uploadFile = new UploadFile();
                String fileName = name;
                Long fileId = new FileDao().setNewFileInDB(id, name, type);
                uploadFile.setName(fileName);
                uploadFile.setId(fileId);
                uploadFile.setOrderId(id);
                String filePath = SERVER_UPLOAD_LOCATION_FOLDER + id + "/" + type + "/" + fileId + fileName.substring(fileName.length() - 4, fileName.length());
                ;
                // save the file to the server
                java.nio.file.Path outputPath = FileSystems.getDefault().getPath(filePath);
                try {
                    Files.copy(fileInputStream, outputPath);
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //saveFile(fileInputStream, filePath);
                new TokenDAO().updateTokenTimeByTokenInDB(token);
                return Response.ok().entity(uploadFile).build();
            }
        }
        return Response.status(401).build();
    }

    // save uploaded file to a defined location on the server
//    private void saveFile(InputStream uploadedInputStream,
//                          String serverLocation) {
//        OutputStream outpuStream = null;
//        try {
//            outpuStream = new FileOutputStream(new File(serverLocation));
//            int read = 0;
//            byte[] bytes = new byte[1024];
//            outpuStream = new FileOutputStream(new File(serverLocation));
//            while ((read = uploadedInputStream.read(bytes)) != -1) {
//                outpuStream.write(bytes, 0, read);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if(uploadedInputStream != null) {
//                    uploadedInputStream.close();
//                }
//                if(outpuStream != null) {
//                    outpuStream.flush();
//                    outpuStream.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @DELETE
    @Path("/upload/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeFile(@HeaderParam("token") String token, @PathParam("id") Integer id) throws
            ParseException {
        String[] allowRoles = {"admin", "dispatcher", "agent"};
        if (token == null) {
            return Response.status(401).build();
        }
        if (TokenGenerator.checkToken(token) && TokenGenerator.checkTokenTime(token)) {
            ArrayList<String> roles = new RoleDao().getRolesByToken(token);
            if (roleContain(roles, allowRoles)) {
                new FileDao().deleteFileFromDBAndServer(id);
                new TokenDAO().updateTokenTimeByTokenInDB(token);
                return Response.ok().build();
            }
        }
        return Response.status(401).build();
    }


    @GET
    @Path("/pdf/{id}")
    @Produces("application/pdf")
    public Response getPDFById(@HeaderParam("token") String token, @PathParam("id") int id,
                               @QueryParam("orderid") int orderId) throws ParseException {
        String[] allowRoles = {"admin", "dispatcher", "agent", "management"};
        if (token == null) {
            return Response.status(401).build();
        }
        if (TokenGenerator.checkToken(token) && TokenGenerator.checkTokenTime(token)) {
            ArrayList<String> roles = new RoleDao().getRolesByToken(token);
            if (roleContain(roles, allowRoles)) {
                Connection connection = ConnectionToMySQLDB.getConnaction();
                String name = null;
                String type = null;
                String sql = "SELECT files.filename, files.filetype " +
                        "FROM airfood.files WHERE files.fileid = ?";
                ResultSet rs = null;
                try {
                    //Statement statement = connection.createStatement();
                    //rs = statement.executeQuery(sql);
                    PreparedStatement psql = connection.prepareStatement(sql);
                    psql.setInt(1, id);
                    rs = psql.executeQuery();
                    while (rs.next()) {
                        name = rs.getString("files.filename");
                        type = rs.getString("files.filetype");
                    }
                    rs.close();
                    psql.close();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                File file = new File(SERVER_UPLOAD_LOCATION_FOLDER + orderId + "/" + type + "/" + id + ".pdf");
                Response.ResponseBuilder response = Response.ok((Object) file);
                response.header("Content-Disposition",
                        "attachment; filename=" + name);
                new TokenDAO().updateTokenTimeByTokenInDB(token);
                return response.build();
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
