package pe.edu.upeu.app.dao;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import pe.com.syscenterlife.autocomp.ModeloDataAutocomplet;
import pe.edu.upeu.app.dao.conx.ConnS;
import pe.edu.upeu.app.modelo.CategoriaTO;
import pe.edu.upeu.app.modelo.MarcaTO;
import pe.edu.upeu.app.modelo.ProductoTO;
import pe.edu.upeu.app.util.ErrorLogger;

/**
 *
 * @author Lab Software
 */
public class ProductoDao implements ProductoDaoI {

    Statement stmt = null;
    Vector columnNames;
    Vector visitdata;
    ConnS instance = ConnS.getInstance();
    Connection connection = instance.getConnection();
    static PreparedStatement ps;
    static ErrorLogger log = new ErrorLogger(ProductoDao.class.getName());
    ResultSet rs = null;

    public ProductoDao() {
        columnNames = new Vector();
        visitdata = new Vector();
        connection = ConnS.getInstance().getConnection();
    }

    @Override
    public int create(ProductoTO d) {
        int rsId = 0;
        String[] returns = {"id_producto"};

        String sql = "INSERT INTO producto( nombre, pu, utilidad, stock, id_categoria, id_marca) "
                + " VALUES(?,?,?,?,?,?)";
        int i = 0;
        try {
            ps = connection.prepareStatement(sql, returns);
            ps.setString(++i, d.getNombre());
            ps.setDouble(++i, d.getPu());
            ps.setDouble(++i, d.getUtilidad());
            ps.setDouble(++i, d.getStock());
            ps.setInt(++i, d.getIdCategoria());
            ps.setInt(++i, d.getIdMarca());
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
        System.out.println("actualizar d.getNombre: " + d.getNombre());
        int comit = 0;
        String sql = "UPDATE producto SET "
                + "pu=?, "
                + "utilidad=? "
                + "stock=? "
                + "id_categoria=? "
                + "id_marca=? "
                + "WHERE nombre=?";
        int i = 0;
        try {
            ps = connection.prepareStatement(sql);
            ps.setDouble(++i, d.getPu());
            ps.setDouble(++i, d.getUtilidad());
            ps.setDouble(++i, d.getStock());
            ps.setInt(++i, d.getIdCategoria());
            ps.setInt(++i, d.getIdMarca());
            ps.setString(++i, d.getNombre());
            comit = ps.executeUpdate();
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "update", ex);
        }
        return comit;
    }

    @Override
    public int delete(String id) throws Exception {
        int comit = 0;
        String sql = "DELETE FROM producto WHERE nombre = ?";
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, id);
            comit = ps.executeUpdate();
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "delete", ex);
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
    public List<ProductoTO> listarProductos() {
        List<ProductoTO> listarproductos = new ArrayList();
        String sql = "SELECT * FROM producto";
        try {
            //connection = new Conn().connectSQLite();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                ProductoTO pro = new ProductoTO();
                pro.setNombre(rs.getString("nombre"));
                pro.setPu(rs.getDouble("pu"));
                pro.setUtilidad(rs.getDouble("utilidad"));
                pro.setStock(rs.getDouble("stock"));
                pro.setIdCategoria(rs.getInt("id_categoria"));
                pro.setIdMarca(rs.getInt("id_marca"));
                listarproductos.add(pro);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return listarproductos;
    }

    @Override
    public void reportarProducto() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<ModeloDataAutocomplet> listAutoComplet(String filter) {
        List<ModeloDataAutocomplet> listarProducto = new ArrayList();
        String sql = "select * from producto where nombre like ?";
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, filter + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                ModeloDataAutocomplet data = new ModeloDataAutocomplet();
                ModeloDataAutocomplet.TIPE_DISPLAY = "ID";
                data.setIdx(rs.getInt("idProducto") + ":" + rs.getInt("idMarca") + ":" + rs.getInt("idCategoria"));
                data.setNombreDysplay(rs.getString("nombre"));
                data.setOtherData(rs.getDouble("pu") + ":" + rs.getDouble("utilidad") + ":" + rs.getDouble("stock"));
                listarProducto.add(data);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return listarProducto;
    }

    @Override
    public ProductoTO buscarProductos(String nombre) {
        ProductoTO producto = new ProductoTO();
        String sql = "SELECT * FROM producto WHERE nombre = ?";
        try {
            //connection = new Conn().connectSQLite();
            ps = connection.prepareStatement(sql);
            ps.setString(1, nombre);
            rs = ps.executeQuery();
            if (rs.next()) {
                producto.setNombre(rs.getString("nombre"));
                producto.setPu(rs.getDouble("pu"));
                producto.setUtilidad(rs.getDouble("utilidad"));
                producto.setStock(rs.getDouble("stock"));
                producto.setIdCategoria(rs.getInt("id_categoria"));
                producto.setIdMarca(rs.getInt("id_marca"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return producto;
    }

    @Override
    public List<MarcaTO> listCmbMarca(String filter) {
        List<MarcaTO> listarproductos = new ArrayList();
        String sql = "SELECT * FROM marca";
        try {
            //connection = new Conn().connectSQLite();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                MarcaTO cli = new MarcaTO();
                cli.setIdMarca(rs.getInt("id_marca"));
                cli.setNombre(rs.getString("nombre"));
                listarproductos.add(cli);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return listarproductos;
    }

    @Override
    public List<CategoriaTO> listCmbCategoria(String filter) {
        List<CategoriaTO> listarproductos = new ArrayList();
        String sql = "SELECT * FROM categoria";
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                CategoriaTO cli = new CategoriaTO();
                cli.setIdCategoria(rs.getInt("id_categoria"));
                cli.setNombre(rs.getString("nombre"));
                listarproductos.add(cli);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return listarproductos;
    }

}
