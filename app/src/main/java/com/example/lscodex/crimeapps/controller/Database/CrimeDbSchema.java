package com.example.lscodex.crimeapps.controller.Database;

/**
 * table name init
 */

public class CrimeDbSchema {
    public static final class CrimeTable{
        public static final String TABLE_NAME ="t_crimes";

        public static final class Cols{
            public static final String UUID="uuid";
            public static  final String TITLE ="title";
            public static final String DATE ="date";
            public static final String SOLVED ="solved";
            public static final String SUSPECT  = "suspect";
        }
    }
}
