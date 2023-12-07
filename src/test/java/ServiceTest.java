import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.UsuarioDTOInput;
import org.example.service.UsuarioService;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;


import static org.junit.Assert.assertTrue;


public class ServiceTest {
    private final UsuarioService usuarioService = new UsuarioService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldAddToTheList(){
        UsuarioDTOInput usuarioDTOInput = new UsuarioDTOInput();
        usuarioService.adicionarUsuario(usuarioDTOInput);
        usuarioService.listarUsuarios();
        System.out.println("Usuario should be added and list, size should increase -> expected result: 1 || actual restul: " + usuarioService.listarUsuarios().size());
        assertTrue(usuarioService.listarUsuarios().size() == 1);
    }

    @Test
    public void shouldReturn200() throws IOException {
        //Rodar aplicação principal primeiro e adicionar alguem usuário
        URL urlObj = new URL("http://localhost:4567/usuarios");
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
        connection.setRequestMethod("GET");
        assertTrue(connection.getResponseCode() == 200);
    }

    @Test
    public void shouldReturn201() throws IOException {
        UsuarioDTOInput usuario = new UsuarioDTOInput();
        URL urlObjget = new URL("https://randomuser.me/api/");
        HttpURLConnection connection1 = (HttpURLConnection) urlObjget.openConnection();
        connection1.setRequestMethod("GET");
        int responseCode = connection1.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
//            JSONObject jsonObject = new JSONObject(response.toString());
//            JSONArray resultArray = jsonObject.getJSONArray("results");
//            int id = resultArray.getJSONObject(0).optInt("id", 0);
//            JSONObject nameObject = resultArray.getJSONObject(0).getJSONObject("name");
//            String nome = nameObject.getString("first") + " " + nameObject.getString("last");
//            String senha = resultArray.getJSONObject(0).getJSONObject("login").getString("password");
//            usuario.setId(id);
//            usuario.setNome(nome);
//            usuario.setSenha(senha);

            JsonNode jsonNode = objectMapper.readTree(response.toString());
            JsonNode results = jsonNode.get("results").get(0);
            usuario.setId(0);
            usuario.setNome(results.get("name").get("first").asText());
            usuario.setSenha(results.get("login").get("password").asText());
        }
        URL urlObj = new URL("http://localhost:4567/usuarios");
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
        connection.setRequestProperty("Aceept", "application/json");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        String json = objectMapper.writeValueAsString(usuario);
        connection.getOutputStream().write(json.getBytes());
        assertTrue(connection.getResponseCode() == 201);
    }

}
