package id.ac.ui.cs.advprog.youkosoadmin.utils;

public class DefaultResponseBuilder<T> {
    private int statusCode;
    private String message;
    private boolean success;
    private T data;

    public DefaultResponseBuilder<T> statusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public DefaultResponseBuilder<T> message(String message) {
        this.message = message;
        return this;
    }

    public DefaultResponseBuilder<T> success(boolean success) {
        this.success = success;
        return this;
    }

    public DefaultResponseBuilder<T> data(T data) {
        this.data = data;
        return this;
    }

    public DefaultResponse<T> build() {
        DefaultResponse<T> response = new DefaultResponse<>();
        response.setStatusCode(this.statusCode);
        response.setMessage(this.message);
        response.setSuccess(this.success);
        response.setData(this.data);
        return response;
    }
}