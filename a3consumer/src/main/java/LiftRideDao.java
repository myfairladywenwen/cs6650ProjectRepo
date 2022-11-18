import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LiftRideDao {
    private static BasicDataSource dataSource;

    public LiftRideDao() {
        dataSource = DBCPDataSource.getDataSource();
    }

    public void createLiftRide(LiftRide newLiftRide) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String insertQueryStatement = "INSERT INTO LiftRides (skierId, resortId, seasonId, dayId, time, liftId) " +
                "VALUES (?,?,?,?,?,?)";
        try {
            conn = dataSource.getConnection();
            preparedStatement = conn.prepareStatement(insertQueryStatement);
            preparedStatement.setInt(1, newLiftRide.getSkierID());
            preparedStatement.setInt(2, newLiftRide.getResortID());
            preparedStatement.setInt(3, newLiftRide.getSeasonID());
            preparedStatement.setInt(4, newLiftRide.getDayID());
            preparedStatement.setInt(5, newLiftRide.getTime());
            preparedStatement.setInt(6, newLiftRide.getLiftId());

            // execute insert SQL statement
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}