/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.NonexistentEntityException;
import Controlador.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Almacenes;
import Entidades.Centros;
import Entidades.Empleados;
import Entidades.Salidas;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author charliVB
 */
public class SalidasJpaController implements Serializable {

    public SalidasJpaController() {
        emf = Persistence.createEntityManagerFactory("ProyectoPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Salidas salidas) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Almacenes almacen = salidas.getAlmacen();
            if (almacen != null) {
                almacen = em.getReference(almacen.getClass(), almacen.getCodAlmacen());
                salidas.setAlmacen(almacen);
            }
            Centros centro = salidas.getCentro();
            if (centro != null) {
                centro = em.getReference(centro.getClass(), centro.getCodCentro());
                salidas.setCentro(centro);
            }
            Empleados empleado = salidas.getEmpleado();
            if (empleado != null) {
                empleado = em.getReference(empleado.getClass(), empleado.getCodEmpleado());
                salidas.setEmpleado(empleado);
            }
            em.persist(salidas);
            if (almacen != null) {
                almacen.getSalidasList().add(salidas);
                almacen = em.merge(almacen);
            }
            if (centro != null) {
                centro.getSalidasList().add(salidas);
                centro = em.merge(centro);
            }
            if (empleado != null) {
                empleado.getSalidasList().add(salidas);
                empleado = em.merge(empleado);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSalidas(salidas.getNumSalida()) != null) {
                throw new PreexistingEntityException("Salidas " + salidas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Salidas salidas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Salidas persistentSalidas = em.find(Salidas.class, salidas.getNumSalida());
            Almacenes almacenOld = persistentSalidas.getAlmacen();
            Almacenes almacenNew = salidas.getAlmacen();
            Centros centroOld = persistentSalidas.getCentro();
            Centros centroNew = salidas.getCentro();
            Empleados empleadoOld = persistentSalidas.getEmpleado();
            Empleados empleadoNew = salidas.getEmpleado();
            if (almacenNew != null) {
                almacenNew = em.getReference(almacenNew.getClass(), almacenNew.getCodAlmacen());
                salidas.setAlmacen(almacenNew);
            }
            if (centroNew != null) {
                centroNew = em.getReference(centroNew.getClass(), centroNew.getCodCentro());
                salidas.setCentro(centroNew);
            }
            if (empleadoNew != null) {
                empleadoNew = em.getReference(empleadoNew.getClass(), empleadoNew.getCodEmpleado());
                salidas.setEmpleado(empleadoNew);
            }
            salidas = em.merge(salidas);
            if (almacenOld != null && !almacenOld.equals(almacenNew)) {
                almacenOld.getSalidasList().remove(salidas);
                almacenOld = em.merge(almacenOld);
            }
            if (almacenNew != null && !almacenNew.equals(almacenOld)) {
                almacenNew.getSalidasList().add(salidas);
                almacenNew = em.merge(almacenNew);
            }
            if (centroOld != null && !centroOld.equals(centroNew)) {
                centroOld.getSalidasList().remove(salidas);
                centroOld = em.merge(centroOld);
            }
            if (centroNew != null && !centroNew.equals(centroOld)) {
                centroNew.getSalidasList().add(salidas);
                centroNew = em.merge(centroNew);
            }
            if (empleadoOld != null && !empleadoOld.equals(empleadoNew)) {
                empleadoOld.getSalidasList().remove(salidas);
                empleadoOld = em.merge(empleadoOld);
            }
            if (empleadoNew != null && !empleadoNew.equals(empleadoOld)) {
                empleadoNew.getSalidasList().add(salidas);
                empleadoNew = em.merge(empleadoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = salidas.getNumSalida();
                if (findSalidas(id) == null) {
                    throw new NonexistentEntityException("The salidas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Salidas salidas;
            try {
                salidas = em.getReference(Salidas.class, id);
                salidas.getNumSalida();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The salidas with id " + id + " no longer exists.", enfe);
            }
            Almacenes almacen = salidas.getAlmacen();
            if (almacen != null) {
                almacen.getSalidasList().remove(salidas);
                almacen = em.merge(almacen);
            }
            Centros centro = salidas.getCentro();
            if (centro != null) {
                centro.getSalidasList().remove(salidas);
                centro = em.merge(centro);
            }
            Empleados empleado = salidas.getEmpleado();
            if (empleado != null) {
                empleado.getSalidasList().remove(salidas);
                empleado = em.merge(empleado);
            }
            em.remove(salidas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Salidas> findSalidasEntities() {
        return findSalidasEntities(true, -1, -1);
    }

    public List<Salidas> findSalidasEntities(int maxResults, int firstResult) {
        return findSalidasEntities(false, maxResults, firstResult);
    }

    private List<Salidas> findSalidasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Salidas.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Salidas findSalidas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Salidas.class, id);
        } finally {
            em.close();
        }
    }

    public int getSalidasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Salidas> rt = cq.from(Salidas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
