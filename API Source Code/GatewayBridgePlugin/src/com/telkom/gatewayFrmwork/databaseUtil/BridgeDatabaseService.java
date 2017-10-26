package com.telkom.gatewayFrmwork.databaseUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Restrictions;

@SuppressWarnings("rawtypes")
public class BridgeDatabaseService {
	private Session session;
	static private SessionFactory factory;

	public BridgeDatabaseService() {
		if (factory == null) {
			factory = new AnnotationConfiguration().configure().buildSessionFactory();
		}
		session = factory.openSession();
	}

	public List getAll(Class arg) {
		return session.createCriteria(arg).list();
	}

	public Serializable add(Object arg) {
		Transaction t = session.beginTransaction();
		int i = (int) session.save(arg);
		t.commit();
		return i;
	}

	public void delete(Object arg) {
		Transaction t = session.beginTransaction();
		session.delete(arg);
		t.commit();
	}

	public Object load(Class arg, int id) {
		return session.load(arg, id);
	}

	public void update(Object obj) {
		Transaction t = session.beginTransaction();
		session.update(obj);
		t.commit();
	}

	public List loadBykey(Class arg, Map<String, Object> mapping) {
		Criteria criteria = session.createCriteria(arg);
		for (Map.Entry m : mapping.entrySet()) {
			criteria = criteria.add(Restrictions.eq((String) m.getKey(), m.getValue()));
		}
		return criteria.list();
	}

	/**
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}

	public void closeSession() {
		session.close();
		factory.close();
	}

	public void saveorUpdate(Object obj) {
		session.saveOrUpdate(obj);
	}

}
