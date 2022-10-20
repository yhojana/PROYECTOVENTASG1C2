/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.upeu.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import pe.edu.upeu.app.dao.conx.Conn;
import pe.edu.upeu.app.modelo.ProductoTO;
import pe.edu.upeu.app.util.ErrorLogger;

/**
 *
 * @author Usuario
 */
public class ProductoDao implements ProductoDaoI {

    Statement stmt = null;
    Vector columnNames;
    Vector visitdata;
    Connection connection = Conn.connectSQLite();
    static PreparedStatement ps;
    static ErrorLogger log = new ErrorLogger(ProductoDao.class.getName());
    ResultSet rs = null;

    public ProductoDao() {
        columnNames = new Vector();
        visitdata = new Vector();
    }

    @Override
    public int create(ProductoTO d) {
        int rsId = 0;
        String[] returns = {"URL"};
        String sql = "INSERT INTO producto(URL, nombrers, precio, estado) "
                + "VALUES(?,?,?)";
        int i = 0;
        try {
            ps = connection.prepareStatement(sql, returns);
            ps.setString(++i, d.getURL());
            ps.setString(++i, d.getNombrers());
            ps.setString(++i, d.getPrecio());
            ps.setString(++i, d.getEstado());
            rsId = ps.executeUpdate();// 0 no o 1 si commit
            try ( ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    rsId = rs.getInt(1);
                }
                rs.close();
            }
        } catch (SQLException ex) {
            //System.err.println("create:" + ex.toString());
            log.log(Level.SEVERE, "create", ex);
        }
        return rsId;
    }

    @Override
    public int update(ProductoTO d) {
        System.out.println("actualizar d.getURL: " + d.getURL());
        int comit = 0;
        String sql = "UPDATE producto SET "
                + "nombrers=?, "
                + "precio=?, "
                + "estado=? "
                + "WHERE URL=?";
        int i = 0;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(++i, d.getNombrers());
            ps.setString(++i, d.getPrecio());
            ps.setString(++i, d.getEstado());
            ps.setString(++i, d.getURL());
            comit = ps.executeUpdate();
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "update", ex);
        }
        return comit;
    }

    @Override
    public int delete(String id) throws Exception {
        int comit = 0;
        String sql = "DELETE FROM producto WHERE URL = ?";
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, id);
            comit = ps.executeUpdate();
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "eliminar", ex);
            // System.err.println("NO del " + ex.toString());
            throw new Exception("Detalle:" + ex.getMessage());
        }
        return comit;
    }

    @Override
    public List<ProductoTO> listCmb(String filter) {
        List<ProductoTO> ls = new ArrayList();
        ls.add(new ProductoTO());
        ls.addAll(listarProductos());
        return ls;
    }

    @Override
    public List listarProductos() {
        List<ProductoTO> listarproductos = new ArrayList();
        String sql = "SELECT * FROM cliente";
        try {
            connection = new Conn().connectSQLite();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                ProductoTO cli = new ProductoTO();
                cli.setURL(rs.getString("url"));
                cli.setNombrers(rs.getString("nombrers"));
                cli.setPrecio(rs.getString("precio"));
                cli.setEstado(rs.getString("estado"));
                listarproductos.add(cli);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return listarproductos;
    }

    @Override
    public ProductoTO buscarProductos(String URL) {
        ProductoTO producto = new ProductoTO();
        String sql = "SELECT * FROM producto WHERE url = ?";
        try {
            connection = new Conn().connectSQLite();
            ps = connection.prepareStatement(sql);
            ps.setString(1, URL);
            rs = ps.executeQuery();
            if (rs.next()) {
                producto.setURL(rs.getString("url"));
                producto.setNombrers(rs.getString("nombrers"));
                producto.setPrecio(rs.getString("precio"));
                producto.setEstado(rs.getString("estado"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return producto;
    }

    @Override
    public void reportarProducto() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
