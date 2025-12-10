package actions;

public enum Endpoints {
    CREATE_COURIER("/api/v1/courier"),
    LOGIN_COURIER("/api/v1/courier/login"),
    ORDERS("/api/v1/orders");

    private final String title;

    Endpoints(String title) {
        this.title = title;
    }

    public String get() {
        return title;
    }
}