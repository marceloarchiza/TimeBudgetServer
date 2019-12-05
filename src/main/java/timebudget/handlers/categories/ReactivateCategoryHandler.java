package timebudget.handlers.categories;

import com.sun.net.httpserver.HttpExchange;
import timebudget.ServerFacade;
import timebudget.TBSerializer;
import timebudget.exceptions.BadCategoryException;
import timebudget.exceptions.BadEventException;
import timebudget.exceptions.BadUserException;
import timebudget.handlers.HandlerBase;
import timebudget.log.Corn;
import timebudget.model.Category;
import timebudget.model.Event;
import timebudget.model.User;
import timebudget.model.request.CategoryIDRequest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ReactivateCategoryHandler extends HandlerBase {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Corn.log(Level.FINEST, "Reactivate Category Handler");
        try {
            String token = getAuthenticationToken(httpExchange);
            if (token == null) {
                Corn.log(Level.SEVERE, "Unable to retrieve user token!");
                throw new BadUserException("Unable to retrieve user token!");
            }

            String reqBody = getRequestBody(httpExchange);
            if(reqBody == null || reqBody.isEmpty()) {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, -1);
                return;
            }

            CategoryIDRequest categoryID = (CategoryIDRequest) TBSerializer.jsonToObj(reqBody, CategoryIDRequest.class);

            if(!isValidSQLiteID(categoryID.getID())) {
                throw new BadCategoryException("categoryID of " + String.valueOf(categoryID.getID()) + " invalid!");
            }
            
            // will throw an exception if it fails
            ServerFacade.getInstance().reactivateCategory(new User(token), categoryID);

            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            sendResponseBody(httpExchange, response, false);
        } catch(Exception e){
            System.out.println(e.getMessage());
            Corn.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
        }
    }
}
