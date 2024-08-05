package be.jossart.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import be.jossart.javabeans.Person;
import oracle.jdbc.OracleTypes;

public class PersonDAO extends DAO<Person>{

	public PersonDAO(Connection conn) {
		super(conn);
	}

	@Override
	public  boolean create(Person obj) {
		boolean success = false;

		String query = "{call Insert_Person(?,?,?,?)}";
		try (CallableStatement cs = this.connect.prepareCall(query)) {

			cs.setString(1, obj.getFirstname());
			cs.setString(2, obj.getLastname());
			cs.setString(3, obj.getUsername());
			cs.setString(4, obj.getPassword());
			
			cs.executeUpdate(); // return int type that's why i use 
								//success = true; and not success = cs.executeUpdate();
			success = true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return success;
	}

	@Override
	public boolean delete(Person obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Person obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
    public Person find(int id) {
		Person person = null;

        String query = "{ call findPersonById(?, ?) }";
	    try (CallableStatement cs = this.connect.prepareCall(query)) {
	    	cs.setInt(1, id);
            cs.registerOutParameter(2, OracleTypes.CURSOR);

	        cs.execute();

	        try (ResultSet resultSet = (ResultSet) cs.getObject(3)) {
	            if (resultSet.next()) {
	                person = new Person();
	                person.setIdPerson(resultSet.getInt("IdPerson"));
	                person.setFirstname(resultSet.getString("Firstname"));
	                person.setLastname(resultSet.getString("Lastname"));
	                person.setUsername(resultSet.getString("Username"));
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("Error: " + e.getMessage());
	    }

	    return person;
    }
	
	public Person login(String username, String password) {
	    Person person = null;

	    String query = "{call Login(?,?,?)}";
	    try (CallableStatement cs = this.connect.prepareCall(query)) {
	        cs.setString(1, username);
	        cs.setString(2, password);
	        cs.registerOutParameter(3, OracleTypes.CURSOR);

	        cs.execute();

	        try (ResultSet resultSet = (ResultSet) cs.getObject(3)) {
	            if (resultSet.next()) {
	                person = new Person();
	                person.setIdPerson(resultSet.getInt("IdPerson"));
	                person.setFirstname(resultSet.getString("Firstname"));
	                person.setLastname(resultSet.getString("Lastname"));
	                person.setUsername(resultSet.getString("Username"));
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("Error: " + e.getMessage());
	    }

	    return person;
	}

	public boolean updatePassword(int idPerson, String newPassword) {
		
		String query = "{call UpdatePassword(?,?)}";
	    
		try (CallableStatement cs = this.connect.prepareCall(query)) {
	        cs.setInt(1, idPerson);
	        cs.setString(2, newPassword);

	        int updatedRows = cs.executeUpdate();

	        return updatedRows > 0;
	    } catch (SQLException e) {
	        System.out.println("Error: " + e.getMessage());
	        return false;
	    }
	}
	public Person findId(Person person) {
        String query = "{ call findPersonId(?, ?) }";
	    try (CallableStatement cs = this.connect.prepareCall(query)) {
	    	cs.setString(1, person.getUsername());
            cs.registerOutParameter(2, OracleTypes.CURSOR);

	        cs.execute();

	        try (ResultSet resultSet = (ResultSet) cs.getObject(3)) {
	            if (resultSet.next()) {
	                person = new Person();
	                person.setIdPerson(resultSet.getInt("IdPerson"));
	                person.setFirstname(resultSet.getString("Firstname"));
	                person.setLastname(resultSet.getString("Lastname"));
	                person.setUsername(resultSet.getString("Username"));
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("Error: " + e.getMessage());
	    }

	    return person;
    }
	public  Person getPersonByPersonId(int person_id) {
		String query = "SELECT * FROM PERSON WHERE IDPERSON = ?";
		Person person = new Person();
        try (PreparedStatement preparedStatement = this.connect.prepareStatement(query)) {
            preparedStatement.setInt(1, person_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
            	if (resultSet.next()) {
	                person = new Person();
	                person.setIdPerson(resultSet.getInt("IdPerson"));
	                person.setFirstname(resultSet.getString("Firstname"));
	                person.setLastname(resultSet.getString("Lastname"));
	                person.setUsername(resultSet.getString("Username"));
	                //System.out.println(person);
	            }
            } catch (SQLException e) {
    	        System.out.println("Error: " + e.getMessage());
    	    }

		} catch (SQLException e) {
	        System.out.println("Error: " + e.getMessage());
	    }
		return person;
	}
}
