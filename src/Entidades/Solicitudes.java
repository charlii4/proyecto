/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author charliVB
 */
@Entity
@Table(name = "solicitudes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Solicitudes.findAll", query = "SELECT s FROM Solicitudes s")
    , @NamedQuery(name = "Solicitudes.findByCodSolicitud", query = "SELECT s FROM Solicitudes s WHERE s.codSolicitud = :codSolicitud")
    , @NamedQuery(name = "Solicitudes.findByCodPresupuesto", query = "SELECT s FROM Solicitudes s WHERE s.codPresupuesto = :codPresupuesto")
    , @NamedQuery(name = "Solicitudes.findByFecha", query = "SELECT s FROM Solicitudes s WHERE s.fecha = :fecha")
    , @NamedQuery(name = "Solicitudes.findByTotal", query = "SELECT s FROM Solicitudes s WHERE s.total = :total")})
public class Solicitudes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cod_solicitud")
    private Integer codSolicitud;
    @Column(name = "cod_presupuesto")
    private Integer codPresupuesto;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "total")
    private Integer total;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "solicitudes")
    private List<Facturas> facturasList;
    @JoinColumn(name = "responsable", referencedColumnName = "cod_responsable")
    @ManyToOne
    private Responsables responsable;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "solicitudes")
    private List<Ordenes> ordenesList;

    public Solicitudes() {
    }

    public Solicitudes(Integer codSolicitud) {
        this.codSolicitud = codSolicitud;
    }

    public Integer getCodSolicitud() {
        return codSolicitud;
    }

    public void setCodSolicitud(Integer codSolicitud) {
        this.codSolicitud = codSolicitud;
    }

    public Integer getCodPresupuesto() {
        return codPresupuesto;
    }

    public void setCodPresupuesto(Integer codPresupuesto) {
        this.codPresupuesto = codPresupuesto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @XmlTransient
    public List<Facturas> getFacturasList() {
        return facturasList;
    }

    public void setFacturasList(List<Facturas> facturasList) {
        this.facturasList = facturasList;
    }

    public Responsables getResponsable() {
        return responsable;
    }

    public void setResponsable(Responsables responsable) {
        this.responsable = responsable;
    }

    @XmlTransient
    public List<Ordenes> getOrdenesList() {
        return ordenesList;
    }

    public void setOrdenesList(List<Ordenes> ordenesList) {
        this.ordenesList = ordenesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codSolicitud != null ? codSolicitud.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Solicitudes)) {
            return false;
        }
        Solicitudes other = (Solicitudes) object;
        if ((this.codSolicitud == null && other.codSolicitud != null) || (this.codSolicitud != null && !this.codSolicitud.equals(other.codSolicitud))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Solicitudes[ codSolicitud=" + codSolicitud + " ]";
    }
    
}
