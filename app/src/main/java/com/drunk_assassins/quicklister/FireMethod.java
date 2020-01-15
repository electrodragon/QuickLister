package com.drunk_assassins.quicklister;

public class FireMethod {

    /* Start ---------------------------------------------------------------------------------------
     * Following Methods Returns a 2D Array of UsersData which is Stored DB.java
     */

    public static String[][] fetchArrayFromDB(int position) {
        String[][] tempArray;

        if (position == 0) {
            tempArray = DB.MOBILE_APPLICATION_DEVELOPMENT;
        } else if (position == 1) {
            tempArray = DB.CCTV;
        } else if (position == 2) {
            tempArray = DB.GRAPHICS;
        } else if (position == 3) {
            tempArray = DB.NETWORKING;
        } else {
            tempArray = DB.SAFETY;
        }

        return tempArray;
    }

    /* End fetchArrayFromDB() --------------------------------------------------------------------*/

}
