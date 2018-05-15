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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "jefes_area")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JefesArea.findAll", query = "SELECT j FROM JefesArea j")
    , @NamedQuery(name = "JefesArea.findByCodJefe", query = "SELECT j FROM JefesArea j WHERE j.codJefe = :codJefe")
    , @NamedQuery(name = "JefesArea.findByNombre", query = "SELECT j FROM JefesArea j WHERE j.nombre = :nombre")
    , @NamedQuery(name = "JefesArea.findBySalario", query = "SELECT j FROM JefesArea j WHERE j.salario = :salario")
    , @NamedQuery(name = "JefesArea.findByArea", query = "SELECT j FROM JefesArea j WHERE j.area = :area")})
public class JefesArea implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cod_jefe")
    private Integer codJefe;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "salario")
    private Integer salario;
    @Column(name = "area")
    private String area;
    @OneToMany(mappedBy = "jefe")
    private List<Responsables> responsablesList;
    @JoinColumn(name = "centro", referencedColumnName = "cod_centro")
    @ManyToOne
    private Centros centro;
    @JoinColumn(name = "director", referencedColumnName = "cod_director")
    @ManyToOne
    private Directores director;
    @OneToMany(mappedBy = "jefeArea")
    private List<Empleados> empleadosList;

    public JefesArea() {
    }

    public JefesArea(Integer codJefe) {
        this.codJefe = codJefe;
    }

    public Integer getCodJefe() {
        return codJefe;
    }

    public void setCodJefe(Integer codJefe) {
        this.codJefe = codJefe;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getSalario() {
        return salario;
    }

    public void setSalario(Integer salario) {
        this.salario = salario;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @XmlTransient
    public List<Responsables> getResponsablesList() {
        return responsablesList;
    }

    public void setResponsablesList(List<Responsables> responsablesList) {
        this.responsablesList = responsablesList;
    }

    public Centros getCentro() {
        return centro;
    }

    public void setCentro(Centros centro) {
        this.centro = centro;
    }

    public Directores getDirector() {
        return director;
    }

    public void setDirector(Directores director) {
        this.director = director;
    }

    @XmlTransient
    public List<Empleados> getEmpleadosList() {
        return empleadosList;
    }

    public void setEmpleadosList(List<Empleados> empleadosList) {
        this.empleadosList = empleadosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codJefe != null ? codJefe.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JefesArea)) {
            return false;
        }
        JefesArea other = (JefesArea) object;
        if ((this.codJefe == null && other.codJefe != null) || (this.codJefe != null && !this.codJefe.equals(other.codJefe))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.JefesArea[ codJefe=" + codJefe + " ]";
    }
    
}
