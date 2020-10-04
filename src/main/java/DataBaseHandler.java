import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBaseHandler {
    private Connection conn;

    public DataBaseHandler(){
        String url = System.getenv("MYSQL_URL");
        String user = System.getenv("MYSQL_USER");
        String password = System.getenv("MYSQL_PASSWORD");

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setServerName(url);

        try {
            this.conn = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertIntoDB(MailObject obj) throws SQLException {
        String query = "INSERT INTO Mail (email, subject, text)" + " VALUES (?, ?, ?)";
        PreparedStatement pst = this.conn.prepareStatement(query);
        pst.setString(1, obj.getEmail());
        pst.setString(2, obj.getSubject());
        pst.setString(3, obj.getText());

        pst.executeUpdate();
    }
}
