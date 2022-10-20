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
        String[] returns = {"id_producto"};
        String sql = "INSERT INTO producto(id_producto, nombre, pu, utilidad, stock, id_categoria, id_marca) "
                + "VALUES(?,?,?,?,?,?,?)";
        int i = 0;
        try {
            ps = connection.prepareStatement(sql, returns);
            ps.setInt(++i, d.getId_producto());
            ps.setString(++i, d.getNombre());
            ps.setInt(++i, d.getPu());
            ps.setInt(++i, d.getUtilidad());
            ps.setInt(++i, d.getStock());
            ps.setInt(++i, d.getId_categoria());
            ps.setString(++i, d.getId_marca());
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
        System.out.println("actualizar d.getId_producto: " + d.getId_producto());
        int comit = 0;
        String sql = "UPDATE producto SET "
                + "nombrer=?, "
                + "pu=?, "
                + "utilidad=? "
                + "stock=? "
                + "id_categoria=? "
                + "id_marca=? "
                + "WHERE id_producto=?";
        int i = 0;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(++i, d.getNombre());
            ps.setInt(++i, d.getPu());
            ps.setInt(++i, d.getUtilidad());
            ps.setInt(++i, d.getStock());
            ps.setInt(++i, d.getId_categoria());
            ps.setString(++i, d.getId_marca());
            ps.setInt(++i, d.getId_producto());
            comit = ps.executeUpdate();
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "update", ex);
        }
        return comit;
    }

    @Override
    public int delete(String id) throws Exception {
        int comit = 0;
        String sql = "DELETE FROM producto WHERE id_producto = ?";
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
        String sql = "SELECT * FROM producto";
        try {
            connection = new Conn().connectSQLite();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                ProductoTO cli = new ProductoTO();
                cli.setId_producto(rs.getInt("id_producto"));
                cli.setNombre(rs.getString("nombrer"));
                cli.setPu(rs.getInt("pu"));
                cli.setUtilidad(rs.getInt("utilidad"));
                cli.setStock(rs.getInt("stock"));
                cli.setId_categoria(rs.getInt("id_categoria"));
                cli.setId_marca(rs.getString("id_marca"));
                listarproductos.add(cli);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return listarproductos;
    }

    @Override
    public ProductoTO buscarProductos(String id_producto) {
        ProductoTO producto = new ProductoTO();
        String sql = "SELECT * FROM producto WHERE id_producto = ?";
        try {
            connection = new Conn().connectSQLite();
            ps = connection.prepareStatement(sql);
            ps.setString(1, id_producto);
            rs = ps.executeQuery();
            if (rs.next()) {
                producto.setId_producto(rs.getInt("id_producto"));
                producto.setNombre(rs.getString("nombrer"));
                producto.setPu(rs.getInt("pu"));
                producto.setUtilidad(rs.getInt("utilidad"));
                producto.setStock(rs.getInt("stock"));
                producto.setId_categoria(rs.getInt("id_categoria"));
                producto.setId_marca(rs.getString("id_marca"));
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
