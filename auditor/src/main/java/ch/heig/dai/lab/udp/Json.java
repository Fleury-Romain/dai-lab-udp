package ch.heig.dai.lab.udp;

public class Json {
    private String data;

    public Json(String data){
        this.data = data;
    }
    public String getValue(String key) {
        String[] parts = data.split("\"");
        for (int i = 0; i < parts.length - 1; i++) {
            if (parts[i].trim().equals(key)) {
                return parts[i + 2];
            }
        }
        return null;
    }

    public String createJsonString(String uuid, String instrument, long lastActivity) {
        return "{\"uuid\":\"" + uuid + "\",\"instrument\":\"" + instrument + "\",\"lastActivity\":\"" + lastActivity + "\"}";
    }
}
