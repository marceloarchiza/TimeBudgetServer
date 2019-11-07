package timebudget.handlers.categories;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.logging.Level;

import com.sun.net.httpserver.HttpExchange;
import timebudget.ServerFacade;
import timebudget.handlers.HandlerBase;
import timebudget.log.Corn;
import timebudget.model.Category;
import timebudget.model.User;

import static timebudget.ServerFacade.getInstance;


public class GetAllCategoriesHandler extends HandlerBase {

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		Corn.log(Level.FINEST, "Get All Categories Handler");
		try {
			String token = getAuthenticationToken(httpExchange);
//			String reqBody = getRequestBody(httpExchange);
//			if(reqBody == null || reqBody.isEmpty()) {
//				httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, -1);
//				return;
//			}

			List<Category> results = ServerFacade.getInstance().getAllActiveCategories(new User(token));

			httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			sendResponseBody(httpExchange, results);
		} catch(Exception e){
			Corn.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
			httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
		}
	}
}
