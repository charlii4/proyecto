/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author charliVB
 */
@Entity
@Table(name = "bienes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bienes.findAll", query = "SELECT b FROM Bienes b")
    , @NamedQuery(name = "Bienes.findByCodItem", query = "SELECT b FROM Bienes b WHERE b.codItem = :codItem")
    , @NamedQuery(name = "Bienes.findByNombre", query = "SELECT b FROM Bienes b WHERE b.nombre = :nombre")
    , @NamedQuery(name = "Bienes.findByCaracter", query = "SELECT b FROM Bienes b WHERE b.caracter = :caracter")
    , @NamedQuery(name = "Bienes.findByValorUnitario", query = "SELECT b FROM Bienes b WHERE b.valorUnitario = :valorUnitario")})
public class Bienes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cod_item")
    private Integer codItem;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "caracter")
    private String caracter;
    @Column(name = "valor_unitario")
    private Integer valorUnitario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bienes")
    private List<Facturas> facturasList;
    @JoinColumn(name = "proveedor", referencedColumnName = "nit")
    @ManyToOne(optional = false)
    private Proveedores proveedor;
    @OneToMany(mappedBy = "item")
    private List<Entradas> entradasList;

    public Bienes() {
    }

    public Bienes(Integer codItem) {
        this.codItem = codItem;
    }

    public Integer getCodItem() {
        return codItem;
    }

    public void setCodItem(Integer codItem) {
        this.codItem = codItem;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCaracter() {
        return caracter;
    }

    public void setCaracter(String caracter) {
        this.caracter = caracter;
    }

    public Integer getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(Integer valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    @XmlTransient
    public List<Facturas> getFacturasList() {
        return facturasList;
    }

    public void setFacturasList(List<Facturas> facturasList) {
        this.facturasList = facturasList;
    }

    public Proveedores getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedores proveedor) {
        this.proveedor = proveedor;
    }

    @XmlTransient
    public List<Entradas> getEntradasList() {
        return entradasList;
    }

    public void setEntradasList(List<Entradas> entradasList) {
        this.entradasList = entradasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codItem != null ? codItem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bienes)) {
            return false;
        }
        Bienes other = (Bienes) object;
        if ((this.codItem == null && other.codItem != null) || (this.codItem != null && !this.codItem.equals(other.codItem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Bienes[ codItem=" + codItem + " ]";
    }
    
}
