package de.lmu.ifi.dbs.sysdev.jersey;

import de.lmu.ifi.dbs.sysdev.google.GoogleNearby;
import java.io.IOException;
import java.net.URI;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import java.util.Scanner;


public class SysDevJerseyServer {

    private static String BASE_URL = "http://localhost:9090/sysdev/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     *
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer(String uri) {
        // create a resource config that scans for JAX-RS resources and providers in de.lmu.ifi.dbs.sysdev.jersey package
        ResourceConfig rc = new ResourceConfig().packages("de.lmu.ifi.dbs.sysdev.jersey");
        rc.register(new CORSFilter());
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(uri), rc);
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer(BASE_URL);
        System.out.println(String.format("--------SysDev Jersey Server started with WADL available at %sapplication.wadl --------", BASE_URL));
        System.out.println("Type exit to shut server down.");
        
        //Define a scanner
        Scanner scanner = new Scanner(System.in);

        // Test call
        while(true){
            System.out.println("Enter latitude cordinate e.g., 52.533975860483864");
            String s_lat = scanner.nextLine();
            if (s_lat.equals("exit")){
                break;
            }
            double lat = Double.parseDouble(s_lat);

            System.out.println("Enter longitude cordinate e.g., 13.365554809570312");
            String s_lon = scanner.nextLine();
            if (s_lon.equals("exit")){
                break;
            }
            double lon = Double.parseDouble(s_lon);

            System.out.println("Hit enter to call Google API");
            System.in.read();

            GoogleNearby.nearbySearchJersey(lat, lon, 1, BASE_URL);
        }
        
        //System.in.read();
        server.shutdown();
        System.out.println("Server shut down");
    }

    @Provider
    public static class CORSFilter implements ContainerResponseFilter {

        @Override
        public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
            response.getHeaders().add("Access-Control-Allow-Origin", "*");
            response.getHeaders().add("Access-Control-Allow-Headers","origin, content-type, accept, authorization");
            response.getHeaders().add("Access-Control-Allow-Credentials", "true");
            response.getHeaders().add("Access-Control-Allow-Methods","GET, POST, PUT, DELETE, OPTIONS, HEAD");
        }
    }

}
