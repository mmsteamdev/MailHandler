import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class DataBaseHandler {
    private Connection conn;

    public DataBaseHandler() {
        String url = System.getenv("MYSQL_URL") + "/" + System.getenv("MYSQL_DATABASE");
        String user = System.getenv("MYSQL_USER");
        String password = System.getenv("MYSQL_PASSWORD");
        String root_user = "root";
        String root_password = System.getenv("MYSQL_ROOT_PASSWORD");
        String driverClass = "com.mysql.cj.jdbc.Driver";

        try {
            Class.forName(driverClass);
            this.conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertIntoDB(MailObject obj) throws SQLException {
        int index = 1;
        String table = "__";

        PreparedStatement pst = this.conn.prepareStatement(
                "INSERT INTO " +
                        table +
                        " (" +
                        obj.getKeysInsert() +
                        ") VALUES (?" +
                        this.getValuesQuestionMarks(obj) +
                        ")"
        );

        for (Map.Entry<String, String> entry : obj.getJsonDict().entrySet()){
            pst.setString(index, entry.getValue());
            index ++;
        }
        pst.executeUpdate();
    }

    private String getValuesQuestionMarks(MailObject obj){
        return new String(new char[obj.getJsonDictLen() - 1]).replace("\0", ", ?");
    }
}
