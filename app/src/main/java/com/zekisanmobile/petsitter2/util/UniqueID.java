package com.zekisanmobile.petsitter2.util;

import java.util.UUID;

public class UniqueID {

    public static String generateUniqueID() {
        return UUID.randomUUID().toString();
    }

}
