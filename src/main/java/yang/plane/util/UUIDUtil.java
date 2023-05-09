package yang.plane.util;

import java.util.UUID;

public class UUIDUtil {

    //得到32位的uuid
    public static String getUUID32(){

        return UUID.randomUUID().toString().replace("-", "").toLowerCase();

    }

    public static void main(String[] args) {
        System.out.printf(getUUID32());
    }



}
