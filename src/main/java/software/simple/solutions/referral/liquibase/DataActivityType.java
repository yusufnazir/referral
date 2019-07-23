package software.simple.solutions.referral.liquibase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.DatabaseException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import software.simple.solutions.framework.core.liquibase.CustomDataTaskChange;
import software.simple.solutions.referral.constants.ReferralTables;

public class DataActivityType extends CustomDataTaskChange {

	private JdbcConnection connection;
	private String id;
	private String name;
	private String code;
	private String description;

	@Override
	public String getConfirmationMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUp() throws SetupException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFileOpener(ResourceAccessor resourceAccessor) {
		// TODO Auto-generated method stub

	}

	@Override
	public ValidationErrors validate(Database database) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(Database database) throws CustomChangeException {
		connection = (JdbcConnection) database.getConnection();

		try {
			String query = "select id_ from " + ReferralTables.ACTIVITY_TYPES_.NAME + " where id_=?";
			PreparedStatement prepareStatement = connection.prepareStatement(query);
			setData(prepareStatement, 1, id);
			ResultSet resultSet = prepareStatement.executeQuery();

			boolean exists = false;
			while (resultSet.next()) {
				exists = true;
			}
			resultSet.close();
			prepareStatement.close();

			if (exists) {
				String update = "update " + ReferralTables.ACTIVITY_TYPES_.NAME + " set "
						+ ReferralTables.ACTIVITY_TYPES_.COLUMNS.NAME_ + "=?,"
						+ ReferralTables.ACTIVITY_TYPES_.COLUMNS.CODE_ + "=?,"
						+ ReferralTables.ACTIVITY_TYPES_.COLUMNS.DESCRIPTION_ + "=? where id_=?";
				prepareStatement = connection.prepareStatement(update);
				setData(prepareStatement, 1, name);
				setData(prepareStatement, 2, code);
				setData(prepareStatement, 3, description);
				setData(prepareStatement, 4, id);
				prepareStatement.executeUpdate();
				prepareStatement.close();
			} else {
				String insert = "insert into " + ReferralTables.ACTIVITY_TYPES_.NAME + "(id_,"
						+ ReferralTables.ACTIVITY_TYPES_.COLUMNS.NAME_ + ","
						+ ReferralTables.ACTIVITY_TYPES_.COLUMNS.CODE_ + ","
						+ ReferralTables.ACTIVITY_TYPES_.COLUMNS.DESCRIPTION_ + ") " + "values(?,?,?,?)";
				prepareStatement = connection.prepareStatement(insert);
				setData(prepareStatement, 1, id);
				setData(prepareStatement, 2, name);
				setData(prepareStatement, 3, code);
				setData(prepareStatement, 4, description);
				prepareStatement.executeUpdate();
				prepareStatement.close();
			}

		} catch (DatabaseException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
