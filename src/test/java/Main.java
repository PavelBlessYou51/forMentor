import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Hello", 5);
        map.put("By", 2);
        map.put("xixixi", null);
        map.computeIfAbsent("java", s -> s.length());
        map.forEach((k, v) -> System.out.println(k + " : " + v));
    }
}
