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
@Table(name = "directores")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Directores.findAll", query = "SELECT d FROM Directores d")
    , @NamedQuery(name = "Directores.findByCodDirector", query = "SELECT d FROM Directores d WHERE d.codDirector = :codDirector")
    , @NamedQuery(name = "Directores.findByNombre", query = "SELECT d FROM Directores d WHERE d.nombre = :nombre")
    , @NamedQuery(name = "Directores.findBySalario", query = "SELECT d FROM Directores d WHERE d.salario = :salario")})
public class Directores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cod_director")
    private Integer codDirector;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "salario")
    private Integer salario;
    @OneToMany(mappedBy = "director")
    private List<JefesArea> jefesAreaList;
    @JoinColumn(name = "centro", referencedColumnName = "cod_centro")
    @ManyToOne
    private Centros centro;

    public Directores() {
    }

    public Directores(Integer codDirector) {
        this.codDirector = codDirector;
    }

    public Integer getCodDirector() {
        return codDirector;
    }

    public void setCodDirector(Integer codDirector) {
        this.codDirector = codDirector;
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

    @XmlTransient
    public List<JefesArea> getJefesAreaList() {
        return jefesAreaList;
    }

    public void setJefesAreaList(List<JefesArea> jefesAreaList) {
        this.jefesAreaList = jefesAreaList;
    }

    public Centros getCentro() {
        return centro;
    }
    
    public int getCodCentro() {
        return centro.getCodCentro();
    }

    public void setCentro(Centros centro) {
        this.centro = centro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codDirector != null ? codDirector.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Directores)) {
            return false;
        }
        Directores other = (Directores) object;
        if ((this.codDirector == null && other.codDirector != null) || (this.codDirector != null && !this.codDirector.equals(other.codDirector))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Directores[ codDirector=" + codDirector + " ]";
    }
    
}
