package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Request {

    private BufferedReader reader;
    private Map<String, String> headers;
    private Map<String, String> variables;
    private boolean headersReaded = false;
    private boolean variablesReaded = false;

    public Request(InputStream in) {
        this.reader = new BufferedReader(new InputStreamReader(in));
    }

    public String getVariable(String key) {
        try {
            if (!variablesReaded) {
                readVariables();
            }
            if (variables.containsKey(key))
                return variables.get(key);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getParameter(String key) {
        try {
            if (!headersReaded) {
                readHeaders();
            }
            if (headers.containsKey(key))
                return headers.get(key);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getMethod() {
        try {
            if (!headersReaded) {
                readHeaders();
            }

            if (headers.containsKey("GET")) return "GET";
            if (headers.containsKey("POST")) return "POST";

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getPath() {
        try {
            if (!headersReaded) {
                readHeaders();
            }

            String path = headers.get(getMethod());
            int pathEndIndex = path.indexOf('?') > 0 ?
                    Math.min(path.indexOf('?'), path.indexOf(' ')) :
                    path.indexOf(' ');
            return path.substring(0, pathEndIndex);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void readVariables()
            throws IOException {

        if (!headersReaded) {
            readHeaders();
        }

        variables = new HashMap<>();
        if (getMethod().equals("POST")) {
            readVariablesPost();
        }
        variablesReaded = true;
    }

    private void readVariablesPost()
            throws IOException {

        String variableStrings[] = readVariableString().split("&");

        for (String variable : variableStrings) {
            variables.put(
                    variable.substring(0, variable.indexOf('=')),
                    variable.substring(variable.indexOf('=') + 1));
        }
    }

    private String readVariableString()
            throws IOException {

        int contentLength = Integer.parseInt(headers.get("Content-Length"));
        StringBuilder body = new StringBuilder();

        for (int i = 0; i < contentLength; i++) {
            int c = reader.read();
            body.append((char) c);
        }

        return body.toString();
    }

    private void readHeaders()
            throws IOException {

        headers = new HashMap<>();
        String line;

        while (!(line = reader.readLine()).equals("") && line != null) {
            int separatorIndex = line.indexOf(':') == -1 ? line.indexOf(' ') : line.indexOf(':');
            String key = line.substring(0, separatorIndex);
            String value = line.substring(separatorIndex + 1).trim();

            headers.put(key, value);
        }
        headersReaded = true;
    }
}
