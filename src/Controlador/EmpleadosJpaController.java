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
import Entidades.Centros;
import Entidades.Empleados;
import Entidades.JefesArea;
import Entidades.Responsables;
import Entidades.Salidas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author charliVB
 */
public class EmpleadosJpaController implements Serializable {

    public EmpleadosJpaController() {
        emf = Persistence.createEntityManagerFactory("ProyectoPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empleados empleados) throws PreexistingEntityException, Exception {
        if (empleados.getSalidasList() == null) {
            empleados.setSalidasList(new ArrayList<Salidas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Centros centro = empleados.getCentro();
            if (centro != null) {
                centro = em.getReference(centro.getClass(), centro.getCodCentro());
                empleados.setCentro(centro);
            }
            JefesArea jefeArea = empleados.getJefeArea();
            if (jefeArea != null) {
                jefeArea = em.getReference(jefeArea.getClass(), jefeArea.getCodJefe());
                empleados.setJefeArea(jefeArea);
            }
            Responsables responsable = empleados.getResponsable();
            if (responsable != null) {
                responsable = em.getReference(responsable.getClass(), responsable.getCodResponsable());
                empleados.setResponsable(responsable);
            }
            List<Salidas> attachedSalidasList = new ArrayList<Salidas>();
            for (Salidas salidasListSalidasToAttach : empleados.getSalidasList()) {
                salidasListSalidasToAttach = em.getReference(salidasListSalidasToAttach.getClass(), salidasListSalidasToAttach.getNumSalida());
                attachedSalidasList.add(salidasListSalidasToAttach);
            }
            empleados.setSalidasList(attachedSalidasList);
            em.persist(empleados);
            if (centro != null) {
                centro.getEmpleadosList().add(empleados);
                centro = em.merge(centro);
            }
            if (jefeArea != null) {
                jefeArea.getEmpleadosList().add(empleados);
                jefeArea = em.merge(jefeArea);
            }
            if (responsable != null) {
                responsable.getEmpleadosList().add(empleados);
                responsable = em.merge(responsable);
            }
            for (Salidas salidasListSalidas : empleados.getSalidasList()) {
                Empleados oldEmpleadoOfSalidasListSalidas = salidasListSalidas.getEmpleado();
                salidasListSalidas.setEmpleado(empleados);
                salidasListSalidas = em.merge(salidasListSalidas);
                if (oldEmpleadoOfSalidasListSalidas != null) {
                    oldEmpleadoOfSalidasListSalidas.getSalidasList().remove(salidasListSalidas);
                    oldEmpleadoOfSalidasListSalidas = em.merge(oldEmpleadoOfSalidasListSalidas);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEmpleados(empleados.getCodEmpleado()) != null) {
                throw new PreexistingEntityException("Empleados " + empleados + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empleados empleados) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleados persistentEmpleados = em.find(Empleados.class, empleados.getCodEmpleado());
            Centros centroOld = persistentEmpleados.getCentro();
            Centros centroNew = empleados.getCentro();
            JefesArea jefeAreaOld = persistentEmpleados.getJefeArea();
            JefesArea jefeAreaNew = empleados.getJefeArea();
            Responsables responsableOld = persistentEmpleados.getResponsable();
            Responsables responsableNew = empleados.getResponsable();
            List<Salidas> salidasListOld = persistentEmpleados.getSalidasList();
            List<Salidas> salidasListNew = empleados.getSalidasList();
            if (centroNew != null) {
                centroNew = em.getReference(centroNew.getClass(), centroNew.getCodCentro());
                empleados.setCentro(centroNew);
            }
            if (jefeAreaNew != null) {
                jefeAreaNew = em.getReference(jefeAreaNew.getClass(), jefeAreaNew.getCodJefe());
                empleados.setJefeArea(jefeAreaNew);
            }
            if (responsableNew != null) {
                responsableNew = em.getReference(responsableNew.getClass(), responsableNew.getCodResponsable());
                empleados.setResponsable(responsableNew);
            }
            List<Salidas> attachedSalidasListNew = new ArrayList<Salidas>();
            for (Salidas salidasListNewSalidasToAttach : salidasListNew) {
                salidasListNewSalidasToAttach = em.getReference(salidasListNewSalidasToAttach.getClass(), salidasListNewSalidasToAttach.getNumSalida());
                attachedSalidasListNew.add(salidasListNewSalidasToAttach);
            }
            salidasListNew = attachedSalidasListNew;
            empleados.setSalidasList(salidasListNew);
            empleados = em.merge(empleados);
            if (centroOld != null && !centroOld.equals(centroNew)) {
                centroOld.getEmpleadosList().remove(empleados);
                centroOld = em.merge(centroOld);
            }
            if (centroNew != null && !centroNew.equals(centroOld)) {
                centroNew.getEmpleadosList().add(empleados);
                centroNew = em.merge(centroNew);
            }
            if (jefeAreaOld != null && !jefeAreaOld.equals(jefeAreaNew)) {
                jefeAreaOld.getEmpleadosList().remove(empleados);
                jefeAreaOld = em.merge(jefeAreaOld);
            }
            if (jefeAreaNew != null && !jefeAreaNew.equals(jefeAreaOld)) {
                jefeAreaNew.getEmpleadosList().add(empleados);
                jefeAreaNew = em.merge(jefeAreaNew);
            }
            if (responsableOld != null && !responsableOld.equals(responsableNew)) {
                responsableOld.getEmpleadosList().remove(empleados);
                responsableOld = em.merge(responsableOld);
            }
            if (responsableNew != null && !responsableNew.equals(responsableOld)) {
                responsableNew.getEmpleadosList().add(empleados);
                responsableNew = em.merge(responsableNew);
            }
            for (Salidas salidasListOldSalidas : salidasListOld) {
                if (!salidasListNew.contains(salidasListOldSalidas)) {
                    salidasListOldSalidas.setEmpleado(null);
                    salidasListOldSalidas = em.merge(salidasListOldSalidas);
                }
            }
            for (Salidas salidasListNewSalidas : salidasListNew) {
                if (!salidasListOld.contains(salidasListNewSalidas)) {
                    Empleados oldEmpleadoOfSalidasListNewSalidas = salidasListNewSalidas.getEmpleado();
                    salidasListNewSalidas.setEmpleado(empleados);
                    salidasListNewSalidas = em.merge(salidasListNewSalidas);
                    if (oldEmpleadoOfSalidasListNewSalidas != null && !oldEmpleadoOfSalidasListNewSalidas.equals(empleados)) {
                        oldEmpleadoOfSalidasListNewSalidas.getSalidasList().remove(salidasListNewSalidas);
                        oldEmpleadoOfSalidasListNewSalidas = em.merge(oldEmpleadoOfSalidasListNewSalidas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = empleados.getCodEmpleado();
                if (findEmpleados(id) == null) {
                    throw new NonexistentEntityException("The empleados with id " + id + " no longer exists.");
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
            Empleados empleados;
            try {
                empleados = em.getReference(Empleados.class, id);
                empleados.getCodEmpleado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleados with id " + id + " no longer exists.", enfe);
            }
            Centros centro = empleados.getCentro();
            if (centro != null) {
                centro.getEmpleadosList().remove(empleados);
                centro = em.merge(centro);
            }
            JefesArea jefeArea = empleados.getJefeArea();
            if (jefeArea != null) {
                jefeArea.getEmpleadosList().remove(empleados);
                jefeArea = em.merge(jefeArea);
            }
            Responsables responsable = empleados.getResponsable();
            if (responsable != null) {
                responsable.getEmpleadosList().remove(empleados);
                responsable = em.merge(responsable);
            }
            List<Salidas> salidasList = empleados.getSalidasList();
            for (Salidas salidasListSalidas : salidasList) {
                salidasListSalidas.setEmpleado(null);
                salidasListSalidas = em.merge(salidasListSalidas);
            }
            em.remove(empleados);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empleados> findEmpleadosEntities() {
        return findEmpleadosEntities(true, -1, -1);
    }

    public List<Empleados> findEmpleadosEntities(int maxResults, int firstResult) {
        return findEmpleadosEntities(false, maxResults, firstResult);
    }

    private List<Empleados> findEmpleadosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empleados.class));
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

    public Empleados findEmpleados(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empleados.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empleados> rt = cq.from(Empleados.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
