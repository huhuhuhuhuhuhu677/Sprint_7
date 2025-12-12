package actions;

public enum Endpoints {
    CREATE_COURIER("/api/v1/courier"),
    LOGIN_COURIER("/api/v1/courier/login"),
    ORDERS("/api/v1/orders");

    private final String path;

    Endpoints(String path) {
        this.path = path;
    }

    public String get() {
        return path;
    }
}