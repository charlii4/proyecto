/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "ordenes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ordenes.findAll", query = "SELECT o FROM Ordenes o")
    , @NamedQuery(name = "Ordenes.findByNumOrden", query = "SELECT o FROM Ordenes o WHERE o.ordenesPK.numOrden = :numOrden")
    , @NamedQuery(name = "Ordenes.findBySolicitud", query = "SELECT o FROM Ordenes o WHERE o.ordenesPK.solicitud = :solicitud")
    , @NamedQuery(name = "Ordenes.findByFechaEntrega", query = "SELECT o FROM Ordenes o WHERE o.fechaEntrega = :fechaEntrega")
    , @NamedQuery(name = "Ordenes.findByMonto", query = "SELECT o FROM Ordenes o WHERE o.monto = :monto")})
public class Ordenes implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OrdenesPK ordenesPK;
    @Column(name = "fecha_entrega")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrega;
    @Column(name = "monto")
    private Integer monto;
    @JoinColumn(name = "nit", referencedColumnName = "nit")
    @ManyToOne
    private Proveedores nit;
    @JoinColumn(name = "solicitud", referencedColumnName = "cod_solicitud", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Solicitudes solicitudes;

    public Ordenes() {
    }

    public Ordenes(OrdenesPK ordenesPK) {
        this.ordenesPK = ordenesPK;
    }

    public Ordenes(int numOrden, int solicitud) {
        this.ordenesPK = new OrdenesPK(numOrden, solicitud);
    }

    public OrdenesPK getOrdenesPK() {
        return ordenesPK;
    }

    public void setOrdenesPK(OrdenesPK ordenesPK) {
        this.ordenesPK = ordenesPK;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Integer getMonto() {
        return monto;
    }

    public void setMonto(Integer monto) {
        this.monto = monto;
    }

    public Proveedores getNit() {
        return nit;
    }

    public void setNit(Proveedores nit) {
        this.nit = nit;
    }

    public Solicitudes getSolicitudes() {
        return solicitudes;
    }

    public void setSolicitudes(Solicitudes solicitudes) {
        this.solicitudes = solicitudes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ordenesPK != null ? ordenesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ordenes)) {
            return false;
        }
        Ordenes other = (Ordenes) object;
        if ((this.ordenesPK == null && other.ordenesPK != null) || (this.ordenesPK != null && !this.ordenesPK.equals(other.ordenesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Ordenes[ ordenesPK=" + ordenesPK + " ]";
    }
    
}
