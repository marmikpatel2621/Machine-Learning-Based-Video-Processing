
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by marmik2621 on 12/2/16.
 */
public class KafkaRestCall {

    public static void restCall(String msg)
    {
        Client client = Client.create();
        try {
            msg = URLEncoder.encode(msg, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        WebResource web = client.resource("http://localhost:8000/Client?topic=test&msg="+msg);

        ClientResponse response1 = web.type("application/json")
                .get(ClientResponse.class);
        String resp = response1.getEntity(String.class);
        System.out.println(resp);
    }
}
