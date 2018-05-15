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
@Table(name = "empleados")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empleados.findAll", query = "SELECT e FROM Empleados e")
    , @NamedQuery(name = "Empleados.findByCodEmpleado", query = "SELECT e FROM Empleados e WHERE e.codEmpleado = :codEmpleado")
    , @NamedQuery(name = "Empleados.findByNombre", query = "SELECT e FROM Empleados e WHERE e.nombre = :nombre")
    , @NamedQuery(name = "Empleados.findBySalario", query = "SELECT e FROM Empleados e WHERE e.salario = :salario")})
public class Empleados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cod_empleado")
    private Integer codEmpleado;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "salario")
    private Integer salario;
    @JoinColumn(name = "centro", referencedColumnName = "cod_centro")
    @ManyToOne
    private Centros centro;
    @JoinColumn(name = "jefe_area", referencedColumnName = "cod_jefe")
    @ManyToOne
    private JefesArea jefeArea;
    @JoinColumn(name = "responsable", referencedColumnName = "cod_responsable")
    @ManyToOne
    private Responsables responsable;
    @OneToMany(mappedBy = "empleado")
    private List<Salidas> salidasList;

    public Empleados() {
    }

    public Empleados(Integer codEmpleado) {
        this.codEmpleado = codEmpleado;
    }

    public Integer getCodEmpleado() {
        return codEmpleado;
    }

    public void setCodEmpleado(Integer codEmpleado) {
        this.codEmpleado = codEmpleado;
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

    public Centros getCentro() {
        return centro;
    }

    public void setCentro(Centros centro) {
        this.centro = centro;
    }

    public JefesArea getJefeArea() {
        return jefeArea;
    }

    public void setJefeArea(JefesArea jefeArea) {
        this.jefeArea = jefeArea;
    }

    public Responsables getResponsable() {
        return responsable;
    }

    public void setResponsable(Responsables responsable) {
        this.responsable = responsable;
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
        hash += (codEmpleado != null ? codEmpleado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empleados)) {
            return false;
        }
        Empleados other = (Empleados) object;
        if ((this.codEmpleado == null && other.codEmpleado != null) || (this.codEmpleado != null && !this.codEmpleado.equals(other.codEmpleado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Empleados[ codEmpleado=" + codEmpleado + " ]";
    }
    
}
