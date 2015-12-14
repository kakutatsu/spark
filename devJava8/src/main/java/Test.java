import java.util.ArrayList;

/**
 * Created by chenguo on 9/22/15.
 */
public class Test {
    public static void main(String[] args) {
        Integer a = null;

        System.out.println(a);
        System.out.println(a.intValue());
    }

    public static void aa(Integer... integers) {
        System.out.println(integers.getClass().getName());
        for (Integer integer : integers) {
            System.out.print(integer);
        }
        System.out.println();
    }
}
