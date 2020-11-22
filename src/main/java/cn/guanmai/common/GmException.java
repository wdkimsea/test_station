package cn.guanmai.common;

/**
 * @program: test_station
 * @description: gm错误类
 * @author: weird
 * @create: 2019-01-10 18:10
 **/
public class GmException extends Exception {
    static final long serialVersionUID = -987516993124229948L;
    public static String err_msg = "";
    public static Integer err_code = -1;

    public GmException(String message, Integer code) {
        super(message);
        err_msg = message;
        err_code = code;
    }
    public GmException(String message) {
        super(message);
        err_msg = message;
    }

    public GmException(String message, Throwable cause) {
        super(message, cause);
        err_msg = message;
    }

    public String toString() {
        return String.format("\"err_code\": %d, \"err_msg\": %s", err_code, err_msg);
    }
}
