package id.ac.ui.cs.advprog.youkosoadmin.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultResponse<T> {
    private int statusCode;
    private String message;
    private boolean success;
    private T data;
}
