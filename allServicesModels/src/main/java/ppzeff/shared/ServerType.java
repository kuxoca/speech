package ppzeff.shared;

public enum ServerType {
    SBER("sber"),
    YANDEX("yandex");
    private final String type;

    ServerType(String type) {
        this.type = type;
    }
    public String getServerType(){ return type;}
}
