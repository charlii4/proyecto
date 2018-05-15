/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author charliVB
 */
@Entity
@Table(name = "entradas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Entradas.findAll", query = "SELECT e FROM Entradas e")
    , @NamedQuery(name = "Entradas.findByNumEntrada", query = "SELECT e FROM Entradas e WHERE e.numEntrada = :numEntrada")
    , @NamedQuery(name = "Entradas.findByFecha", query = "SELECT e FROM Entradas e WHERE e.fecha = :fecha")
    , @NamedQuery(name = "Entradas.findByNumFactura", query = "SELECT e FROM Entradas e WHERE e.numFactura = :numFactura")
    , @NamedQuery(name = "Entradas.findByCantidad", query = "SELECT e FROM Entradas e WHERE e.cantidad = :cantidad")
    , @NamedQuery(name = "Entradas.findByTotal", query = "SELECT e FROM Entradas e WHERE e.total = :total")})
public class Entradas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "num_entrada")
    private Integer numEntrada;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "num_factura")
    private Integer numFactura;
    @Column(name = "cantidad")
    private Integer cantidad;
    @Column(name = "total")
    private Integer total;
    @JoinColumn(name = "almacen", referencedColumnName = "cod_almacen")
    @ManyToOne
    private Almacenes almacen;
    @JoinColumn(name = "item", referencedColumnName = "cod_item")
    @ManyToOne
    private Bienes item;
    @JoinColumn(name = "proveedor", referencedColumnName = "nit")
    @ManyToOne(optional = false)
    private Proveedores proveedor;

    public Entradas() {
    }

    public Entradas(Integer numEntrada) {
        this.numEntrada = numEntrada;
    }

    public Integer getNumEntrada() {
        return numEntrada;
    }

    public void setNumEntrada(Integer numEntrada) {
        this.numEntrada = numEntrada;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getNumFactura() {
        return numFactura;
    }

    public void setNumFactura(Integer numFactura) {
        this.numFactura = numFactura;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Almacenes getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacenes almacen) {
        this.almacen = almacen;
    }

    public Bienes getItem() {
        return item;
    }

    public void setItem(Bienes item) {
        this.item = item;
    }

    public Proveedores getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedores proveedor) {
        this.proveedor = proveedor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numEntrada != null ? numEntrada.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Entradas)) {
            return false;
        }
        Entradas other = (Entradas) object;
        if ((this.numEntrada == null && other.numEntrada != null) || (this.numEntrada != null && !this.numEntrada.equals(other.numEntrada))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Entradas[ numEntrada=" + numEntrada + " ]";
    }
    
}
