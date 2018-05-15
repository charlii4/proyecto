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
@Table(name = "centros")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Centros.findAll", query = "SELECT c FROM Centros c")
    , @NamedQuery(name = "Centros.findByCodCentro", query = "SELECT c FROM Centros c WHERE c.codCentro = :codCentro")
    , @NamedQuery(name = "Centros.findByNombre", query = "SELECT c FROM Centros c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "Centros.findByUbicacion", query = "SELECT c FROM Centros c WHERE c.ubicacion = :ubicacion")})
public class Centros implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cod_centro")
    private Integer codCentro;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "ubicacion")
    private String ubicacion;
    @OneToMany(mappedBy = "centro")
    private List<JefesArea> jefesAreaList;
    @OneToMany(mappedBy = "centro")
    private List<Empleados> empleadosList;
    @OneToMany(mappedBy = "centro")
    private List<Directores> directoresList;
    @OneToMany(mappedBy = "centro")
    private List<Salidas> salidasList;

    public Centros() {
    }

    public Centros(Integer codCentro) {
        this.codCentro = codCentro;
    }

    public Integer getCodCentro() {
        return codCentro;
    }

    public void setCodCentro(Integer codCentro) {
        this.codCentro = codCentro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    @XmlTransient
    public List<JefesArea> getJefesAreaList() {
        return jefesAreaList;
    }

    public void setJefesAreaList(List<JefesArea> jefesAreaList) {
        this.jefesAreaList = jefesAreaList;
    }

    @XmlTransient
    public List<Empleados> getEmpleadosList() {
        return empleadosList;
    }

    public void setEmpleadosList(List<Empleados> empleadosList) {
        this.empleadosList = empleadosList;
    }

    @XmlTransient
    public List<Directores> getDirectoresList() {
        return directoresList;
    }

    public void setDirectoresList(List<Directores> directoresList) {
        this.directoresList = directoresList;
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
        hash += (codCentro != null ? codCentro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Centros)) {
            return false;
        }
        Centros other = (Centros) object;
        if ((this.codCentro == null && other.codCentro != null) || (this.codCentro != null && !this.codCentro.equals(other.codCentro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Centros[ codCentro=" + codCentro + " ]";
    }
    
}
