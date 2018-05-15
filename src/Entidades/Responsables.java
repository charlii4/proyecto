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
@Table(name = "responsables")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Responsables.findAll", query = "SELECT r FROM Responsables r")
    , @NamedQuery(name = "Responsables.findByCodResponsable", query = "SELECT r FROM Responsables r WHERE r.codResponsable = :codResponsable")
    , @NamedQuery(name = "Responsables.findByNombre", query = "SELECT r FROM Responsables r WHERE r.nombre = :nombre")
    , @NamedQuery(name = "Responsables.findBySalario", query = "SELECT r FROM Responsables r WHERE r.salario = :salario")
    , @NamedQuery(name = "Responsables.findByCentro", query = "SELECT r FROM Responsables r WHERE r.centro = :centro")})
public class Responsables implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cod_responsable")
    private Integer codResponsable;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "salario")
    private Integer salario;
    @Column(name = "centro")
    private Integer centro;
    @JoinColumn(name = "jefe", referencedColumnName = "cod_jefe")
    @ManyToOne
    private JefesArea jefe;
    @OneToMany(mappedBy = "responsable")
    private List<Empleados> empleadosList;
    @OneToMany(mappedBy = "responsable")
    private List<Solicitudes> solicitudesList;

    public Responsables() {
    }

    public Responsables(Integer codResponsable) {
        this.codResponsable = codResponsable;
    }

    public Integer getCodResponsable() {
        return codResponsable;
    }

    public void setCodResponsable(Integer codResponsable) {
        this.codResponsable = codResponsable;
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

    public Integer getCentro() {
        return centro;
    }

    public void setCentro(Integer centro) {
        this.centro = centro;
    }

    public JefesArea getJefe() {
        return jefe;
    }

    public void setJefe(JefesArea jefe) {
        this.jefe = jefe;
    }

    @XmlTransient
    public List<Empleados> getEmpleadosList() {
        return empleadosList;
    }

    public void setEmpleadosList(List<Empleados> empleadosList) {
        this.empleadosList = empleadosList;
    }

    @XmlTransient
    public List<Solicitudes> getSolicitudesList() {
        return solicitudesList;
    }

    public void setSolicitudesList(List<Solicitudes> solicitudesList) {
        this.solicitudesList = solicitudesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codResponsable != null ? codResponsable.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Responsables)) {
            return false;
        }
        Responsables other = (Responsables) object;
        if ((this.codResponsable == null && other.codResponsable != null) || (this.codResponsable != null && !this.codResponsable.equals(other.codResponsable))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Responsables[ codResponsable=" + codResponsable + " ]";
    }
    
}
