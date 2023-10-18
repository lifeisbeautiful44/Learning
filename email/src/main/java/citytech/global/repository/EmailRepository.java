package citytech.global.repository;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;


@Singleton
@Transactional
public class EmailRepository {

    private DataSource dataSource;

    @Inject
    public EmailRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<String> findEmail() throws SQLException {
        Connection connection = dataSource.getConnection();
        String query = "SELECT * FROM email_db.email where id = ? ";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, 1);

        ResultSet resultSet = preparedStatement.executeQuery();
        String email = null;
        while (resultSet.next()) {
            email = resultSet.getString("email");
        }

        if (email != null) {
            return Optional.of(email);
        } else {
            return Optional.empty();
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String toString() {
        return "TestRepository{" +
                "dataSource=" + dataSource +
                '}';
    }
}
