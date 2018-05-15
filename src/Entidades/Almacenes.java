/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author charliVB
 */
@Entity
@Table(name = "almacenes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Almacenes.findAll", query = "SELECT a FROM Almacenes a")
    , @NamedQuery(name = "Almacenes.findByCodAlmacen", query = "SELECT a FROM Almacenes a WHERE a.codAlmacen = :codAlmacen")
    , @NamedQuery(name = "Almacenes.findByCapacidad", query = "SELECT a FROM Almacenes a WHERE a.capacidad = :capacidad")
    , @NamedQuery(name = "Almacenes.findByUbicacion", query = "SELECT a FROM Almacenes a WHERE a.ubicacion = :ubicacion")})
public class Almacenes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cod_almacen")
    private Integer codAlmacen;
    @Column(name = "capacidad")
    private Integer capacidad;
    @Column(name = "ubicacion")
    private String ubicacion;
    @OneToMany(mappedBy = "almacen")
    private List<Entradas> entradasList;
    @OneToMany(mappedBy = "almacen")
    private List<Salidas> salidasList;

    public Almacenes() {
    }

    public Almacenes(Integer codAlmacen) {
        this.codAlmacen = codAlmacen;
    }

    public Integer getCodAlmacen() {
        return codAlmacen;
    }

    public void setCodAlmacen(Integer codAlmacen) {
        this.codAlmacen = codAlmacen;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    @XmlTransient
    public List<Entradas> getEntradasList() {
        return entradasList;
    }

    public void setEntradasList(List<Entradas> entradasList) {
        this.entradasList = entradasList;
    }

    @XmlTransient
    public List<Salidas> getSalidasList() {
        return salidasList;
    }

    public void setSalidasList(List<Salidas> salidasList) {
        this.salidasList = salidasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codAlmacen != null ? codAlmacen.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Almacenes)) {
            return false;
        }
        Almacenes other = (Almacenes) object;
        if ((this.codAlmacen == null && other.codAlmacen != null) || (this.codAlmacen != null && !this.codAlmacen.equals(other.codAlmacen))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Almacenes[ codAlmacen=" + codAlmacen + " ]";
    }
    
}
