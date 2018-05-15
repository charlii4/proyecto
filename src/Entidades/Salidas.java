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
@Table(name = "salidas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Salidas.findAll", query = "SELECT s FROM Salidas s")
    , @NamedQuery(name = "Salidas.findByNumSalida", query = "SELECT s FROM Salidas s WHERE s.numSalida = :numSalida")
    , @NamedQuery(name = "Salidas.findByFechaSalida", query = "SELECT s FROM Salidas s WHERE s.fechaSalida = :fechaSalida")
    , @NamedQuery(name = "Salidas.findByFechaEntrega", query = "SELECT s FROM Salidas s WHERE s.fechaEntrega = :fechaEntrega")})
public class Salidas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "num_salida")
    private Integer numSalida;
    @Column(name = "fecha_salida")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSalida;
    @Column(name = "fecha_entrega")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrega;
    @JoinColumn(name = "almacen", referencedColumnName = "cod_almacen")
    @ManyToOne
    private Almacenes almacen;
    @JoinColumn(name = "centro", referencedColumnName = "cod_centro")
    @ManyToOne
    private Centros centro;
    @JoinColumn(name = "empleado", referencedColumnName = "cod_empleado")
    @ManyToOne
    private Empleados empleado;

    public Salidas() {
    }

    public Salidas(Integer numSalida) {
        this.numSalida = numSalida;
    }

    public Integer getNumSalida() {
        return numSalida;
    }

    public void setNumSalida(Integer numSalida) {
        this.numSalida = numSalida;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Almacenes getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacenes almacen) {
        this.almacen = almacen;
    }

    public Centros getCentro() {
        return centro;
    }

    public void setCentro(Centros centro) {
        this.centro = centro;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numSalida != null ? numSalida.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Salidas)) {
            return false;
        }
        Salidas other = (Salidas) object;
        if ((this.numSalida == null && other.numSalida != null) || (this.numSalida != null && !this.numSalida.equals(other.numSalida))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Salidas[ numSalida=" + numSalida + " ]";
    }
    
}
