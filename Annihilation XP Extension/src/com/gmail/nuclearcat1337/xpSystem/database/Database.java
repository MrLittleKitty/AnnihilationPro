package com.gmail.nuclearcat1337.xpSystem.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;

public abstract class Database
{
	public static Database getMySQLDatabase(String hostname, String port,String database, String username, String password)
	{
		Database data = new MySQLDatabase(hostname,port,database,username,password);
		try
		{
			data.openConnection();
		}
		catch (ClassNotFoundException | SQLException e)
		{
			//e.printStackTrace();
			return null;
		}
		return data;
	}
	
	protected ConcurrentLinkedQueue<AsyncQuery> quereys;
	protected ConcurrentLinkedQueue<AsyncLogQuery> logs;
	protected Connection connection;

	public void addNewAsyncQuery(AsyncQuery x)
	{
		quereys.add(x);
	}
	
	public void addNewAsyncLogQuery(AsyncLogQuery x)
	{
		logs.add(x);
	}
	
	public void call(AsyncQuery x)
	{
		try
		{
			Statement statement = getConnection().createStatement();
	
			if (x.isCallback()) 
			{
				try 
				{
					statement.execute(x.getQuerey());
					x.setResult(statement.getResultSet());
					x.run();
				}
				catch (Exception e) 
				{
					e.printStackTrace();
					Bukkit.getLogger().log(Level.SEVERE, "CCM: Error executing setResult: " + e.getMessage());
				}
			}
			else 
			{
				statement.execute(x.getQuerey());
			}
			
			statement.close();
		}
		catch(SQLException g)
		{
			Bukkit.getLogger().info(x.getQuerey()+" HAD AN ERROR!!!!!!");
			g.printStackTrace();
		}
	}
	
	
	/**
	 * Creates a new Database
	 * 
	 * @param plugin
	 *            Plugin instance
	 */
	protected Database()
	{
		// this.plugin = plugin;
		this.connection = null;
		quereys = new ConcurrentLinkedQueue<AsyncQuery>();
		logs = new ConcurrentLinkedQueue<AsyncLogQuery>();
		
		Bukkit.getScheduler().runTaskLaterAsynchronously(AnnihilationMain.getInstance(), new DBQueryAsync(), 5);
		Bukkit.getScheduler().runTaskLaterAsynchronously(AnnihilationMain.getInstance(), new DBLogQueryAsync(), 5);
	}

	/**
	 * Opens a connection with the database
	 * 
	 * @return Opened connection
	 * @throws SQLException
	 *             if the connection can not be opened
	 * @throws ClassNotFoundException
	 *             if the driver cannot be found
	 */
	public abstract Connection openConnection() throws SQLException,
			ClassNotFoundException;

	/**
	 * Checks if a connection is open with the database
	 * 
	 * @return true if the connection is open
	 * @throws SQLException
	 *             if the connection cannot be checked
	 */
	public boolean checkConnection() throws SQLException
	{
		return connection != null && !connection.isClosed();
	}

	/**
	 * Gets the connection with the database
	 * 
	 * @return Connection with the database, null if none
	 */
	public Connection getConnection()
	{
		return connection;
	}

	/**
	 * Closes the connection with the database
	 * 
	 * @return true if successful
	 * @throws SQLException
	 *             if the connection cannot be closed
	 */
	public boolean closeConnection()
	{
		if (connection == null)
		{
			return false;
		}
		try
		{
			connection.close();
		}
		catch (SQLException e)
		{
			return false;
		}
		return true;
	}

	/**
	 * Executes a SQL Query<br>
	 * 
	 * If the connection is closed, it will be opened
	 * 
	 * @param query
	 *            Query to be run
	 * @return the results of the query
	 * @throws SQLException
	 *             If the query cannot be executed
	 * @throws ClassNotFoundException
	 *             If the driver cannot be found; see {@link #openConnection()}
	 */
	public ResultSet querySQL(String query) throws SQLException,
			ClassNotFoundException
	{
		if (!checkConnection())
		{
			openConnection();
		}

		Statement statement = connection.createStatement();

		ResultSet result = statement.executeQuery(query);

		return result;
	}

	/**
	 * Executes an Update SQL Query<br>
	 * See {@link java.sql.Statement#executeUpdate(String)}<br>
	 * If the connection is closed, it will be opened
	 * 
	 * @param query
	 *            Query to be run
	 * @return Result Code, see {@link java.sql.Statement#executeUpdate(String)}
	 * @throws SQLException
	 *             If the query cannot be executed
	 * @throws ClassNotFoundException
	 *             If the driver cannot be found; see {@link #openConnection()}
	 */
	public int updateSQL(String query) throws SQLException,
			ClassNotFoundException
	{
		if (!checkConnection())
		{
			openConnection();
		}

		Statement statement = connection.createStatement();

		int result = statement.executeUpdate(query);

		return result;
	}
	
	private class DBQueryAsync implements Runnable 
	{
		public void run() 
		{
			AsyncQuery x;
			
			while (quereys != null && !quereys.isEmpty()) 
			{
				try 
				{
					x = quereys.poll();
				}
				catch (NullPointerException e)
				{
					break;
				}
				
				try 
				{
					Statement statement = getConnection().createStatement();

					if (x.isCallback()) 
					{
						try 
						{
							statement.execute(x.getQuerey());
							x.setResult(statement.getResultSet());
							Bukkit.getScheduler().scheduleSyncDelayedTask(AnnihilationMain.getInstance(), x);
						}
						catch (Exception e) 
						{
							e.printStackTrace();
							Bukkit.getLogger().log(Level.SEVERE, "Annihilation: Error executing setResult: " + e.getMessage());
						}
					}
					else 
					{
						statement.execute(x.getQuerey());
					}
					
					statement.close();
				}
				catch (SQLException e)
				{
					e.printStackTrace();
					Bukkit.getLogger().log(Level.SEVERE, "Annihilation: Error executing query: " + e.getMessage() + " -- " + x.getQuerey());
					break;
				}
			}
			
			Bukkit.getScheduler().runTaskLaterAsynchronously(AnnihilationMain.getInstance(), this, 5);
		}
	}
	
	
	private class DBLogQueryAsync implements Runnable 
	{
		public void run() 
		{
			AsyncLogQuery dbQuery;
			
			while (logs != null && !logs.isEmpty())
			{
				
				try 
				{
					dbQuery = logs.poll();
				}
				catch (NullPointerException e)
				{
					break;
				}
				
				try 
				{
					Statement statement = getConnection().createStatement();
					statement.executeUpdate(dbQuery.getQuery());
					//plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, dbQuery);
				}
				catch (SQLException e)
				{
					e.printStackTrace();
					Bukkit.getLogger().log(Level.SEVERE, "CCDB: Error executing setResult: " + e.getMessage());
				}

			}
			Bukkit.getScheduler().runTaskLaterAsynchronously(AnnihilationMain.getInstance(), this, 5);
		}
	}
}
