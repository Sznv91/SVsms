package ru.softvillage.sms.Common;

public class SimTable {

    public static final String TABLE = "sims";

    public static class COLUMN {
        public static final String ID = "iccid";
        public static final String OPERATOR = "name_o";
        public static final String SLOT = "slot"; //integer
        public static final String SECURE_CODE = "code"; //integer
        public static final String PHONE_NUMBER = "phone_number";

    }

    public static final String CREATE_SCRIPT =
            String.format("create table %s ("
                            + "%s text primary key,"
                            + "%s text,"
                            + "%s integer,"
                            + "%s integer,"
                            + "%s text" + ");",
                    TABLE, COLUMN.ID, COLUMN.OPERATOR, COLUMN.SLOT, COLUMN.SECURE_CODE, COLUMN.PHONE_NUMBER);
}
