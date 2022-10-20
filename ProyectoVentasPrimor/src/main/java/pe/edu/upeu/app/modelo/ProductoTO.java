/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.upeu.app.modelo;

import lombok.Data;

/**
 *
 * @author Usuario
 */
@Data
public class ProductoTO {
    String nombre, id_marca;
    int id_producto, id_categoria, pu, utilidad, stock;
    
}
