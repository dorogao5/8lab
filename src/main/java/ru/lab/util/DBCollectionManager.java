package ru.lab.util;

import ru.lab.Main;
import ru.lab.model.*;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class DBCollectionManager {
    private final String GET_ALL_VEHICLES = "select id, name, x_coord, y_coord, creation_date, engine_power, " +
            "vehicle_type, fuel_type, owner " +
            "from vehicles;";

    private final String INSERT_NEW_VEHICLE = "insert into vehicles (\n" +
            "id, name, x_coord, y_coord, creation_date, engine_power, vehicle_type, fuel_type, owner) " +
            "values (nextval('vehicle_seq'), ?, ?, ?, ?, ?, ?, ?, ?);";

    private final String CLEAR_ALL_VEHICLES = "delete from vehicles";

    private final String GET_NEXT_ID = "select nextval('vehicle_seq')";

    private final String ALTER_ID = "ALTER sequence vehicle_seq RESTART with ";

    private Connection conn = null;


    public DBCollectionManager() {
        try {
            // System.out.println("1.0 loading postgresql driver");
            this.conn = DriverManager.getConnection(Main.getConnectionString());
            //System.out.println("1.1 postgresql driver successfully loaded");
        } catch (SQLException e) {
            // System.out.println("1.2 failed to load postgresql driver");
            throw new RuntimeException(e);
        }
    }

    public Hashtable<Integer, Vehicle> getCollection() {
        Hashtable<Integer, Vehicle> collection = new Hashtable<>() ;
        try {
            //System.out.println("2.1 loading collection from DB");
            ResultSet rset1 = conn.createStatement().executeQuery(GET_ALL_VEHICLES);
            while(rset1.next()) {
                int id = rset1.getInt("id");
                String name = rset1.getString("name");

                Coordinates coordinates = new Coordinates(
                        rset1.getInt("x_coord"),
                        rset1.getInt("y_coord"));

                Date creationDate= rset1.getDate("creation_date");
                float enginePower = rset1.getFloat("engine_power");

                String vehicleType1 = rset1.getString("vehicle_type");
                String fuelType1 = rset1.getString("fuel_type");
                VehicleType vehicleType = vehicleType1 == null ? null : VehicleType.valueOf(vehicleType1);
                FuelType fuelType = fuelType1 == null ? null : FuelType.valueOf(fuelType1);
                String owner = rset1.getString("owner");

                Vehicle vehicle = new Vehicle(id, name, coordinates, enginePower, creationDate, vehicleType, fuelType, owner);
                collection.put(id, vehicle);
                //System.out.println("2.2 loaded record with vehicle [" + vehicle + "]");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return collection;
    }

    private void addVehicle(Vehicle vehicle) {
        try {
            PreparedStatement ps1 = conn.prepareStatement(INSERT_NEW_VEHICLE, 1);
            ps1.setString(1, vehicle.getName());
            ps1.setInt(2, (int) vehicle.getCoordinates().getX());
            ps1.setInt(3, vehicle.getCoordinates().getY());
            ps1.setDate(4, new java.sql.Date(vehicle.getCreationDate().getTime()));
            ps1.setFloat(5, vehicle.getEnginePower());
            ps1.setString(6, vehicle.getType() == null ? null : vehicle.getType().toString());
            ps1.setString(7, vehicle.getFuelType() == null ? null : vehicle.getFuelType().toString());
            ps1.setString(8, DBUserManager.getInstance().getCurrentUser().getUsername());
            ps1.execute();
            // System.out.println("vehicle with id [" + vehicle.getId() + "] was added.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void clear() {
        try {
            conn.createStatement().execute(CLEAR_ALL_VEHICLES);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getNextId() {
        try {
            ResultSet rs = conn.createStatement().executeQuery(GET_NEXT_ID);
            while (rs.next()) {
                return rs.getInt(1);
            }
            throw new RuntimeException("sequence has not returned any value - abnormal exit");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Hashtable<Integer, Vehicle> collection1) {
        this.clear();
        this.alterID(1);

        List<Integer> keys = new ArrayList<>(collection1.keySet());
        Collections.sort(keys);

        for (Integer key : keys) {
            Vehicle v = collection1.get(key);
            //System.out.println("вызываю сохранение для объекта с ID [" + v.getId());
            this.addVehicle(v);
        }
    }

    public void alterID(int new_start) {
        try {
            String statement = ALTER_ID + new_start;
            conn.createStatement().execute(statement);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
