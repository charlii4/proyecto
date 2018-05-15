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
import Entidades.Directores;
import Entidades.Responsables;
import java.util.ArrayList;
import java.util.List;
import Entidades.Empleados;
import Entidades.JefesArea;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author charliVB
 */
public class JefesAreaJpaController implements Serializable {

    public JefesAreaJpaController() {
        emf = Persistence.createEntityManagerFactory("ProyectoPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(JefesArea jefesArea) throws PreexistingEntityException, Exception {
        if (jefesArea.getResponsablesList() == null) {
            jefesArea.setResponsablesList(new ArrayList<Responsables>());
        }
        if (jefesArea.getEmpleadosList() == null) {
            jefesArea.setEmpleadosList(new ArrayList<Empleados>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Centros centro = jefesArea.getCentro();
            if (centro != null) {
                centro = em.getReference(centro.getClass(), centro.getCodCentro());
                jefesArea.setCentro(centro);
            }
            Directores director = jefesArea.getDirector();
            if (director != null) {
                director = em.getReference(director.getClass(), director.getCodDirector());
                jefesArea.setDirector(director);
            }
            List<Responsables> attachedResponsablesList = new ArrayList<Responsables>();
            for (Responsables responsablesListResponsablesToAttach : jefesArea.getResponsablesList()) {
                responsablesListResponsablesToAttach = em.getReference(responsablesListResponsablesToAttach.getClass(), responsablesListResponsablesToAttach.getCodResponsable());
                attachedResponsablesList.add(responsablesListResponsablesToAttach);
            }
            jefesArea.setResponsablesList(attachedResponsablesList);
            List<Empleados> attachedEmpleadosList = new ArrayList<Empleados>();
            for (Empleados empleadosListEmpleadosToAttach : jefesArea.getEmpleadosList()) {
                empleadosListEmpleadosToAttach = em.getReference(empleadosListEmpleadosToAttach.getClass(), empleadosListEmpleadosToAttach.getCodEmpleado());
                attachedEmpleadosList.add(empleadosListEmpleadosToAttach);
            }
            jefesArea.setEmpleadosList(attachedEmpleadosList);
            em.persist(jefesArea);
            if (centro != null) {
                centro.getJefesAreaList().add(jefesArea);
                centro = em.merge(centro);
            }
            if (director != null) {
                director.getJefesAreaList().add(jefesArea);
                director = em.merge(director);
            }
            for (Responsables responsablesListResponsables : jefesArea.getResponsablesList()) {
                JefesArea oldJefeOfResponsablesListResponsables = responsablesListResponsables.getJefe();
                responsablesListResponsables.setJefe(jefesArea);
                responsablesListResponsables = em.merge(responsablesListResponsables);
                if (oldJefeOfResponsablesListResponsables != null) {
                    oldJefeOfResponsablesListResponsables.getResponsablesList().remove(responsablesListResponsables);
                    oldJefeOfResponsablesListResponsables = em.merge(oldJefeOfResponsablesListResponsables);
                }
            }
            for (Empleados empleadosListEmpleados : jefesArea.getEmpleadosList()) {
                JefesArea oldJefeAreaOfEmpleadosListEmpleados = empleadosListEmpleados.getJefeArea();
                empleadosListEmpleados.setJefeArea(jefesArea);
                empleadosListEmpleados = em.merge(empleadosListEmpleados);
                if (oldJefeAreaOfEmpleadosListEmpleados != null) {
                    oldJefeAreaOfEmpleadosListEmpleados.getEmpleadosList().remove(empleadosListEmpleados);
                    oldJefeAreaOfEmpleadosListEmpleados = em.merge(oldJefeAreaOfEmpleadosListEmpleados);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findJefesArea(jefesArea.getCodJefe()) != null) {
                throw new PreexistingEntityException("JefesArea " + jefesArea + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(JefesArea jefesArea) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            JefesArea persistentJefesArea = em.find(JefesArea.class, jefesArea.getCodJefe());
            Centros centroOld = persistentJefesArea.getCentro();
            Centros centroNew = jefesArea.getCentro();
            Directores directorOld = persistentJefesArea.getDirector();
            Directores directorNew = jefesArea.getDirector();
            List<Responsables> responsablesListOld = persistentJefesArea.getResponsablesList();
            List<Responsables> responsablesListNew = jefesArea.getResponsablesList();
            List<Empleados> empleadosListOld = persistentJefesArea.getEmpleadosList();
            List<Empleados> empleadosListNew = jefesArea.getEmpleadosList();
            if (centroNew != null) {
                centroNew = em.getReference(centroNew.getClass(), centroNew.getCodCentro());
                jefesArea.setCentro(centroNew);
            }
            if (directorNew != null) {
                directorNew = em.getReference(directorNew.getClass(), directorNew.getCodDirector());
                jefesArea.setDirector(directorNew);
            }
            List<Responsables> attachedResponsablesListNew = new ArrayList<Responsables>();
            for (Responsables responsablesListNewResponsablesToAttach : responsablesListNew) {
                responsablesListNewResponsablesToAttach = em.getReference(responsablesListNewResponsablesToAttach.getClass(), responsablesListNewResponsablesToAttach.getCodResponsable());
                attachedResponsablesListNew.add(responsablesListNewResponsablesToAttach);
            }
            responsablesListNew = attachedResponsablesListNew;
            jefesArea.setResponsablesList(responsablesListNew);
            List<Empleados> attachedEmpleadosListNew = new ArrayList<Empleados>();
            for (Empleados empleadosListNewEmpleadosToAttach : empleadosListNew) {
                empleadosListNewEmpleadosToAttach = em.getReference(empleadosListNewEmpleadosToAttach.getClass(), empleadosListNewEmpleadosToAttach.getCodEmpleado());
                attachedEmpleadosListNew.add(empleadosListNewEmpleadosToAttach);
            }
            empleadosListNew = attachedEmpleadosListNew;
            jefesArea.setEmpleadosList(empleadosListNew);
            jefesArea = em.merge(jefesArea);
            if (centroOld != null && !centroOld.equals(centroNew)) {
                centroOld.getJefesAreaList().remove(jefesArea);
                centroOld = em.merge(centroOld);
            }
            if (centroNew != null && !centroNew.equals(centroOld)) {
                centroNew.getJefesAreaList().add(jefesArea);
                centroNew = em.merge(centroNew);
            }
            if (directorOld != null && !directorOld.equals(directorNew)) {
                directorOld.getJefesAreaList().remove(jefesArea);
                directorOld = em.merge(directorOld);
            }
            if (directorNew != null && !directorNew.equals(directorOld)) {
                directorNew.getJefesAreaList().add(jefesArea);
                directorNew = em.merge(directorNew);
            }
            for (Responsables responsablesListOldResponsables : responsablesListOld) {
                if (!responsablesListNew.contains(responsablesListOldResponsables)) {
                    responsablesListOldResponsables.setJefe(null);
                    responsablesListOldResponsables = em.merge(responsablesListOldResponsables);
                }
            }
            for (Responsables responsablesListNewResponsables : responsablesListNew) {
                if (!responsablesListOld.contains(responsablesListNewResponsables)) {
                    JefesArea oldJefeOfResponsablesListNewResponsables = responsablesListNewResponsables.getJefe();
                    responsablesListNewResponsables.setJefe(jefesArea);
                    responsablesListNewResponsables = em.merge(responsablesListNewResponsables);
                    if (oldJefeOfResponsablesListNewResponsables != null && !oldJefeOfResponsablesListNewResponsables.equals(jefesArea)) {
                        oldJefeOfResponsablesListNewResponsables.getResponsablesList().remove(responsablesListNewResponsables);
                        oldJefeOfResponsablesListNewResponsables = em.merge(oldJefeOfResponsablesListNewResponsables);
                    }
                }
            }
            for (Empleados empleadosListOldEmpleados : empleadosListOld) {
                if (!empleadosListNew.contains(empleadosListOldEmpleados)) {
                    empleadosListOldEmpleados.setJefeArea(null);
                    empleadosListOldEmpleados = em.merge(empleadosListOldEmpleados);
                }
            }
            for (Empleados empleadosListNewEmpleados : empleadosListNew) {
                if (!empleadosListOld.contains(empleadosListNewEmpleados)) {
                    JefesArea oldJefeAreaOfEmpleadosListNewEmpleados = empleadosListNewEmpleados.getJefeArea();
                    empleadosListNewEmpleados.setJefeArea(jefesArea);
                    empleadosListNewEmpleados = em.merge(empleadosListNewEmpleados);
                    if (oldJefeAreaOfEmpleadosListNewEmpleados != null && !oldJefeAreaOfEmpleadosListNewEmpleados.equals(jefesArea)) {
                        oldJefeAreaOfEmpleadosListNewEmpleados.getEmpleadosList().remove(empleadosListNewEmpleados);
                        oldJefeAreaOfEmpleadosListNewEmpleados = em.merge(oldJefeAreaOfEmpleadosListNewEmpleados);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = jefesArea.getCodJefe();
                if (findJefesArea(id) == null) {
                    throw new NonexistentEntityException("The jefesArea with id " + id + " no longer exists.");
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
            JefesArea jefesArea;
            try {
                jefesArea = em.getReference(JefesArea.class, id);
                jefesArea.getCodJefe();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The jefesArea with id " + id + " no longer exists.", enfe);
            }
            Centros centro = jefesArea.getCentro();
            if (centro != null) {
                centro.getJefesAreaList().remove(jefesArea);
                centro = em.merge(centro);
            }
            Directores director = jefesArea.getDirector();
            if (director != null) {
                director.getJefesAreaList().remove(jefesArea);
                director = em.merge(director);
            }
            List<Responsables> responsablesList = jefesArea.getResponsablesList();
            for (Responsables responsablesListResponsables : responsablesList) {
                responsablesListResponsables.setJefe(null);
                responsablesListResponsables = em.merge(responsablesListResponsables);
            }
            List<Empleados> empleadosList = jefesArea.getEmpleadosList();
            for (Empleados empleadosListEmpleados : empleadosList) {
                empleadosListEmpleados.setJefeArea(null);
                empleadosListEmpleados = em.merge(empleadosListEmpleados);
            }
            em.remove(jefesArea);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<JefesArea> findJefesAreaEntities() {
        return findJefesAreaEntities(true, -1, -1);
    }

    public List<JefesArea> findJefesAreaEntities(int maxResults, int firstResult) {
        return findJefesAreaEntities(false, maxResults, firstResult);
    }

    private List<JefesArea> findJefesAreaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(JefesArea.class));
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

    public JefesArea findJefesArea(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(JefesArea.class, id);
        } finally {
            em.close();
        }
    }

    public int getJefesAreaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<JefesArea> rt = cq.from(JefesArea.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
