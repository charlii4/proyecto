/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author charliVB
 */
@Embeddable
public class OrdenesPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "num_orden")
    private int numOrden;
    @Basic(optional = false)
    @Column(name = "solicitud")
    private int solicitud;

    public OrdenesPK() {
    }

    public OrdenesPK(int numOrden, int solicitud) {
        this.numOrden = numOrden;
        this.solicitud = solicitud;
    }

    public int getNumOrden() {
        return numOrden;
    }

    public void setNumOrden(int numOrden) {
        this.numOrden = numOrden;
    }

    public int getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(int solicitud) {
        this.solicitud = solicitud;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) numOrden;
        hash += (int) solicitud;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrdenesPK)) {
            return false;
        }
        OrdenesPK other = (OrdenesPK) object;
        if (this.numOrden != other.numOrden) {
            return false;
        }
        if (this.solicitud != other.solicitud) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.OrdenesPK[ numOrden=" + numOrden + ", solicitud=" + solicitud + " ]";
    }
    
}
