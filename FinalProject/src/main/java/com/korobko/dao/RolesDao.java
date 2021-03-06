package com.korobko.dao;

import com.korobko.entities.EmployeePosition;
import com.korobko.utils.ResourceManager;
import com.korobko.utils.connection.ConnectionWrapper;
import com.korobko.utils.connection.TransactionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vova Korobko
 */
public class RolesDao implements Dao {

    private static final String SELECT_SUBORDINATES_POSITIONS = "select.subordinates.positions";

    RolesDao() {
    }

    /**
     * Fetches all {@code EmployeePosition} objects except {@code EmployeePosition.DIRECTOR}
     *
     * @return the Java {@code List} of {@code EmployeePosition} objects
     */
    public List<EmployeePosition> getSubordinatePositions() {
        ConnectionWrapper connectionWrapper = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<EmployeePosition> positions = null;
        String sql = ResourceManager.QUERIES.getProperty(SELECT_SUBORDINATES_POSITIONS);
        try {
            connectionWrapper = TransactionManager.getConnectionWrapper();
            preparedStatement = connectionWrapper.getPreparedStatement(sql);
            resultSet = preparedStatement.executeQuery();
            positions = new ArrayList<>();
            while (resultSet.next()) {
                EmployeePosition position = EmployeePosition.valueOf(resultSet.getString(DBColumns.ROLE));
                positions.add(position);
            }
        } catch (SQLException e) {
            logger.error("Exception while getting subordinate positions", e);
        } finally {
            closeResources(connectionWrapper, preparedStatement, resultSet);
        }
        return positions;
    }
}
